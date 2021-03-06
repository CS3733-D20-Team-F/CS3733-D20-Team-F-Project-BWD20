package edu.wpi.teamF.ModelClasses;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.TestData;
import java.sql.SQLException;
import java.util.List;
import javax.management.InstanceNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NodeFactoryTest {

  static TestData testData = null;
  static Node[] validNodes = null;
  static DatabaseManager databaseManager = DatabaseManager.getManager();

  @BeforeEach
  public void initialize() throws Exception {
    testData = new TestData();
    validNodes = testData.validNodes;
    databaseManager.initialize();
  }

  @AfterAll
  public static void reset() throws SQLException {
    databaseManager.reset();
  }

  @Test
  public void testCreateReadDelete() throws Exception {
    try {
      databaseManager.manipulateNode(null);
      fail("Creating a null value is unacceptable");
    } catch (NullPointerException e) {
      // ignore as expected
    }
    try {
      for (Node node : validNodes) {
        databaseManager.manipulateNode(node);

        Node readNode = databaseManager.readNode(node.getId());
        assertTrue(readNode.equals(node));

        databaseManager.deleteNode(node.getId());

        try {
          readNode = databaseManager.readNode(node.getId());
        } catch (InstanceNotFoundException e) {
          // ignore
        } catch (Exception e) {
          fail(e.getMessage() + ", " + e.getClass());
        }
      }
    } catch (Exception e) {
      fail(e.getMessage() + ", " + e.getClass());
    }
  }

  @Test
  public void testCreateReadUpdateDelete() {
    try {
      for (Node node : validNodes) {
        databaseManager.manipulateNode(node);

        node.setBuilding("Hello");
        databaseManager.manipulateNode(node);

        Node readNode = databaseManager.readNode(node.getId());

        assertTrue(node.equals(readNode));

        databaseManager.deleteNode(node.getId());
      }
    } catch (Exception e) {
      fail(e.getMessage() + ", " + e.getClass());
    }
  }

  @Test
  public void testGetNodesByType() {
    Node node1 = validNodes[0];
    Node node2 = validNodes[1];
    Node node3 = validNodes[2];
    Node node4 = validNodes[3];

    node1.setType(Node.NodeType.ELEV);
    node2.setType(Node.NodeType.ELEV);
    node3.setType(Node.NodeType.CONF);
    node4.setType(Node.NodeType.DEPT);

    try {
      databaseManager.manipulateNode(node1);
      databaseManager.manipulateNode(node2);
      databaseManager.manipulateNode(node3);
      databaseManager.manipulateNode(node4);

      List<Node> elevatorNodes = databaseManager.getNodesByType(Node.NodeType.ELEV);

      assertTrue(elevatorNodes.contains(node1));
      assertTrue(elevatorNodes.contains(node2));
      assertTrue(elevatorNodes.size() == 2);

      List<Node> conferenceNodes = databaseManager.getNodesByType(Node.NodeType.CONF);
      assertTrue(conferenceNodes.contains(node3));
      assertTrue(conferenceNodes.size() == 1);

      databaseManager.deleteNode(node1.getId());
      databaseManager.deleteNode(node2.getId());
      databaseManager.deleteNode(node3.getId());
      databaseManager.deleteNode(node4.getId());
    } catch (Exception e) {
      fail(e.getMessage() + ", " + e.getClass());
    }
  }

  @Test
  public void testGetAllNodes() {
    Node node1 = validNodes[0];
    Node node2 = validNodes[1];
    Node node3 = validNodes[2];
    Node node4 = validNodes[3];

    node1.setType(Node.NodeType.ELEV);
    node2.setType(Node.NodeType.ELEV);
    node3.setType(Node.NodeType.CONF);
    node4.setType(Node.NodeType.DEPT);

    try {
      databaseManager.manipulateNode(node1);
      databaseManager.manipulateNode(node2);
      databaseManager.manipulateNode(node3);
      databaseManager.manipulateNode(node4);

      List<Node> elevatorNodes = databaseManager.getAllNodes();

      assertTrue(elevatorNodes.contains(node1));
      assertTrue(elevatorNodes.contains(node2));
      assertTrue(elevatorNodes.contains(node3));
      assertTrue(elevatorNodes.contains(node4));
      assertTrue(elevatorNodes.size() == 4);

      databaseManager.deleteNode(node1.getId());
      databaseManager.deleteNode(node2.getId());
      databaseManager.deleteNode(node3.getId());
      databaseManager.deleteNode(node4.getId());
    } catch (Exception e) {
      fail(e.getMessage() + ", " + e.getClass());
    }
  }
}
