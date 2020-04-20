package edu.wpi.teamF;

import edu.wpi.teamF.DatabaseManipulators.CSVManipulator;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import java.io.IOException;

public class Main {
  private static CSVManipulator csvm = new CSVManipulator();
  private static DatabaseManager dbm = new DatabaseManager();

  public static void main(String[] args) throws IOException {

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      initDB();
    } catch (ClassNotFoundException e) {
      System.out.println("Driver Not found");
    }

    DatabaseManager databaseManager = new DatabaseManager();
    databaseManager.initialize();
    CSVManipulator newManipulator = new CSVManipulator();
    newManipulator.readCSVFileNode(
        Main.class.getResource("CSVFiles/MapFAllnodes.csv").openStream());
    newManipulator.readCSVFileEdge(
        Main.class.getResource("CSVFiles/MapFAlledges.csv").openStream());

    App.launch(App.class, args);
  }

  public static void initDB() {
    dbm.initialize();
    csvm.readCSVFileNode(Main.class.getResourceAsStream("CSVFiles/MapFAllnodes.csv"));
    csvm.readCSVFileEdge(Main.class.getResourceAsStream("CSVFiles/MapFAlledges.csv"));
  }
}
