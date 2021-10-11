package unsw.gloriaromanus;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.util.Duration;


public class GloriaRomanusApplication extends Application {

  private static GloriaRomanusController controller;
  private MediaPlayer mediaPlayer;

  @Override
  public void start(Stage stage) throws IOException {
   // music();
    // set up the scene
    FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
    Parent root = loader.load();
    controller = loader.getController();
    Scene scene = new Scene(root);

    // set up the stage
    stage.setTitle("Gloria Romanus");
    stage.setWidth(800);
    stage.setHeight(700);
    stage.setScene(scene);
    stage.show();

  }

  // public void music() {
  //   String s = "/Users/Mosaddad/Desktop/init1/uni/2020T3/2511/project/m13a-bug/images/test.mp3";
  //   Media h = new Media(Paths.get(s).toUri().toString());
  //   mediaPlayer = new MediaPlayer(h);
  //   mediaPlayer.setOnEndOfMedia(new Runnable() {
  //     public void run() {
  //       mediaPlayer.seek(Duration.ZERO);
  //     }
  //   });
  //   mediaPlayer.play();

  // }

  /**
   * Stops and releases all resources used in application.
   */
  @Override
  public void stop() {
    controller.terminate();
  }

  /**
   * Opens and runs application.
   *
   * @param args arguments passed to this application
   */
  public static void main(String[] args) {

    Application.launch(args);
  }
}