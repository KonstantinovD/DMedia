package dmedia.view.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class ApplicationAlert extends Alert {
    public ApplicationAlert(Alert.AlertType type){
        super(type);
        setCSS();
        this.setTitle("");
    }

    public ApplicationAlert(Alert.AlertType type, String content){
        this(type);
        this.setContentText(content);
    }

    public ApplicationAlert(Alert.AlertType type, String content, String header){
        this(type, content);
        this.setHeaderText(header);
    }

    private void setCSS(){
        this.getDialogPane().getStylesheets().add(getClass().getResource("ApplicationAlert.css").toExternalForm());
        this.getDialogPane().getStyleClass().add("myDialog");
    }
}
