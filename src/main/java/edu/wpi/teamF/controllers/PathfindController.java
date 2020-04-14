package edu.wpi.teamF.controllers;

import edu.wpi.teamF.factories.NodeFactory;
import edu.wpi.teamF.ModelClasses.ElevatorScorer;
import edu.wpi.teamF.ModelClasses.ElevatorScorer2;
import edu.wpi.teamF.ModelClasses.EuclideanScorer;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.RouteNode;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuButton;
import javafx.scene.paint.Color;

public class PathfindController extends SceneController {

  public static int MAP_HEIGHT = 1485;
  public static int MAP_WIDTH = 2475;

  public Canvas canvasMap;
  public MenuButton destinationPicker;
  private NodeFactory nodeFactory;
  private Node startNode;
  private Node destination;

  public PathfindController() {

    this.nodeFactory = NodeFactory.getFactory();
  }

  //  public PathfindController(NodeFactory nodeFactory) {
  //    this.nodeFactory = nodeFactory;
  //  }

  public List<Node> getPath(Node startNode, Node endNode) {
    // Check if the destination is on a different floor
    if (startNode.getFloor() != endNode.getFloor()) {
      // create list of elevators to navigate
      List<Node> elevatorList = nodeFactory.getNodesByType(Node.NodeType.ELEV);
      // If it is, navigate to the most practical elevator instead
      ElevatorScorer2 elevScorer =
          new ElevatorScorer2(elevatorList); // Need to find out how we pass it a list of elevators?
      endNode = elevScorer.elevatorScorer(startNode, endNode);
    }
    PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<RouteNode>();
    HashSet<Node> visited = new HashSet<Node>();
    EuclideanScorer scorer = new EuclideanScorer();
    // Create the first node and add it to the Priority Queue
    RouteNode start = new RouteNode(startNode, null, 0, scorer.computeCost(startNode, endNode));
    priorityQueue.add(start);
    while (!priorityQueue.isEmpty()) {
      RouteNode currentNode = priorityQueue.poll();
      if (!visited.contains(currentNode.getNode())) {
        visited.add(currentNode.getNode());
        if (currentNode.getNode().equals(endNode)) {
          // Has reached the goal node
          List<Node> path = new LinkedList<>();
          do {
            path.add(0, currentNode.getNode());
            currentNode = currentNode.getPrevious();
          } while (currentNode != null);
          return path;
        }
        // Make a list of all of the neighbors of this node
        Set<Node> neighbors = new HashSet<>();
        for (String neighborNode : currentNode.getNode().getNeighbors()) {
          try {
            neighbors.add(nodeFactory.read(neighborNode));
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
        }
        for (Node neighbor : neighbors) {
          if (!visited.contains(neighbor)) {
            double distanceToEnd =
                scorer.computeCost(neighbor, endNode); // Estimated distance to end
            double distanceFromStart =
                currentNode.getRouteScore()
                    + scorer.computeCost(currentNode.getNode(), neighbor); // Actual path distance
            double estimatedCostOfNeighbor = distanceToEnd + distanceFromStart;
            RouteNode neighborOnRoute =
                new RouteNode(neighbor, currentNode, distanceFromStart, estimatedCostOfNeighbor);
            priorityQueue.add(neighborOnRoute);
          }
        }
      }
    }
    // If it exits the while loop without returning a path
    System.out.println("No Route Found");
    return new ArrayList<Node>();
  }

  public List<Node> getPath2(Node startNode, Node endNode) {
    // Check if the destination is on a different floor
    PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<RouteNode>();
    HashSet<Node> visited = new HashSet<Node>();
    EuclideanScorer scorer = new EuclideanScorer();
    ElevatorScorer elevScorer = new ElevatorScorer(nodeFactory.getNodesByType(Node.NodeType.ELEV));
    // Create the first node and add it to the Priority Queue
    RouteNode start;
    if (startNode.getFloor() != endNode.getFloor()) {
      // If it is, navigate to the most practical elevator instead
      start = new RouteNode(startNode, null, 0, elevScorer.computeCost(startNode, endNode));
    } else {
      start = new RouteNode(startNode, null, 0, scorer.computeCost(startNode, endNode));
    }
    priorityQueue.add(start);
    while (!priorityQueue.isEmpty()) {
      RouteNode currentNode = priorityQueue.poll();
      if (!visited.contains(currentNode.getNode())) {
        visited.add(currentNode.getNode());
        if (currentNode.getNode().equals(endNode)) {
          // Has reached the goal node
          List<Node> path = new LinkedList<>();
          do {
            path.add(0, currentNode.getNode());
            currentNode = currentNode.getPrevious();
          } while (currentNode != null);
          return path;
        }
        // Make a list of all of the neighbors of this node
        Set<Node> neighbors = new HashSet<>();
        for (String neighborNode : currentNode.getNode().getNeighbors()) {
          try {
            neighbors.add(nodeFactory.read(neighborNode));
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
        }
        for (Node neighbor : neighbors) {
          if (!visited.contains(neighbor)) {
            double distanceToEnd = 0;
            if (currentNode.getNode().getFloor() != endNode.getFloor()) {
              // If its not on the same floor, use elevator scorer
              distanceToEnd = elevScorer.computeCost(currentNode.getNode(), endNode);
            } else {
              distanceToEnd = scorer.computeCost(startNode, endNode);
            }
            double distanceFromStart =
                currentNode.getRouteScore()
                    + scorer.computeCost(currentNode.getNode(), neighbor); // Actual path distance
            double estimatedCostOfNeighbor = distanceToEnd + distanceFromStart;
            RouteNode neighborOnRoute =
                new RouteNode(neighbor, currentNode, distanceFromStart, estimatedCostOfNeighbor);
            priorityQueue.add(neighborOnRoute);
          }
        }
      }
    }
    // If it exits the while loop without returning a path
    System.out.println("No Route Found");
    return new ArrayList<Node>();
  }

  public void printPath(List<Node> path) {
    for (int i = 0; i < path.size(); i++) {
      System.out.println((i + 1) + ". " + path.get(i).getName());
    }
  }

  // Canvas Testing
  public void drawCanvas(ActionEvent actionEvent) {
    GraphicsContext gc = canvasMap.getGraphicsContext2D();
    if (destination != null) {
      gc.clearRect(0, 0, canvasMap.getWidth(), canvasMap.getHeight());
      List<Node> path = getPath(startNode, destination);
      drawPath(gc, path);
    } else {
      gc.strokeText("NO DESTINATION", 100, 100);
    }
  }

  private void drawLines(GraphicsContext gc) {
    double heightRatio = canvasMap.getHeight() / MAP_HEIGHT;
    double widthRatio = canvasMap.getWidth() / MAP_WIDTH;
    gc.setStroke(Color.RED);
    gc.beginPath();

    gc.moveTo(675 * widthRatio, 1215 * widthRatio);
    gc.lineTo(685 * widthRatio, 1215 * widthRatio);
    gc.lineTo(785 * widthRatio, 1215 * widthRatio);
    gc.lineTo(930 * widthRatio, 1215 * widthRatio);
    gc.lineTo(1120 * widthRatio, 1210 * widthRatio);
    gc.lineTo(1245 * widthRatio, 1210 * widthRatio);
    gc.lineTo(1250 * widthRatio, 1120 * widthRatio);
    gc.lineTo(1250 * widthRatio, 1065 * widthRatio);
    gc.lineTo(1260 * widthRatio, 1060 * widthRatio);
    gc.stroke();
  }

  /**
   * Draw the path on the canvas
   *
   * @param path
   */
  private void drawPath(GraphicsContext gc, List<Node> path) {
    double heightRatio = canvasMap.getHeight() / MAP_HEIGHT;
    double widthRatio = canvasMap.getWidth() / MAP_WIDTH;
    gc.setStroke(Color.RED);
    gc.beginPath();
    gc.moveTo(path.get(0).getXCoord() * widthRatio, path.get(0).getYCoord() * heightRatio);
    for (int i = 1; i < path.size(); i++) {
      gc.lineTo(path.get(i).getXCoord() * widthRatio, path.get(i).getYCoord() * heightRatio);
    }
    gc.stroke();
  }

  /**
   * retrieve the node from database based on destination name
   *
   * @param destinationName
   */
  public void setDestination(String destinationName) {
    try {
      destination = nodeFactory.read(destinationName);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void psychiatricImpatientCareButton(ActionEvent actionEvent) {
    setDestination("Psychiatric Impatient Care");
    destinationPicker.setText("Psychiatric Impatient Care");
  }

  public void addictionCareButton(ActionEvent actionEvent) {
    setDestination("Addiction Care");
    destinationPicker.setText("Addiction Care");
  }

  public void mechanicalSpaceButton(ActionEvent actionEvent) {
    setDestination("Mechanical Space");
    destinationPicker.setText("Mechanical Space");
  }

  public void occupationalTherapyButton(ActionEvent actionEvent) {
    setDestination("Occupational Therapy");
    destinationPicker.setText("Occupational Therapy");
  }

  public void physicalTherapyButton(ActionEvent actionEvent) {
    setDestination("Physical Therapy");
    destinationPicker.setText("Physical Therapy");
  }

  public void foodServicesButton(ActionEvent actionEvent) {
    setDestination("Food Service");
    destinationPicker.setText("Food Service");
  }

  public void otolaryngologyButton(ActionEvent actionEvent) {
    setDestination("Otolaryngology");
    destinationPicker.setText("Otolarngology");
  }

  public void plasticSurgeryButton(ActionEvent actionEvent) {
    setDestination("Plastic Surgery");
    destinationPicker.setText("Plastic Surgery");
  }

  public void centralSterileSupplyButton(ActionEvent actionEvent) {
    setDestination("Central Sterile Supply");
    destinationPicker.setText("Central Sterile Supply");
  }

  public void laundryButton(ActionEvent actionEvent) {
    setDestination("Laundry");
    destinationPicker.setText("Laundry");
  }

  public void biomedButton(ActionEvent actionEvent) {
    setDestination("Biomedical Engineering");
    destinationPicker.setText("Biomedical Engineering");
  }

  public void morgueButton(ActionEvent actionEvent) {
    setDestination("Morgue");
    destinationPicker.setText("Morgue");
  }

  public void materialManagementButton(ActionEvent actionEvent) {
    setDestination("Material Management");
    destinationPicker.setText("Material Management");
  }
}
