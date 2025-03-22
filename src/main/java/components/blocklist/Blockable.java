package components.blocklist;

public class Blockable {
    private final String id;
    private final String name;

    /**
     * Constructs a new Blockable object with the specified id and name.
     *
     * @param id the unique identifier for the Blockable object
     * @param name the name associated with the Blockable object
     */
    public Blockable(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the unique identifier of the Blockable object.
     *
     * @return the unique identifier of the Blockable object
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the name associated with the Blockable object.
     *
     * @return the name associated with the Blockable object
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the Blockable object, including its
     * unique identifier and associated name.
     *
     * @return a string representation of the Blockable object in the format "Blockable[id=<id>, name=<name>]"
     */
    @Override
    public String toString() {
        return String.format("%s", name);
    }
}