package dmedia.appbuilder;

import dmedia.Main;
import dmedia.controller.PanelController;
import dmedia.model.media.MediaState;
import dmedia.view.PanelView;
import dmedia.view.RootView;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ApplicationBuilder {

    private RootView mRootView;
    private PanelView mPanelView;

    private PanelController mPanelController;

    private MediaState mModel;

    private Stage mStage;

    private AnchorPane mRoot;
    private VBox mPanel;

    /**
     * Constructs all the scene
     * @param stage
     * @return
     * @throws IOException
     */
    public Scene buildAppScene(Stage stage)throws IOException{
        mStage = stage;

        mModel = new MediaState();
        mPanelController = new PanelController();
        loadFXML();
        constructView();

        Scene scene = new Scene(mRoot);
        initListeners(scene);

        buildMVC();

        return scene;
    }

    public void setupStageProperities(Stage stage){
        mStage.setTitle("DMedia");
    }

    /**
     * Loads FXML files and sets both main containers for each view
     * and controllers for them
     * @throws IOException
     */
    private void loadFXML() throws IOException{
        FXMLLoader rootLayoutLoader = new FXMLLoader();
        FXMLLoader panelLoader = new FXMLLoader();

        rootLayoutLoader.setLocation(ApplicationBuilder.class.getResource("..\\view\\RootLayout.fxml"));
        panelLoader.setLocation(ApplicationBuilder.class.getResource("..\\view\\Panel.fxml"));

        mRoot = (AnchorPane) rootLayoutLoader.load();
        mPanel = (VBox) panelLoader.load();

        mRootView = rootLayoutLoader.getController();
        mPanelView = panelLoader.getController();

        setCSS();
    }

    /**
     * Adds panel to application root container
     */
    private void constructView(){
        mRoot.getChildren().add(mPanel);
        AnchorPane.setBottomAnchor(mPanel, 0.0);
        AnchorPane.setLeftAnchor(mPanel, 0.0);
        AnchorPane.setRightAnchor(mPanel, 0.0);
    }

    private void initListeners(Scene scene) {

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent doubleClick) {
                if (doubleClick.getClickCount() == 2) {//make app fullscreen
                    mPanelView.setVisible(false);
                    mStage.setFullScreen(true);
                }
            }
        });
    }

    /**
     * Connects model, view and controller
     */
    private void buildMVC(){
        mModel.setPanelView(mPanelView);
        mModel.setRootView(mRootView);

        mPanelView.setController(mPanelController);
        mPanelView.setModel(mModel);

        mPanelController.setModel(mModel);
        mPanelController.setPanelView(mPanelView);
    }

    private void setCSS(){
        String css = Main.class.getResource("view/style.css").toExternalForm();
        mPanel.getStylesheets().add(css);
    }
}
