package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Node;
import java.net.URL;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MapViewNodePopup implements Initializable {

  @FXML private JFXTextField shortNameInput;

  @FXML private JFXTextField longNameInput;

  @FXML private JFXTextField xCoorInput;

  @FXML private JFXTextField yCoorInput;

  @FXML private ChoiceBox buildingInput;

  @FXML private ChoiceBox floorInput;

  @FXML private ChoiceBox typeInput;

  @FXML private JFXButton locationButton;

  @FXML private JFXButton addNodeButton;

  @FXML private JFXButton modifyNodeButton;

  @FXML private JFXButton deleteNodeButton;

  @FXML private Label errorLabel;

  private AnchorPane mapPane;

  private DataMapViewController dataMapViewController;

  private DatabaseManager databaseManager = DatabaseManager.getManager();

  Node node;
  boolean isAdd;

  public MapViewNodePopup() throws Exception {}

  public MapViewNodePopup(
      DataMapViewController dataMapViewController, Node node) { // modify constructor
    this.dataMapViewController = dataMapViewController;
    this.node = node;
    this.isAdd = false;
  }

  public MapViewNodePopup(DataMapViewController dataMapViewController) { // add constructor
    System.out.println("In Constructor" + dataMapViewController);

    this.dataMapViewController = dataMapViewController;
    this.isAdd = true;
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    typeInput
        .getItems()
        .addAll(
            "CONF", "DEPT", "EXIT", "HALL", "INFO", "LABS", "REST", "RETL", "SERV", "STAF", "STAI");
    typeInput.setValue("CONF");

    if (isAdd) {
      addNodeButton.setVisible(true);
      deleteNodeButton.setVisible(false);
      modifyNodeButton.setVisible(false);
    } else {
      addNodeButton.setVisible(false);
      deleteNodeButton.setVisible(true);
      modifyNodeButton.setVisible(true);
    }
  }

  @FXML
  private void addNode(ActionEvent event) throws Exception {

    Stage stage;

    int tracker = 0;
    int instance = 0;
    int newInstance = 0;
    int instanceNum = 0;

    // try { // is the input valid?
    short xCoordinate = Short.parseShort(xCoorInput.getText());
    short yCoordinate = Short.parseShort(yCoorInput.getText());
    String building = buildingInput.getValue().toString();
    String longName = longNameInput.getText();
    String shortName = shortNameInput.getText();
    Node.NodeType nodeType = Node.NodeType.getEnum(typeInput.getValue().toString());
    String floorNumber = floorInput.getValue().toString();
    List<Node> typeNodes = databaseManager.getNodesByType(nodeType);

    List<Integer> typeInstances = new ArrayList<>();

    for (int i = 0; i < typeNodes.size(); i++) { // collects all of the instances for the given type
      if (typeNodes.get(i).getFloor() == floorNumber) {
        instanceNum = Integer.parseInt(typeNodes.get(i).getId().substring(5, 8));
        typeInstances.add(instanceNum);
      }
    }

    Collections.sort(typeInstances); // sorts the list

    if (typeNodes.size() > 0) {
      for (int i = 0; i < typeInstances.size(); i++) {
        System.out.println(typeInstances.get(i));
        instance = typeInstances.get(i); // 1
        if (instance - tracker > 1) { // 1-0 = 1
          newInstance = tracker + 1;
          break;
        } else if (instance == typeInstances.size()) {
          newInstance = typeInstances.size() + 1;
          break;
        } else {
          tracker++;
        }
      }
    } else {
      newInstance = 1;
    }
    String strInstance = "" + newInstance;
    String strFloor = "0" + floorNumber;

    switch (strInstance.length()) {
      case 1:
        strInstance = "00" + strInstance;
        break;
      case 2:
        strInstance = "0" + strInstance;
        break;
    }

    String ID = "F" + typeInput.getValue() + strInstance + strFloor;

    Node newNode =
        new Node(
            ID,
            xCoordinate,
            yCoordinate,
            building,
            longName,
            shortName,
            nodeType,
            floorNumber); // creates a new node

    databaseManager.manipulateNode(newNode); // creates the node in the db
    dataMapViewController.drawNode(newNode);
    stage = (Stage) addNodeButton.getScene().getWindow();
    stage.close();

    //    } catch (Exception e) { // throws an error if the input provided by the user is invalid
    //      errorLabel.setText("The input is not valid");
    //    }
  }

  @FXML
  private void modifyNode(ActionEvent event) {

    //
    //    short oldXCoordinate = node.getXCoord();
    //    short oldYCoordinate = node.getYCoord();
    //    String oldBuilding = node.getBuilding();
    //    String oldLongName = node.getLongName();
    //    String oldShortName = node.getShortName();
    //    Node.NodeType oldNodeType = node.getType();
    //    String oldFloorNumber = node.getFloor(); // stores the input in variables
    //
    //    try { // is the input correct?
    //      short xCoordinate = Short.parseShort(xCoorInput.getText());
    //      short yCoordinate = Short.parseShort(yCoorInput.getText());
    //      String building = buildingInput.getValue().toString();
    //      String longName = longNameInput.getText();
    //      String shortName = shortNameInput.getText();
    //      Node.NodeType nodeType = Node.NodeType.getEnum(typeInput.getValue().toString());
    //      String floorNumber = floorInput.getValue().toString(); // stores the input in variables
    //
    //      node.setXCoord(xCoordinate);
    //      node.setYCoord(yCoordinate);
    //      node.setBuilding(building);
    //      node.setLongName(longName);
    //      node.setShortName(shortName);
    //      node.setType(nodeType);
    //      node.setFloor(floorNumber); // sets the node to the provided values
    //
    //      databaseManager.manipulateNode(node);
    //
    //      mapPane = dataMapViewController.findPane(node);
    //
    //      if (oldFloorNumber != floorNumber) { // not on the same floor
    //        for (Edge edge : databaseManager.getAllEdgesConnectedToNode(node.getId())) { // for
    // all the edges connected to the node
    //          for (int i = 0; i < mapPane.getChildren().size(); i++) {
    //            javafx.scene.Node children = mapPane.getChildren().get(i);
    //            if (children instanceof Line && children.getId().equals(edge.getId())) {
    //              mapPane.getChildren().remove(children);
    //            }
    //          }
    //        }
    //        mapPane.getChildren().remove(nodeButton);
    //        switch (floorNumber) {
    //          case 1:
    //            mapPane1.getChildren().add(nodeButton);
    //            break;
    //          case 2:
    //            mapPane2.getChildren().add(nodeButton);
    //            break;
    //          case 3:
    //            mapPane3.getChildren().add(nodeButton);
    //            break;
    //          case 4:
    //            mapPane4.getChildren().add(nodeButton);
    //            break;
    //          case 5:
    //            mapPane5.getChildren().add(nodeButton);
    //            break;
    //        }
    //      } else { // on the same floor
    //        for (Edge edge : databaseManager.getAllEdgesConnectedToNode(node.getId())) {
    //          for (int i = 0; i < mapPane.getChildren().size(); i++) {
    //            javafx.scene.Node children = mapPane.getChildren().get(i);
    //            if (children instanceof Line && children.getId().equals(edge.getId())) {
    //              Line line = (Line) children;
    //              if (edge.getNode1().equals(node.getId())) {
    //                line.setStartX(xCoordinate * widthRatio);
    //                line.setStartY(yCoordinate * heightRatio);
    //              } else { // if node two then it is an ending coordinate
    //                line.setEndX(xCoordinate * widthRatio);
    //                line.setEndY(yCoordinate * heightRatio);
    //              }
    //              break;
    //            }
    //          }
    //        }
    //      }
    //
    //      nodeButton.setLayoutX(xCoordinate * widthRatio - 3);
    //      nodeButton.setLayoutY(yCoordinate * heightRatio - 3);
    //
    //      clearNode();
    //
    //    } catch (Exception e) { // throws an error if the input is not valid
    //      if (oldXCoordinate == node.getXCoord()
    //              && oldYCoordinate == node.getYCoord()
    //              && oldBuilding == node.getBuilding()
    //              && oldLongName.equals(node.getLongName())
    //              && oldShortName.equals(node.getShortName())
    //              && oldNodeType.equals(node.getType())
    //              && oldFloorNumber == node.getFloor()) {
    //        nodeErrorLabel.setText("The input is invalid");
    //      }
    //    }
  }

  @FXML
  private void deleteNode(ActionEvent event) {}

  @FXML
  private void selectLocation(ActionEvent event) {
    Stage stage;
    stage = (Stage) addNodeButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void validateNodeText(KeyEvent keyEvent) {
    if (!xCoorInput.getText().isEmpty()
        && !yCoorInput.getText().isEmpty()
        && !buildingInput.getValue().toString().isEmpty()
        && !longNameInput.getText().isEmpty()
        && !shortNameInput.getText().isEmpty()
        && !floorInput.getValue().toString().isEmpty()) { // ADD TYPE
      modifyNodeButton.setDisable(false);
      modifyNodeButton.setOpacity(1);
      addNodeButton.setDisable(false);
      addNodeButton.setOpacity(1);
    } else {
      modifyNodeButton.setDisable(true);
      modifyNodeButton.setOpacity(.4);
      addNodeButton.setDisable(true);
      addNodeButton.setOpacity(.4);
    }
  }
}
