/**
 * One directed EDGE of the story graph.
 *
 * It means: "from the scene that owns this choice, the player may go to
 * the scene called targetId". The edge does not store where it starts,
 * because it is kept inside the adjacency list of the scene it starts from.
 *
 * requiredItem: if not null, the player must be carrying this item to use
 * the edge (a "locked" edge).
 */
public class Choice {
    private String label;
    private String targetId;
    private String requiredItem;

    public Choice(String label, String targetId, String requiredItem) {
        this.label = label;
        this.targetId = targetId;
        this.requiredItem = requiredItem;
    }

    public String getLabel() {
        return label;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getRequiredItem() {
        return requiredItem;
    }

    public boolean isLocked() {
        return requiredItem != null;
    }
}
