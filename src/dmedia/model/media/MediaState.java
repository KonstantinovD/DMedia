package dmedia.model.media;

import dmedia.view.PanelView;
import dmedia.view.RootView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MediaState {

    private static double MIN_CHANGE = 0.5;
    private double mTotalMediaTimeInSeconds;
    /**
     * Sets the duration of the media.
     * Takes relative value (in range [0.0, 1.0])
     * of media duration
     * @param duration
     */
    public void setDuration(double duration) {
        if(duration >= 0.0) {
            double currentTime = mediaPlayer.getCurrentTime().toSeconds();
            double sliderTime = duration * mTotalMediaTimeInSeconds;

            if(Math.abs(currentTime - sliderTime) >= MIN_CHANGE) {

                mDuration.set(duration);

                double currentTimeInSeconds = mediaPlayer.getMedia().getDuration().toSeconds();//convert
                mediaPlayer.seek(Duration.seconds(currentTimeInSeconds * mDuration.get()));
            }
        }else throw new IllegalArgumentException();
    }


    /**
     * Sets volume of video
     * Takes value in range [0.0, 1.0] - relative level
     * of media volume
     * @param volume
     */
    public void setVolume(double volume) {
        if(volume >= 0.0) {
            setSoundOn(true);
            mVolume.setValue(volume);
            if (mIsLoaded.get()) {
                mediaPlayer.setVolume(volume);
            }
        }else throw new IllegalArgumentException();
    }

    /**
     * Sets video rate
     * Takes non-negative value - relative video rate
     * @param rate
     */
    public void setRate(double rate){
        if (rate >= 0.0) {
            mRate.set(rate);
            if(mIsLoaded.get()) { mediaPlayer.setRate(rate); }
        }
    }

    public void setLoaded(boolean isLoaded) {
        mIsLoaded.setValue(isLoaded);
    }

    public void openMedia(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            //remove current media
            playMedia(false);
            setLoaded(false);

            String filePath = file.toURI().toString();

            try {
                mMedia = new Media(filePath);
                mediaPlayer = new MediaPlayer(mMedia);
                initMediaPlayer();
                //new media player should be send to MediaView
                mRootView.setMediaPlayer(mediaPlayer);
                setLoaded(true);
                setDuration(0.0);




                //TODO: unnecessary  setVolume(0.2);

                mediaPlayer.setOnReady(() -> {
                    mTotalMediaTimeInSeconds = mediaPlayer.getTotalDuration().toSeconds();
                    mPanelView.initNewMedia(mediaPlayer);
                    playMedia(true);
                });
                mRootView.getMediaView().setMediaPlayer(mediaPlayer);

                //bind
                DoubleProperty width = mRootView.getMediaView().fitWidthProperty();
                DoubleProperty height = mRootView.getMediaView().fitHeightProperty();
                //make width equals to real (resizing)
                width.bind(Bindings.selectDouble(mRootView.getMediaView().sceneProperty(), "width"));
                height.bind(Bindings.selectDouble(mRootView.getMediaView().sceneProperty(), "height"));

            }catch(Exception ex){
                //remove current unloaded media
                playMedia(false);
                setLoaded(false);
                mRootView.generateAlert(Alert.AlertType.ERROR,
                        "Could not load data from file:\n" + file.getPath(),
                        "Could not load media!");
            }
        }
    }
    private void initMediaPlayer(){
        mediaPlayer.setRate(mRate.get());
        mediaPlayer.setVolume(mVolume.get());
    }

    public void playMedia(boolean isPlayed) {
        if(mIsLoaded.get()) {
            mIsPlayed.setValue(isPlayed);
            if (mIsPlayed.get()) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    /**
     * Plays media if it isn't played and pauses it
     * in another case
     */
    public void reversePlayingMode(){
        playMedia(!mIsPlayed.get());
    }


    private double mPreviousVolume;
    /**
     * Sets sound off if {@code isSoundOn} is {@code false}, and sets it on
     * in another case. The method sets media volume value to previous one if
     * sound is being setting off
     * @param isSoundOn
     */
    public void setSoundOn(boolean isSoundOn){
        if(this.mIsSoundOn.get() != isSoundOn){
            double resultVolume;
            if(this.mIsSoundOn.get()){
                mPreviousVolume = mVolume.get();
                resultVolume = 0.0;
            }else{
                resultVolume = mPreviousVolume;
            }
            if(mIsLoaded.get()) {
                mediaPlayer.setVolume(resultVolume);
            }
        }
        mIsSoundOn.set(isSoundOn);
    }


    /**
     * ets sound off if {@code isSoundOn} is {@code false}, and sets it on
     * in another case. The method sets media volume value to
     * {@code volume} parameter value if sound is being setting off
     * @param isSoundOn
     * @param volume volume to be set to media if sound is being setting off
     */
    @Deprecated
    public void setSoundOn(boolean isSoundOn, double volume){
        if(this.mIsSoundOn.get() != isSoundOn){
            if(this.mIsSoundOn.get()){
                mPreviousVolume = mVolume.get();
                setVolume(0.0);
            }else{
                if(volume >= 0.0) {
                    setVolume(volume);
                }else throw new IllegalArgumentException();
            }
        }
        mIsSoundOn.set(isSoundOn);
    }


    /**
     * Sets sound off if it is on, and sets it on
     * in another case
     */
    public void reverseSoundMode(){
        setSoundOn(!mIsSoundOn.get());
    }


    public void stopMedia(){
        if(mIsLoaded.get()) {
            playMedia(false);
            setDuration(0.0);
            mediaPlayer.stop();
        }
    }

    private SimpleDoubleProperty mDuration;
    private SimpleDoubleProperty mVolume;
    private SimpleDoubleProperty mRate;

    private SimpleBooleanProperty mIsPlayed;
    private SimpleBooleanProperty mIsLoaded;

    private SimpleBooleanProperty mIsSoundOn;

    private RootView mRootView;
    private PanelView mPanelView;

    private Media mMedia;
    private MediaPlayer mediaPlayer;


    public MediaState(){
        initProperities();
    }

    public MediaState(RootView rootView, PanelView panelView){
        this();
        mRootView = rootView;
        mPanelView = panelView;
    }

    public void setPanelView(PanelView panelView){
        mPanelView = panelView;
    }

    public void setRootView(RootView rootView){
        mRootView = rootView;
    }

    private void initProperities(){
        mDuration = new SimpleDoubleProperty(0.0);
        mVolume = new SimpleDoubleProperty(0.0);
        mRate = new SimpleDoubleProperty(1.0);
        mIsPlayed = new SimpleBooleanProperty(false);
        mIsLoaded = new SimpleBooleanProperty(false);
        mIsSoundOn = new SimpleBooleanProperty(true);
        mPreviousVolume = 0.0;
    }

    //getters for properties

    public SimpleBooleanProperty getIsLoadedProperty() {
        return mIsLoaded;
    }

    public SimpleBooleanProperty getIsPlayedProperty() {
        return mIsPlayed;
    }

    public SimpleDoubleProperty getDurationProperty() {
        return mDuration;
    }

    public SimpleDoubleProperty getVolumeProperty() {
        return mVolume;
    }

    public SimpleDoubleProperty getRateProperty() { return mRate; }

    public SimpleBooleanProperty getIsSoundOnProperty() { return mIsSoundOn; }

    public MediaPlayer getMediaPlayer(){ return mediaPlayer; }
}
