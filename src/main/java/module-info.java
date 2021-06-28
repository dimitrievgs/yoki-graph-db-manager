module org.vanilla_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires orientdb.core;

    opens org.yoki_manager to javafx.fxml;
    exports org.yoki_manager;
    exports org.yoki_manager.orientdb;
    opens org.yoki_manager.orientdb to javafx.fxml;
    exports org.yoki_manager.general;
    opens org.yoki_manager.general to javafx.fxml;
    exports org.yoki_manager.orientdb.controls;
    opens org.yoki_manager.orientdb.controls to javafx.fxml;
    exports org.yoki_manager.orientdb.controls.odocument;
    opens org.yoki_manager.orientdb.controls.odocument to javafx.fxml;
    exports org.yoki_manager.orientdb.controls.oproperty;
    opens org.yoki_manager.orientdb.controls.oproperty to javafx.fxml;
    exports org.yoki_manager.orientdb.treetableview;
    opens org.yoki_manager.orientdb.treetableview to javafx.fxml;
    exports org.yoki_manager.trash;
    opens org.yoki_manager.trash to javafx.fxml;
    exports org.yoki_manager.dialogs;
    opens org.yoki_manager.dialogs to javafx.fxml;
    exports org.yoki_manager.orientdb.controls.titledpanes;
    opens org.yoki_manager.orientdb.controls.titledpanes to javafx.fxml;
}