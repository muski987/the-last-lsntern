import java.util.ArrayList;
import java.util.Scanner;

/**
 * One play-through of the story.
 *
 * Playing the game = WALKING THE GRAPH one edge at a time:
 *   we stand on a vertex (current scene), show its outgoing edges (choices),
 *   the player picks one, and we move to the vertex that edge points to.
 *
 * Extra data structures used here:
 *   StackADT history      : the scenes we came from, so "u" (undo) can pop
 *                           the last one and go back (LIFO: last in, first out).
 *                           It is our own LinkedStack, written from scratch.
 *   ArrayList<String> path: every scene visited, printed at the end
 */
public class Game {
    private StoryGraph graph;
    private Player player;
    private Scanner in;
    private StackADT history = new LinkedStack();
    private ArrayList<String> path = new ArrayList<String>();

    public Game(StoryGraph graph, Player player, Scanner in) {
        this.graph = graph;
        this.player = player;
        this.in = in;
    }

    /** Plays until an ending is reached or the player quits. Returns the ending id, or null. */
    public String play() {
        String currentId = Story.START;
        path.add(currentId);
        boolean justArrived = true;   // print the scene text only when we arrive, not after every command

        while (true) {
            Scene scene = graph.getScene(currentId);
            if (justArrived) {
                scene.display();     // POLYMORPHISM: NormalScene and EndingScene print differently
                // Pick up the item in this scene, if there is one.
                if (scene.getItem() != null) {
                    player.addItem(scene.getItem());
                }
                justArrived = false;
            }

            if (scene.isEnding()) {
                System.out.println("Your path: " + TextUtil.pathToString(path));
                System.out.println("Number of choices made: " + (path.size() - 1));
                return currentId;
            }

            // Show the outgoing edges of this vertex.
            ArrayList<Choice> choices = graph.getChoices(currentId);
            System.out.println();
            for (int i = 0; i < choices.size(); i++) {
                Choice c = choices.get(i);
                String line = "  " + (i + 1) + ". " + c.getLabel();
                if (c.isLocked() && !player.hasItem(c.getRequiredItem())) {
                    line = line + "  [locked: needs " + c.getRequiredItem() + "]";
                }
                System.out.println(line);
            }
            System.out.println("  (i = inventory, h = hint, u = undo, q = quit)");

            System.out.print("> ");
            if (!in.hasNextLine()) {
                return null;
            }
            String input = in.nextLine().trim().toLowerCase();

            if (input.equals("q")) {
                System.out.println("You stand still, and the village waits.");
                return null;
            } else if (input.equals("i")) {
                showInventory();
            } else if (input.equals("h")) {
                showHint(currentId);
            } else if (input.equals("u")) {
                String before = currentId;
                currentId = undo(currentId);
                justArrived = !currentId.equals(before);
            } else {
                int number = toNumber(input);
                if (number < 1 || number > choices.size()) {
                    System.out.println("Please type a number from 1 to " + choices.size() + ".");
                    continue;
                }
                Choice picked = choices.get(number - 1);
                if (picked.isLocked() && !player.hasItem(picked.getRequiredItem())) {
                    System.out.println("You cannot do that yet. You need: " + picked.getRequiredItem());
                    continue;
                }
                // Safety check: only move along an edge that really exists.
                if (graph.isValidMove(currentId, picked.getTargetId())) {
                    history.push(currentId);          // remember where we were
                    currentId = picked.getTargetId(); // move along the edge
                    path.add(currentId);
                    justArrived = true;
                }
            }
        }
    }

    /** UNDO with a STACK: pop the previous scene and go back to it. */
    private String undo(String currentId) {
        if (history.isEmpty()) {
            System.out.println("You are at the start. There is nothing to undo.");
            return currentId;
        }
        String previous = history.pop();
        path.add(previous);
        System.out.println("You retrace your steps...");
        return previous;
    }

    /** HINT uses BFS shortest path from the CURRENT scene to the nearest ending. */
    private void showHint(String currentId) {
        ArrayList<String> shortest = graph.shortestPathToEnding(currentId);
        if (shortest.isEmpty()) {
            System.out.println("Hint: no ending can be reached from here.");
        } else {
            System.out.println("Hint: the nearest ending is " + (shortest.size() - 1)
                + " choice(s) away: " + TextUtil.pathToString(shortest));
        }
    }

    private void showInventory() {
        ArrayList<String> items = player.getInventory();
        if (items.isEmpty()) {
            System.out.println(player.getName() + " is carrying nothing.");
        } else {
            System.out.println(player.getName() + " is carrying: " + items);
        }
    }

    /** Turns text into a number. Returns -1 if it is not a number (EXCEPTION HANDLING). */
    private int toNumber(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
