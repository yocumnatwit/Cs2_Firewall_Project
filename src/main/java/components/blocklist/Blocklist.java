package components.blocklist;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The purpose of Blocklist is to manage a list of Blockable objects.
 * It provides streamlined access and functionality to a preconstructed ArrayList.
 */
public class Blocklist {
    private final ArrayList<Blockable> listBlocked = new ArrayList<>();

    /**
     * Constructs a new Blocklist object that initializes an empty list of blocked items.
     */
    public Blocklist() {}

    /**
     * Constructs a new Blocklist object and initializes it with the specified array
     * of Blockable objects. Each object in the array is added to the internal list
     * of blocked items.
     *
     * @param blocklist the array of Blockable objects to be added to the blocked list
     */
    public Blocklist(Blockable[] blocklist) {
        Collections.addAll(listBlocked, blocklist);
    }

    /**
     * Adds a Blockable object to the list of blocked items.
     *
     * @param block the Blockable object to be added to the blocked list
     */
    public void addListBlocked(Blockable block) {
        listBlocked.add(block);
    }

    /**
     * Removes a Blockable object from the list of blocked items if an object with
     * the same id and name exists in the list.
     *
     * @param block the Blockable object to be removed from the blocked list
     */
    public void removeListBlocked(Blockable block) {
        listBlocked.removeIf(b -> b.getId().equals( block.getId() ) && b.getName().equals( block.getName() ) );
    }

    /**
     * Checks if a given Blockable object is present in the list of blocked items.
     *
     * @param block the Blockable object to be checked for presence in the blocked list
     * @return true if the specified Blockable object is in the blocked list, false otherwise
     */
    public boolean checkBlocked(Blockable block) {
        return listBlocked.contains(block);
    }

    /**
     * Returns the total number of Blockable objects currently in the blocked list.
     *
     * @return the number of Blockable objects in the blocked list
     */
    public int totalBlocked() {
        return listBlocked.size();
    }

    /*
     * Return raw ArrayList.
     * 
     * @return the source ArrayList
     */
    public ArrayList<Blockable> toArray(){
        return listBlocked;
    }



}