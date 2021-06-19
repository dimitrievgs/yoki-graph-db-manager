module org.vanilla_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires orientdb.core;
    /*requires orientdb.graphdb;
    requires orientdb.graphdb;
    requires blueprints.core;
    requires orientdb.core;*/

    opens org.vanilla_manager to javafx.fxml;
    exports org.vanilla_manager;
    exports org.vanilla_manager.orientdb;
    opens org.vanilla_manager.orientdb to javafx.fxml;
    exports org.vanilla_manager.OVertex_Controls;
    opens org.vanilla_manager.OVertex_Controls to javafx.fxml;
}