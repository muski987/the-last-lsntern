import java.util.ArrayList;
import java.util.Scanner;

/**
 * THE LAST LANTERN - an interactive story game built on a graph.
 *
 * Main shows the menu (a MENU-DRIVEN program using switch-case).
 * Option 1 plays the game; options 2 to 5 run graph algorithms on the story
 * so you can SEE the graph working; option 6 demonstrates our Stack and
 * Queue ADTs.
 *
 * Compile:  javac *.java
 * Run:      java Main
 */
public class Main {

    public static void main(String[] args) {
        StoryGraph graph = Story.build();
        Scanner in = new Scanner(System.in);
        ArrayList<String> endingsFound = new ArrayList<String>();  // endings unlocked so far

        System.out.println("==========================================");
        System.out.println("            THE LAST LANTERN");
        System.out.println("        Every choice leaves a mark.");
        System.out.println("==========================================");
        System.out.print("Enter your name: ");
        String name = in.hasNextLine() ? in.nextLine().trim() : "";
        if (name.isEmpty()) {
            name = "Traveller";
        }

        boolean running = true;
        while (running) {
            int totalEndings = graph.findEndings().size();
            System.out.println();
            System.out.println("========== MENU ==========  (endings found: "
                + endingsFound.size() + "/" + totalEndings + ")");
            System.out.println("1. Play the story");
            System.out.println("2. Show the story map (adjacency list)");
            System.out.println("3. Shortest way to an ending (BFS)");
            System.out.println("4. All possible story paths (DFS)");
            System.out.println("5. Check the story for mistakes (BFS)");
            System.out.println("6. Stack and Queue ADT demo");
            System.out.println("7. Quit");
            System.out.print("> ");
            if (!in.hasNextLine()) {
                break;
            }
            String option = in.nextLine().trim();

            switch (option) {
                case "1":
                    // A new Player each game, so the inventory starts empty.
                    Game game = new Game(graph, new Player(name), in);
                    String ending = game.play();
                    if (ending != null && !endingsFound.contains(ending)) {
                        endingsFound.add(ending);
                        System.out.println("New ending unlocked! (" + endingsFound.size()
                            + "/" + totalEndings + ")");
                    }
                    break;
                case "2":
                    showMap(graph);
                    break;
                case "3":
                    showShortestPath(graph);
                    break;
                case "4":
                    showAllPaths(graph);
                    break;
                case "5":
                    checkStory(graph);
                    break;
                case "6":
                    adtDemo();
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Please type a number from 1 to 7.");
            }
        }
        System.out.println("The lanterns burn on without you, " + name + ".");
    }

    /**
     * Shows the Stack ADT and Queue ADT working on scene ids, so the
     * difference between LIFO and FIFO is easy to see.
     * The variables have the INTERFACE type (StackADT / QueueADT) and hold
     * our own linked-list classes.
     */
    private static void adtDemo() {
        String[] ids = {"GATE", "WELL", "SHRINE"};

        System.out.println();
        System.out.println("=== STACK ADT (LinkedStack, LIFO) ===");
        StackADT stack = new LinkedStack();
        for (int i = 0; i < ids.length; i++) {
            stack.push(ids[i]);
            System.out.println("push(" + ids[i] + ")   top = " + stack.peek() + ", size = " + stack.size());
        }
        while (!stack.isEmpty()) {
            System.out.println("pop()  -> " + stack.pop());
        }
        System.out.println("Last one in came out first.");

        System.out.println();
        System.out.println("=== QUEUE ADT (LinkedQueue, FIFO) ===");
        QueueADT queue = new LinkedQueue();
        for (int i = 0; i < ids.length; i++) {
            queue.enqueue(ids[i]);
            System.out.println("enqueue(" + ids[i] + ")   front = " + queue.peek() + ", size = " + queue.size());
        }
        while (!queue.isEmpty()) {
            System.out.println("dequeue() -> " + queue.dequeue());
        }
        System.out.println("First one in came out first.");
        System.out.println();
        System.out.println("The game uses the Stack for UNDO and the Queue for BFS.");
    }

    /** Prints the ADJACENCY LIST: every vertex and the vertices its edges point to. */
    private static void showMap(StoryGraph graph) {
        System.out.println();
        System.out.println("=== STORY MAP (adjacency list) ===");
        System.out.println("Vertices (scenes): " + graph.sceneCount());
        System.out.println("Edges (choices):   " + graph.choiceCount());
        System.out.println();
        ArrayList<String> ids = graph.getSceneIds();
        for (int i = 0; i < ids.size(); i++) {
            String id = ids.get(i);
            ArrayList<Choice> choices = graph.getChoices(id);
            String line = id + " -> [";
            for (int j = 0; j < choices.size(); j++) {
                if (j > 0) {
                    line = line + ", ";
                }
                line = line + choices.get(j).getTargetId();
                if (choices.get(j).isLocked()) {
                    line = line + "(locked)";
                }
            }
            line = line + "]   in-degree=" + graph.inDegree(id) + ", out-degree=" + graph.outDegree(id);
            System.out.println("  " + line);
        }
    }

    /** BFS shortest path from the start to the closest ending. */
    private static void showShortestPath(StoryGraph graph) {
        ArrayList<String> path = graph.shortestPathToEnding(Story.START);
        System.out.println();
        System.out.println("=== SHORTEST PATH (BFS) ===");
        if (path.isEmpty()) {
            System.out.println("No ending can be reached!");
        } else {
            System.out.println("Fewest choices to finish: " + (path.size() - 1));
            System.out.println(TextUtil.pathToString(path));
        }
    }

    /** DFS: every simple path from the start to any ending. */
    private static void showAllPaths(StoryGraph graph) {
        ArrayList<ArrayList<String>> paths = graph.dfsAllPaths(Story.START);
        System.out.println();
        System.out.println("=== ALL STORY PATHS (DFS) ===");
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + TextUtil.pathToString(paths.get(i)));
        }
        System.out.println("Total different paths to an ending: " + paths.size());
    }

    /** Uses BFS and degrees to find writing mistakes in the story. */
    private static void checkStory(StoryGraph graph) {
        System.out.println();
        System.out.println("=== STORY CHECK ===");
        System.out.println("Reachable from " + Story.START + " (BFS order): "
            + graph.bfsReachable(Story.START));
        System.out.println("Unreachable scenes: " + graph.findUnreachable(Story.START));
        System.out.println("Ending scenes:      " + graph.findEndings());
        System.out.println("Dead ends:          " + graph.findDeadEnds());
    }
}
