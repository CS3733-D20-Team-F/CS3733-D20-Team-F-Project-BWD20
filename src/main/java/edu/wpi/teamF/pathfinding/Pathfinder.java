package edu.wpi.teamF.pathfinding;

import java.nio.channels.FileChannel;
import java.util.*;


public class Pathfinder {

    public List<GraphNode> getPath(GraphNode startNode, GraphNode endNode, Set<GraphNode> graph){
        PriorityQueue<RouteNode> priorityQueue = new PriorityQueue<RouteNode>();
        HashSet<RouteNode> visited = new HashSet<RouteNode>();
        EuclideanScorer scorer = new EuclideanScorer();

        //Create the first node and add it to the Priority Queue
        RouteNode start = new RouteNode(startNode, null, 0, scorer.computeCost(startNode, endNode));
        priorityQueue.add(start);
        visited.add(start);

        while (!priorityQueue.isEmpty()){
            RouteNode currentNode = priorityQueue.poll();

            if(!visited.contains(currentNode)){
                visited.add(currentNode);

                if (currentNode.getCurrent().equals(endNode)){
                    //Has reached the goal node
                    List<GraphNode> path = new ArrayList<GraphNode>();
                    do {
                        path.add(currentNode.getCurrent());
                        currentNode = currentNode.getPrevious();
                    } while (currentNode != null);
                    return path;
                 }

                //Make a list of all of the neighbors of this node
                Set<GraphNode> neighbors = currentNode.getCurrent().getNeighbors();
                for (GraphNode neighbor : neighbors){
                    if (!visited.contains(neighbor)){
                        double distanceToEnd = scorer.computeCost(neighbor, endNode); //Estimated distance to end
                        double distanceFromStart = currentNode.getRouteScore() + scorer.computeCost(startNode, neighbor); //Actual path distance
                        double estimatedCostOfNeighbor = distanceToEnd + distanceFromStart;

                        RouteNode neighborOnRoute = new RouteNode(neighbor, currentNode.getCurrent(), distanceFromStart, estimatedCostOfNeighbor);
                        priorityQueue.add(neighborOnRoute);
                    }
                }
            }
            else {
                //It has been visited, we need to check if the current route has a lower cost or not
            }
        }

        //If it exits the while loop without returning a path
        System.out.println("No Route Found");
        return new ArrayList<GraphNode>();

    }

}
