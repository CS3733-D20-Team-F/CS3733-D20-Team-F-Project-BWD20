package edu.wpi.teamF;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.teamF.controllers.PathfindController;
import edu.wpi.teamF.factories.NodeFactory;
import edu.wpi.teamF.modelClasses.ElevatorScorer2;
import edu.wpi.teamF.modelClasses.EuclideanScorer;
import edu.wpi.teamF.modelClasses.Node;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AStarTest {

  Node testNode1 = new Node((short) 0, (short) 0, "node1", Node.NodeType.HALL, 1);
  Node testNode2 = new Node((short) 0, (short) 5, "node2", Node.NodeType.HALL, 1);



  static NodeFactory nodeFactory;
  static PathfindController pathfinder;

  @BeforeAll
  public static void setup() {
    nodeFactory = new NodeFactory();
    pathfinder = new PathfindController(nodeFactory);
  }

  @Test
  public void testOfEuclideanScorer() {
    EuclideanScorer testScorer = new EuclideanScorer();
    assertEquals(5, testScorer.computeCost(testNode1, testNode2), "message");
  }

  @Test
  public void testElevatorScorer2_1() {
    Node elev1 = new Node((short) 1, (short) 1, "elev1", Node.NodeType.ELEV, 1);
    Node elev2 = new Node((short) 10, (short) 10, "elev2", Node.NodeType.ELEV, 1);
    Node elev3 = new Node((short) 8, (short) 3, "elev3", Node.NodeType.ELEV, 1);
    List<Node> elevators = Arrays.asList(elev1, elev2, elev3);
    Node start = new Node((short) 3, (short) 2, "start", Node.NodeType.HALL, 1);
    Node end = new Node((short) 10, (short) 2, "end", Node.NodeType.HALL, 1);
    ElevatorScorer2 tester = new ElevatorScorer2(elevators);

    Assertions.assertEquals(elev1, tester.elevatorScorer(start, end), "Elevator 1 is closest");
  }
}
