package org.vanilla_manager;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MyDialog extends Dialog {
    //private MyController сontroller;
    private AnchorPane rootLayout;

    public MyDialog(String name) {
        try {
            setTitle("My dialog");
            setResizable(false);

            loadContent();

            var dialogPane = getDialogPane();
            dialogPane.setContent(rootLayout);

            setStyle(dialogPane);
        } catch (IOException e) {
            MessageBox.Show(e); //e.printStackTrace();
        }
    }

    private void loadContent() throws IOException {
        //var loader = new FXMLLoader();
        //var classLoader = MyDialog.class.getClassLoader();
        //loader.setLocation(classLoader.getResource("add_overtex.fxml"));

        //FXMLLoader loader = new FXMLLoader(ReportMenu.this.getClass().getResource("/fxml/" + report.getClass().getCanonicalName().substring(18).replaceAll("Controller", "") + ".fxml"));

        FXMLLoader loader = new FXMLLoader(MyDialog.this.getClass().getResource("add_overtex.fxml"));

        rootLayout = loader.load();
        //сontroller = loader.getController();
    }

    private void setStyle(DialogPane dialogPane) {
        var stage = (Stage) dialogPane.getScene().getWindow();
        var classLoader = MyDialog.class.getClassLoader();

        stage.getIcons().add(new Image("image/avatar.png"));

        dialogPane.getStylesheets()
                .add(classLoader.getResource("css/style.css").toString());

        stage.setOnCloseRequest(event -> {
            stage.close();
            //сontroller.getMediaPlayer().stop();
        });
    }
}