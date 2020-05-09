package edu.wpi.teamF.Controllers.MapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Edge;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.ValidationException;
import javafx.css.StyleableProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.Map;

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
    Map<String,NodeButton> nodeButtonMap;
    Map<String,EdgeLine> edgeLineMap;
    State state;

    //Edge variables
    EdgeSelection edgeSelection;
    EdgeLine selectedEdge;
    String addNode1ID;

    //Node variables
    NodeButton nodeButton;

    public MapEditorController(MapView mapView) throws Exception {
        this.mapView = mapView;
        state = State.MODIFY_NODE;
        edgeSelection = EdgeSelection.NODE1;
        for (Node node: databaseManager.getAllNodes()) {
            nodeButtonMap.put(node.getId(),new NodeButton(node));
        }
        for (Edge edge: databaseManager.getAllEdges()) {
            edgeLineMap.put(edge.getId(),new EdgeLine(edge));
        }
    }

    public void addEdgeButtonHandler(JFXButton button) {
        button.setOnMousePressed(mouseEvent -> {
            state = State.ADD_EDGE;
            mapView.setAsCancelView();
            edgeSelection = EdgeSelection.NODE1;
        });
    }

    public void setCancelButtonHandler(JFXButton button) {
        button.setOnMousePressed(mouseEvent -> {
            try {
                if (state == State.ADD_EDGE) {
                    resetEdgeStateToDefault();
                } else if (state == State.MODIFY_EDGE) {
                    selectedEdge.reset();
                    mapView.redrawEdge(selectedEdge.edge);
                    resetEdgeStateToDefault();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void resetEdgeStateToDefault() {
        mapView.setAsDefaultView();
        edgeSelection = null;
        addNode1ID = null;
        selectedEdge = null;
        state = null;
    }


    public void setEdgeSelectionButtonHandler(JFXButton button) {
        button.setOnMousePressed(mouseEvent -> {
            if ("Node1".equals(button.getId())) {
                edgeSelection = EdgeSelection.NODE1;
            } else if ("Node2".equals(button.getId())) {
                edgeSelection = EdgeSelection.NODE2;
            }
        });
    }

    public void setMapEventHandlers(AnchorPane floorPane) {
        floorPane.setOnMouseClicked(mouseEvent -> {
            if (state == State.ADD_NODE && !mouseEvent.isDragDetect()) {
                paneAddNodeHandler(mouseEvent);
            }
        });
    }


    public void setEdgeEventHandlers(Line line){
        line.setOnMousePressed(mouseEvent -> {
            try {
                lineModifyEdgeHandler(line,mouseEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        
    }

    public void setNodeEventHandlers(JFXButton button) {

        button.setOnMousePressed(mouseEvent -> {
            try {
                if (state == State.MODIFY_EDGE && edgeSelection != null) {
                    nodeModifyEdgeHandler(button, mouseEvent);
                } else if (state == State.ADD_EDGE) {
                    nodeAddEdgeHandler(button, mouseEvent);
                } else {
                    nodeModifyNodeHandler(button, mouseEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void nodeModifyNodeHandler(JFXButton button, MouseEvent mouseEvent) {
        if(state == State.MODIFY_EDGE){
            resetEdgeStateToDefault();
        }
        state = State.MODIFY_NODE;
        mapView.setAsModifyNodeView(nodeButtonMap.get(button).getNode());
    }


    private void lineModifyEdgeHandler(Line line,MouseEvent mouseEvent) throws Exception {
        state = State.MODIFY_EDGE;
        mapView.setAsEdgeView();
        selectedEdge = edgeLineMap.get(line.getId());
        edgeSelection = null;
        mapView.highlightEdge(line.getId(), selectedEdge.getNode1(), selectedEdge.getNode2());
    }





    private void paneAddNodeHandler(MouseEvent mouseEvent) {

    }




    private void nodeAddEdgeHandler(JFXButton button,MouseEvent mouseEvent) throws Exception {
        if (edgeSelection == EdgeSelection.NODE1) {
            selectedEdge.setNode1(button.getId());
            edgeSelection = EdgeSelection.NODE2;
            addNode1ID = button.getId();
            mapView.setButtonColor(button,"#012D5A",1);
        }
        if (edgeSelection == EdgeSelection.NODE2) {
            selectedEdge.setNode2(button.getId());
            Edge newEdge = new Edge(addNode1ID + "_" + button.getId(),addNode1ID,button.getId());
            Line line = mapView.drawEdge(newEdge);
            edgeLineMap.put(newEdge.getId(),new EdgeLine(newEdge));
            lineModifyEdgeHandler(line,mouseEvent);
        }
    }

    private void nodeModifyEdgeHandler(JFXButton button,MouseEvent mouseEvent) throws Exception {
        if (edgeSelection == EdgeSelection.NODE1) {
            selectedEdge.setNode1(button.getId());
        }
        if (edgeSelection == EdgeSelection.NODE2) {
            selectedEdge.setNode2(button.getId());
        }
        mapView.highlightEdge(selectedEdge.getEdgeID(), selectedEdge.getTempNode1(), selectedEdge.getTempNode2());
    }

    public void setAddNodeButtonHandler() throws Exception {
        //add try catch statements for error label
        String shortName = mapView.getShortName();
        String longName = mapView.getLongName();
        String xCoord = mapView.getXCoord();
        String yCoord = mapView.getYCoord();
        String building = mapView.getBuilding();
        String floor = mapView.getFloor();
        String type = mapView.getType();
        nodeButton.modifyNode(shortName, longName, xCoord, yCoord, building, floor, type);
    }

    public void setModifyNodeButtonHandler() {
    }

    public void setDeleteNodeButtonHandler() {
    }



}
