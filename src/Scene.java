/**
 * ABSTRACT CLASS: one VERTEX (node) of the story graph.
 *
 * A scene only knows about itself: its id, title, text and an optional item
 * the player finds here. It does NOT know where the player can go next.
 * Those connections (the EDGES) are stored in StoryGraph's adjacency list.
 *
 * It is abstract because we never make a plain "Scene". We make either a
 * NormalScene or an EndingScene (INHERITANCE).
 */
public abstract class Scene implements Displayable {
    protected String id;      // unique key, for example "GATE"
    protected String title;   // short heading
    protected String text;    // the story text
    protected String item;    // item found here, or null if there is none

    public Scene(String id, String title, String text, String item) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getItem() {
        return item;
    }

    /** Every subclass must say whether it ends the story. */
    public abstract boolean isEnding();

    /** Shared code for printing the title and the wrapped text. */
    public void display() {
        System.out.println();
        System.out.println("--- " + title + " ---");
        System.out.println(TextUtil.wrap(text, 70));
    }

    public String toString() {
        return id + " (" + title + ")";
    }
}
