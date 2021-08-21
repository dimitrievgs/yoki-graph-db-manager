package org.yoki_manager.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MessageBox {
    public static void Show(String Text, String Header, String Caption, Alert.AlertType alert_Type, int Width) {
        Alert alert = new Alert(alert_Type);
        alert.setTitle(Caption);
        alert.setHeaderText(Header);
        alert.setContentText(Text);
        alert.getDialogPane().setMinWidth(Width);
        //alert.setWidth(1200);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }

    public static void Show(String Text, String Header, String Caption, Alert.AlertType alert_Type) {
        Show(Text, Header, Caption, alert_Type, (int)(Screen.getPrimary().getVisualBounds().getWidth() / 3.0));
    }

    public static void Show(String Text) {
        Show(Text, "", "Warning", Alert.AlertType.INFORMATION);
    }

    //https://stackoverflow.com/questions/26336437/java-exception-handling-get-console-error-message
    public static void Show(Exception e) {
        List<String> result = getExceptionDump(e);
        Show(result.get(2), result.get(1), result.get(0), Alert.AlertType.ERROR);
    }

    private static char delimiter = '.';

    private static List<String> getExceptionDump(Exception ex) {
        List<String> result = new ArrayList<>();
        StringBuilder error_strb = new StringBuilder();
        String error_Header = "";
        error_strb.append("Details: ");
        for (Throwable cause = ex; cause != null; cause = cause.getCause()) {
            if (error_strb.length() > 0)
                error_strb.append("Caused by: ");
            error_strb.append(cause.getClass().getName());
            error_strb.append(": ");
            error_strb.append(cause.getMessage());
            if (error_Header == "") error_Header = cause.getClass().getName() + ":\n" + cause.getMessage();
            //error_strb.append("\n");
            for (StackTraceElement element: cause.getStackTrace()) {
                //error_strb.append("\t");
                error_strb.append("at ");
                error_strb.append(element.getMethodName());
                error_strb.append("(");
                error_strb.append(element.getFileName());
                error_strb.append(":");
                error_strb.append(element.getLineNumber());
                error_strb.append(")");
                //error_strb.append("\n");
            }
        }
        String title;
        String[] cause_top_array = ex.getClass().getName().split(Pattern.quote(String.valueOf(delimiter))); //input.split("[@\\^]");
        if (cause_top_array.length > 0) title = cause_top_array[cause_top_array.length - 1];
        else title = "Error";
        result.add(title);
        result.add(error_Header);
        result.add(error_strb.toString());
        return result;
    }

    public static void LicenseDialog2()
    {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);

        VBox vbox = new VBox(new Text("Hi"), new Button("Ok."));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));

        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }

    public static void LicenseDialog(String title){
        Stage dialogStage = new Stage();
        GridPane grd_pan = new GridPane();
        grd_pan.setAlignment(Pos.CENTER);
        grd_pan.setHgap(10);
        grd_pan.setVgap(10);//pading
        int w = 600, h = 600, h_gap = 35, w_gap = 8;
        Scene scene =new Scene(grd_pan,w + w_gap * 2,h + h_gap * 2);
        dialogStage.setScene(scene);
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);

        TextArea lab_alert= new TextArea();
        String s = ReadLicenseFile();
        /*String nl = System.getProperty("line.separator");
        for (int i = 0; i < 100; i++) {
            s += "dfdf <br/>" + nl;
        }*/
        lab_alert.setText(s);
        lab_alert.setEditable(false);
        lab_alert.setMinWidth(w);
        lab_alert.setMinHeight(h);
        grd_pan.add(lab_alert, 0, 1);

        Button btn_ok = new Button("OK");
        btn_ok.setMinWidth(80);
        btn_ok.setMinHeight(30);
        btn_ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                // TODO Auto-generated method stub
                dialogStage.hide();

            }
        });
        grd_pan.add(btn_ok, 0, 2);

        dialogStage.show();

    }

    private static String ReadLicenseFile()
    {
        String everything = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("LICENSE.md"));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } finally {
                br.close();
            }
        }
        catch (Exception s)
        {

        }
        return everything;
    }
}
