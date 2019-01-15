package dmedia;

import dmedia.appbuilder.ApplicationBuilder;
import dmedia.model.media.MediaState;
import dmedia.view.PanelView;
import dmedia.view.RootView;
import dmedia.view.alerts.ApplicationAlert;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {


    private RootView mRootView;
    private PanelView mPanelView;

    private dmedia.controller.PanelController mPanelController;

    private MediaState mModel;

    private Stage mStage;

    private AnchorPane root;
    private VBox panel;

    @Override
    public void start(Stage stage) throws Exception{
        mStage = stage;
        
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream("resources/images/appicon.png"), 32, 32, false, false));

        ApplicationBuilder applicationBuilder = new ApplicationBuilder();
        Scene scene = applicationBuilder.buildAppScene(mStage);
        applicationBuilder.setupStageProperities(mStage);

        //mStage

        mStage.setScene(scene);
        mStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }



}
