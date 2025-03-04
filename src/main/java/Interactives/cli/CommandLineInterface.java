package Interactives.cli;

public class CommandLineInterface {
    private final Command[] commands;

    /**
     * Initializes a new instance of CommandLineInterface.
     * This constructor sets up default commands for interaction,
     * including a "Help" command with a description that provides assistance to users.
     */
    public CommandLineInterface() {
        this.commands = new Command[] {new Command("Help", "Displays a help text")};
    }

    private void executeCommand(String commandName) {
        // @ToDo: Write this method
    }

    private String[] parseInput(String input) {
        String[] delimitedInput = input.split(" ");

        return null; // @ToDo: Finish this method
    }

    public void showCommands() {
        for (Command command : commands) {
            System.out.print(command.toString());
        }
    }

}
