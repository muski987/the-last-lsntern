# The Last Lantern (console version)

An interactive horror story game in plain Java. The story is stored as a **directed graph**: every scene is a vertex and every choice is an edge. No GUI, no libraries, only core Java.

## How to run

Open a terminal in the `src` folder and type:

```
javac *.java
java Main
```

(In VS Code / IntelliJ you can also just run `Main.java`.)

## Menu

| Option | What it does | Graph concept |
|---|---|---|
| 1. Play the story | Walk through the story choice by choice | Traversing edges one at a time |
| 2. Show the story map | Prints every scene and where its choices lead, with in-degree and out-degree | Adjacency list, degree |
| 3. Shortest way to an ending | Fewest choices from the start to any ending | BFS shortest path |
| 4. All possible story paths | Lists every different route to an ending (10 of them) | DFS with recursion and backtracking |
| 5. Check the story for mistakes | Finds scenes nobody can reach (BELL_TOWER) and dead ends | BFS reachability |
| 6. Stack and Queue ADT demo | Pushes/pops and enqueues/dequeues scene ids to show LIFO vs FIFO | Stack ADT, Queue ADT |
| 7. Quit | Ends the program | |

While playing: type the number of a choice, or `i` (inventory), `h` (hint), `u` (undo), `q` (quit).

## Files (read them in this order)

| File | What it is | Concepts used |
|---|---|---|
| `Displayable.java` | Interface with one method, `display()` | Interface |
| `Scene.java` | Abstract class for one scene (a **vertex**) | Abstract class, encapsulation, `implements` |
| `NormalScene.java` | A scene in the middle of the story | Inheritance (`extends`), constructor overloading |
| `EndingScene.java` | A scene that ends the story | Inheritance, method overriding, `super` |
| `Choice.java` | One choice (a directed **edge**), can be locked by an item | Class and objects |
| `GraphADT.java` | Interface listing every graph operation | ADT (interface) |
| `StackADT.java` / `LinkedStack.java` | Stack ADT and our own linked-list stack | ADT, linked list, LIFO |
| `QueueADT.java` / `LinkedQueue.java` | Queue ADT and our own linked-list queue | ADT, linked list, FIFO |
| `Node.java` | One node of a singly linked list | Self-referential class |
| `Player.java` | Player name and inventory | `ArrayList` |
| `StoryGraph.java` | **The graph** (implements `GraphADT`) and all graph algorithms | `HashMap`, `ArrayList`, BFS, DFS, recursion |
| `Story.java` | The actual story: 12 scenes and 19 choices | Building the graph |
| `Game.java` | The game loop for one play-through | `Scanner`, `LinkedStack` (undo), exception handling |
| `TextUtil.java` | Wraps long text to fit the screen | Arrays, `String` methods |
| `Main.java` | The menu | `switch`, loops |

## Where each graph concept is used

**Vertex (node)** = a scene. Class `Scene` (and its children `NormalScene` and `EndingScene`). Example: `GATE`, `WELL`, `COURTYARD`.

**Edge** = a choice. Class `Choice`. Example: `GATE -> WELL` labelled "Follow the sound of crying".

**Directed graph.** An edge goes one way only. `WELL -> GATE` exists, but that does not mean every edge has a way back.

**Adjacency list.** In `StoryGraph`: `HashMap<String, ArrayList<Choice>> adjacencyList`. For each scene id it stores the list of its outgoing edges. Menu option 2 prints it. We chose a list instead of a matrix because a matrix would need 12 x 12 = 144 boxes, but there are only 19 edges.

**In-degree and out-degree.** `inDegree()` and `outDegree()` in `StoryGraph`. Out-degree = number of choices in a scene. An ending has out-degree 0. `BELL_TOWER` has in-degree 0, which is why nobody can reach it.

**Cycle.** `GATE -> WELL -> GATE` is a loop. BFS and DFS both keep track of visited scenes so they don't go round forever.

**BFS (Breadth First Search).** `bfsReachable()` in `StoryGraph`. Uses a `Queue` (`LinkedList`). Visits scenes level by level: first everything 1 choice away, then 2 choices away, and so on. Time O(V + E).

**Reachability / unreachable scenes.** `findUnreachable()` runs BFS from `GATE`. Any scene BFS never visits can never be played. `BELL_TOWER` was left unconnected on purpose so the check has something to find.

**Shortest path.** `shortestPathToEnding()`. Because every choice "costs" 1, BFS finds the shortest path: the first ending it reaches is the nearest one. A `parent` HashMap remembers where we came from, and we walk it backwards to rebuild the path. Used by menu option 3 and by the `h` (hint) command during play, where it starts from the scene you are in.

**DFS (Depth First Search).** `dfsAllPaths()` and the recursive `dfs()`. It goes as deep as possible down one choice, and when it reaches an ending it saves the path, then **backtracks** (removes the last scene) and tries the next choice. Menu option 4 shows all 10 paths.

**Dead end.** `findDeadEnds()`: a normal scene with out-degree 0. The player would be stuck, so it's a bug. Our story has none.

**Edge validation.** `isValidMove()` checks that an edge really exists before the player moves.

**Locked edge.** `COURTYARD -> ENDING_DAWN` needs the "Blank Spirit Tablet", which you pick up at the Ancestral Altar. The game checks the player's inventory before allowing that edge.

## ADTs (Abstract Data Types)

An ADT describes **what** operations a data structure has, not **how** they work. In Java we write it as an **interface**, and a class **implements** it.

| ADT (interface) | Operations | Our implementation | Used for |
|---|---|---|---|
| `GraphADT` | addScene, addChoice, getChoices, inDegree, outDegree, bfsReachable, shortestPathToEnding, dfsAllPaths | `StoryGraph` (adjacency list) | The whole story |
| `StackADT` | push, pop, peek, isEmpty, size | `LinkedStack` (linked list, top pointer) | Undo (`u`) in `Game.java` |
| `QueueADT` | enqueue, dequeue, peek, isEmpty, size | `LinkedQueue` (linked list, front and rear pointers) | BFS in `StoryGraph.java` |

We wrote the Stack and Queue ourselves using `Node` objects instead of using Java's built-in `Stack` and `Queue`. All their operations are O(1).

- **Stack** in `Game.java`: every move pushes the old scene; `u` (undo) pops it to go back. Last in, first out.
- **Queue** in BFS: first in, first out.
- **Recursion** in DFS: the call stack remembers where to come back to.

## Questions ma'am might ask

**Why is this a graph and not a tree?** In a tree every node has exactly one parent and there are no loops. Here `GATE` can be reached from 5 different scenes (in-degree 5) and there are loops (`GATE -> WELL -> GATE`), so it's a general directed graph.

**Why BFS for shortest path and not DFS?** BFS explores in order of distance, so the first time it reaches an ending, that is the closest. DFS might go down a long path first.

**Why do BFS/DFS need a visited list?** Because the story has cycles. Without it, `GATE -> WELL -> GATE -> WELL ...` would run forever.

**What is the time complexity of BFS and DFS?** O(V + E), where V = 12 scenes and E = 19 choices. (Listing all paths with DFS can grow faster on big graphs, because the number of paths can be very large.)

**Why is Scene abstract?** We never create a plain scene, only a `NormalScene` or an `EndingScene`. Each must say if it is an ending (`isEnding()`).

**Where is polymorphism?** In `Game.play()`, `scene.display()` is called on a `Scene` variable, but an `EndingScene` runs its own overridden `display()` that also prints THE END.

**What is an ADT and where did you use it?** An ADT is a data type defined by its operations, not its storage. We have three: `GraphADT`, `StackADT` and `QueueADT` (all interfaces). `StoryGraph`, `LinkedStack` and `LinkedQueue` implement them. If we changed `StoryGraph` to use an adjacency matrix, the rest of the program would not change, because it only calls the ADT's methods.

**Why use your own stack and queue instead of Java's?** To show how they work inside: a linked list of `Node`s, with `top` for the stack and `front`/`rear` for the queue. Push, pop, enqueue and dequeue are all O(1).

**How would you add a new scene?** Only `Story.java` changes: one `addScene(...)` and some `addChoice(...)` lines. The graph code doesn't change.
