import java.util.ArrayList;

/**
 * The person playing: a name and an inventory (ArrayList of item names).
 */
public class Player {
    private String name;
    private ArrayList<String> inventory = new ArrayList<String>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addItem(String item) {
        if (!inventory.contains(item)) {
            inventory.add(item);
            System.out.println("[You picked up: " + item + "]");
        }
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }

    public ArrayList<String> getInventory() {
        return inventory;
    }
}
