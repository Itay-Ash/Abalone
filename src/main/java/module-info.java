module GuiClasses {
    requires javafx.controls;
    requires javafx.fxml;


    opens Game_Package to javafx.fxml;
    exports Game_Package;
    exports Helpers;
    opens Helpers to javafx.fxml;
    exports Enums;
    opens Enums to javafx.fxml;
}