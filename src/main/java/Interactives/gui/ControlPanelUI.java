package Interactives.gui;

import components.firewallman.FirewallManager;
import components.portscan.PortScanner;
import components.threadhandler.ThreadHandler;
import components.warnman.WarningManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;



/**
 * This class represents the Control Panel UI for the firewall project.
 * It utilizes JavaFX to create a graphical user interface.
 */
public class ControlPanelUI extends Application {


    private int width = 1280; //setting default values for now
    private int height = 960;
    private String windowName = "JavaFireWall";
    private int buffer;
    private String inputToggle = "none";
    private Text mainText;


    //JavaFX objects
    private Group group;
    private Scene scene;
    private GraphicsContext gc;
    private Canvas canvas;

    /**
     * Overrides main fucntion, the launching point for javaFX UI
     *
     * @param stage the stage for all javaFX elements to be added to
     */
    @Override
    public void start(Stage stage) {

        //Setting up basic javaFX stuff
        canvas = new Canvas(width, height);
        stage.setTitle(windowName);
        gc = canvas.getGraphicsContext2D();
        group = new Group(canvas); //Group acts as root
        scene = new Scene(group, width, height);
        stage.setScene(scene);

        buffer = (int) ((canvas.getWidth() + canvas.getHeight()) / 80);

        //Setting up the backend
        ThreadHandler threadHandler = new ThreadHandler();
        FirewallManager firewallManager = new FirewallManager();
        PortScanner portScanner = new PortScanner();
        WarningManager warningManager = new WarningManager();




        //Actually showing things to the screen
        drawBackground();
        createButtons();
        createInputField();

        stage.show();
    }


    /**
        Draws the background elements of the UI and adds them to the root
     */
    private void drawBackground(){
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();


        //Fill Grey in background
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        //Add places for text boxes
        ArrayList<Rectangle> textPlacements = new ArrayList<>();
        textPlacements.add(new Rectangle((canvasWidth / 4) + buffer, buffer, (canvasWidth / 2) - (buffer * 2), canvasHeight * 5 / 8)); // Main textbox
        textPlacements.add(new Rectangle((canvasWidth * 3 / 4), buffer, (canvasWidth / 4) - buffer, canvasHeight / 4)); // Right textbox

        //Set the color of all text placements to black and round corners
        for (Rectangle rect : textPlacements) {
            rect.setFill(Color.BLACK);
            rect.setArcWidth(20);
            rect.setArcHeight(20);
        }

        //Add all labels for text boxes
        ArrayList<Text> panelLabels = new ArrayList<>();
        double textOffsetY = 20;
        panelLabels.add(new Text(((canvasWidth / 4) + buffer) + ((canvasWidth / 2) - (buffer * 2)) / 2, buffer + textOffsetY, "Main Panel")); // Main panel label
        panelLabels.add(new Text((canvasWidth * 3 / 4) + ((canvasWidth / 4) - buffer) / 2, buffer + textOffsetY, "Warning Panel")); // Right panel label






        //Change all text to green and possibly change font in the future
        for (Text text: panelLabels){
            text.setFill(Color.GREEN);
            text.setTextAlignment(TextAlignment.CENTER);
            centerText(text);
        }

        //Add all collections to group
        group.getChildren().addAll(textPlacements);
        group.getChildren().addAll(panelLabels);
    }


    /**
     * Creates buttons for UI and adds to root
     *
     * Establishes functions for each of the buttons when pushed
     */
    private void createButtons(){
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        //Text box for main display
        mainText = new Text((canvasWidth / 4) + buffer * 1.5, canvas.getHeight() * 5 / 8, "Testing");
        mainText.setFill(Color.GREEN);
        mainText.setTextAlignment(TextAlignment.LEFT);



        Button warningButton = new Button("Show Warnings");
        double warningTextBoxWidth = (canvasWidth / 4) - buffer;
        warningButton.setLayoutX(((canvasWidth * 3 / 4) + ((canvasWidth / 4) - buffer) / 2) - (warningTextBoxWidth / 4));
        warningButton.setLayoutY(buffer * 1.5 + canvasHeight / 4);
        warningButton.setPrefWidth(warningTextBoxWidth / 2);

        //Method that is called on button press for listing warnings
        EventHandler<ActionEvent> warningsButtonPress = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                displayWarning(mainText);
            }
        };

        warningButton.setOnAction(warningsButtonPress);


        double mainTextPanelWidth = (canvasWidth / 2) - (buffer * 2);
        Button listBlockedIPsButton = new Button("List Blocked IPs");
        listBlockedIPsButton.setLayoutX((canvasWidth / 4) + buffer);
        listBlockedIPsButton.setLayoutY(canvasHeight * 5 / 8 + buffer * 1.5);
        listBlockedIPsButton.setPrefWidth(mainTextPanelWidth / 4);

        //Method that is called on button press for listing blocked IPs
        EventHandler<ActionEvent> blockedIPsButtonPress = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                displayBlockedIPs(mainText);
            }
        };

        listBlockedIPsButton.setOnAction(blockedIPsButtonPress);

        Button listOpenPortsButton = new Button("List Open Ports");
        listOpenPortsButton.setLayoutX(listBlockedIPsButton.getLayoutX() + listBlockedIPsButton.getPrefWidth() + buffer);
        listOpenPortsButton.setLayoutY(canvasHeight * 5 / 8 + buffer * 1.5);
        listOpenPortsButton.setPrefWidth(mainTextPanelWidth / 4 - buffer);

        //Method that is called on button press for listing open ports
        EventHandler<ActionEvent> openPortsButtonPress = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                displayOpenPorts(mainText);
            }
        };

        listOpenPortsButton.setOnAction(openPortsButtonPress);




        //add text for toggle inputs
        Text toggleInfo = new Text((canvasWidth / 4) + buffer, canvasHeight * 5 / 8 + buffer * 4, "Input Toggles:");
        toggleInfo.setFill(Color.BLACK);

        //add buttons for toggle
        //add button to block IP
        Button addBlockedIPButton = new Button("Add Blocked IP");
        addBlockedIPButton.setLayoutX(toggleInfo.getX());
        addBlockedIPButton.setLayoutY(toggleInfo.getY() + buffer / 2.0);
        addBlockedIPButton.setPrefWidth(mainTextPanelWidth / 4 - buffer * .8);

        //Method that changes toggle to change input to add blocked IPs
        EventHandler<ActionEvent> addBlockedIPs = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inputToggle = "block ip";
                addTextToPanel(mainText, "Input set to Block IP");
            }
        };

        addBlockedIPButton.setOnAction(addBlockedIPs);

        //add button to change input to remove blocked IPs
        Button removeBlockedIPButton = new Button("Remove Blocked IP");
        removeBlockedIPButton.setLayoutX(addBlockedIPButton.getLayoutX() + addBlockedIPButton.getPrefWidth() + buffer);
        removeBlockedIPButton.setLayoutY(addBlockedIPButton.getLayoutY());
        removeBlockedIPButton.setPrefWidth(addBlockedIPButton.getPrefWidth());

        //Method that changes toggle to remove blocked IPs
        EventHandler<ActionEvent> removeBlockedIPs = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inputToggle = "remove blocked ip";
                addTextToPanel(mainText, "Input set to Remove Blocked IP");
            }
        };

        removeBlockedIPButton.setOnAction(removeBlockedIPs);

        //add button to change input to block port
        Button addBlockedPortButton = new Button( "Add Blocked Port");
        addBlockedPortButton.setLayoutX(removeBlockedIPButton.getLayoutX() + removeBlockedIPButton.getPrefWidth() + buffer);
        addBlockedPortButton.setLayoutY(removeBlockedIPButton.getLayoutY());
        addBlockedPortButton.setPrefWidth(removeBlockedIPButton.getPrefWidth());

        //Method to change toggle to add blocked ports
        EventHandler<ActionEvent> addBlockedPort = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inputToggle = "block port";
                addTextToPanel(mainText, "Input set to Block Port");
            }
        };

        addBlockedPortButton.setOnAction(addBlockedPort);


        //add button to change input to remove blocked port
        Button removeBlockedPortButton = new Button( "Remove Blocked Port");
        removeBlockedPortButton.setLayoutX(addBlockedPortButton.getLayoutX() + addBlockedPortButton.getPrefWidth() + buffer);
        removeBlockedPortButton.setLayoutY(addBlockedPortButton.getLayoutY());



        //Method to change toggle to remove blocked ports
        EventHandler<ActionEvent> removeBlockedPort = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                inputToggle = "remove blocked port";
                addTextToPanel(mainText, "Input set to Remove Blocked Port");
            }
        };

        removeBlockedPortButton.setOnAction(removeBlockedPort);




        //add buttons to group
        group.getChildren().add(warningButton);
        group.getChildren().add(listBlockedIPsButton);
        group.getChildren().add(listOpenPortsButton);
        group.getChildren().add(addBlockedIPButton);
        group.getChildren().add(removeBlockedIPButton);
        group.getChildren().add(addBlockedPortButton);
        group.getChildren().add(removeBlockedPortButton);


        //add text to group
        group.getChildren().add(mainText);
        group.getChildren().add(toggleInfo);
    }

    /**
     * Displays warning texts to main display
     *
     * @param textPanel text panel that the text will be added to
     */
    private void displayWarning(Text textPanel){
        String newText = "Testing Warnings";
        addTextToPanel(textPanel, newText);
    }

    /**
     * Display blocked IPs to main display
     *
     * @param textPanel text panel that the text will be added to
     */
    private void displayBlockedIPs(Text textPanel){
        String newText = "Testing Blocked IPs";
        addTextToPanel(textPanel, newText);
    }

    /**
     * Display open ports to main display
     *
     * @param textPanel text panel that the text will be added to
     */
    private void displayOpenPorts(Text textPanel){
        String newText = "Testing Open Ports";
        addTextToPanel(textPanel, newText);
    }

    /**
     * Adds text to panel and makes sure it fits in the size of panel
     *
     * @param textPanel text panel that the text will be added to
     * @param newText text being added to panel
     */
    private void addTextToPanel(Text textPanel, String newText) {
        String currentText = textPanel.getText();
        currentText = currentText + "\n" + newText;

        String[] lines = currentText.split("\n");

        double maxHeight = canvas.getHeight() * 5 / 8;

        StringBuilder sb = new StringBuilder();
        for (int i = lines.length - 1; i >= 0; i--) {
            sb.insert(0, lines[i] + "\n");
            textPanel.setText(sb.toString().trim());

            double textHeight = textPanel.getBoundsInLocal().getHeight();

            if (textHeight > maxHeight) {
                sb = new StringBuilder();
                for (int j = i + 1; j < lines.length; j++) {
                    sb.append(lines[j]).append("\n");
                }
                break;
            }
        }

        textPanel.setText(sb.toString().trim());

        double textHeight = textPanel.getBoundsInLocal().getHeight();
        textPanel.setY(maxHeight - textHeight + buffer);
    }


    /**
     * Creates input field element for UI and adds to root
     */
    private void createInputField(){
        //Create text above inputs
        Text inputText = new Text(canvas.getWidth() / 4 + buffer, canvas.getHeight() * 5 / 8 + buffer * 6.5, "Input:");

        //Create text field
        TextField inputTextField = new TextField();
        inputTextField.setPrefWidth(canvas.getWidth() / 2);
        inputTextField.setLayoutX(inputText.getX());
        inputTextField.setLayoutY(inputText.getY() + buffer / 2);
        inputTextField.setPromptText("Add or Remove Blocked IPs and Ports");


        //Create method to handle inputs
        EventHandler<ActionEvent> resolveInput = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String input = inputTextField.getText();
                switch (inputToggle){
                    case "none":
                        addTextToPanel(mainText, "Please choose an Input Option");
                        break;

                    case "block ip":
                        //addTextToPanel(mainText, "Testing block ip input: " + input);
                        blockIP(input);
                        break;

                    case "remove blocked ip":
                        // addTextToPanel(mainText, "Testing remove blocked ip input: " + input);
                        removeBlockedIP(input);
                        break;

                    case "block port":
                        //addTextToPanel(mainText, "Testing block port input: " + input);
                        blockPort(input);
                        break;

                    case "remove blocked port":
                        //addTextToPanel(mainText, "Testing remove blocked port input: " + input);
                        removeBlockedPort(input);
                        break;
                }
                inputTextField.clear();
            }
        };


        inputTextField.setOnAction(resolveInput);

        //add elements to group
        group.getChildren().add(inputText);
        group.getChildren().add(inputTextField);
    }

    /**
     * Blocks the IP that is entered into the input field
     *
     * @param input input that will be validated to retrieve an IP that will be blocked
     */
    private void blockIP(String input){
        // TODO: Implement this method
    }

    /**
     * Unblocks the IP that is entered into the input field
     *
     * @param input input will be validated to retrieve an IP that will be unblocked
     */
    private void removeBlockedIP(String input){
        // TODO: Implement this method
    }

    /**
     * Blocks the port that is entered into the input field
     *
     * @param input  input will be validated to retrieve a port that will be blocked
     */
    private void blockPort(String input){
        // TODO: Implement this method
    }


    /**
     * Unblocks the port that is entered into the input field
     *
     * @param input input will be validated to retrieve a port that will be unblocked
     */
    private void removeBlockedPort(String input){
        // TODO: Implement this method
    }


    /**
     * Clears all UI elements, not used but might be needed in the future
     *
     * @param group root that holds all elements to be cleared
     */
    private void clearUI(Group group){
        group.getChildren().removeAll();
    }


    /**
     * Centers Text around its x and y location
     *
     * @param text text element that will be centered
     */
    private void centerText(Text text) {
        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();

        text.setX(text.getX() - (textWidth / 2));
        text.setY(text.getY() - (textHeight / 4));
    }

    /**
     * Overridden
     *
     * @param args command line arguments
     */
    public static void launchControlPanel(String[] args) {
        launch(args);
    }
}