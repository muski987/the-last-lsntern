/**
 * A scene in the middle of the story. The player picks a choice to move on.
 * (INHERITANCE: NormalScene IS-A Scene.)
 */
public class NormalScene extends Scene {

    public NormalScene(String id, String title, String text) {
        super(id, title, text, null);
    }

    public NormalScene(String id, String title, String text, String item) {
        super(id, title, text, item);   // CONSTRUCTOR OVERLOADING: with an item
    }

    public boolean isEnding() {
        return false;
    }
}
