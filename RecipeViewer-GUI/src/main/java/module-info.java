module ru.morozovit.recipeviewer.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.desktop;
    requires org.jetbrains.annotations;
    requires util;
    requires ru.morozovit.recipeviewer;
    requires json.simple;

    opens ru.morozovit.recipeviewer.gui to javafx.fxml;
    exports ru.morozovit.recipeviewer.gui;
}