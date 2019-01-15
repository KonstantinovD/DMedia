package dmedia.view;

import dmedia.view.alerts.ApplicationAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import dmedia.Main;


public class RootView {

    private Stage mMainStage;

    @FXML
    private MediaView mediaView;

    @FXML
    private StackPane stackPane;

    public MediaView getMediaView(){
        return mediaView;
    }

    public void setMediaPlayer(MediaPlayer mp){ mediaView.setMediaPlayer(mp); }

    /**
     * Generates and displays error alert with specified content and header.
     * @param content
     * @param header
     */
    public void generateAlert(Alert.AlertType type, String content, String header){
        ApplicationAlert alert = new ApplicationAlert(type, content, header);
        alert.showAndWait();
    }

    /**
     * Generates and displays error alert with specified content.
     * @param content
     */
    public void generateAlert(Alert.AlertType type, String content){
        ApplicationAlert alert = new ApplicationAlert(type, content);
        alert.showAndWait();
    }

}
