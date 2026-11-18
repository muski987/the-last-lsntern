import java.util.ArrayList;
import java.util.HashMap;

/**
 * The whole story stored as a DIRECTED GRAPH.
 *
 *   vertex (node) = a scene   -> Scene
 *   edge          = a choice  -> Choice, it points FROM one scene TO another
 *
 * "Directed" means an edge has a direction: GATE -> WELL does not
 * automatically mean WELL -> GATE. If we want both, we add two edges.
 *
 * HOW THE GRAPH IS STORED (ADJACENCY LIST):
 *
 *   scenes        : HashMap  id -> Scene               (find a scene fast)
 *   adjacencyList : HashMap  id -> ArrayList<Choice>   (its outgoing edges)
 *   sceneOrder    : ArrayList of ids in the order we added them
 *                   (a HashMap does not keep order, so this is for printing)
 *
 * Why an adjacency list and not an adjacency matrix?
 *   A matrix needs V x V boxes (12 x 12 = 144) even though most scenes have
 *   only 2 or 3 choices. The list only stores the edges that really exist,
 *   so it uses O(V + E) memory and finding a scene's choices is quick.
 */
public class StoryGraph {
    private HashMap<String, Scene> scenes = new HashMap<String, Scene>();
    private HashMap<String, ArrayList<Choice>> adjacencyList = new HashMap<String, ArrayList<Choice>>();
    private ArrayList<String> sceneOrder = new ArrayList<String>();

    // =================================================================
    // 1. BUILDING THE GRAPH
    // =================================================================

    /** Adds a VERTEX. It starts with an empty list of edges. */
    public void addScene(Scene scene) {
        if (scenes.containsKey(scene.getId())) {
            System.out.println("Warning: duplicate scene id " + scene.getId());
            return;
        }
        scenes.put(scene.getId(), scene);
        adjacencyList.put(scene.getId(), new ArrayList<Choice>());
        sceneOrder.add(scene.getId());
    }

    /** Adds a normal directed EDGE  fromId -> toId. */
    public void addChoice(String fromId, String toId, String label) {
        addChoice(fromId, toId, label, null);
    }

    /** Adds a directed EDGE that needs an item (a locked edge). */
    public void addChoice(String fromId, String toId, String label, String requiredItem) {
        if (!scenes.containsKey(fromId) || !scenes.containsKey(toId)) {
            System.out.println("Warning: bad edge " + fromId + " -> " + toId);
            return;
        }
        // Go to fromId's list in the adjacency list and add the edge to it.
        adjacencyList.get(fromId).add(new Choice(label, toId, requiredItem));
    }

    // =================================================================
    // 2. SIMPLE LOOKUPS
    // =================================================================

    public Scene getScene(String id) {
        return scenes.get(id);          // HashMap lookup: O(1) on average
    }

    /** The outgoing edges of a scene = the choices the player sees. */
    public ArrayList<Choice> getChoices(String id) {
        return adjacencyList.get(id);
    }

    public ArrayList<String> getSceneIds() {
        return sceneOrder;
    }

    public int sceneCount() {
        return scenes.size();           // number of vertices V
    }

    public int choiceCount() {          // number of edges E
        int total = 0;
        for (int i = 0; i < sceneOrder.size(); i++) {
            total = total + adjacencyList.get(sceneOrder.get(i)).size();
        }
        return total;
    }

    /** OUT-DEGREE = how many edges leave this vertex. */
    public int outDegree(String id) {
        return adjacencyList.get(id).size();
    }

    /** IN-DEGREE = how many edges come INTO this vertex (we must check every list). */
    public int inDegree(String id) {
        int count = 0;
        for (int i = 0; i < sceneOrder.size(); i++) {
            ArrayList<Choice> list = adjacencyList.get(sceneOrder.get(i));
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getTargetId().equals(id)) {
                    count++;
                }
            }
        }
        return count;
    }

    /** True only if there really is an edge fromId -> toId. Stops illegal jumps. */
    public boolean isValidMove(String fromId, String toId) {
        ArrayList<Choice> list = adjacencyList.get(fromId);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTargetId().equals(toId)) {
                return true;
            }
        }
        return false;
    }

    // =================================================================
    // 3. BFS (Breadth First Search) - which scenes can be reached?
    // =================================================================

    /**
     * BFS visits the start scene, then every scene 1 choice away, then every
     * scene 2 choices away, and so on, like ripples in water.
     *
     * It uses a QUEUE (our LinkedQueue, first in, first out) and a "visited" list so that a
     * loop (for example WELL -> GATE -> WELL) does not run forever.
     *
     * Time: O(V + E), every scene enters the queue once and every edge is
     * looked at once.
     */
    public ArrayList<String> bfsReachable(String startId) {
        ArrayList<String> visited = new ArrayList<String>();
        QueueADT queue = new LinkedQueue();

        visited.add(startId);
        queue.enqueue(startId);

        while (!queue.isEmpty()) {
            String current = queue.dequeue();                // take from the front
            ArrayList<Choice> edges = adjacencyList.get(current);
            for (int i = 0; i < edges.size(); i++) {
                String next = edges.get(i).getTargetId();
                if (!visited.contains(next)) {              // not seen yet
                    visited.add(next);
                    queue.enqueue(next);                        // add to the back
                }
            }
        }
        return visited;
    }

    /** Scenes that BFS never reached: no player can ever see them (a writing bug). */
    public ArrayList<String> findUnreachable(String startId) {
        ArrayList<String> reachable = bfsReachable(startId);
        ArrayList<String> unreachable = new ArrayList<String>();
        for (int i = 0; i < sceneOrder.size(); i++) {
            if (!reachable.contains(sceneOrder.get(i))) {
                unreachable.add(sceneOrder.get(i));
            }
        }
        return unreachable;
    }

    // =================================================================
    // 4. BFS SHORTEST PATH - fewest choices from one scene to an ending
    // =================================================================

    /**
     * In a graph where every edge has the same cost (1 choice), BFS finds the
     * SHORTEST PATH, because it reaches all scenes 1 step away before any
     * scene 2 steps away. The first ending it meets is therefore the closest.
     *
     * To rebuild the path we remember a PARENT for every scene:
     *   parent.get("WELL") = "GATE" means we first reached WELL from GATE.
     * At the end we walk back from the ending to the start using parents,
     * then reverse it.
     *
     * Returns the path as a list of ids, or an empty list if no ending is
     * reachable.
     */
    public ArrayList<String> shortestPathToEnding(String startId) {
        HashMap<String, String> parent = new HashMap<String, String>();
        ArrayList<String> visited = new ArrayList<String>();
        QueueADT queue = new LinkedQueue();

        visited.add(startId);
        queue.enqueue(startId);
        String endingFound = null;

        while (!queue.isEmpty()) {
            String current = queue.dequeue();
            if (scenes.get(current).isEnding()) {
                endingFound = current;      // first ending BFS meets = nearest one
                break;
            }
            ArrayList<Choice> edges = adjacencyList.get(current);
            for (int i = 0; i < edges.size(); i++) {
                String next = edges.get(i).getTargetId();
                if (!visited.contains(next)) {
                    visited.add(next);
                    parent.put(next, current);   // we came to next from current
                    queue.enqueue(next);
                }
            }
        }

        ArrayList<String> path = new ArrayList<String>();
        if (endingFound == null) {
            return path;                    // empty: no ending reachable
        }
        // Walk backwards: ending -> its parent -> ... -> start.
        String step = endingFound;
        while (step != null) {
            path.add(0, step);              // add at the front, so the list ends up in order
            step = parent.get(step);        // the start has no parent, so this becomes null
        }
        return path;
    }

    // =================================================================
    // 5. DFS (Depth First Search) - every possible story path
    // =================================================================

    /**
     * DFS goes as DEEP as possible down one choice before trying the next
     * one (it BACKTRACKS). We write it with RECURSION, which uses the call
     * stack like a Stack data structure.
     *
     * Here it lists every different way to go from the start to an ending.
     * A scene already on the current path is skipped, so loops such as
     * GATE -> WELL -> GATE are not followed again (simple paths only).
     *
     * Item locks are ignored here: this looks at the shape of the story.
     */
    public ArrayList<ArrayList<String>> dfsAllPaths(String startId) {
        ArrayList<ArrayList<String>> allPaths = new ArrayList<ArrayList<String>>();
        ArrayList<String> currentPath = new ArrayList<String>();
        dfs(startId, currentPath, allPaths);
        return allPaths;
    }

    private void dfs(String current, ArrayList<String> currentPath,
                     ArrayList<ArrayList<String>> allPaths) {
        currentPath.add(current);                           // step forward

        if (scenes.get(current).isEnding()) {
            // Reached an ending: save a COPY of the path (the original keeps changing).
            allPaths.add(new ArrayList<String>(currentPath));
        } else {
            ArrayList<Choice> edges = adjacencyList.get(current);
            for (int i = 0; i < edges.size(); i++) {
                String next = edges.get(i).getTargetId();
                if (!currentPath.contains(next)) {          // avoid going in a circle
                    dfs(next, currentPath, allPaths);       // go deeper
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);         // BACKTRACK: step back
    }

    // =================================================================
    // 6. CHECKING THE STORY FOR MISTAKES
    // =================================================================

    /** All ending scenes (EndingScene objects). */
    public ArrayList<String> findEndings() {
        ArrayList<String> endings = new ArrayList<String>();
        for (int i = 0; i < sceneOrder.size(); i++) {
            if (scenes.get(sceneOrder.get(i)).isEnding()) {
                endings.add(sceneOrder.get(i));
            }
        }
        return endings;
    }

    /**
     * DEAD END = a normal (non-ending) scene with out-degree 0.
     * The player would be stuck there forever, so it is a bug in the story.
     */
    public ArrayList<String> findDeadEnds() {
        ArrayList<String> deadEnds = new ArrayList<String>();
        for (int i = 0; i < sceneOrder.size(); i++) {
            String id = sceneOrder.get(i);
            if (!scenes.get(id).isEnding() && outDegree(id) == 0) {
                deadEnds.add(id);
            }
        }
        return deadEnds;
    }
}
