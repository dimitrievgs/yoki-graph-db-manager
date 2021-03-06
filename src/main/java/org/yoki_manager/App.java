package org.yoki_manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.yoki_manager.dialogs.MessageBox;
import org.yoki_manager.orientdb.OrientdbJavafx;

import java.io.IOException;
import java.io.InputStream;

//руководство по созданию проекта javafx: https://openjfx.io/openjfx-docs/
//javafx - overview: https://docs.oracle.com/javafx/2/ui_controls/overview.htm

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    public static void main(String[] args) {
        launch();
    }

    FXMLLoader fxmlLoader;

    OrientdbJavafx orientdbJavafx;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = GetLoader("primary");
        scene = new Scene(loader.load(), 640, 480);
        scene.getStylesheets().add(ResourcesManager.getResource("scene.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graph Database Manager (test)");
        primaryStage.setMaximized(true);
        addicon(primaryStage);

        orientdbJavafx = new OrientdbJavafx();

        PrimaryController controller = (PrimaryController) loader.getController();
        controller.postInitialize(primaryStage, orientdbJavafx);

        primaryStage.show();
    }

    @Override
    public void stop(){
        orientdbJavafx.closeDB(); //otherwise since orientdb 3.1.X it's not closing, needs to check out why
        System.out.println("Stage is closing");
    }

    private static FXMLLoader GetLoader(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ResourcesManager.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(GetLoader(fxml).load());
    }

    /********************************************/

    private void addicon(Stage primaryStage) {
        try {
            InputStream iconStream = ResourcesManager.getResourceAsStream("icons/graph-db-pic.png");
            Image image = new Image(iconStream);
            primaryStage.getIcons().add(image);
        } catch (Exception e) {
            MessageBox.Show(e);
        }
    }
}