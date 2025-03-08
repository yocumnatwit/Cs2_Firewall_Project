package Interactives.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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


    //JavaFX objects
    private Group group;
    private Scene scene;
    private GraphicsContext gc;
    private Canvas canvas;


    @Override
    public void start(Stage stage) {

        //Setting up basic javaFX stuff
        canvas = new Canvas(width, height);
        stage.setTitle(windowName);
        gc = canvas.getGraphicsContext2D();
        group = new Group(canvas); //Group acts as root
        scene = new Scene(group, width, height);
        stage.setScene(scene);


        //Actually showing things to the screen
        drawBackground();
        createButtons();

        stage.show();
    }

    private void drawBackground(){
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        int buffer = (int) ((canvasWidth + canvasHeight) / 80);


        //Fill Grey in background
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        //Add places for text boxes
        ArrayList<Rectangle> textPlacements = new ArrayList<>();
        textPlacements.add(new Rectangle((canvasWidth / 4) + buffer, buffer, (canvasWidth / 2) - (buffer * 2), canvasHeight * 5 / 8)); // Main textbox
        textPlacements.add(new Rectangle(buffer, buffer, (canvasWidth / 4) - buffer, canvasHeight / 4)); // Left textbox
        textPlacements.add(new Rectangle((canvasWidth * 3 / 4), buffer, (canvasWidth / 4) - buffer, canvasHeight / 4)); // Right textbox

        //Set the color of all textplacements to black and round corners
        for (Rectangle rect : textPlacements) {
            rect.setFill(Color.BLACK);
            rect.setArcWidth(20);
            rect.setArcHeight(20);
        }

        //Add all labels for text boxes
        ArrayList<Text> panelLabels = new ArrayList<>();
        double textOffsetY = 20;
        panelLabels.add(new Text(((canvasWidth / 4) + buffer) + ((canvasWidth / 2) - (buffer * 2)) / 2, buffer + textOffsetY, "Main Panel")); // Main panel label
        panelLabels.add(new Text(buffer + ((canvasWidth / 4) - buffer) / 2, buffer + textOffsetY, "Placeholder Panel")); // Left panel label
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

    private void createButtons(){
        int buffer = (int) ((canvas.getWidth() + canvas.getHeight()) / 80);
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        //Text box for main display
        Text mainText = new Text(canvas.getWidth() / 2, canvas.getHeight() * 5 / 8 + buffer, "Testing");



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

        Button listBlockedIPsButton = new Button("List Blocked IPs");
        listBlockedIPsButton.setLayoutX((canvasWidth / 4) + buffer);
        listBlockedIPsButton.setLayoutY(canvasHeight * 5 / 8 + buffer * 1.5);

        //Method that is called on button press for listing blocked IPs
        EventHandler<ActionEvent> blockedIPsButtonPress = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                displayBlockedIPs(mainText);
            }
        };







        //add buttons to group
        group.getChildren().add(warningButton);
        group.getChildren().add(listBlockedIPsButton);
    }

    //Displays warning texts to main display
    private void displayWarning(Text textPanel){

    }

    //Display blocked IPs to main display
    private void displayBlockedIPs(Text textPanel){

    }





    //clears the background
    private void clearUI(Group group){
        group.getChildren().removeAll();
    }


    //Centers Text around its x and y location
    private void centerText(Text text) {
        double textWidth = text.getBoundsInLocal().getWidth();
        double textHeight = text.getBoundsInLocal().getHeight();

        text.setX(text.getX() - (textWidth / 2));
        text.setY(text.getY() - (textHeight / 4));
    }

    public static void launchControlPanel(String[] args) {
        launch(args);
    }
}