package edu.wpi.teamF.Controllers.MapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Edge;
import edu.wpi.teamF.ModelClasses.Node;
import java.util.HashMap;
import java.util.Map;

import edu.wpi.teamF.ModelClasses.ValidationException;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class MapEditorController {

  private enum State {
    ADD_NODE,
    MODIFY_NODE,
    MODIFY_EDGE,
    ADD_EDGE
  }

  private enum EdgeSelection {
    NODE1,
    NODE2
  }

  private DatabaseManager databaseManager = DatabaseManager.getManager();
  MapView mapView;
  Map<String, NodeButton> nodeButtonMap = new HashMap<>();
  Map<String, EdgeLine> edgeLineMap = new HashMap<>();
  State state;

  // Edge variables
  EdgeSelection edgeSelection;
  EdgeLine selectedEdge;
  String addNode1ID;

  // Node Variables
  String selectedNode;

  public MapEditorController(MapView mapView) throws Exception {
    this.mapView = mapView;
    state = State.MODIFY_NODE;
    edgeSelection = EdgeSelection.NODE1;
    for (Node node : databaseManager.getAllNodes()) {
      nodeButtonMap.put(node.getId(), new NodeButton(node));
    }
    for (Edge edge : databaseManager.getAllEdges()) {
      edgeLineMap.put(edge.getId(), new EdgeLine(edge));
    }
  }

  public void addEdgeButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          state = State.ADD_EDGE;
          mapView.setAsCancelView();
          edgeSelection = EdgeSelection.NODE1;
        });
  }

  public void setCancelButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          try {
            cancelEdgeHandler();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void cancelEdgeHandler() throws Exception {
    if (state == State.ADD_EDGE) {
      resetEdgeStateToDefault();
    } else if (state == State.MODIFY_EDGE) {
      mapView.unHighlightButton(selectedEdge.getTempNode1());
      mapView.unHighlightButton(selectedEdge.getTempNode2());
      selectedEdge.reset();
      if (addNode1ID != null) {
        mapView.removeEdge(selectedEdge.getEdgeID());
      } else {
        mapView.redrawEdge(selectedEdge.edge);
      }
      resetEdgeStateToDefault();
    }
  }

  public void edgeModifyButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          try {
            mapView.removeEdge(selectedEdge.getEdgeID());
            mapView.unHighlightButton(selectedEdge.getTempNode1());
            mapView.unHighlightButton(selectedEdge.getTempNode2());
            edgeLineMap.remove(selectedEdge.getEdgeID());
            selectedEdge.updateDatabase();
            edgeLineMap.put(selectedEdge.getEdgeID(), selectedEdge);
            mapView.drawEdge(selectedEdge.getEdge());
            resetEdgeStateToDefault();
            mapView.setAsDefaultView();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  public void edgeDeleteButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          try {
            mapView.removeEdge(selectedEdge.getEdgeID());
            mapView.unHighlightButton(selectedEdge.getTempNode1());
            mapView.unHighlightButton(selectedEdge.getTempNode2());
            selectedEdge.deleteEdgeFromDatabase();
            edgeLineMap.remove(selectedEdge.getEdgeID());
            resetEdgeStateToDefault();
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  private void resetEdgeStateToDefault() {
    if (addNode1ID != null) {
      mapView.unHighlightButton(addNode1ID);
    }
    mapView.setAsDefaultView();
    edgeSelection = null;
    addNode1ID = null;
    selectedEdge = null;
    state = null;
  }

  public void setEdgeSelectionButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          if ("Node1".equals(button.getId())) {
            edgeSelection = EdgeSelection.NODE1;
          } else if ("Node2".equals(button.getId())) {
            edgeSelection = EdgeSelection.NODE2;
          }
        });
  }

  public void setMapEventHandlers(AnchorPane floorPane) {
    floorPane.setOnMouseClicked(
        mouseEvent -> {
          if (state == State.ADD_NODE && !mouseEvent.isDragDetect()) {
            paneAddNodeHandler(mouseEvent);
          }
        });
  }

  public void setEdgeEventHandlers(Line line) {
    line.setOnMousePressed(
        mouseEvent -> {
          try {
            if (state == State.ADD_EDGE || state == State.MODIFY_EDGE) {
              cancelEdgeHandler();
            }
            lineModifyEdgeHandler(line, mouseEvent);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

    public void setNodeEventHandlers(JFXButton button) {

        button.setOnMousePressed(
                mouseEvent -> {
                    try {
                        if (state == State.MODIFY_EDGE && edgeSelection != null) { // modifying an edge
                            nodeModifyEdgeHandler(button, mouseEvent);
                        } else if (state == State.ADD_EDGE) {
                            nodeAddEdgeHandler(button, mouseEvent); // adding an edge
                        } else {
                            nodeModifyNodeHandler(button, mouseEvent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        button.setOnMouseDragged(mouseEvent -> {
//            if (state == State.MODIFY_NODE) {
//                button.setLayoutX(mouseEvent.getSceneX() / imageStackPane.getScaleX() + deltaX);
//                button.setLayoutY(mouseEvent.getSceneY() / imageStackPane.getScaleY() + deltaY);
//                double nodeWidth = (button.getLayoutX() + 4) / widthRatio;
//                double nodeHeight = (button.getLayoutY() + 4) / heightRatio;
//                xCoorInput.setText("" + nodeWidth);
//                yCoorInput.setText("" + nodeHeight);
//
//                try {
//                    node.setXCoord((short) nodeWidth);
//                    node.setYCoord((short) nodeHeight);
//                } catch (ValidationException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    for (Edge edge : databaseManager.getAllEdgesConnectedToNode(node.getId())) {
//                        for (int i = 0; i < mapPane.getChildren().size(); i++) {
//                            javafx.scene.Node children = mapPane.getChildren().get(i);
//                            if (children instanceof Line && children.getId().equals(edge.getId())) {
//                                Line line = (Line) children;
//                                if (edge.getNode2().equals(node.getId())) {
//                                    line.setEndX(
//                                            mouseEvent.getSceneX() / imageStackPane.getScaleX() + 4 + deltaX);
//                                    line.setEndY(
//                                            mouseEvent.getSceneY() / imageStackPane.getScaleY() + 4 + deltaY);
//                                } else {
//                                    line.setStartX(
//                                            mouseEvent.getSceneX() / imageStackPane.getScaleX() + 4 + deltaX);
//                                    line.setStartY(
//                                            mouseEvent.getSceneY() / imageStackPane.getScaleY() + 4 + deltaY);
//                                }
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        });

    }

  private void nodeModifyNodeHandler(JFXButton button, MouseEvent mouseEvent) {
    selectedNode = button.getId();
    if (state == State.MODIFY_EDGE) {
      resetEdgeStateToDefault();
    }
    state = State.MODIFY_NODE;
    mapView.setAsModifyNodeView();
    mapView.displayInitialNodeData(nodeButtonMap.get(button.getId()).getNode());

    // update global variable
    // display the node pane
    // display the node attributes
    // add checks to the input
    // allow edge to be draggable

  }

  private void lineModifyEdgeHandler(Line line, MouseEvent mouseEvent) throws Exception {
    state = State.MODIFY_EDGE;
    mapView.setAsEdgeView();
    selectedEdge = edgeLineMap.get(line.getId());
    edgeSelection = null;
    mapView.highlightUpdatedEdge(line.getId(), selectedEdge.getNode1(), selectedEdge.getNode2());
  }

  private void paneAddNodeHandler(MouseEvent mouseEvent) {}

  private void nodeAddEdgeHandler(JFXButton button, MouseEvent mouseEvent) throws Exception {
    if (edgeSelection == EdgeSelection.NODE1) {
      edgeSelection = EdgeSelection.NODE2;
      addNode1ID = button.getId();
      mapView.setButtonColor(button, "#012D5A", 1);
    } else if (edgeSelection == EdgeSelection.NODE2) {

      Edge newEdge = new Edge(addNode1ID + "_" + button.getId(), addNode1ID, button.getId());
      Line line = mapView.drawEdge(newEdge);
      selectedEdge = new EdgeLine(newEdge);
      edgeLineMap.put(newEdge.getId(), selectedEdge);
      lineModifyEdgeHandler(line, mouseEvent);
    }
  }

  private void nodeModifyEdgeHandler(JFXButton button, MouseEvent mouseEvent) throws Exception {
    if (edgeSelection == EdgeSelection.NODE1) {
      mapView.unHighlightButton(selectedEdge.getTempNode1());
      selectedEdge.setNode1(button.getId());
    }
    if (edgeSelection == EdgeSelection.NODE2) {
      mapView.unHighlightButton(selectedEdge.getTempNode2());
      selectedEdge.setNode2(button.getId());
    }
    mapView.highlightUpdatedEdge(
        selectedEdge.getEdgeID(), selectedEdge.getTempNode1(), selectedEdge.getTempNode2());
  }

  public void setAddNodeButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          try {
            NodeButton newNode =
                new NodeButton(
                    mapView.getShortName(),
                    mapView.getLongName(),
                    mapView.getXCoord(),
                    mapView.getYCoord(),
                    mapView.getBuilding(),
                    mapView.getFloor(),
                    mapView.getType());
            mapView.drawNode(newNode.getNode());
            nodeButtonMap.put(newNode.getNode().getId(), newNode); // add new
            nodeButtonMap.put(newNode.getNode().getId(), newNode);
            mapView.drawNode(newNode.getNode());
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  public void setModifyNodeButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          NodeButton nodeButton = nodeButtonMap.get(selectedNode);
          try {
            nodeButton.modifyNode(
                mapView.getShortName(),
                mapView.getLongName(),
                mapView.getXCoord(),
                mapView.getYCoord(),
                mapView.getBuilding(),
                mapView.getFloor(),
                mapView.getType());
            mapView.removeNode(selectedNode);
            mapView.drawNode(nodeButton.getNode());
            nodeButtonMap.remove(selectedNode); // remove old
            nodeButtonMap.put(nodeButton.getNode().getId(), nodeButton); // add new
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }

  public void setDeleteNodeButtonHandler(JFXButton button) {
    button.setOnMousePressed(
        mouseEvent -> {
          try {
            mapView.removeNode(selectedNode);
          } catch (Exception e) {
            e.printStackTrace();
          }
          nodeButtonMap.remove(selectedNode);
        });
  }
}
