package Interactives.cli;

public class Command {
    private final String name;
    private final String description;

    /**
     * Constructs a new instance of Command with the specified name and description.
     *
     * @param name the name of the command
     * @param description the description of the command
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns a string representation of the Command object, including the name and description.
     *
     * @return a formatted string containing the name and description of the command
     */
    @Override
    public String toString() {
        return String.format("name: %s%n description: %s%n", name, description);
    }
}