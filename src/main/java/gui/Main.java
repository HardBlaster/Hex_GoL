package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import state.State;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("HEX");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.setFullScreen(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    /*
        int[] a = {3};
        int[] b = {2, 4};
        int[] c = {1, 5, 6, 7, 8};
        State test = new State(50, 10, 10);
        test.setBornIf(a);
        test.setStableIf(b);
        test.setDeadIf(c);
        test.printMap();
        test.refresh();
        test.printMap();
        test.refresh();
        test.printMap();*/
    }


    public static void main(String[] args) {
        launch(args);
    }
}
