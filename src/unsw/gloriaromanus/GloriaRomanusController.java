package unsw.gloriaromanus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.Collections;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.GeoPackage;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.internal.io.handler.request.ServerContextConcurrentHashMap.HashMapChangedEvent.Action;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol.HorizontalAlignment;
import com.esri.arcgisruntime.symbology.TextSymbol.VerticalAlignment;
import com.esri.arcgisruntime.data.Feature;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.text.Text;
import javafx.util.Pair;

public class GloriaRomanusController {

  @FXML
  private MapView mapView;
  @FXML
  private TextArea output_terminal;
  @FXML
  private TextArea player_information;
  @FXML
  private TextArea player_provinces;
  ///////////////////////////////////////
  @FXML
  private ChoiceBox<String> units = new ChoiceBox<>();
  ///////////////////////////////////////

  @FXML
  private StackPane stackPaneMain;

  private ArrayList<Pair<MenuController, VBox>> controllerParentPairs;


  private ArcGISMap map;

  private Map<String, String> provinceToOwningFactionMap;

  private Map<String, Integer> provinceToNumberTroopsMap;

  private HashMap<String, Graphic> graphicList;
  private HashMap<String, Graphic> flagList;
  private HashMap<String, Graphic> buildingList;
  private HashMap<String, Graphic> marketList;
  private HashMap<String, Graphic> mineList;

  private Boolean newGame;

  private Boolean loadGame;

  private Boolean saveGame;

  private Feature currentlySelectedHumanProvince;
  //private Feature currentlySelectedHumanProvinceToMove;
  private Feature currentlySelectedEnemyProvince;

  private FeatureLayer featureLayer_provinces;

  private Player currPlayer;
  private Game game;
  private String f1 = null;
  private String f2 = null;
  private String comGoal = null;
 

  @FXML
  private void initialize() throws Exception {
    // TODO = you should rely on an object oriented design to determine ownership

    graphicList = new HashMap<String, Graphic>();
    flagList = new HashMap<String, Graphic>();
    buildingList = new HashMap<String, Graphic>();
    marketList = new HashMap<String, Graphic>();
    mineList = new HashMap<String, Graphic>();
    provinceToOwningFactionMap = getProvinceToOwningFactionMap();
    
    provinceToNumberTroopsMap = new HashMap<String, Integer>();
    for (String provinceName : provinceToOwningFactionMap.keySet()) {
      provinceToNumberTroopsMap.put(provinceName, 0);
    }

    
    String[] menus = { "basic_menu.fxml", "invasion_menu.fxml" };
    controllerParentPairs = new ArrayList<Pair<MenuController, VBox>>();
    for (String fxmlName : menus) {
      System.out.println(fxmlName);
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName));
      VBox root = (VBox) loader.load();
      MenuController menuController = (MenuController) loader.getController();
      menuController.setParent(this);
      controllerParentPairs.add(new Pair<MenuController, VBox>(menuController, root));
    }

    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());

    initializeProvinceLayers();
    game = new Game();
    
    // Initialise all classes to have an instant (Game , ManageTurn,) // initalise
    // all classes which need to be intialised
    // add 2 players in class

    
    // TODO = load this from a configuration file you create (user should be able to
    // select in loading screen)
    // TINA CHOOSE FACTION HERE
    // humanFaction = "Rome";
    currPlayer = game.getPlayes().get(0);
    currentlySelectedHumanProvince = null;
    currentlySelectedEnemyProvince = null;

  }

  public HashMap<String, Graphic> getMarketList(){
    return marketList;
  }


  public HashMap<String, Graphic> getBuildingList(){
    return buildingList;
  }

  public void setCurrPlayer(Player currPlayer) {
    this.currPlayer = currPlayer;
  }

  public HashMap<String, Graphic> getGraphicList() {
    return graphicList;
  }

  public HashMap<String, Graphic> getFlagList() {
    return flagList;
  }

  public ArrayList<Pair<MenuController, VBox>> getControllerParentPairs() {
    return controllerParentPairs;
  }

  public StackPane getStackPane() {
    return stackPaneMain;
  }

  public MapView getMapView() {
    return mapView;
  }

  public Game getGame() {
    return game;
  }

  public Player getCurrPlayer() {
    return currPlayer;
  }

  public Feature getHumanProvince() {
    return currentlySelectedHumanProvince;
  }

  public void addToGraphicList(String id, Graphic g){
    graphicList.put(id, g);
  }

  public HashMap<String,Graphic> getMineList(){
    return mineList;
  }
  public void replaceGraphicList(String id, Graphic old, Graphic newg){
    graphicList.replace(id, old, newg);
  }

  public Feature getEnemyProvince() {
    return currentlySelectedEnemyProvince;
  }

  // public Feature getHumanProvinceToMove() {
  //   return currentlySelectedHumanProvinceToMove;
  // }

  
  /**
   * run this initially to update province owner, change feature in each
   * FeatureLayer to be visible/invisible depending on owner. Can also update
   * graphics initially
   */
  private void initializeProvinceLayers() throws JsonParseException, JsonMappingException, IOException {

    Basemap myBasemap = Basemap.createImagery();
    // myBasemap.getReferenceLayers().remove(0);
    map = new ArcGISMap(myBasemap);
    mapView.setMap(map);

    // note - tried having different FeatureLayers for AI and human provinces to
    // allow different selection colors, but deprecated setSelectionColor method
    // does nothing
    // so forced to only have 1 selection color (unless construct graphics overlays
    // to give color highlighting)
    GeoPackage gpkg_provinces = new GeoPackage("src/unsw/gloriaromanus/provinces_right_hand_fixed.gpkg");
    gpkg_provinces.loadAsync();
    gpkg_provinces.addDoneLoadingListener(() -> {
      if (gpkg_provinces.getLoadStatus() == LoadStatus.LOADED) {
        // create province border feature
        featureLayer_provinces = createFeatureLayer(gpkg_provinces);
        map.getOperationalLayers().add(featureLayer_provinces);

      } else {
        System.out.println("load failure");
      }
    });

    addAllPointGraphics();
  }

  private void addAllPointGraphics() throws JsonParseException, JsonMappingException, IOException {
    mapView.getGraphicsOverlays().clear();

    InputStream inputStream = new FileInputStream(new File("src/unsw/gloriaromanus/provinces_label.geojson"));
    FeatureCollection fc = new ObjectMapper().readValue(inputStream, FeatureCollection.class);

    GraphicsOverlay graphicsOverlay = new GraphicsOverlay();

    for (org.geojson.Feature f : fc.getFeatures()) {
      if (f.getGeometry() instanceof org.geojson.Point) {
        org.geojson.Point p = (org.geojson.Point) f.getGeometry();
        LngLatAlt coor = p.getCoordinates();
        Point curPoint = new Point(coor.getLongitude(), coor.getLatitude(), SpatialReferences.getWgs84());
        PictureMarkerSymbol s = null;
        String province = (String) f.getProperty("name");
        String faction = provinceToOwningFactionMap.get(province);

        TextSymbol t = new TextSymbol(10, faction + "\n" + province + "\n" + provinceToNumberTroopsMap.get(province),
            0xFFFF0000, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);

        switch (faction) {
          case "Gaul":
            // note can instantiate a PictureMarkerSymbol using the JavaFX Image class - so
            // could
            // construct it with custom-produced BufferedImages stored in Ram
            // http://jens-na.github.io/2013/11/06/java-how-to-concat-buffered-images/
            // then you could convert it to JavaFX image
            // https://stackoverflow.com/a/30970114

            // you can pass in a filename to create a PictureMarkerSymbol...
            s = new PictureMarkerSymbol(new Image(
                (new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString()));
            break;
          case "Rome":
            // you can also pass in a javafx Image to create a PictureMarkerSymbol
            // (different to BufferedImage)
            s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png");
            break;
          // TODO = handle all faction names, and find a better structure...
        }
        
        
        //Point sp_p = new Point(longitude+20, latitude+20, SpatialReferences.getWgs84());
        //Graphic grff = new Graphic(sp_p ,sp);
  
        //Point ptf = new Point(longitude, latitude, SpatialReferences.getWgs84());
       
        t.setHaloColor(0xFFFFFFFF);
        t.setHaloWidth(2);
        // Graphic test = new Graphic(newpos,sth);
        // Graphic test1 = new Graphic(newposs,chrt);
        Graphic gPic = new Graphic(curPoint, s);
        flagList.put(province, gPic);
        Graphic gText = new Graphic(curPoint, t);
        graphicsOverlay.getGraphics().add(gPic);
        graphicsOverlay.getGraphics().add(gText);
        // graphicsOverlay.getGraphics().add(test);
        // graphicsOverlay.getGraphics().add(test1);
      } else {
        System.out.println("Non-point geo json object in file");
      }

    }

    inputStream.close();
    mapView.getGraphicsOverlays().add(graphicsOverlay);
  }



  public Graphic createProvinceTypeGraphic(Province p, String type, String state) throws Exception {
    double[] coor = getCoordinates(p.getName());
    if (type.equals("flag")) {
      PictureMarkerSymbol s;
      if (p.getOwner().getFaction().getName().equals("Rome")) {
        s = new PictureMarkerSymbol("images/CS2511Sprites_No_Background/Flags/Roman/RomanFlag.png");
      } else {
        s = new PictureMarkerSymbol(
            new Image((new File("images/CS2511Sprites_No_Background/Flags/Celtic/CelticFlag.png")).toURI().toString()));
      }

      double longitude = coor[0];
      double latitude = coor[1];


      Point ptf = new Point(longitude, latitude, SpatialReferences.getWgs84());
      Graphic grf = new Graphic(ptf, s);
      return grf;

    }

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/image.json"));
    JSONObject imageAdd = new JSONObject(content);

    String imageAddress = imageAdd.getJSONObject(type).getString("address");
    PictureMarkerSymbol sth = new PictureMarkerSymbol(new Image(new File(imageAddress).toURI().toString()));
    if (state.equals("untrained"))
      sth.setOpacity((float) 0.5);
    double offset_longitude = coor[0] + imageAdd.getJSONObject(type).getDouble("offset_longitude");
    double offset_latitude = coor[1] + imageAdd.getJSONObject(type).getDouble("offset_latitude");
    Point pt = new Point(offset_longitude, offset_latitude, SpatialReferences.getWgs84());
    Graphic g = new Graphic(pt, sth);

    return g;
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


  private FeatureLayer createFeatureLayer(GeoPackage gpkg_provinces) {
    FeatureTable geoPackageTable_provinces = gpkg_provinces.getGeoPackageFeatureTables().get(0);

    // Make sure a feature table was found in the package
    if (geoPackageTable_provinces == null) {
      System.out.println("no geoPackageTable found");
      return null;
    }

    // Create a layer to show the feature table
    FeatureLayer flp = new FeatureLayer(geoPackageTable_provinces);

    // https://developers.arcgis.com/java/latest/guide/identify-features.htm
    // listen to the mouse clicked event on the map view
    mapView.setOnMouseClicked(e -> {
      // was the main button pressed?
      if (e.getButton() == MouseButton.PRIMARY) {
        // get the screen point where the user clicked or tapped
        Point2D screenPoint = new Point2D(e.getX(), e.getY());

        // specifying the layer to identify, where to identify, tolerance around point,
        // to return pop-ups only, and
        // maximum results
        // note - if select right on border, even with 0 tolerance, can select multiple
        // features - so have to check length of result when handling it
        final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(flp, screenPoint, 0,
            false, 25);

        // add a listener to the future
        identifyFuture.addDoneListener(() -> {
          try {
            // get the identify results from the future - returns when the operation is
            // complete
            IdentifyLayerResult identifyLayerResult = identifyFuture.get();
            // a reference to the feature layer can be used, for example, to select
            // identified features
            if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
              FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
              // select all features that were identified
              List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f)
                  .collect(Collectors.toList());

              if (features.size() > 1) {
                printMessageToTerminal("Have more than 1 element - you might have clicked on boundary!");
              } else if (features.size() == 1) {
                // note maybe best to track whether selected...
                Feature f = features.get(0);
                String province = (String) f.getAttributes().get("name");


                if (game.getProvinceByName(province).getOwner().equals(currPlayer)) {
                  // province owned by human
                  if (currentlySelectedHumanProvince != null) {
                    featureLayer.unselectFeature(currentlySelectedHumanProvince);
                  }
                  
                  currentlySelectedHumanProvince = f;
                  //invading_province.setText(province);
                  //units.getItems().clear();
                  // loadRecruitBox();
                  // moveTroopBox.getItems().clear();
                  // loadMoveBox();
                  // destBox.getItems().clear();
                  // loadDestBox();
                  // trainBox.getItems().clear();
                  // loadTrainBox();
                  // taxBox.getItems().clear();
                  // loadTaxBox();
                  // attackBox.getItems().clear();
                  // loadAttackBox();
                  //moveTroopBox.getItems().clear();
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController) {
                    ((InvasionMenuController) controllerParentPairs.get(0).getKey()).setInvadingProvince(province);
                  }

                } else {
                  if (currentlySelectedEnemyProvince != null) {
                    featureLayer.unselectFeature(currentlySelectedEnemyProvince);
                  }
                  currentlySelectedEnemyProvince = f;
                  if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController) {
                    ((InvasionMenuController) controllerParentPairs.get(0).getKey()).setOpponentProvince(province);
                  }
                }
                featureLayer.selectFeature(f);
              }

            }
          } catch (InterruptedException | ExecutionException ex) {
            // ... must deal with checked exceptions thrown from the async identify
            // operation
            System.out.println("InterruptedException occurred");
          }
        });
      }
    });
    return flp;
  }

  private Map<String, String> getProvinceToOwningFactionMap() throws IOException {
    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    Map<String, String> m = new HashMap<String, String>();
    for (String key : ownership.keySet()) {
      // key will be the faction name
      JSONArray ja = ownership.getJSONArray(key);
      // value is province name
      for (int i = 0; i < ja.length(); i++) {
        String value = ja.getString(i);
        m.put(value, key);
      }
    }
    return m;
  }

  private ArrayList<String> getHumanProvincesList() throws IOException {
    // https://developers.arcgis.com/labs/java/query-a-feature-layer/

    String content = Files.readString(Paths.get("src/unsw/gloriaromanus/initial_province_ownership.json"));
    JSONObject ownership = new JSONObject(content);
    return ArrayUtil.convert(ownership.getJSONArray(currPlayer.getFaction().getName()));
  }

  private boolean confirmIfProvincesConnected(String province1, String province2) throws IOException {
    String content = Files
        .readString(Paths.get("src/unsw/gloriaromanus/province_adjacency_matrix_fully_connected.json"));
    JSONObject provinceAdjacencyMatrix = new JSONObject(content);
    return provinceAdjacencyMatrix.getJSONObject(province1).getBoolean(province2);
  }

  private void resetSelections() {
    featureLayer_provinces
        .unselectFeatures(Arrays.asList(currentlySelectedEnemyProvince, currentlySelectedHumanProvince));
    currentlySelectedEnemyProvince = null;
    currentlySelectedHumanProvince = null;
    // ZOMBIE AREA! ALERT ALERT
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController) {
      ((InvasionMenuController) controllerParentPairs.get(0).getKey()).setInvadingProvince("");
      ((InvasionMenuController) controllerParentPairs.get(0).getKey()).setOpponentProvince("");
    }
    // ZOMBIE AREA! ALERT ALERT

    // invading_province.setText("");
    // opponent_province.setText("");
  }

  private void printMessageToTerminal(String message) {
    // ZOMBIE AREA! ALERT ALERT
    if (controllerParentPairs.get(0).getKey() instanceof InvasionMenuController) {
      ((InvasionMenuController) controllerParentPairs.get(0).getKey()).appendToTerminal(message);
    }
    // ZOMBIE AREA! ALERT ALERT
    // output_terminal.appendText(message + "\n");
  }

  /**
   * Stops and releases all resources used in z.
   */
  void terminate() {

    if (mapView != null) {
      mapView.dispose();
    }
  }

  public void setFactions(String f1, String f2){
    game.assignFactions(f1, f2);
  }
  public void setGoal(String g){
    game.setGoal(g);
  }
  public void switchMenu() throws JsonParseException, JsonMappingException, IOException {
    // if(fac.equals("Rome")){
    //   f1 = "Rome";
    //   f2 = "Gaul";
    // }else{
    //   f2 = "Rome";
    //   f1 = "Gaul";
    // }
    // comGoal = g;
    System.out.println("trying to switch menu");
    stackPaneMain.getChildren().remove(controllerParentPairs.get(0).getValue());
    Collections.reverse(controllerParentPairs);
    stackPaneMain.getChildren().add(controllerParentPairs.get(0).getValue());
  }


}
