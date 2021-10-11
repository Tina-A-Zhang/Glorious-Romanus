package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;
import javafx.scene.control.ToggleButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.nio.file.Paths;
import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.util.Duration;

public class BasicMenuController extends MenuController {

    // https://stackoverflow.com/a/30171444
    @FXML
    private URL location; // has to be called location

    @FXML
    private ToggleButton musicButton = new ToggleButton("Music On/Off");

    private MediaPlayer mediaPlayer;

    @FXML
    private ChoiceBox<String> factionBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> battleBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> goalBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> buildingBox = new ChoiceBox<>();

    ObservableList<String> faclist = FXCollections.observableArrayList();
    ObservableList<String> battlelist = FXCollections.observableArrayList();
    ObservableList<String> goallist = FXCollections.observableArrayList();
    ObservableList<String> buildingBoxList = FXCollections.observableArrayList();

  
  
    @FXML
  private void loadFacBox() {
    factionBox.getItems().clear();
    faclist.removeAll(faclist);
    String rome = "Rome";
    String gaul = "Gaul";
    
   faclist.addAll(rome,gaul);
    // list.addAll(cavalry, infantry, artillery, hInfantry, spearman, mInfantry,
    // mCavalry, horse, elephant, chariot);
    factionBox.getItems().addAll(faclist);
  }
  @FXML
  private void loadBattleBox() {
    battleBox.getItems().clear();
    battlelist.removeAll(battlelist);
    String b1 = "Default";
    
   battlelist.addAll(b1);
    // list.addAll(cavalry, infantry, artillery, hInfantry, spearman, mInfantry,
    // mCavalry, horse, elephant, chariot);
    battleBox.getItems().addAll(battlelist);
  }
  @FXML
  private void loadGoalBox() {
    goalBox.getItems().clear();
    goallist.removeAll(goallist);
    String g1 = "Conquer Goal";
    String g2 = "Treasury Goal";
    String g3 = "Wealth Goal";
    
   goallist.addAll(g1,g2,g3);
    // list.addAll(cavalry, infantry, artillery, hInfantry, spearman, mInfantry,
    // mCavalry, horse, elephant, chariot);
    goalBox.getItems().addAll(goallist);
  }


    @FXML
    public void intialise(){
        music();
        loadFacBox();
        loadBattleBox();
        loadGoalBox();
    }

    // @FXML
    // public void clickedInvadeButton(ActionEvent e) throws Exception {
    // getParent().clickedInvadeButton(e);
    // }

    @FXML
    public void saveButton(ActionEvent e) throws IOException {
        getParent().getCurrPlayer().save();
    }

    @FXML
    public void LoadButton(ActionEvent e) throws Exception {
        getParent().getGame().loadGame(getParent().getCurrPlayer());
    }

    @FXML
    public void exitButton() {
        Platform.exit();
    }

    @FXML
    public void helpSwitchMenu(ActionEvent e) throws Exception{
        String fac = factionBox.getValue();
        String bat = battleBox.getValue();
        String goal = goalBox.getValue();
        if(fac==null || bat==null||goal==null){
            return;
        }
        if(fac.equals("Rome")){
          this.getParent().getGame().assignFactions("Rome", "Gaul");
        }else{
          this.getParent().getGame().assignFactions("Gaul", "Rome");
        }
        this.getParent().getGame().setGoal(goal);
        clickedSwitchMenu();
    }
  
  @FXML
  public void music() {
    String s = "images/test.mp3";
    Media h = new Media(Paths.get(s).toUri().toString());
    mediaPlayer = new MediaPlayer(h);
    mediaPlayer.setOnEndOfMedia(new Runnable() {
    public void run() {
      mediaPlayer.seek(Duration.ZERO);
      }
    });
    musicButton.setOnAction(event -> {
    if (musicButton.isSelected()) {
      mediaPlayer.pause();
    }else {
      mediaPlayer.play();
    }
    });
  }

}
