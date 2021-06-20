package org.vanilla_manager;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Screen;

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
}
