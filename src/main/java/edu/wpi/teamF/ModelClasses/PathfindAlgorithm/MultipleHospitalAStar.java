package edu.wpi.teamF.ModelClasses.PathfindAlgorithm;

import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.RouteNode;
import edu.wpi.teamF.ModelClasses.Scorer.Scorer;
import java.util.*;

public class MultipleHospitalAStar extends HeuristicSearch {
  public MultipleHospitalAStar(List<Node> nodeList) {
    super(nodeList);
  }

  @Override
  public double calcEstimatedCostOfNeighbor(
      RouteNode currentNode, Node neighbor, Node endNode, Scorer scorer) {
    System.out.println("Distance to end");
    double distanceToEnd = scorer.computeCost(neighbor, endNode);

    System.out.println("Distance from start");
    double distanceFromStart =
        currentNode.getRouteScore()
            + scorer.computeCost(currentNode.getNode(), neighbor); // Actual path distance
    return distanceToEnd + distanceFromStart;
  }
}
