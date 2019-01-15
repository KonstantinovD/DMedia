package dmedia.view;

import dmedia.controller.Controller;
import dmedia.model.media.MediaState;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class PanelView {

    /**
     * Sets all GUI settings appropriate for new media has been loaded
     * It also binds {@code MediaPlayer} to sliders
     * @param mp
     */
    public void initNewMedia(MediaPlayer mp){
        //durationSlider.setMax(mp.getMedia().getDuration().toSeconds());
        //volumeSlider.setMax(mp.getVolume());
        bindVolumeSliderToPlayer(mp);
        bindVideoSliderToPlayer(mp);
    }

    private MediaState mMediaState;
    /**
     * Sets model for current view and enables view to listen model
     * @param model
     */
    public void setModel(MediaState model){
        mMediaState = model;
        model.getVolumeProperty().addListener((observable, oldValue, newValue) -> {
            setVolume(newValue.doubleValue());
        });
        model.getIsPlayedProperty().addListener((observable, oldValue, newValue) -> {
            setPlayed(newValue);
        });
        model.getIsLoadedProperty().addListener((observable, oldValue, newValue) -> {
            setLoaded(newValue);
        });
        model.getIsSoundOnProperty().addListener((observable, oldValue, newValue) -> {
            setSoundOn(newValue);
        });
    }

    private Controller mController;
    /**
     * Sets controller for current view
     * @param controller
     */
    public void setController(Controller controller){
        mController = controller;
    }

    public void setVisible(boolean isVisible){
        frontPanel.setVisible(isVisible);
    }


    //
    private void setVolume(double volume){
        volumeSlider.setValue(volume);
    }

    private void setSoundOn(boolean isSoundOn){
        if(isSoundOn){
            soundModeImage.setImage(imageSoundOn);
        }else{
            soundModeImage.setImage(imageSoundOff);
        }
    }

    private void setDuration(double duration){
        durationSlider.setValue(duration * durationSlider.getMax());
    }

    private void setPlayed(boolean isPlayed){
        if(isPlayed){
            playButton.setGraphic(new ImageView(imagePause));
        }else{
            playButton.setGraphic(new ImageView(imagePlay));
        }
    }

    //TODO: change header of app
    private void setLoaded(boolean isLoaded){

    }


    @FXML
    private VBox rootPanel;

    @FXML
    private VBox frontPanel;

    @FXML
    private Slider durationSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ChoiceBox choiceSpeed;
    @FXML
    private ImageView choiceSpeedImage;

    @FXML
    private ImageView soundModeImage;

    @FXML
    private Button openfileButton;
    @FXML
    private Button playButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button exitButton;



    private boolean mIsInitialized = false;
    @FXML
    public void initialize() {
        mIsInitialized = true;

        String relativePathImgDir = "../resources/images/";
        imagePlay = new Image(getClass().getResourceAsStream(relativePathImgDir + "player_play.png"), 32, 32, false, false);
        imagePause = new Image(getClass().getResourceAsStream(relativePathImgDir + "player_pause.png"), 32, 32, false, false);
        imageSoundOn = new Image(getClass().getResourceAsStream(relativePathImgDir + "player_soundon.png"), 32, 32, false, false);
        imageSoundOff = new Image(getClass().getResourceAsStream(relativePathImgDir + "player_soundoff.png"), 32, 32, false, false);
        imageChoiceSpeed = new Image(getClass().getResourceAsStream(relativePathImgDir + "player_speed.png"), 32, 32, false, false);

        playButton.setGraphic(new ImageView(imagePlay));
        choiceSpeedImage.setImage(imageChoiceSpeed);
        soundModeImage.setImage(imageSoundOn);

        initChoiceSpeed();
        initSliders();
        if(mController != null) {
            mController.informControllerAboutLoading();
        }
    }
    public boolean isInitialized(){ return mIsInitialized; }


    private void initChoiceSpeed(){
        choiceSpeed.getItems().addAll(0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 2.0);
        choiceSpeed.setValue(1.0);
    }

    private void initSliders(){
        durationSlider.setMax(1.0);
        volumeSlider.setMax(1.0);
    }

    private void bindVideoSliderToPlayer(MediaPlayer mp){
        mp.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            durationSlider.setValue(newValue.toSeconds() / mp.getMedia().getDuration().toSeconds());
        });
    }

    private void bindVolumeSliderToPlayer(MediaPlayer mp){
        mp.volumeProperty().addListener(observable -> {
            volumeSlider.setValue(mp.getVolume());
        });
    }



    //image resources
    private Image imagePlay;
    private Image imagePause;
    private Image imageSoundOn;
    private Image imageSoundOff;
    private Image imageChoiceSpeed;


    //getters for controller
    public VBox getRootPanel() {
        return rootPanel;
    }

    public VBox getFrontPanel() {
        return frontPanel;
    }

    public Slider getDurationSlider() {
        return durationSlider;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public ChoiceBox getChoiceSpeed() {
        return choiceSpeed;
    }

    public ImageView getSoundModeImage() {
        return soundModeImage;
    }

    public Button getOpenfileButton() {
        return openfileButton;
    }

    public Button getPlayButton() {
        return playButton;
    }

    public Button getStopButton() {
        return stopButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}
