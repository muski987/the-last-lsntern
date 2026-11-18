import java.util.ArrayList;

/**
 * ADT (Abstract Data Type): DIRECTED GRAPH of story scenes
 *
 * This interface lists WHAT a story graph can do. StoryGraph is one way of
 * doing it (with an adjacency list). We could write another class that uses
 * an adjacency MATRIX instead, and Main/Game would not need to change,
 * because they only use these operations. That is the point of an ADT:
 * the user of the data type does not depend on how it is stored.
 */
public interface GraphADT {
    // Building the graph
    void addScene(Scene scene);                               // add a vertex
    void addChoice(String fromId, String toId, String label); // add a directed edge

    // Basic queries
    Scene getScene(String id);
    ArrayList<Choice> getChoices(String id);   // outgoing edges of a vertex
    int sceneCount();                          // V
    int choiceCount();                         // E
    int inDegree(String id);
    int outDegree(String id);
    boolean isValidMove(String fromId, String toId);

    // Graph algorithms
    ArrayList<String> bfsReachable(String startId);
    ArrayList<String> shortestPathToEnding(String startId);
    ArrayList<ArrayList<String>> dfsAllPaths(String startId);
}
