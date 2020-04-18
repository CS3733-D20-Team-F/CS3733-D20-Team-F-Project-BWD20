package edu.wpi.teamF.DatabaseManipulators;

import edu.wpi.teamF.ModelClasses.Edge;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.ValidationException;
import edu.wpi.teamF.ModelClasses.Validators;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EdgeFactory {

  private static final EdgeFactory factory = new EdgeFactory();

  NodeFactory nodeFactory = NodeFactory.getFactory();

  public static EdgeFactory getFactory() {
    return factory;
  }

  /**
   * Creates edge entries in database
   *
   * @param edge the edge to add
   * @throws ValidationException if anything goes wrong
   */
  public void create(Edge edge) throws ValidationException {
    String insertStatement =
            "INSERT INTO "
                    + DatabaseManager.EDGES_TABLE_NAME
                    + " ( "
                    + DatabaseManager.EDGEID_KEY
                    + ", "
                    + DatabaseManager.NODE_1_KEY
                    + ", "
                    + DatabaseManager.NODE_A_KEY
                    + " ) "
                    + "VALUES (?, ?, ?)";
    Validators.edgeValidation(edge);
    try (PreparedStatement prepareStatement =
                 DatabaseManager.getConnection().prepareStatement(insertStatement)) {
      int param = 1;
      prepareStatement.setString(param++, edge.getId());
      prepareStatement.setString(param++,edge.getNode1().getId());
      prepareStatement.setString(param++, edge.getNode2().getId());
      try {
        int numRows = prepareStatement.executeUpdate();
        if (numRows < 1) {
          throw new SQLException("Created more than one rows");
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Reads edge entries from database
   *
   * @param id the id of the edge to read
   * @return the edge read
   */
  public Edge read(String id){
    Edge edge = null;
    String selectStatement =
            "SELECT * FROM "
                    + DatabaseManager.EDGES_TABLE_NAME
                    + " WHERE "
                    + DatabaseManager.EDGEID_KEY
                    + " = ?";

    try (PreparedStatement preparedStatement =
                 DatabaseManager.getConnection().prepareStatement(selectStatement)) {
      preparedStatement.setString(1, id);

      try {
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        edge =
                new Edge(
                        resultSet.getString(DatabaseManager.EDGEID_KEY),
                        nodeFactory.read(resultSet.getString(DatabaseManager.NODE_1_KEY)),
                        nodeFactory.read(resultSet.getString(DatabaseManager.NODE_A_KEY)));
      } catch (ValidationException e) {
        throw e;
      }
    } catch (IllegalArgumentException e) {
      throw e;
    } catch (Exception e) {
      System.out.println("Exception in EdgeFactory read: " + e.getMessage() + ", " + e.getClass());
    }
    return edge;
  }

  /**
   * Updates edge entries in the database
   *
   * @param edge the edge to update
   */
  public void update(Edge edge) {
    String updateStatement =
            "UPDATE "
                    + DatabaseManager.EDGES_TABLE_NAME
                    + " SET "
                    + DatabaseManager.EDGEID_KEY
                    + " = ?, "
                    + DatabaseManager.NODE_1_KEY
                    + " = ?, "
                    + DatabaseManager.NODE_A_KEY
                    + " = ? "
                    + "WHERE "
                    + DatabaseManager.EDGEID_KEY
                    + " = ?";
    try (PreparedStatement preparedStatement =
                 DatabaseManager.getConnection().prepareStatement(updateStatement)) {
      int param = 1;
      preparedStatement.setString(param++, edge.getId());
      preparedStatement.setString(param++, edge.getNode1().getId());
      preparedStatement.setString(param++, edge.getNode2().getId());
      int numRows = preparedStatement.executeUpdate();
      if (numRows != 1) {
        throw new Exception("Updated " + numRows + " rows");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

  }

  /**
   * Deletes edges from the database
   *
   * @param id the id of the edge to delete
   */
  public void delete(String id) {
    String deleteStatement =
            "DELETE FROM "
                    + DatabaseManager.EDGES_TABLE_NAME
                    + " WHERE "
                    + DatabaseManager.EDGEID_KEY
                    + " = ?";
    try (PreparedStatement preparedStatement =
                 DatabaseManager.getConnection().prepareStatement(deleteStatement)) {
      preparedStatement.setString(1, id);

      int numRows = preparedStatement.executeUpdate();
      if (numRows > 1) {
        throw new SQLException("Deleted " + numRows + " rows");
      }
    } catch (SQLException e) {
      System.out.println("Error: " + e.getMessage() + ", " + e.getCause());
    }

  }

  /**
   * Gets all edges from the database
   *
   * @return a list of all edges in the database
   */
  public List<Edge> getAllEdges() {
    List<Edge> edges = null;
    String selectStatement = "SELECT * FROM " + DatabaseManager.EDGES_TABLE_NAME;

    try (PreparedStatement preparedStatement =
                 DatabaseManager.getConnection().prepareStatement(selectStatement);
         ResultSet resultSet = preparedStatement.executeQuery()) {;
      edges = new ArrayList<>();
      while (resultSet.next()) {
        edges.add(
                new Edge(
                        resultSet.getString(DatabaseManager.EDGEID_KEY),
                        nodeFactory.read(resultSet.getString(DatabaseManager.NODE_1_KEY)),
                        nodeFactory.read(resultSet.getString(DatabaseManager.NODE_A_KEY)));
      }
    } catch (Exception e) {
      System.out.println(
              "Exception in EdgeFactory getAllEdges: " + e.getMessage() + ", " + e.getClass());
    }
    return edges;
  }
  }
}
