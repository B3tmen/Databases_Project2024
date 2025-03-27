package org.unibl.etf.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Optional;

public class AlertShower {
    private Alert.AlertType alertType;
    private String title;
    private String headerText;
    private String contentText;
    private ButtonType[] buttonTypes;

    public AlertShower(Alert.AlertType alertType, String title, String headerText, String contentText, ButtonType... buttons) {
        this.alertType = alertType;
        this.title = title;
        this.headerText = headerText;
        this.contentText = contentText;
        if(buttons.length > 0){
            this.buttonTypes = buttons;
        }
        else{
            this.buttonTypes = null;
        }

    }

    public Optional<ButtonType> showAlert(){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        if(alertType.equals(Alert.AlertType.CONFIRMATION)){
            alert.setGraphic(new ImageView(new Image(getClass().getResource(ImagePaths.ALERT_CUSTOM_CHECKMARK).toExternalForm())));
        }

        if (buttonTypes != null) {
            alert.getButtonTypes().setAll(buttonTypes);
        }
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }
}
