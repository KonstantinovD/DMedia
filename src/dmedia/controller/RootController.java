package dmedia.controller;

import dmedia.model.media.MediaState;
import dmedia.view.PanelView;
import dmedia.view.RootView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RootController{

    private RootView mRootView;
    private PanelView mPanelView;
    private Stage mStage;
    private MediaState mediaModel;


    public void setRootView(RootView rootView) {
        this.mRootView = rootView;
    }

    public void setStage(Stage stage) {
        this.mStage = stage;
    }

    public void setPanelView(PanelView panelView) {
        this.mPanelView = panelView;
    }

    public void setMediaModel(MediaState mediaModel) {
        this.mediaModel = mediaModel;
    }

    public void initListeners() {
        if(mRootView == null || mPanelView == null || mStage == null) return;
        mRootView.getStackPane().setOnMouseClicked((new EventHandler<>() {
            @Override
            public void handle(MouseEvent doubleClick) {
                if (doubleClick.getClickCount() == 2) {//make app fullscreen
                    mPanelView.setVisible(false);
                    mStage.setFullScreen(true);
                }
            }
        }));


        initKeyListener();
        setupStage(mStage);
    }

    /**
     * Media will be rewound about this number of seconds
     */
    private int skippingInerval = 5;

    private void initKeyListener(){
        mRootView.getStackPane().setOnKeyPressed(event -> {
            switch(event.getCode()){
                case LEFT:{

                    MediaPlayer mp = mRootView.getMediaView().getMediaPlayer();
                    Duration duration = new Duration(mp.getCurrentTime().toSeconds() + skippingInerval);
                    mp.seek(duration);

                    break;
                }
                case RIGHT:{
                    System.out.println("A RIGHT key was pressed");
                    break;
                }
                case SPACE:{
                    mediaModel.reversePlayingMode();
                }
            }
        });
    }

    private final String TITLE = "DMedia ";
    private void setupStage(Stage stage){
        //setup stage
        stage.setTitle(TITLE);
        stage.setFullScreenExitHint("");
    }
}

