module Interactives.gui {
    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.commons.io;
    requires java.desktop;

    opens Interactives.gui to javafx.graphics;
    exports Interactives.gui;
}