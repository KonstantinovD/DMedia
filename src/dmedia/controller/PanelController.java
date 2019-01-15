package dmedia.controller;

import dmedia.model.media.MediaState;
import dmedia.view.PanelView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PanelController implements Controller{

    @Override
    public void informControllerAboutLoading() {
        initListeners();
    }

    private PanelView mPanelView;

    private MediaState mMediaState;

    public PanelController(){}

    public PanelController(MediaState model, PanelView panelView){
        setPanelView(panelView);
        setModel(model);
    }

    public void setPanelView(PanelView panelView){
        mPanelView = panelView;
        if(mPanelView.isInitialized()){
            //if model is initialised, it will not call informControllerAboutLoading() method
            initListeners();
        }
    }

    public void setModel(MediaState model){
        mMediaState = model;
    }

    private void initListeners(){
        mPanelView.getOpenfileButton().setOnAction(event -> {
            handleOpenFileButton();
        });
        mPanelView.getPlayButton().setOnAction(event -> {
            handlePlayButton();
        });
        mPanelView.getStopButton().setOnAction(event -> {
            handleStopButton();
        });
        mPanelView.getExitButton().setOnAction(event -> {
            handleExitButton();
        });
        mPanelView.getChoiceSpeed().valueProperty().addListener((observable, oldValue, newValue) -> {
            handleChoiceSpeedImage((Double)newValue);
        });
        mPanelView.getSoundModeImage().addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            handleSoundModeImage();
        });
        mPanelView.getVolumeSlider().valueProperty().addListener(observable -> {
            mMediaState.setVolume(mPanelView.getVolumeSlider().getValue());
        });
        mPanelView.getDurationSlider().valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mMediaState.setDuration(newValue.doubleValue());
            }
        });

        initHidePanelListeners();
    }


    private void handleOpenFileButton(){
        mMediaState.openMedia();
    }

    private void handlePlayButton(){
        mMediaState.reversePlayingMode();
    }

    private void handleStopButton(){
        mMediaState.stopMedia();
    }

    private void handleExitButton(){
        System.exit(0);
    }

    private void handleChoiceSpeedImage(double rate){
        mMediaState.setRate(rate);
    }

    private void handleSoundModeImage(){
        mMediaState.reverseSoundMode();
    }


    private boolean isMouseOverThePanel = false;
    private final Duration TIME_TO_HIDE = Duration.seconds(3);
    /**
     * Init listeners for border panel to hide it if mouse hasn't been
     * situated over the panel more than TIME_TO_HIDE seconds
     */
    private void initHidePanelListeners(){
        VBox rootPanel = mPanelView.getRootPanel();
        //set up timer for border panel hiding
        Timeline timer = new Timeline(new KeyFrame(TIME_TO_HIDE, event -> {
            if(!isMouseOverThePanel){
                mPanelView.setVisible(false);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
        //front bottom panel appears if mouse is entered
        rootPanel.setOnMouseEntered(event -> {
            isMouseOverThePanel = true;
            mPanelView.setVisible(true);
        });
        rootPanel.setOnMouseExited(event -> {
            isMouseOverThePanel = false;
            timer.stop();
            timer.play();
        });
    }


}
