package Interactives.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Rectangle;
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
        textPlacements.add(new Rectangle((canvasWidth / 4) + buffer, buffer,(canvasWidth / 2) - (buffer * 2), (canvasHeight * 5 / 8))); //Add main textbox
        textPlacements.add(new Rectangle(buffer, buffer, ((canvasWidth / 4) - buffer), canvasHeight / 4)); //Adds Left Textbox unsure of what is going there for now
        textPlacements.add(new Rectangle((canvasWidth * 3 / 4), buffer, ((canvasWidth / 4) - buffer ), canvasHeight / 4));

        //Set the color of all textplacements to black and round corners
        for (int i = 0; i < textPlacements.size(); i++){
            textPlacements.get(i).setFill(Color.BLACK);
            textPlacements.get(i).setArcWidth(20);
            textPlacements.get(i).setArcHeight(20);
        }


        group.getChildren().addAll(textPlacements);
    }

    //clears the background
    private void clearUI(Group group){
        group.getChildren().removeAll();
    }

    public static void launchControlPanel(String[] args) {
        launch(args);
    }
}