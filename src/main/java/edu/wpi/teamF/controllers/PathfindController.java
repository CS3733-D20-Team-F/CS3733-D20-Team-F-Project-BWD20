package edu.wpi.teamF.controllers;

import edu.wpi.teamF.factories.NodeFactory;
import edu.wpi.teamF.pathfinding.EuclideanScorer;
import edu.wpi.teamF.pathfinding.GraphNode;
import edu.wpi.teamF.pathfinding.RouteNode;

import java.util.*;

public class PathfindController extends SceneController {

    private NodeFactory nodeFactory;

    public PathfindController(NodeFactory nodeFactory){
        this.nodeFactory = nodeFactory;
    }

    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public void setNodeFactory(NodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }

    public List<GraphNode> getPath(GraphNode startNode, GraphNode endNode) {
        PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<RouteNode>();
        HashSet<GraphNode> visited = new HashSet<GraphNode>();
        EuclideanScorer scorer = new EuclideanScorer();

        // Create the first node and add it to the Priority Queue
        RouteNode start = new RouteNode(startNode, null, 0, scorer.computeCost(startNode, endNode));
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            RouteNode currentNode = priorityQueue.poll();

            if (!visited.contains(currentNode.getCurrent())) {
                visited.add(currentNode.getCurrent());

                if (currentNode.getCurrent().equals(endNode)) {
                    // Has reached the goal node
                    List<GraphNode> path = new LinkedList<>();
                    do {
                        path.add(0, currentNode.getCurrent());
                        currentNode = currentNode.getPrevious();
                    } while (currentNode != null);
                    return path;
                }

                // Make a list of all of the neighbors of this node
                Set<GraphNode> neighbors = currentNode.getCurrent().getNeighbors();
                for (GraphNode neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        double distanceToEnd =
                                scorer.computeCost(neighbor, endNode); // Estimated distance to end
                        double distanceFromStart =
                                currentNode.getRouteScore()
                                        + scorer.computeCost(startNode, neighbor); // Actual path distance
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
        return new ArrayList<GraphNode>();
    }


}
