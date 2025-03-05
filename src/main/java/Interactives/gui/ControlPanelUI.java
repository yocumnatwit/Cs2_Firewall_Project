package Interactives.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

/**
 * This class represents the Control Panel UI for the firewall project.
 * It utilizes JavaFX to create a graphical user interface.
 */
public class ControlPanelUI extends Application {


    private int width = 800; //setting default values for now
    private int height = 800;
    private String windowName = "JavaFireWall";

    @Override
    public void start(Stage stage) {

        //Setting up basic javaFX stuff
        Canvas canvas = new Canvas(width, height);
        stage.setTitle(windowName);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Group group = new Group(canvas);
        Scene scene = new Scene(group, width, height);
        stage.setScene(scene);





        stage.show();
    }

    public static void launchControlPanel(String[] args) {
        launch(args);
    }
}