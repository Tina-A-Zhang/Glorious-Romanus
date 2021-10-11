package unsw.gloriaromanus;

import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.util.ListenableList;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import javafx.scene.control.ListView;

import org.json.JSONObject;

public class InvasionMenuController extends MenuController {

  @FXML
  private TextField invading_province;

  @FXML
  private TextField opponent_province;

  @FXML
  private TextArea output_terminal;

  @FXML
  private ChoiceBox<String> units = new ChoiceBox<>();

  @FXML
  private ChoiceBox<String> moveTroopBox;

  @FXML
  private ChoiceBox<String> destBox = new ChoiceBox<>();

  @FXML
  private ChoiceBox<String> trainBox = new ChoiceBox<>();

  @FXML
  private ChoiceBox<String> taxBox = new ChoiceBox<>();

  @FXML
  private ChoiceBox<String> attackBox = new ChoiceBox<>();

  private int numTurn = 1;
  private Boolean hasWon = false;

  @FXML
  private Label myl = new Label("Year 1");

  private SimpleStringProperty yeardisplay = new SimpleStringProperty();
  // https://stackoverflow.com/a/30171444
  @FXML
  private URL location; // has to be called location
  @FXML
  private ChoiceBox<String> buildingBox = new ChoiceBox<>();

  ObservableList<String> list = FXCollections.observableArrayList();
  ObservableList<String> movelist = FXCollections.observableArrayList();

  ObservableList<String> destlist = FXCollections.observableArrayList();
  ObservableList<String> trainlist = FXCollections.observableArrayList();
  ObservableList<String> taxlist = FXCollections.observableArrayList();
  ObservableList<String> buildingBoxList = FXCollections.observableArrayList();

  ListView<String> listTroops = new ListView<String>();
  private String[] provinces = { "Britannia", "Lugdunensis", "Belgica", "Germania Inferior", "Aquitania",
      "Germania Superior", "Alpes Graiae et Poeninae", "XI", "Alpes Cottiae", "Alpes Maritimae", "IX", "Narbonensis",
      "Tarraconensis", "Baetica", "Lusitania", "Raetia", "Noricum", "X", "VIII", "VII", "VI", "IV", "V", "I", "III",
      "Sicilia", "Pannonia Superior", "Pannonia Inferior", "Dalmatia", "II", "Sardinia et Corsica", "Moesia Superior",
      "Dacia", "Moesia Inferior", "Thracia", "Macedonia", "Achaia", "Bithynia et Pontus", "Cilicia", "Creta et Cyrene",
      "Cyprus", "Aegyptus", "Arabia", "Iudaea", "Syria", "Africa Proconsularis", "Numidia", "Mauretania Caesariensis",
      "Mauretania Tingitana", "Galatia et Cappadocia", "Lycia et Pamphylia", "Asia", "Armenia Mesopotamia" };

  @FXML
  private void loadBuildingBoxList() {
    buildingBox.getItems().clear();
    buildingBoxList.removeAll(buildingBoxList);
    String troopBuilding = "Troop Production Building";
    String market = "Market";
    String mine = "Mine";

    buildingBoxList.addAll(troopBuilding, market, mine);
    buildingBox.getItems().addAll(buildingBoxList);

  }

  @FXML
  private void upgrade(ActionEvent e) throws Exception {
    String type = buildingBox.getValue();
    if (type == null)
      return;
    if (type.equals("Mine")) {
      upgradeMineButton();
    } else if (type.equals("Market")) {
      upgradeMarketButton();
    } else {
      upgradeButton();
    }

  }

  @FXML
  private void loadRecruitBox() {
    units.getItems().clear();
    String name = (String) this.getParent().getHumanProvince().getAttributes().get("name");

    Province p = this.getParent().getGame().getProvinceByName(name);
    list.removeAll(list);
    String cavalry = "Cavalry";
    String infantry = "Infantry";
    String artillery = "Artillery";
    String hInfantry = "Heavy infantry";
    String spearman = "Spearmen";
    String mInfantry = "Missile infantry";
    String mCavalry = "Melee cavalry";
    String horse = "Horse archers";
    String elephant = "Elephants";
    String chariot = "Chariots";
    if (p.getBuildingLevel() == 1) {
      list.addAll(cavalry, infantry, artillery);
    }
    if (p.getBuildingLevel() == 2) {
      list.addAll(cavalry, infantry, artillery, hInfantry, spearman, mInfantry);

    }
    if (p.getBuildingLevel() == 3) {
      list.addAll(cavalry, infantry, artillery, hInfantry, spearman, mInfantry, mCavalry, horse, elephant, chariot);

    }
    units.getItems().addAll(list);
  }

  @FXML
  private void loadMoveBox() {
    moveTroopBox.getItems().clear();
    movelist.removeAll(movelist);
    if (this.getParent().getHumanProvince() != null) {
      String name = (String) this.getParent().getHumanProvince().getAttributes().get("name");

      Province p = this.getParent().getGame().getProvinceByName(name);
      for (Unit u : p.getUnits()) {
        if (!u.getState().equals("trained"))
          continue;
        movelist.add(u.getID());
      }
    }
    moveTroopBox.getItems().addAll(movelist);
  }

  @FXML
  private void loadAttackBox() {
    attackBox.getItems().clear();
    movelist.removeAll(movelist);
    if (this.getParent().getHumanProvince() != null) {
      String name = (String) this.getParent().getHumanProvince().getAttributes().get("name");

      Province p = this.getParent().getGame().getProvinceByName(name);
      for (Unit u : p.getUnits()) {
        if (!u.getState().equals("trained"))
          continue;
        movelist.add(u.getID());
      }
    }
    attackBox.getItems().addAll(movelist);
  }

  @FXML
  private void loadDestBox() {
    destBox.getItems().clear();
    destlist.removeAll(destlist);
    if (this.getParent().getHumanProvince() != null) {
      String name = (String) this.getParent().getHumanProvince().getAttributes().get("name");

      Province p = this.getParent().getGame().getProvinceByName(name);
      for (String i : provinces) {
        destlist.add(i);
      }

    }
    Collections.sort(destlist);
    destBox.getItems().addAll(destlist);

  }

  @FXML
  private void loadTrainBox() {
    trainBox.getItems().clear();
    trainlist.removeAll(trainlist);
    if (this.getParent().getHumanProvince() != null) {
      String name = (String) this.getParent().getHumanProvince().getAttributes().get("name");

      Province p = this.getParent().getGame().getProvinceByName(name);
      for (Unit u : p.getUnits()) {
        if (u.getState().equals("untrained"))
          trainlist.add(u.getID());
      }
    }
    trainBox.getItems().addAll(trainlist);

  }

  @FXML
  private void loadTaxBox() {
    taxlist.removeAll(taxlist);
    String lt = "0.1";
    String nt = "0.15";
    String ht = "0.2";
    String vht = "0.25";

    taxlist.addAll(lt, nt, ht, vht);
    taxBox.getItems().addAll(taxlist);

  }

  @FXML
  public void taxButton() {
    if (taxBox.getValue() == null)
      return;
    if (this.getParent().getHumanProvince() == null)
      return;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);
    int taxrate = (int) (Double.parseDouble(taxBox.getValue()) * 100);
    p.getWealth().setTaxRate(taxrate);
    appendToTerminal("tax rate set to " + taxrate + "%");
  }

  @FXML
  private void initialize() {
    loadTaxBox();
    trainBox.getItems().clear();
    yeardisplay.set("Year 1");
    myl.textProperty().bind(yeardisplay);
  }

  public void setInvadingProvince(String p) {
    invading_province.setText(p);
  }

  public void setOpponentProvince(String p) {
    opponent_province.setText(p);
  }

  public void appendToTerminal(String message) {
    output_terminal.appendText(message + "\n");
  }

  @FXML
  public void recruitButton(ActionEvent e) throws Exception {
    GraphicsOverlay go = new GraphicsOverlay();
    if (this.getParent().getHumanProvince() == null) {
      return;
    }
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    double[] coOrdinates = getCoordinates(province);

    Province p = this.getParent().getGame().getProvinceByName(province);
    if (p == null)
      return;
    String type = (String) units.getValue();

    if (type == null)
      return;
    Unit u = this.getParent().getCurrPlayer().recruit(type, p);
    if (u != null) {
      int i = 0;
      for (Unit un : this.getParent().getCurrPlayer().getUnit()) {
        if (un.getKind().equals(type))
          i++;
      }

      u.setID(type + " " + i + " " + p.getName());
      String content = Files.readString(Paths.get("src/unsw/gloriaromanus/image.json"));
      JSONObject imageAdd = new JSONObject(content);

      String imageAddress = imageAdd.getJSONObject(type).getString("address");
      PictureMarkerSymbol sth = new PictureMarkerSymbol(new Image(new File(imageAddress).toURI().toString()));
      sth.setOpacity((float) 0.50);

      double offset_longitude = coOrdinates[0] + imageAdd.getJSONObject(type).getDouble("offset_longitude");
      double offset_latitude = coOrdinates[1] + imageAdd.getJSONObject(type).getDouble("offset_latitude");
      Point pt = new Point(offset_longitude, offset_latitude, SpatialReferences.getWgs84());
      Graphic g = new Graphic(pt, sth);
      go.getGraphics().add(g);
      this.getParent().getMapView().getGraphicsOverlays().add(go);
      appendToTerminal("You have successfully recruited a " + type + " unit.");
      this.getParent().addToGraphicList(u.getID(), g);
    } else {
      appendToTerminal("Recruit unsuccessful");
    }
  }

  public void resetUpgradeBuildingNum() {
    for (Province p : this.getParent().getGame().getProvinces())
      p.resetUpgradeBuildingNum();
  }

  @FXML
  public void trainUnit(ActionEvent e) throws IOException {
    // the units to be moved
    String troopID = (String) trainBox.getValue();
    if (troopID == null)
      return;
    Unit toMov = null;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);

    if (p.getUnits().isEmpty()) return;
    for (Unit u : p.getUnits()) {
      if (u.getID().equals(troopID)) {
        toMov = u;
      }
    }

    if (toMov == null) {
      return;
    }
    if (!toMov.getState().equals("untrained"))
      appendToTerminal("Already trained");

    Boolean trainState = false;
    trainState = this.getParent().getCurrPlayer().train(toMov, p);
    if (trainState) {
      // move the icons
      appendToTerminal("You have successfully trained your troops");

    } else {
      appendToTerminal("Unsuccessful Training");
    }
  }

  public double[] getCoordinates(String province) throws JsonParseException, JsonMappingException, IOException {
    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);
    double[] coOrdinates = new double[2];
    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {

        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        if (((String) f.getProperty("name")).equals(province)) {
          LngLatAlt coor = p.getCoordinates();

          double longitude = (double) coor.getLongitude();
          double latitude = (double) coor.getLatitude();
          coOrdinates[0] = longitude;
          coOrdinates[1] = latitude;
        }
      }

    }
    return coOrdinates;
  }

  @FXML
  public void moveButton(ActionEvent e) throws Exception {
    // the units to be moved
    String troopID = (String) moveTroopBox.getValue();
    if (troopID == null)
      return;
    Unit toMov = null;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);
    String dest = (String) destBox.getValue();
    Province destP = this.getParent().getGame().getProvinceByName(dest);
    if (dest == null)
      return;
    for (Unit u : p.getUnits()) {
      if (u.getID().equals(troopID))
        toMov = u;
    }

    if (toMov == null)
      return;
    ArrayList<Unit> unitsT = new ArrayList<Unit>();
    unitsT.add(toMov);

    Boolean moveState = false;
    if (destP.getOwner().equals(this.getParent().getCurrPlayer())) {
      moveState = this.getParent().getCurrPlayer().moveUnits(unitsT, destP);
    }
    if (moveState) {
      // move the icons
      appendToTerminal("You have successfully moved your troops");
      ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();

      ArrayList<Graphic> toMove = new ArrayList<Graphic>();
      for (Unit ui : unitsT) {
        toMove.add(this.getParent().getGraphicList().get(ui.getID()));
      }

      for (GraphicsOverlay grov : g) {
        // if (grov.getGraphics().contains(toMove.get(0))) {
        for (Graphic gr : toMove) {
          grov.getGraphics().remove(gr);
        }
        // }
      }
      for (Unit ui : unitsT) {
        Graphic n = this.getParent().createProvinceTypeGraphic(destP, ui.getKind(), ui.getState());
        g.get(0).getGraphics().add(n);
        Graphic old = this.getParent().getGraphicList().get(ui.getID());
        this.getParent().getGraphicList().replace(ui.getID(), old, n);
      }

    } else {
      appendToTerminal("The move was unsuccessful");
    }
  }

  @FXML
  public void clickedEndTurn(ActionEvent e) throws Exception {
    if (this.getParent().getHumanProvince() == null)
      return;
    this.getParent().getCurrPlayer().setAttack(false);
    this.getParent().getCurrPlayer().setHasUpgrade(false);

    numTurn++;
    yeardisplay.set("Year " + numTurn);

    ArrayList<Unit> tochange = BattleResolver.finishTraining(this.getParent().getCurrPlayer(),
        this.getParent().getGame().getProvinces());

    BattleResolver.collectAllTax(this.getParent().getGame().getProvinces());
    BattleResolver.addAllGrowth(this.getParent().getGame().getProvinces());
    BattleResolver.resetMovementPoint(this.getParent().getGame().getProvinces());
    resetUpgradeBuildingNum();
    treasuryGoal();
    wealthGoal();
    if (!tochange.isEmpty()) {
      ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();
      assert (!g.isEmpty());
      ArrayList<Graphic> toMove = new ArrayList<Graphic>();
      for (Unit ui : tochange) {
        toMove.add(this.getParent().getGraphicList().get(ui.getID()));
      }

      for (GraphicsOverlay grov : g) {
        for (Graphic gr : toMove) {
          grov.getGraphics().remove(gr);
        }
      }

      for (Unit ui : tochange) {
        Graphic n = this.getParent().createProvinceTypeGraphic(ui.getLocation(), ui.getKind(), ui.getState());
        g.get(0).getGraphics().add(n);
        Graphic old = this.getParent().getGraphicList().get(ui.getID());
        this.getParent().getGraphicList().replace(ui.getID(), old, n);
      }
    }
    for (Player pl : this.getParent().getGame().getPlayes()) {
      if (!pl.equals(this.getParent().getCurrPlayer())) {
        this.getParent().setCurrPlayer(pl);
        break;
      }
    }
    appendToTerminal("Faction: " + this.getParent().getCurrPlayer().getFaction().getName() + " playing now");

  }

  @FXML
  public void clickedInvadeButton(ActionEvent e) throws Exception {

    if (this.getParent().getHumanProvince() == null || this.getParent().getEnemyProvince() == null)
      return;
    String humanProvince = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    String enemyProvince = (String) this.getParent().getEnemyProvince().getAttributes().get("name");
    Province myprov = this.getParent().getGame().getProvinceByName(humanProvince);
    Province enprov = this.getParent().getGame().getProvinceByName(enemyProvince);

    if (this.getParent().getCurrPlayer().getHA())
      return;
    // the units to be moved
    String troopID = (String) attackBox.getValue();
    if (troopID == null)
      return;
    Unit toMov = null;
    for (Unit u : myprov.getUnits()) {
      if (u.getID().equals(troopID))
        toMov = u;
    }
    if (toMov == null)
      return;
    ArrayList<Unit> unitsTA = new ArrayList<Unit>();
    unitsTA.add(toMov);
    ArrayList<Unit> togetGraphics = new ArrayList<Unit>();
    togetGraphics.addAll(unitsTA);
    togetGraphics.addAll(enprov.getUnits());
    ArrayList<Graphic> graphicsToDelete = new ArrayList<Graphic>();
    // get all the graphics to delete
    for (Unit u : togetGraphics)
      graphicsToDelete.add(this.getParent().getGraphicList().get(u.getID()));

    Boolean moveState = this.getParent().getCurrPlayer().moveUnitsTA(unitsTA, enprov);
    if (!moveState)
      return;
    this.getParent().getCurrPlayer().attack(unitsTA, enprov);
    if (enprov.getOwner().equals(this.getParent().getCurrPlayer())) {
      appendToTerminal("You have conquered " + enemyProvince);
    } else {
      appendToTerminal("You have lost the battle");
    }

    ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();
    Graphic flagToDelete = this.getParent().getFlagList().get(enemyProvince);

    this.getParent().getCurrPlayer().attack(unitsTA, enprov);
    if (enprov.getOwner().equals(this.getParent().getCurrPlayer())) {
      conquerGoal();
      // delete the old and add the new flag
      for (GraphicsOverlay grov : g) {
        if (grov.getGraphics().contains(flagToDelete)) {
          grov.getGraphics().remove(flagToDelete);
          Graphic newGraphToAdd = this.getParent().createProvinceTypeGraphic(enprov, "flag", "unrelated");
          grov.getGraphics().add(newGraphToAdd);
          this.getParent().getFlagList().replace(enprov.getName(), flagToDelete, newGraphToAdd);
        }
      }
    }
    // remove all the units graphics from the two provinces
    for (GraphicsOverlay grov : g) {
      for (Graphic gp : graphicsToDelete) {
        if (grov.getGraphics().contains(gp)) {
          grov.getGraphics().remove(gp);
        }
      }
    }

    GraphicsOverlay newGOL = new GraphicsOverlay();
    // add unit images to my province
    for (Unit u : myprov.getUnits()) {
      Graphic newg = this.getParent().createProvinceTypeGraphic(myprov, u.getKind(), u.getState());
      newGOL.getGraphics().add(newg);
      Graphic old = this.getParent().getGraphicList().get(u.getID());
      this.getParent().getGraphicList().replace(u.getID(), old, newg);
    }
    // add unit images to enemy province
    for (Unit u : enprov.getUnits()) {
      Graphic newg = this.getParent().createProvinceTypeGraphic(enprov, u.getKind(), u.getState());
      newGOL.getGraphics().add(newg);
      Graphic old = this.getParent().getGraphicList().get(u.getID());
      this.getParent().getGraphicList().replace(u.getID(), old, newg);
    }
    this.getParent().getMapView().getGraphicsOverlays().add(newGOL);

  }

  @FXML
  public void showInfoButton() {
    // Feature f = this.getParent().getHumanProvince();
    // String province = (String) f.getProperty("name");
    if(this.getParent().getHumanProvince()==null )  return;
    String selectedProvinceName = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province selectedProvince = this.getParent().getGame().getProvinceByName(selectedProvinceName);
    Wealth selectedWealth = selectedProvince.getWealth();
    appendToTerminal("$$$$$$$$$$$$$$$$$$");
    appendToTerminal("Wealth Town: " + Integer.toString(selectedWealth.getWealthTown()) + " Gold");
    appendToTerminal("Treasury: " + Integer.toString(selectedWealth.getTeasury()) + " Gold");
    appendToTerminal("Growth: " + Integer.toString(selectedWealth.getGrowth()) + " Gold");
    appendToTerminal("Tax Rate: " + Integer.toString(selectedWealth.getTaxRate()) + "%");
    appendToTerminal("$$$$$$$$$$$$$$$$$$");
    // System.out.println(selectedProvince.getWealth());
    // appendToTerminal();
  }

  @FXML
  private void upgradeButton() throws Exception {
    if (this.getParent().getHumanProvince() == null)
      return;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);
    Boolean upstate = this.getParent().getCurrPlayer().upgradeBuilding(p);
    if (!upstate) {
      appendToTerminal("upgrade unsuccessful");
    } else {
      double[] coor = getCoordinates(province);
      Point pt = new Point(coor[0] + 0.34, coor[1] + 0.25, SpatialReferences.getWgs84());
      PictureMarkerSymbol s = null;
      ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();

      if (p.getBuildingLevel() == 1) {
        s = new PictureMarkerSymbol("images/b1.png");
      } else if (p.getBuildingLevel() == 2) {
        s = new PictureMarkerSymbol("images/b2.png");
      } else if (p.getBuildingLevel() == 3) {
        s = new PictureMarkerSymbol("images/b3.png");
      }
      Graphic n = new Graphic(pt, s);
      if (p.getBuildingLevel() == 1) {
        this.getParent().getBuildingList().put(province, n);
        g.get(0).getGraphics().add(n);

      } else {
        Graphic old = this.getParent().getBuildingList().get(province);
        for (GraphicsOverlay go : g) {
          go.getGraphics().remove(old);
        }
        g.get(0).getGraphics().add(n);
        this.getParent().getBuildingList().replace(province, old, n);
      }
    }

  }
  // public void switchMenu2() throws JsonParseException, JsonMappingException,
  // IOException {
  // System.out.println("trying to switch menu");
  // this.getParent().getStackPane().getChildren().remove(this.getParent().getControllerParentPairs().get(0).getValue());
  // Collections.reverse(this.getParent().getControllerParentPairs());
  // this.getParent().getStackPane().getChildren().add(this.getParent().getControllerParentPairs().get(0).getValue());
  // }

  @FXML
  private void upgradeMarketButton() throws Exception {
    if (this.getParent().getHumanProvince() == null)
      return;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);
    Boolean upstate = this.getParent().getCurrPlayer().upgradeMarket(p);
    if (!upstate) {
      appendToTerminal("upgrade unsuccessful");
    } else {
      double[] coor = getCoordinates(province);
      Point pt = new Point(coor[0] - 0.34, coor[1] - 0.25, SpatialReferences.getWgs84());
      PictureMarkerSymbol s = null;
      ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();

      if (p.getMarketLevel() == 1) {
        s = new PictureMarkerSymbol("images/m1.png");
      } else if (p.getMarketLevel() == 2) {
        s = new PictureMarkerSymbol("images/m2.png");
      }
      Graphic n = new Graphic(pt, s);
      if (p.getMarketLevel() == 1) {
        this.getParent().getMarketList().put(province, n);
        g.get(0).getGraphics().add(n);

      } else {
        Graphic old = this.getParent().getMarketList().get(province);
        for (GraphicsOverlay go : g) {
          go.getGraphics().remove(old);
        }
        g.get(0).getGraphics().add(n);
        this.getParent().getMarketList().replace(province, old, n);
      }
    }
  }

  @FXML
  private void upgradeMineButton() throws Exception {
    if (this.getParent().getHumanProvince() == null)
      return;
    String province = (String) this.getParent().getHumanProvince().getAttributes().get("name");
    Province p = this.getParent().getGame().getProvinceByName(province);
    Boolean upstate = this.getParent().getCurrPlayer().upgradeMine(p);
    if (!upstate) {
      appendToTerminal("upgrade unsuccessful");
    } else {
      double[] coor = getCoordinates(province);
      Point pt = new Point(coor[0] - 0.2, coor[1] + 0.25, SpatialReferences.getWgs84());
      PictureMarkerSymbol s = null;
      ListenableList<GraphicsOverlay> g = this.getParent().getMapView().getGraphicsOverlays();

      if (p.getMineLevel() == 2) {
        s = new PictureMarkerSymbol("images/mn1.png");
      } else if (p.getMineLevel() == 3) {
        s = new PictureMarkerSymbol("images/mn2.png");
      }
      Graphic n = new Graphic(pt, s);
      if (p.getMineLevel() == 2) {
        this.getParent().getMineList().put(province, n);
        g.get(0).getGraphics().add(n);

      } else {
        Graphic old = this.getParent().getMineList().get(province);
        for (GraphicsOverlay go : g) {
          go.getGraphics().remove(old);
        }
        g.get(0).getGraphics().add(n);
        this.getParent().getMineList().replace(province, old, n);
      }
    }
  }

  public Boolean conquerGoal() {
    if (!this.getParent().getGame().getGoal().equals("Conquer Goal"))
      return false;
    if (this.getParent().getCurrPlayer().getProvinces().size() < 53)
      return false;
    appendToTerminal(" ");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal("Congratulations!");
    appendToTerminal("Conquer Goal achieved!");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal(" ");
    if (!hasWon) {
      Popup.display();
      hasWon = true;
    }
    return true;
  }

  // 100,000 gold (TREASURY goal)
  public Boolean treasuryGoal() {
    if (!this.getParent().getGame().getGoal().equals("Treasury Goal"))
      return false;
    if (this.getParent().getCurrPlayer().CalculateTotalTreasurey() < 1000000)
      return false;
    appendToTerminal(" ");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal("Congratulations!");
    appendToTerminal("Treasury Goal achieved!");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal(" ");
    if (!hasWon) {
      Popup.display();
      hasWon = true;
    }
    return true;
  }

  public Boolean wealthGoal() {
    if (!this.getParent().getGame().getGoal().equals("Wealth Goal"))
      return false;
    if (this.getParent().getCurrPlayer().CalculateTotalWealth() < 4000000)
      return false;
    appendToTerminal(" ");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal("Congratulations!");
    appendToTerminal("Wealth Goal achieved!");
    appendToTerminal("+++++++++++++++++++++++++++++++");
    appendToTerminal(" ");
    if (!hasWon) {
      Popup.display();
      hasWon = true;
    }
    return true;
  }
}
