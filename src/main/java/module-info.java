module org.vanilla_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires orientdb.core;

    opens org.vanilla_manager to javafx.fxml;
    exports org.vanilla_manager;
    exports org.vanilla_manager.orientdb;
    opens org.vanilla_manager.orientdb to javafx.fxml;
    exports org.vanilla_manager.general;
    opens org.vanilla_manager.general to javafx.fxml;
    exports org.vanilla_manager.orientdb.controls;
    opens org.vanilla_manager.orientdb.controls to javafx.fxml;
    exports org.vanilla_manager.orientdb.controls.odocument;
    opens org.vanilla_manager.orientdb.controls.odocument to javafx.fxml;
    exports org.vanilla_manager.orientdb.controls.oproperty;
    opens org.vanilla_manager.orientdb.controls.oproperty to javafx.fxml;
    exports org.vanilla_manager.orientdb.treetableview;
    opens org.vanilla_manager.orientdb.treetableview to javafx.fxml;
    exports org.vanilla_manager.trash;
    opens org.vanilla_manager.trash to javafx.fxml;
    exports org.vanilla_manager.dialogs;
    opens org.vanilla_manager.dialogs to javafx.fxml;
}