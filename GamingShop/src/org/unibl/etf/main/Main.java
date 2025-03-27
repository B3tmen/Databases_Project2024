package org.unibl.etf.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.unibl.etf.util.FxmlPaths;
import org.unibl.etf.util.ImagePaths;


public class Main extends Application {

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadLoginWindow(primaryStage);


    }

    private void loadLoginWindow(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FxmlPaths.USER_LOGIN_FXML));
        //fxmlLoader.setController(new CustomerMainShopController(primaryStage));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        primaryStage.setTitle("User login");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource(ImagePaths.APP_ICON).toString()));
        primaryStage.show();
    }
}
