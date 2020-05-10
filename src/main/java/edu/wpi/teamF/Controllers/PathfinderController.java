package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.teamF.App;
import edu.wpi.teamF.Controllers.UISettings.UISetting;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Account.Account;
import edu.wpi.teamF.ModelClasses.Directions.Directions;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.Path;
import edu.wpi.teamF.ModelClasses.PathfindAlgorithm.*;
import edu.wpi.teamF.ModelClasses.Scorer.EuclideanScorer;
import edu.wpi.teamF.ModelClasses.ServiceRequest.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.management.InstanceNotFoundException;
import lombok.SneakyThrows;

public class PathfinderController implements Initializable {

  private static int FAULKNER_MAP_HEIGHT = 1485;
  private static int FAULKNER_MAP_WIDTH = 2475;
  private static int MAIN_MAP_HEIGHT = 3400;
  private static int MAIN_MAP_WIDTH = 5000;

  public static int MAP_HEIGHT = 1485;
  public static int MAP_WIDTH = 2475;
  public static DatabaseManager databaseManager = DatabaseManager.getManager();
  public SceneController sceneController = App.getSceneController();
  // public int currentFloor;
  public AnchorPane currentPane;
  public AnchorPane mainMapPane;
  public AnchorPane mapPaneFaulkner5;
  public AnchorPane mapPaneFaulkner4;
  public AnchorPane mapPaneFaulkner3;
  public AnchorPane mapPaneFaulkner2;
  public AnchorPane mapPaneFaulkner1;
  public StackPane masterPaneFaulkner1;
  public ImageView imageViewFaulkner1;
  public ImageView imageViewFaulkner2;
  public ImageView imageViewFaulkner3;
  public ImageView imageViewFaulkner4;
  public ImageView imageViewFaulkner5;
  public ScrollPane scrollPaneFaulkner1;
  public AnchorPane selectButtonsPane;
  public AnchorPane directionsPane;
  public JFXButton stairsBtn;
  public JFXButton elevBtn;
  public JFXButton bathBtn;
  public AnchorPane selectFloorPane;
  public JFXButton floor1Button;
  public JFXButton floor2Button;
  public JFXButton floor3Button;
  public JFXButton floor4Button;
  public JFXButton floor5Button;
  public Text commandText;
  public JFXComboBox<String> startCombo;
  public JFXComboBox<String> endCombo;
  public JFXButton pathButton;
  public JFXTextArea directionsDisplay;
  public AnchorPane pathSwitchFloorPane;
  public JFXButton pathSwitchNext;
  public JFXButton pathSwitchPrevious;
  public JFXButton textDirections;
  public JFXButton callDirections;
  public JFXButton printDirections;
  public AnchorPane mapPaneMainL2;
  public AnchorPane mapPaneMainL1;
  public AnchorPane mapPaneMainG;
  public AnchorPane mapPaneMain1;
  public AnchorPane mapPaneMain2;
  public AnchorPane mapPaneMain3;
  public ImageView imageViewMainL2;
  public ImageView imageViewMainL1;
  public ImageView imageViewMainG;
  public ImageView imageViewMain1;
  public ImageView imageViewMain2;
  public ImageView imageViewMain3;
  public JFXButton mainFloor1Button;
  public JFXButton mainFloor2Button;
  public JFXButton mainFloor3Button;
  public JFXButton mainFloorGButton;
  public JFXButton mainFloorL2Button;
  public JFXButton mainFloorL1Button;
  public AnchorPane selectFloorPaneMain;
  public Label startLabel;
  public Label endLabel;
  public JFXComboBox<String> hospitalComboBox;
  public HBox btnSpacer;
  public JFXTextField phoneNumber;
  public Label commsResult;
  public AnchorPane externalDirections;
  public Boolean awaitingExternalDirections = false;

  // stairs v elev stuff
  String liftType = "ELEV";
  public JFXButton chooseLiftStairs;
  public JFXButton chooseLiftElevator;
  public JFXToggleButton liftToggle;
  public Label elevatorLabel;
  public Label stairsLabel;

  // error box stuff
  public AnchorPane errorPane;
  public Label errorPaneLabel1;
  public Label errorPaneLabel2;
  public JFXButton errorPaneButton;

  // node info stuff
  public AnchorPane nodeInfoPane;
  public Label nodeInfoLabel1;
  public Label nodeInfoLabel2;
  public Label nodeInfoLabel3;
  public JFXButton nodeInfoButton;
  public JFXComboBox<String> nodeInfoTypeCombo;
  public JFXComboBox<String> nodeInfoCombo;
  Account.Type userLevel = databaseManager.getPermissions();
  List<SanitationServiceRequest> sanitationList = databaseManager.getAllSanitationRequests();
  List<ComputerServiceRequest> computerList = databaseManager.getAllComputerServiceRequests();
  List<FlowerRequest> flowerList = databaseManager.getAllFlowerRequests();
  List<LaundryServiceRequest> laundryList = databaseManager.getAllLaunduaryRequests();
  List<MaintenanceRequest> maintenanceList = databaseManager.getAllMaintenanceRequests();
  List<MariachiRequest> mariachiList = databaseManager.getAllMariachiServiceRequests();
  List<MedicineDeliveryRequest> medicineList = databaseManager.getAllMedicineDeliveryRequests();
  List<LanguageServiceRequest> languageList = databaseManager.getAllLanguageServiceRequests();
  List<SecurityRequest> securityList = databaseManager.getAllSecurityRequests();
  List<TransportRequest> tranportList = databaseManager.getAllTransportRequests();
  ObservableList<String> tempSanitationList = FXCollections.observableArrayList();
  ObservableList<String> tempComputerList = FXCollections.observableArrayList();
  ObservableList<String> tempFlowerList = FXCollections.observableArrayList();
  ObservableList<String> tempLaundryList = FXCollections.observableArrayList();
  ObservableList<String> tempMaintenanceList = FXCollections.observableArrayList();
  ObservableList<String> tempMariachiList = FXCollections.observableArrayList();
  ObservableList<String> tempMedicineList = FXCollections.observableArrayList();
  ObservableList<String> tempLanguageList = FXCollections.observableArrayList();
  ObservableList<String> tempSecurityList = FXCollections.observableArrayList();
  ObservableList<String> tempTransportList = FXCollections.observableArrayList();

  // end of path on floor button
  public JFXButton buttonOnEnd = new JFXButton();

  // intermediate maps stuff
  public ImageView faulknerTo45FrancisImage;
  public ImageView faulknerTo75FrancisImage;
  public ImageView faulknerToBTMImage;
  public ImageView faulknerToShapiroImage;
  public ImageView faulknerTo15FrancisImage;
  public ImageView Francis45ToFaulknerImage;
  public ImageView Francis75ToFaulknerImage;
  public ImageView BTMToFaulknerImage;
  public ImageView shapiroToFaulknerImage;
  public ImageView francis15ToFaulknerImage;
  public ScrollPane scrollPaneIntermediate;
  public StackPane stackPaneIntermediate;
  public GoogleMaps googleMaps;
  public Label driveTime;
  public Label driveDistance;
  public Label transitTime;
  public Label transitDistance;
  public Label bikeTime;
  public Label bikeDistance;
  public Label walkTime;
  public Label walkDistance;
  public ImageView faulknerTo45FrancisQR;
  public ImageView faulknerTo75FrancisQR;
  public ImageView faulknerToBTMQR;
  public ImageView faulknerToShapiroQR;
  public ImageView faulknerTo15FrancisQR;
  public ImageView Francis45ToFaulknerQR;
  public ImageView Francis75ToFaulknerQR;
  public ImageView BTMToFaulknerQR;
  public ImageView shapiroToFaulknerQR;
  public ImageView francis15ToFaulknerQR;

  public List<Node> fullNodeList;
  public int state;
  public UISetting uiSetting = new UISetting();
  public UISetting intermediateSetting;
  private String currentBuilding;
  private String currentFloor;
  Node startNode = null;
  Node endNode = null;
  public Path path;
  public Path globalPath;
  public Directions directions;
  public int locationIndex;

  EuclideanScorer euclideanScorer = new EuclideanScorer();
  PathfindAlgorithm pathFindAlgorithm;
  private static String newPathfind = " ";

  public PathfinderController() throws Exception {}

  public static void setPathFindAlgorithm(String newPathFindAlgorithm) {
    newPathfind = newPathFindAlgorithm;
    System.out.println("set new pathfind: " + newPathFindAlgorithm);
  }

  private void updatePathFindAlgorithm() {
    switch (newPathfind) {
      case "A Star":
        MultipleHospitalAStar currentAlgorithm1 = new MultipleHospitalAStar(fullNodeList);
        currentAlgorithm1.setLiftType(liftType);
        this.pathFindAlgorithm = currentAlgorithm1;
        System.out.println("successful astar");
        break;
      case "Breadth First":
        BreadthFirst currentAlgorithm2 = new BreadthFirst(fullNodeList);
        currentAlgorithm2.setLiftType(liftType);
        this.pathFindAlgorithm = currentAlgorithm2;
        System.out.println("successful breath");
        break;
      case "Depth First":
        DepthFirstSearch currentAlgorithm3 = new DepthFirstSearch(fullNodeList);
        currentAlgorithm3.setLiftType(liftType);
        this.pathFindAlgorithm = currentAlgorithm3;
        System.out.println("successful Depth first");
        break;
      default:
        break;
    }
  }

  public void draw(Path path) throws InstanceNotFoundException {
    this.path = path;
    List<Node> pathNodes = path.getPath();

    double heightRatioFaulkner = currentPane.getPrefHeight() / FAULKNER_MAP_HEIGHT;
    double widthRatioFaulkner = currentPane.getPrefWidth() / FAULKNER_MAP_WIDTH;
    double heightRatioMain = currentPane.getPrefHeight() / MAIN_MAP_HEIGHT;
    double widthRatioMain = currentPane.getPrefWidth() / MAIN_MAP_WIDTH;

    if (endNode == null) {
      endNode = pathNodes.get(pathNodes.size() - 1);
    }
    try {
      ReportsClass newNode = databaseManager.readReport(startNode.getId());
      ReportsClass oldNode = databaseManager.readReport(endNode.getId());
      if (newNode != null) {
        newNode.setTimesVisited(newNode.getTimesVisited() + 1);
      } else {
        newNode = new ReportsClass(startNode.getId(), 1, 0, "NA");
      }
      if (oldNode != null) {
        oldNode.setTimesVisited(oldNode.getTimesVisited() + 1);
      } else {
        oldNode = new ReportsClass(endNode.getId(), 1, 0, "NA");
      }
      databaseManager.manipulateReport(newNode);
      databaseManager.manipulateReport(oldNode);
      System.out.println(databaseManager.readReport(newNode.getNodeID()));
      System.out.println(oldNode.getNodeID());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    double heightRatio = currentPane.getPrefHeight() / MAP_HEIGHT;
    double widthRatio = currentPane.getPrefWidth() / MAP_WIDTH;

    for (int i = 0; i < pathNodes.size() - 1; i++) {
      Node start = pathNodes.get(i);
      Node end = pathNodes.get(i + 1);
      if (start.getFloor().equals(end.getFloor())
          && sameHospital(start.getBuilding(), end.getBuilding())) {
        double startX;
        double startY;
        double endX;
        double endY;
        if ("Faulkner".equals(start.getBuilding())) {
          startX = (start.getXCoord() * widthRatioFaulkner);
          startY = (start.getYCoord() * heightRatioFaulkner);
          endX = (end.getXCoord() * widthRatioFaulkner);
          endY = (end.getYCoord() * heightRatioFaulkner);
        } else {
          startX = (start.getXCoord() * widthRatioMain);
          startY = (start.getYCoord() * heightRatioMain);
          endX = (end.getXCoord() * widthRatioMain);
          endY = (end.getYCoord() * heightRatioMain);
        }
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.RED);
        line.setStrokeWidth(2);
        animateLine(line);
        getFloorPane(pathNodes.get(i).getFloor(), pathNodes.get(i).getBuilding())
            .getChildren()
            .add(line);
      }
    }

    // path end button
    for (Node node : pathNodes) {
      List<Node> nodesOnFloor = getNodesOnFloor(node.getFloor());
      if ((Node.NodeType.ELEV.equals(node.getType()) || Node.NodeType.STAI.equals(node.getType()))
          && node.getId().equals(nodesOnFloor.get(nodesOnFloor.size() - 1).getId())) {
        JFXButton endButton = new JFXButton();
        endButton.setId(node.getId());
        endButton.setMinSize(6, 6);
        endButton.setMaxSize(6, 6);
        endButton.setPrefSize(6, 6);
        double xPos;
        double yPos;
        if ("Faulkner".equals(node.getBuilding())) {
          xPos = ((node.getXCoord() * widthRatioFaulkner) - 3);
          yPos = ((node.getYCoord() * heightRatioFaulkner) - 3);
        } else {
          xPos = ((node.getXCoord() * widthRatioMain) - 3);
          yPos = ((node.getYCoord() * heightRatioMain) - 3);
        }
        endButton.setLayoutX(xPos);
        endButton.setLayoutY(yPos);
        endButton.setStyle(
            "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #ff0000; -fx-border-color: #000000; -fx-border-width: 1px"); // 00cc00
        endButton.setOnMouseClicked(
            actionEvent -> {
              pathSwitchNext.fire();
            });
      } else if ((Node.NodeType.ELEV.equals(node.getType())
              || Node.NodeType.STAI.equals(node.getType()))
          && node.getId().equals(nodesOnFloor.get(0).getId())) {
        JFXButton endButton = new JFXButton();
        endButton.setId(node.getId());
        endButton.setMinSize(6, 6);
        endButton.setMaxSize(6, 6);
        endButton.setPrefSize(6, 6);
        double xPos;
        double yPos;
        if ("Faulkner".equals(node.getBuilding())) {
          xPos = ((node.getXCoord() * widthRatioFaulkner) - 3);
          yPos = ((node.getYCoord() * heightRatioFaulkner) - 3);
        } else {
          xPos = ((node.getXCoord() * widthRatioMain) - 3);
          yPos = ((node.getYCoord() * heightRatioMain) - 3);
        }
        endButton.setLayoutX(xPos);
        endButton.setLayoutY(yPos);
        endButton.setStyle(
            "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #00cc00; -fx-border-color: #000000; -fx-border-width: 1px"); // 800000
        endButton.setOnMouseClicked(
            actionEvent -> {
              pathSwitchPrevious.fire();
            });
      }
    }

    selectButtonsPane.setVisible(false);
    directionsPane.setVisible(true);
    this.directions = new Directions(fullNodeList, path, startNode, endNode);
    System.out.println(directions.getFullDirectionsString());
    pathSwitchFloorPane.setVisible(true);

    System.out.println("Number of Uniques: " + path.getUniqueLocations());
    if (path.getUniqueLocations() > 1) {
      // Spans multiple floors
      pathSwitchPrevious.setVisible(false);
      pathSwitchPrevious.setPrefHeight(0);
      pathSwitchNext.setVisible(true);
      pathSwitchNext.setPrefHeight(50);
      btnSpacer.setPrefHeight(0);

      if ((path.getLocationAtIndex(1).getBuilding().equals("OUT"))) {
        // Next stop is at a different hospital
        pathSwitchNext.setText("Next: Go to " + path.getLocationAtIndex(2).getBuilding());
      } else {
        pathSwitchNext.setText(
            "Next: Go to floor " + path.getLocationAtIndex(1).getFloor().replace("F", ""));
      }
      locationIndex = 0;
      System.out.println("Changed to Index: " + locationIndex);
      System.out.println("Floor: " + path.getLocationAtIndex(locationIndex).getFloor());
      System.out.println("Building: " + path.getLocationAtIndex(locationIndex).getBuilding());

      directionsDisplay.setText(directions.getDirectionsStringForIndex(locationIndex));
    } else {
      // Single floor navigation
      pathSwitchPrevious.setVisible(false);
      pathSwitchPrevious.setPrefHeight(0);
      pathSwitchNext.setVisible(false);
      pathSwitchNext.setPrefHeight(0);
      btnSpacer.setPrefHeight(0);

      directionsDisplay.setText(directions.getFullDirectionsString());
    }
  }

  public void placeButton(Node node) {

    double heightRatioFaulkner = (double) currentPane.getPrefHeight() / FAULKNER_MAP_HEIGHT;
    double widthRatioFaulkner = (double) currentPane.getPrefWidth() / FAULKNER_MAP_WIDTH;
    double heightRatioMain = (double) currentPane.getPrefHeight() / MAIN_MAP_HEIGHT;
    double widthRatioMain = (double) currentPane.getPrefWidth() / MAIN_MAP_WIDTH;

    JFXButton button = new JFXButton();
    button.setId(node.getId());
    button.setMinSize(6, 6);
    button.setMaxSize(6, 6);
    button.setPrefSize(6, 6);
    button.setStyle(
        "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; -fx-border-color: #000000; -fx-border-width: 1px"); // ff0000
    double xPos;
    double yPos;
    if ("Faulkner".equals(node.getBuilding())) {
      xPos = ((node.getXCoord() * widthRatioFaulkner) - 3);
      yPos = ((node.getYCoord() * heightRatioFaulkner) - 3);
    } else {
      xPos = ((node.getXCoord() * widthRatioMain) - 3);
      yPos = ((node.getYCoord() * heightRatioMain) - 3);
    }
    button.setLayoutX(xPos);
    button.setLayoutY(yPos);

    button.setOnMouseClicked(
        actionEvent -> {
          if (actionEvent.getButton() == MouseButton.SECONDARY) {
            nodeInfoPane.setVisible(true);
            Node whichNode = null;
            for (Node thisNode : fullNodeList) {
              if (thisNode.getId().equals(button.getId())) {
                whichNode = thisNode;
              }
            }
            try {
              setNodeInfoLabels(whichNode);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    button.setOnAction(
        actionEvent -> {
          if (startNode == node && state == 1) { // Click again to de-select if start has been set
            startNode = null;
            button.setStyle(
                "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; -fx-border-color: #000000; -fx-border-width: 1px"); // ff0000
            state = 0;
            startCombo.setValue(null);
            startCombo.setDisable(false);
          } else if (endNode == node) { // deselect if end has been set, return to 1
            endNode = null;
            button.setStyle(
                "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; -fx-border-color: #000000; -fx-border-width: 1px"); // ff0000
            state = 1;
            endCombo.setValue(null);
            pathButton.setDisable(true);
            endCombo.setDisable(false);
          } else if (state == 0) { // if nothing has been set
            startNode = node;
            stairsBtn.setDisable(false);
            elevBtn.setDisable(false);
            bathBtn.setDisable(false);
            button.setStyle(
                "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #00cc00; -fx-border-color: #000000; -fx-border-width: 1px"); // 800000
            commandText.setText("Select End Location or Building Feature");
            state = 1;
            // startCombo.setDisable(true);
            startCombo.setValue(node.getLongName() + " " + node.getId());
            endCombo.setDisable(false);
          } else if (state == 1) { // select end if not set
            endNode = node;
            button.setStyle(
                "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #ff0000; -fx-border-color: #000000; -fx-border-width: 1px"); // 00cc00
            commandText.setText("Select Find Path or Reset");
            state = 2;
            // endCombo.setDisable(true);
            endCombo.setValue(node.getLongName() + " " + node.getId());
            pathButton.setDisable(false);
          }
        });
    getFloorPane(node.getFloor(), node.getBuilding()).getChildren().add(button);
  }

  public void reset() {
    resetPane();
  }

  public void resetPane() {
    resetButtonLine("1", "Faulkner");
    resetButtonLine("2", "Faulkner");
    resetButtonLine("3", "Faulkner");
    resetButtonLine("4", "Faulkner");
    resetButtonLine("5", "Faulkner");
    resetButtonLine("F1", "Main");
    resetButtonLine("F2", "Main");
    resetButtonLine("F3", "Main");
    resetButtonLine("G", "Main");
    resetButtonLine("L1", "Main");
    resetButtonLine("L2", "Main");

    if (startNode != null) {
      for (javafx.scene.Node component : currentPane.getChildren()) {
        if (component.getId().equals(startNode.getId())) {
          component.setStyle(
              "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; "
                  + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
        }
      }
    }
    if (endNode != null) {
      for (javafx.scene.Node component : currentPane.getChildren()) {
        if (component.getId().equals(endNode.getId())) {
          component.setStyle(
              "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; "
                  + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
        }
      }
    }
    startNode = null;
    endNode = null;
    state = 0;

    startCombo.setDisable(false);
    endCombo.setDisable(true);
    pathButton.setDisable(true);
    stairsBtn.setDisable(true);
    elevBtn.setDisable(true);
    bathBtn.setDisable(true);
    commandText.setText("Select Starting Location");
    directionsPane.setVisible(false);
    selectButtonsPane.setVisible(true);
    pathSwitchFloorPane.setVisible(false);
    commsResult.setText("");
    externalDirections.setVisible(false);
    externalDirections.setPrefHeight(0);
    externalDirections.setPrefWidth(0);

    uiSetting.makeZoomable(scrollPaneFaulkner1, masterPaneFaulkner1, 1.33);

    startCombo.setValue(null);
    endCombo.setValue(null);

    globalPath = null;

    setComboBehavior();

    if (startLabel != null) {
      startLabel.setVisible(false);
    }
    if (endLabel != null) {
      endLabel.setVisible(false);
    }
    phoneNumber.setText("");
  }

  private void resetButtonLine(String floor, String building) {
    List<javafx.scene.Node> nodesToRemove = new ArrayList<>();
    for (javafx.scene.Node node : getFloorPane(floor, building).getChildren()) {
      if (node instanceof Line) {
        nodesToRemove.add(node);
      } else if (node instanceof JFXButton) {
        JFXButton button = (JFXButton) node;
        button.setStyle(
            "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; "
                + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
      }
    }
    getFloorPane(floor, building).getChildren().removeAll(nodesToRemove);
  }

  public void drawNodes() {
    for (Node node : fullNodeList) {
      if (!node.getType().equals(Node.NodeType.getEnum("HALL"))
          && !node.getType().equals(Node.NodeType.getEnum("STAI"))
          && !node.getType().equals(Node.NodeType.getEnum("ELEV"))
          && !node.getType().equals(Node.NodeType.getEnum("REST"))) {
        placeButton(node);
        pathButtonGo();
      }
    }
  }

  @SneakyThrows
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    fullNodeList = new ArrayList<>();
    currentPane = mapPaneFaulkner1;
    currentFloor = "1";
    setAllInvisible();
    selectFloorPaneMain.setVisible(false);
    scrollPaneFaulkner1.setVisible(true);
    mapPaneFaulkner1.setVisible(true);
    imageViewFaulkner1.setVisible(true);
    errorPane.setVisible(false);
    nodeInfoPane.setVisible(false);
    floorButtonsSet();
    initializehospitalComboBox();
    setToggleBehavior();
    setErrorPaneButtonBehavior();
    externalDirections.setVisible(false);
    externalDirections.setPrefWidth(0);
    externalDirections.setPrefHeight(0);
    setIntermediateMapsInvisible();

    UISetting uiSetting = new UISetting();
    UISetting intermediateSetting = new UISetting();
    uiSetting.setAsLocationComboBox(startCombo);
    uiSetting.setAsLocationComboBox(endCombo);

    uiSetting.makeZoomable(scrollPaneFaulkner1, masterPaneFaulkner1, 1.33);
    intermediateSetting.makeZoomable(scrollPaneIntermediate, stackPaneIntermediate, 1.00);

    for (Node node : databaseManager.getAllNodes()) {
      node.setEdges(databaseManager.getAllEdgesConnectedToNode(node.getId()));
      fullNodeList.add(node);
    }

    pathFindAlgorithm = new MultipleHospitalAStar(fullNodeList);
    System.out.println("NEW PATHFIND:  " + newPathfind);
    updatePathFindAlgorithm();
    resetPane();
    drawNodes();
    deselectFloorButtons();
    floor1Button.setStyle("-fx-background-color: #012D5A; -fx-background-radius: 10px");
    directionsPane.setVisible(false);
    //    setChooseLiftBehavior();

    // Set up the enable/disable of phone comms btns
    callDirections.setDisable(true);
    textDirections.setDisable(true);
    phoneNumber
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() == 10
                    && newValue.matches("[0-9]+")
                    && directionsDisplay.getText().length() > 0) {
                  callDirections.setDisable(false);
                  textDirections.setDisable(false);
                } else {
                  callDirections.setDisable(true);
                  textDirections.setDisable(true);
                }
              }
            });
  }

  public void animateLine(Line line) {

    line.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
    line.setStrokeWidth(2);

    final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)),
            new KeyFrame(
                Duration.seconds(1),
                new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)));

    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  private void initializehospitalComboBox() {
    hospitalComboBox.setItems(FXCollections.observableArrayList("Faulkner", "Main Campus"));
    hospitalComboBox.setValue("Faulkner");
    hospitalComboBox
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              if (oldValue != null && !oldValue.equals(newValue)) {
                if ("Faulkner".equals(newValue)) {
                  switchToFloor("1", "Faulkner");

                } else if ("Main Campus".equals(newValue)) {
                  switchToFloor("G", "Main Campus");
                }
              }
            }));
  }

  public void findType(String type) throws InstanceNotFoundException {
    switchToFloor(startNode.getFloor(), startNode.getBuilding());
    startCombo.setDisable(true);
    endCombo.setDisable(true);
    globalPath = pathFindAlgorithm.pathfind(startNode, Node.NodeType.getEnum(type));
    draw(globalPath);
    commandText.setText("See Details Below or Reset for New Path");
  }

  public void findElevator(MouseEvent mouseEvent) throws InstanceNotFoundException {
    String holdLiftType = pathFindAlgorithm.getLiftType();
    pathFindAlgorithm.setLiftType("ELEV");
    findType("ELEV");
    pathFindAlgorithm.setLiftType(holdLiftType);
  }

  public void findStairs(MouseEvent mouseEvent) throws InstanceNotFoundException {
    String holdLiftType = pathFindAlgorithm.getLiftType();
    pathFindAlgorithm.setLiftType("STAI");
    findType("STAI");
    pathFindAlgorithm.setLiftType(holdLiftType);
  }

  public void findBathroom(MouseEvent mouseEvent) throws InstanceNotFoundException {
    findType("REST");
  }

  public void comboSelectStart() {
    String startLocation = startCombo.getValue();
    if (startLocation.length() > 10) {
      String startID = startLocation.substring(startLocation.length() - 10);
      for (Node node : fullNodeList) {
        if (node.getId().equals(startID)) {
          if (startNode != null) {
            for (javafx.scene.Node component : currentPane.getChildren()) {
              if (component.getId().equals(startNode.getId())) {
                component.setStyle(
                    "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; "
                        + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
              }
            }
          }
          startNode = node;
          stairsBtn.setDisable(false);
          elevBtn.setDisable(false);
          bathBtn.setDisable(false);
          for (javafx.scene.Node component :
              getFloorPane(node.getFloor(), node.getBuilding()).getChildren()) {
            if (component.getId().equals(node.getId())) {
              component.setStyle(
                  "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #00cc00; "
                      + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
            }
          }
        }
      }
    }
  }

  public void comboSelectEnd() {
    String endLocation = endCombo.getValue();
    if (endLocation.length() > 10) {
      String endID = endLocation.substring(endLocation.length() - 10);
      for (Node node : fullNodeList) {

        if (node.getId().equals(endID)) {
          if (endNode != null) {
            for (javafx.scene.Node component : currentPane.getChildren()) {
              if (component.getId().equals(endNode.getId())) {
                component.setStyle(
                    "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #99d9ea; "
                        + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
              }
            }
          }
          endNode = node;
          stairsBtn.setDisable(true);
          elevBtn.setDisable(true);
          bathBtn.setDisable(true);
          for (javafx.scene.Node component :
              getFloorPane(node.getFloor(), node.getBuilding()).getChildren()) {
            if (component.getId().equals(node.getId())) {
              component.setStyle(
                  "-fx-background-radius: 10px; -fx-border-radius: 10px; -fx-background-color: #ff0000; "
                      + "-fx-border-color: #000000; -fx-border-width: 1px"); // 800000
            }
          }
        }
      }
    }
  }

  public Node findComboLocation(JFXComboBox<String> comboBox) {
    Node returnNode = null;
    String location = comboBox.getValue();
    if (location.length() > 10) {
      String startID = location.substring(location.length() - 10);
      for (Node node : fullNodeList) {
        if (node.getId().equals(startID)) {
          returnNode = node;
        }
      }
    }
    return returnNode;
  }

  public void setComboBehavior() {
    startCombo.setOnAction(
        actionEvent -> {
          String startLocation = startCombo.getValue();
          if (startLocation != null && startLocation.length() > 10) {
            comboSelectStart();
            state = 1;
            commandText.setText("Select End Location or Building Feature");
            endCombo.setDisable(false);
            if (findComboLocation(startCombo).getFloor().equals(currentFloor)
                && findComboLocation(startCombo).getBuilding().equals(currentBuilding)) {
              String nameHolder = startCombo.getValue();
              Node nodeHolder = findComboLocation(startCombo);
              switchToFloor(nodeHolder.getFloor(), nodeHolder.getBuilding());
              startCombo.setValue(nameHolder);
              endNode = nodeHolder;
            }
          }
        });

    endCombo.setOnAction(
        actionEvent -> {
          String endLocation = endCombo.getValue();
          if (endLocation != null && endLocation.length() > 10) {
            comboSelectEnd();
            state = 2;
            commandText.setText("Select Find Path or Reset");
            pathButton.setDisable(false);
          }
        });
  }

  public void pathButtonGo() {
    pathButton.setOnAction(
        actionEvent -> {
          startCombo.setDisable(true);
          endCombo.setDisable(true);
          startNode = findComboLocation(startCombo);
          endNode = findComboLocation(endCombo);
          System.out.println("start" + startNode);
          System.out.println("end" + endNode);
          globalPath = null;

          try {
            globalPath = pathFindAlgorithm.pathfind(startNode, endNode);
          } catch (InstanceNotFoundException e) {
            e.printStackTrace();
          }

          if (null == globalPath.getPath()) {
            showErrorPane();
          } else {
            try {
              commandText.setText("See Path Below for Directions");
              draw(globalPath);
              zoomToPath(globalPath.getPath().get(0).getFloor(), globalPath);
            } catch (InstanceNotFoundException e) {
              e.printStackTrace();
            }
          }

          switchToFloor(startNode.getFloor(), startNode.getBuilding());
          if (!"Faulkner".equals(startNode.getBuilding())) {
            labelNodeStart();
          }
          if (!"Faulkner".equals(endNode.getBuilding())) {
            labelNodeEnd();
          }
        });
  }

  public void floorButtonsSet() {
    floor1Button.setOnAction(actionEvent -> switchToFloor("1", "Faulkner"));
    floor2Button.setOnAction(actionEvent -> switchToFloor("2", "Faulkner"));
    floor3Button.setOnAction(actionEvent -> switchToFloor("3", "Faulkner"));
    floor4Button.setOnAction(actionEvent -> switchToFloor("4", "Faulkner"));
    floor5Button.setOnAction(actionEvent -> switchToFloor("5", "Faulkner"));
    mainFloor1Button.setOnAction(actionEvent -> switchToFloor("F1", "Main"));
    mainFloor2Button.setOnAction(actionEvent -> switchToFloor("F2", "Main"));
    mainFloor3Button.setOnAction(actionEvent -> switchToFloor("F3", "Main"));
    mainFloorGButton.setOnAction(actionEvent -> switchToFloor("G", "Main"));
    mainFloorL1Button.setOnAction(actionEvent -> switchToFloor("L1", "Main"));
    mainFloorL2Button.setOnAction(actionEvent -> switchToFloor("L2", "Main"));
  }

  public void deselectFloorButtons() {
    for (javafx.scene.Node btn : selectFloorPane.getChildren()) {
      btn.setStyle("-fx-background-color: #4d6c8b; -fx-background-radius: 10px");
    }
    for (javafx.scene.Node btn : selectFloorPaneMain.getChildren()) {
      btn.setStyle("-fx-background-color: #4d6c8b; -fx-background-radius: 10px");
    }
  }

  public void setAllInvisible() {
    mapPaneFaulkner1.setVisible(false);
    imageViewFaulkner1.setVisible(false);
    mapPaneFaulkner2.setVisible(false);
    imageViewFaulkner2.setVisible(false);
    mapPaneFaulkner3.setVisible(false);
    imageViewFaulkner3.setVisible(false);
    mapPaneFaulkner4.setVisible(false);
    imageViewFaulkner4.setVisible(false);
    mapPaneFaulkner5.setVisible(false);
    imageViewFaulkner5.setVisible(false);
    mapPaneMain1.setVisible(false);
    mapPaneMain2.setVisible(false);
    mapPaneMain3.setVisible(false);
    mapPaneMainG.setVisible(false);
    mapPaneMainL1.setVisible(false);
    mapPaneMainL2.setVisible(false);
    imageViewMain1.setVisible(false);
    imageViewMain2.setVisible(false);
    imageViewMain3.setVisible(false);
    imageViewMainG.setVisible(false);
    imageViewMainL1.setVisible(false);
    imageViewMainL2.setVisible(false);
  }

  public void switchToFloor(String floorNum, String building) {
    if ("Faulkner".equals(building)) {
      hospitalComboBox.setValue("Faulkner");
      selectFloorPane.setVisible(true);
      selectFloorPaneMain.setVisible(false);
    } else {
      hospitalComboBox.setValue("Main Campus");
      selectFloorPane.setVisible(false);
      selectFloorPaneMain.setVisible(true);
    }

    currentPane = getFloorPane(floorNum, building);
    currentFloor = floorNum;
    currentBuilding = building;
    System.out.println("Testing globalPath");

    if (null != globalPath) {
      System.out.println("globalPath not null");
      // placeFloorEndButton(floorNum);
      if (null != globalPath.getPath()) {
        System.out.println("globalPath.getPath() not null");
        zoomToPath(floorNum, globalPath);
      }
    }

    setAllInvisible();
    currentPane.setVisible(true);
    getFloorImage(floorNum, building).setVisible(true);

    deselectFloorButtons();
    getFloorButton(floorNum, building)
        .setStyle("-fx-background-color: #012D5A; -fx-background-radius: 10px");
  }

  public void pathSwitchPrevious(ActionEvent actionEvent) {
    locationIndex--;
    pathSwitchNext.setVisible(true);
    pathSwitchNext.setPrefHeight(50);
    btnSpacer.setPrefHeight(10);
    // Disable the external directions pane
    externalDirections.setVisible(false);
    externalDirections.setPrefHeight(0);
    externalDirections.setPrefWidth(0);
    System.out.println("Changed to Index: " + locationIndex);
    System.out.println("Floor: " + path.getLocationAtIndex(locationIndex).getFloor());
    System.out.println("Building: " + path.getLocationAtIndex(locationIndex).getBuilding());
    switchToFloor(
        path.getLocationAtIndex(locationIndex).getFloor(),
        path.getLocationAtIndex(locationIndex).getBuilding());

    directionsDisplay.setText(directions.getDirectionsStringForIndex(locationIndex));

    if (locationIndex == 0) {
      // If we have gotten back to the first floor, disable and hide the previous button
      pathSwitchPrevious.setVisible(false);
      pathSwitchPrevious.setPrefHeight(0);
      btnSpacer.setPrefHeight(0);
    } else if ("OUT".equals(path.getLocationAtIndex(locationIndex).getBuilding())) {
      // This is the outdoor node, show the external directions
      externalDirections.setPrefHeight(720);
      externalDirections.setPrefWidth(1070);
      externalDirections.setVisible(true);
      setExternalDirections(
          path.getLocationAtIndex(locationIndex - 1).getBuilding(),
          path.getLocationAtIndex(locationIndex + 1).getBuilding());
      pathSwitchNext.setText(
          "Arrive at " + path.getLocationAtIndex(locationIndex + 1).getBuilding());
      pathSwitchPrevious.setText(
          "Return to " + path.getLocationAtIndex(locationIndex - 1).getBuilding());
    } else if ("OUT".equals(path.getLocationAtIndex(locationIndex - 1).getBuilding())) {
      // Exiting a building to go somewhere
      pathSwitchPrevious.setText(
          "Previous: Go to " + path.getLocationAtIndex(locationIndex - 2).getBuilding());
    } else {
      pathSwitchPrevious.setText(
          "Previous: Go to floor "
              + path.getLocationAtIndex(locationIndex - 1).getFloor().replace("F", ""));
    }

    // Need to update the text for previous button
    if (!"OUT".equals(path.getLocationAtIndex(locationIndex).getBuilding())) {
      if ("OUT".equals(path.getLocationAtIndex(locationIndex + 1).getBuilding())) {
        // next node was the outdoor node
        pathSwitchNext.setText(
            "Next: Go to " + path.getLocationAtIndex(locationIndex + 2).getBuilding());
      } else {
        pathSwitchNext.setText(
            "Next: Go to floor "
                + path.getLocationAtIndex(locationIndex + 1).getFloor().replace("F", ""));
      }
    }
  }

  public void pathSwitchNext(ActionEvent actionEvent) {
    locationIndex++;
    pathSwitchPrevious.setVisible(true);
    pathSwitchPrevious.setPrefHeight(50);
    btnSpacer.setPrefHeight(10);
    // Disable the external directions pane
    externalDirections.setVisible(false);
    externalDirections.setPrefHeight(0);
    externalDirections.setPrefWidth(0);
    System.out.println("Changed to Index: " + locationIndex);
    System.out.println("Floor: " + path.getLocationAtIndex(locationIndex).getFloor());
    System.out.println("Building: " + path.getLocationAtIndex(locationIndex).getBuilding());
    if (!"OUT".equals(path.getLocationAtIndex(locationIndex).getBuilding())) {
      switchToFloor(
          path.getLocationAtIndex(locationIndex).getFloor(),
          path.getLocationAtIndex(locationIndex).getBuilding());
    }
    directionsDisplay.setText(directions.getDirectionsStringForIndex(locationIndex));

    if (locationIndex == (path.getUniqueLocations() - 1)) {
      // If we have gotten to the final location, disable and hide the next button
      pathSwitchNext.setVisible(false);
      pathSwitchNext.setPrefHeight(0);
      btnSpacer.setPrefHeight(0);
    } else if ("OUT".equals(path.getLocationAtIndex(locationIndex).getBuilding())) {
      // This is the outdoor node, show the external directions
      externalDirections.setPrefHeight(720);
      externalDirections.setPrefWidth(1070);
      externalDirections.setVisible(true);
      setExternalDirections(
          path.getLocationAtIndex(locationIndex - 1).getBuilding(),
          path.getLocationAtIndex(locationIndex + 1).getBuilding());
      pathSwitchNext.setText(
          "Arrive at " + path.getLocationAtIndex(locationIndex + 1).getBuilding());
      pathSwitchPrevious.setText(
          "Return to " + path.getLocationAtIndex(locationIndex - 1).getBuilding());
    } else if ("OUT".equals(path.getLocationAtIndex(locationIndex + 1).getBuilding())) {
      // Exiting a building to go somewhere
      pathSwitchNext.setText(
          "Next: Go to " + path.getLocationAtIndex(locationIndex + 2).getBuilding());
    } else {
      pathSwitchNext.setText(
          "Next: Go to floor "
              + path.getLocationAtIndex(locationIndex + 1).getFloor().replace("F", ""));
    }

    // Need to update the text for previous button
    if (!"OUT".equals(path.getLocationAtIndex(locationIndex).getBuilding())) {
      if ("OUT".equals(path.getLocationAtIndex(locationIndex - 1).getBuilding())) {
        // Previous node was the outdoor node
        pathSwitchPrevious.setText(
            "Previous: Go to " + path.getLocationAtIndex(locationIndex - 2).getBuilding());
      } else {
        pathSwitchPrevious.setText(
            "Previous: Go to floor "
                + path.getLocationAtIndex(locationIndex - 1).getFloor().replace("F", ""));
      }
    }
  }

  public AnchorPane getFloorPane(String floor, String building) {
    if ("Faulkner".equals(building)) {
      switch (floor) {
        case "1":
          return mapPaneFaulkner1;
        case "2":
          return mapPaneFaulkner2;
        case "3":
          return mapPaneFaulkner3;
        case "4":
          return mapPaneFaulkner4;
        default:
          return mapPaneFaulkner5;
      }
    } else {
      switch (floor) {
        case "F1":
          return mapPaneMain1;
        case "F2":
          return mapPaneMain2;
        case "F3":
          return mapPaneMain3;
        case "G":
          return mapPaneMainG;
        case "L1":
          return mapPaneMainL1;
        default:
          return mapPaneMainL2;
      }
    }
  }

  public ImageView getFloorImage(String floor, String building) {
    if ("Faulkner".equals(building)) {
      switch (floor) {
        case "1":
          return imageViewFaulkner1;
        case "2":
          return imageViewFaulkner2;
        case "3":
          return imageViewFaulkner3;
        case "4":
          return imageViewFaulkner4;
        default:
          return imageViewFaulkner5;
      }
    } else {
      switch (floor) {
        case "F1":
          return imageViewMain1;
        case "F2":
          return imageViewMain2;
        case "F3":
          return imageViewMain3;
        case "G":
          return imageViewMainG;
        case "L1":
          return imageViewMainL1;
        default:
          return imageViewMainL2;
      }
    }
  }

  public JFXButton getFloorButton(String floor, String building) {
    if ("Faulkner".equals(building)) {
      switch (floor) {
        case "1":
          return floor1Button;
        case "2":
          return floor2Button;
        case "3":
          return floor3Button;
        case "4":
          return floor4Button;
        default:
          return floor5Button;
      }
    } else {
      switch (floor) {
        case "F1":
          return mainFloor1Button;
        case "F2":
          return mainFloor2Button;
        case "F3":
          return mainFloor3Button;
        case "G":
          return mainFloorGButton;
        case "L1":
          return mainFloorL1Button;
        default:
          return mainFloorL2Button;
      }
    }
  }

  //  private void setChooseLiftBehavior() {
  //    chooseLiftElevator.setOnAction(
  //        actionEvent -> {
  //          liftType = "ELEV";
  //          pathFindAlgorithm.setLiftType(liftType);
  //        });
  //    chooseLiftStairs.setOnAction(
  //        actionEvent -> {
  //          liftType = "STAI";
  //          pathFindAlgorithm.setLiftType(liftType);
  //        });
  //  }

  public void labelNodeStart() {
    startLabel = new Label();
    for (javafx.scene.Node component :
        getFloorPane(startNode.getFloor(), startNode.getBuilding()).getChildren()) {
      if (component.getId().equals(startNode.getId())) {
        String msg = " " + startNode.getLongName();
        Text text = new Text(msg);
        Font font = Font.font("Arial", 12);
        text.setFont(font);
        double width = text.getLayoutBounds().getWidth();
        startLabel.setStyle("-fx-font-size: 12");
        startLabel.setText(startNode.getLongName());
        getFloorPane(startNode.getFloor(), startNode.getBuilding()).getChildren().add(startLabel);
        startLabel.setLayoutX(component.getLayoutX() - (width / 2) + 3);
        startLabel.setLayoutY(component.getLayoutY() - 25);
        startLabel.setId("startLabel");
        startLabel.setVisible(true);
        startLabel.setStyle(
            "-fx-max-height: 18px; -fx-min-height: 16px; -fx-background-radius: 3px; -fx-border-radius: 3px; -fx-background-color: rgba(255,255,255,0.7); -fx-border-color: rgba(0,0,0,0.7); -fx-border-width: 1px");
        return;
      }
    }
  }

  private void labelNodeEnd() {
    endLabel = new Label();
    for (javafx.scene.Node component :
        getFloorPane(endNode.getFloor(), endNode.getBuilding()).getChildren()) {
      if (component.getId().equals(endNode.getId())) {
        String msg = " " + endNode.getLongName();
        Text text = new Text(msg);
        Font font = Font.font("Arial", 12);
        text.setFont(font);
        double width = text.getLayoutBounds().getWidth();
        endLabel.setStyle("-fx-font-size: 12");
        endLabel.setText(endNode.getLongName());
        getFloorPane(endNode.getFloor(), endNode.getBuilding()).getChildren().add(endLabel);
        endLabel.setLayoutX(component.getLayoutX() - (width / 2) + 3);
        endLabel.setLayoutY(component.getLayoutY() - 25);
        endLabel.setId("endLabel");
        endLabel.setVisible(true);
        endLabel.setStyle(
            "-fx-max-height: 18px; -fx-min-height: 16px; -fx-background-radius: 3px; -fx-border-radius: 3px; -fx-background-color: rgba(255,255,255,0.7); -fx-border-color: rgba(0,0,0,0.7); -fx-border-width: 1px");
        return;
      }
    }
  }

  private boolean sameHospital(String building1, String building2) {
    return ("Faulkner".equals(building1) && "Faulkner".equals(building2))
        || (!"Faulkner".equals(building1) && !"Faulkner".equals(building2));
  }

  public void textDirections(ActionEvent actionEvent) {
    Boolean success = directions.smsDirections(phoneNumber.getText());
    if (success) {
      commsResult.setText("Success! You will get a text momentarily.");
    } else {
      commsResult.setText("Couldn't send text. Check the number & try again.");
    }
  }

  public void callDirections(ActionEvent actionEvent) {
    Boolean success = directions.callDirections(phoneNumber.getText());
    if (success) {
      commsResult.setText("Success! You will get a call momentarily.");
    } else {
      commsResult.setText("Unable to call. Check the number and try again.");
    }
  }

  public void printDirections(ActionEvent actionEvent) {
    directions.printDirections();
  }

  private void setToggleBehavior() {
    liftToggle.setOnAction(
        actionEvent -> {
          if (liftToggle.isSelected()) {
            liftType = "STAI";
            pathFindAlgorithm.setLiftType(liftType);
          } else {
            liftType = "ELEV";
            pathFindAlgorithm.setLiftType(liftType);
          }
        });
  }

  public void setExternalDirections(String fromBuilding, String toBuilding) {
    System.out.println(fromBuilding);
    System.out.println(toBuilding);
    if ("Faulkner".equals(fromBuilding) && "45 Francis".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      faulknerTo45FrancisImage.setVisible(true);
      faulknerTo45FrancisQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA",
              "45+Francis+Street,+Boston,+MA");
    } else if ("Faulkner".equals(fromBuilding)
        && ("75 Francis".equals(toBuilding) || ("Tower".equals(toBuilding)))) {
      setIntermediateMapsInvisible();
      faulknerTo75FrancisImage.setVisible(true);
      faulknerTo75FrancisQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA",
              "75+Francis+Street,+Boston,+MA");
    } else if ("Faulkner".equals(fromBuilding) && "BTM".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      faulknerToBTMImage.setVisible(true);
      faulknerToBTMQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA",
              "Building+for+Transformative+Medicine+at+Brigham+and+Women's+Hospital,+Fenwood+Road,+Boston,+MA");
    } else if ("Faulkner".equals(fromBuilding) && "Shapiro".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      faulknerToShapiroImage.setVisible(true);
      faulknerToShapiroQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA",
              "The+Carl+J.+And+Ruth+Shapiro+Cardiovascular+Center,+Francis+Street,+Boston,+MA");
    } else if ("Faulkner".equals(fromBuilding)
        && ("15 Francis".equals(toBuilding) || ("FLEX".equals(toBuilding)))) {
      setIntermediateMapsInvisible();
      faulknerTo15FrancisImage.setVisible(true);
      faulknerTo15FrancisQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA",
              "15+Francis+Street,+Boston,+MA");
    } else if ("45 Francis".equals(fromBuilding) && "Faulkner".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      Francis45ToFaulknerImage.setVisible(true);
      Francis45ToFaulknerQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "45+Francis+Street,+Boston,+MA",
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA");
    } else if (("75 Francis".equals(fromBuilding) || ("Tower".equals(fromBuilding)))
        && "Faulkner".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      Francis75ToFaulknerImage.setVisible(true);
      Francis75ToFaulknerQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "75+Francis+Street,+Boston,+MA",
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA");
    } else if ("BTM".equals(fromBuilding) && "Faulkner".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      BTMToFaulknerImage.setVisible(true);
      BTMToFaulknerQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "Building+for+Transformative+Medicine+at+Brigham+and+Women's+Hospital,+Fenwood+Road,+Boston,+MA",
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA");
    } else if ("Shapiro".equals(fromBuilding) && "Faulkner".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      shapiroToFaulknerImage.setVisible(true);
      shapiroToFaulknerQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "The+Carl+J.+And+Ruth+Shapiro+Cardiovascular+Center,+Francis+Street,+Boston,+MA",
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA");
    } else if (("15 Francis".equals(fromBuilding) || ("FLEX".equals(fromBuilding)))
        && "Faulkner".equals(toBuilding)) {
      setIntermediateMapsInvisible();
      francis15ToFaulknerImage.setVisible(true);
      francis15ToFaulknerQR.setVisible(true);
      googleMaps =
          new GoogleMaps(
              "15+Francis+Street,+Boston,+MA",
              "Brigham+and+Women's+Faulkner+Hospital,+Centre+Street,+Boston,+MA");
    }

    driveTime.setText(googleMaps.driveTime());
    driveDistance.setText(googleMaps.driveDistance());
    transitTime.setText(googleMaps.transitTime());
    transitDistance.setText(googleMaps.transitDistance());
    bikeTime.setText(googleMaps.bikeTime());
    bikeDistance.setText(googleMaps.bikeDistance());
    walkTime.setText(googleMaps.walkTime());
    walkDistance.setText(googleMaps.walkDistance());
  }

  private void setErrorPaneButtonBehavior() {
    errorPaneButton.setOnAction(
        actionEvent -> {
          startLabel.setVisible(false);
          endLabel.setVisible(false);
          errorPane.setVisible(false);
        });
  }

  private void showErrorPane() {
    System.out.println("I'm here.");
    errorPane.setVisible(true);
    String lifter;
    if ("ELEV".equals(liftType)) {
      lifter = "stairs";
    } else {
      lifter = "elevator";
    }
    String msg2 = "Maybe try taking the " + lifter + ".";
    Text text2 = new Text(msg2);
    Font font2 = Font.font("System", 12);
    text2.setFont(font2);
    double width2 = text2.getLayoutBounds().getWidth();
    errorPaneLabel2.setLayoutX(errorPaneLabel2.getLayoutX() - (width2 / 2));
    errorPaneLabel2.setText(msg2);
  }

  public void setIntermediateMapsInvisible() {
    faulknerTo45FrancisImage.setVisible(false);
    faulknerTo75FrancisImage.setVisible(false);
    faulknerToBTMImage.setVisible(false);
    faulknerToShapiroImage.setVisible(false);
    faulknerTo15FrancisImage.setVisible(false);
    Francis45ToFaulknerImage.setVisible(false);
    Francis75ToFaulknerImage.setVisible(false);
    BTMToFaulknerImage.setVisible(false);
    shapiroToFaulknerImage.setVisible(false);
    francis15ToFaulknerImage.setVisible(false);
    faulknerTo45FrancisQR.setVisible(false);
    faulknerTo75FrancisQR.setVisible(false);
    faulknerToBTMQR.setVisible(false);
    faulknerToShapiroQR.setVisible(false);
    faulknerTo15FrancisQR.setVisible(false);
    Francis45ToFaulknerQR.setVisible(false);
    Francis75ToFaulknerQR.setVisible(false);
    BTMToFaulknerQR.setVisible(false);
    shapiroToFaulknerQR.setVisible(false);
    francis15ToFaulknerQR.setVisible(false);
  }

  public void switchAbout(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("About");
  }

  public void zoomToPath(String floorNum, Path path) {
    System.out.println(
        "In zoomToPath() --------------------------------------------------------------------");
    System.out.println(floorNum);
    double bigX = 0.0;
    double bigY = 0.0;
    double smallX = 5000.0;
    double smallY = 5000.0;
    double hVal = 0.0;
    double vVal = 0.0;
    List<Node> nodesOnFloor = getNodesOnFloor(floorNum);
    for (Node node : nodesOnFloor) {
      if (node.getXCoord() > bigX) {
        bigX = node.getXCoord();
      }
      if (node.getYCoord() > bigY) {
        bigY = node.getYCoord();
      }
      if (node.getXCoord() < smallX) {
        smallX = node.getXCoord();
      }
      if (node.getYCoord() < smallY) {
        smallY = node.getYCoord();
      }
    }
    System.out.println(nodesOnFloor.size());
    Node node1 = nodesOnFloor.get(0);
    Node node2 = nodesOnFloor.get(nodesOnFloor.size() - 1);

    double xDiff = bigX - smallX;
    double yDiff = bigY - smallY;

    System.out.println(yDiff);
    System.out.println(xDiff);
    if ("Faulkner".equals(nodesOnFloor.get(0).getBuilding())) {
      if (yDiff > xDiff) {
        uiSetting.setZoomScaleValue((FAULKNER_MAP_HEIGHT / yDiff) / 1.5);
      } else {
        uiSetting.setZoomScaleValue((FAULKNER_MAP_WIDTH / xDiff) / 1.5);
      }
      hVal =
          (bigX - ((bigX - smallX) / 2))
              / FAULKNER_MAP_WIDTH; // ((bigX + smallX) / 2) / FAULKNER_MAP_WIDTH;
      vVal =
          (bigY - ((bigY - smallY) / 2))
              / FAULKNER_MAP_HEIGHT; // ((bigY + smallY) / 2) / FAULKNER_MAP_HEIGHT;
      System.out.println("hVal =");
      System.out.println(hVal);
      if (hVal < 0.25) {
        hVal = hVal - (0.5 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal < 0.33) {
        hVal = hVal - (0.4 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal < 0.4) {
        hVal = hVal - (0.3 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal < 0.5) {
        hVal = hVal - (0.2 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal > 0.5) {
        hVal = hVal + (0.2 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal > 0.6) {
        hVal = hVal + (0.5 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal > 0.67) {
        hVal = hVal + (0.4 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      } else if (hVal > 0.75) {
        hVal = hVal + (0.3 * ((xDiff / FAULKNER_MAP_HEIGHT)));
      }

      System.out.println("vVal =");
      System.out.println(vVal);
      if (vVal < 0.25) {
        vVal = vVal - (0.5 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal < 0.33) {
        vVal = vVal - (0.4 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal < 0.4) {
        vVal = vVal - (0.3 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal < 0.5) {
        vVal = vVal - (0.2 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal > 0.75) {
        vVal = vVal + (0.5 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal > 0.67) {
        vVal = vVal + (0.4 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal > 0.6) {
        vVal = vVal + (0.3 * ((yDiff / FAULKNER_MAP_WIDTH)));
      } else if (vVal > 0.5) {
        vVal = vVal + (0.2 * ((yDiff / FAULKNER_MAP_WIDTH)));
      }

    } else { // main campus
      if (yDiff > xDiff) {
        uiSetting.setZoomScaleValue((MAIN_MAP_HEIGHT / yDiff) / 1.5);
      } else {
        uiSetting.setZoomScaleValue((MAIN_MAP_WIDTH / xDiff) / 1.5);
      }
      hVal =
          (bigX - ((bigX - smallX) / 2))
              / MAIN_MAP_WIDTH; // ((bigX + smallX) / 2) / FAULKNER_MAP_WIDTH;
      vVal =
          (bigY - ((bigY - smallY) / 2))
              / MAIN_MAP_HEIGHT; // ((bigY + smallY) / 2) / FAULKNER_MAP_HEIGHT;
      System.out.println("hVal =");
      System.out.println(hVal);
      if (hVal < 0.25) {
        hVal = hVal - (0.5 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal < 0.33) {
        hVal = hVal - (0.4 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal < 0.4) {
        hVal = hVal - (0.3 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal < 0.5) {
        hVal = hVal - (0.2 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal > 0.5) {
        hVal = hVal + (0.2 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal > 0.6) {
        hVal = hVal + (0.5 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal > 0.67) {
        hVal = hVal + (0.4 * ((xDiff / MAIN_MAP_HEIGHT)));
      } else if (hVal > 0.75) {
        hVal = hVal + (0.3 * ((xDiff / MAIN_MAP_HEIGHT)));
      }

      System.out.println("vVal =");
      System.out.println(vVal);
      if (vVal < 0.25) {
        vVal = vVal - (0.5 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal < 0.33) {
        vVal = vVal - (0.4 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal < 0.4) {
        vVal = vVal - (0.3 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal < 0.5) {
        vVal = vVal - (0.2 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal > 0.75) {
        vVal = vVal + (0.5 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal > 0.67) {
        vVal = vVal + (0.4 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal > 0.6) {
        vVal = vVal + (0.3 * ((yDiff / MAIN_MAP_WIDTH)));
      } else if (vVal > 0.5) {
        vVal = vVal + (0.2 * ((yDiff / MAIN_MAP_WIDTH)));
      }
    }
    uiSetting.setScrollPaneValues(hVal, vVal);
    System.out.println(
        "Out of zoomToPath() --------------------------------------------------------------------");
  }

  public void nodeInfoButtonBehavior() {
    nodeInfoLabel1.setText("");
    nodeInfoLabel2.setText("");
    nodeInfoPane.setVisible(false);
  }

  public void setNodeInfoLabels(Node node) throws Exception {
    nodeInfoLabel1.setText(node.getLongName());
    nodeInfoLabel2.setText(node.getId());
    setNodeInfoTypeCombo();

    if (userLevel == null || userLevel == Account.Type.USER) {
      nodeInfoLabel3.setVisible(false);
      nodeInfoTypeCombo.setVisible(false);
      nodeInfoCombo.setVisible(false);
    } else {
      nodeInfoLabel3.setVisible(true);
      nodeInfoTypeCombo.setVisible(true);
      nodeInfoCombo.setVisible(true);
    }

    sanitationList = databaseManager.getAllSanitationRequests();
    computerList = databaseManager.getAllComputerServiceRequests();
    flowerList = databaseManager.getAllFlowerRequests();
    laundryList = databaseManager.getAllLaunduaryRequests();
    maintenanceList = databaseManager.getAllMaintenanceRequests();
    mariachiList = databaseManager.getAllMariachiServiceRequests();
    medicineList = databaseManager.getAllMedicineDeliveryRequests();
    languageList = databaseManager.getAllLanguageServiceRequests();
    securityList = databaseManager.getAllSecurityRequests();
    tranportList = databaseManager.getAllTransportRequests();

    tempSanitationList = FXCollections.observableArrayList();
    for (SanitationServiceRequest sanitationServiceRequest : sanitationList) {
      if (sanitationServiceRequest.getLocation().getId().equals(node.getId())) {
        tempSanitationList.add("Sanitation " + sanitationServiceRequest.getId());
      }
    }

    tempComputerList = FXCollections.observableArrayList();
    for (ComputerServiceRequest computerServiceRequest : computerList) {
      if (computerServiceRequest.getLocation().getId().equals(node.getId())) {
        tempComputerList.add("Computer " + computerServiceRequest.getId());
      }
    }

    tempFlowerList = FXCollections.observableArrayList();
    for (FlowerRequest flowerRequest : flowerList) {
      if (flowerRequest.getLocation().getId().equals(node.getId())) {
        tempFlowerList.add("Flowers " + flowerRequest.getId());
      }
    }

    tempLaundryList = FXCollections.observableArrayList();
    for (LaundryServiceRequest laundryServiceRequest : laundryList) {
      if (laundryServiceRequest.getLocation().getId().equals(node.getId())) {
        tempLaundryList.add("Laundry " + laundryServiceRequest.getId());
      }
    }

    tempMaintenanceList = FXCollections.observableArrayList();
    for (MaintenanceRequest maintenanceRequest : maintenanceList) {
      if (maintenanceRequest.getLocation().getId().equals(node.getId())) {
        tempMaintenanceList.add("Maintenance " + maintenanceRequest.getId());
      }
    }

    tempMariachiList = FXCollections.observableArrayList();
    for (MariachiRequest mariachiRequest : mariachiList) {
      if (mariachiRequest.getLocation().getId().equals(node.getId())) {
        tempMariachiList.add("Mariachi " + mariachiRequest.getId());
      }
    }

    tempMedicineList = FXCollections.observableArrayList();
    for (MedicineDeliveryRequest medicineDeliveryRequest : medicineList) {
      if (medicineDeliveryRequest.getLocation().getId().equals(node.getId())) {
        tempMedicineList.add("Medicine " + medicineDeliveryRequest.getId());
      }
    }

    tempLanguageList = FXCollections.observableArrayList();
    for (LanguageServiceRequest languageServiceRequest : languageList) {
      if (languageServiceRequest.getLocation().getId().equals(node.getId())) {
        tempLanguageList.add("Language " + languageServiceRequest.getId());
      }
    }

    tempSecurityList = FXCollections.observableArrayList();
    for (SecurityRequest securityRequest : securityList) {
      if (securityRequest.getLocation().getId().equals(node.getId())) {
        tempSecurityList.add("Security " + securityRequest.getId());
      }
    }

    tempTransportList = FXCollections.observableArrayList();
    for (TransportRequest transportRequest : tranportList) {
      if (transportRequest.getLocation().getId().equals(node.getId())) {
        tempTransportList.add("Transport " + transportRequest.getId());
      }
    }
  }

  public List<Node> getNodesOnFloor(String floorNum) {
    List<Node> returnList = new ArrayList<>();
    for (Node node : globalPath.getPath()) {
      if (node.getFloor().equals(floorNum)) {
        returnList.add(node);
      }
    }
    return returnList;
  }

  public void setNodeInfoTypeCombo() {
    ObservableList<String> list = FXCollections.observableArrayList();
    list.add("Computer");
    list.add("Flower");
    list.add("Language");
    list.add("Laundry");
    list.add("Maintenance");
    list.add("Mariachi");
    list.add("Medicine");
    list.add("Sanitation");
    list.add("Security");
    list.add("Transport");
    nodeInfoTypeCombo.setItems(list);
  }

  public void setNodeInfoCombo() {
    ObservableList<String> blankList = FXCollections.observableArrayList();
    switch (nodeInfoTypeCombo.getValue()) {
      case "Sanitation":
        if (tempSanitationList.isEmpty()) {
          tempSanitationList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempSanitationList);
        break;
      case "Computer":
        if (tempComputerList.isEmpty()) {
          tempComputerList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempComputerList);
        break;
      case "Flower":
        if (tempFlowerList.isEmpty()) {
          tempFlowerList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempFlowerList);
        break;
      case "Language":
        if (tempLanguageList.isEmpty()) {
          tempLanguageList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempLanguageList);
        break;
      case "Laundry":
        if (tempLaundryList.isEmpty()) {
          tempLaundryList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempLaundryList);
        break;
      case "Maintenance":
        if (tempMaintenanceList.isEmpty()) {
          tempComputerList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempComputerList);
        break;
      case "Mariachi":
        if (tempMariachiList.isEmpty()) {
          tempMariachiList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempMariachiList);
        break;
      case "Medicine":
        if (tempMedicineList.isEmpty()) {
          tempMedicineList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempMedicineList);
        break;
      case "Security":
        if (tempSecurityList.isEmpty()) {
          tempSecurityList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempSecurityList);
        break;
      case "Transport":
        if (tempTransportList.isEmpty()) {
          tempTransportList.add("No service requests");
        }
        nodeInfoCombo.setItems(tempTransportList);
        break;
      default:
        nodeInfoCombo.setItems(blankList);
    }
  }

  //  public void placeFloorEndButton(
  //      String floorNum) { // look into button being created on multiple floors
  //    buttonOnEnd.setVisible(false);
  //    buttonOnEnd = null;
  //    buttonOnEnd = new JFXButton();
  //    List<Node> nodesOnFloor = getNodesOnFloor(floorNum);
  //    Node lastNode = nodesOnFloor.get(nodesOnFloor.size() - 1);
  //
  //    double currentRatioX;
  //    double currentRatioY;
  //    if ("Faulkner".equals(lastNode.getBuilding())) {
  //      currentRatioY = currentPane.getPrefHeight() / FAULKNER_MAP_HEIGHT;
  //      currentRatioX = currentPane.getPrefWidth() / FAULKNER_MAP_WIDTH;
  //    } else {
  //      currentRatioY = currentPane.getPrefHeight() / MAIN_MAP_HEIGHT;
  //      currentRatioX = currentPane.getPrefWidth() / MAIN_MAP_WIDTH;
  //    }
  //
  //    String msg = "Next Floor";
  //    Text text = new Text(msg);
  //    Font font = Font.font("Arial", 12);
  //    text.setFont(font);
  //    double width = text.getLayoutBounds().getWidth();
  //
  //    buttonOnEnd.setStyle("-fx-font-size: 12");
  //    buttonOnEnd.setText("Next Floor");
  //    getFloorPane(floorNum, lastNode.getBuilding()).getChildren().add(buttonOnEnd);
  //    buttonOnEnd.setLayoutX((lastNode.getXCoord() / currentRatioX) - (width / 2) + 3);
  //    buttonOnEnd.setLayoutY((lastNode.getYCoord() / currentRatioY) + 25);
  //    buttonOnEnd.setId("nextFloor");
  //    buttonOnEnd.setStyle(
  //        "-fx-max-height: 18px; -fx-min-height: 16px; -fx-background-radius: 3px;
  // -fx-border-radius: 3px; -fx-background-color: rgba(255,255,255,0.7); -fx-border-color:
  // rgba(0,0,0,0.7); -fx-border-width: 1px");
  //    buttonOnEnd.setVisible(true);
  //  }
}
