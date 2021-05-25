module org.vanilla_manager {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.vanilla_manager to javafx.fxml;
    exports org.vanilla_manager;
}