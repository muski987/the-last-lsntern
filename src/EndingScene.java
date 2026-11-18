/**
 * A scene that finishes the story. In the graph it has NO outgoing edges
 * (out-degree 0).
 *
 * POLYMORPHISM: it overrides display() to also print a THE END banner.
 */
public class EndingScene extends Scene {
    private String endingType;   // "GOOD", "BAD" or "STRANGE"

    public EndingScene(String id, String title, String text, String endingType) {
        super(id, title, text, null);
        this.endingType = endingType;
    }

    public String getEndingType() {
        return endingType;
    }

    public boolean isEnding() {
        return true;
    }

    public void display() {
        super.display();   // print title and text the normal way first
        System.out.println();
        System.out.println("*********  THE END  (" + endingType + " ending)  *********");
    }
}
