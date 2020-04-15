package edu.wpi.teamF.Factories;

import edu.wpi.teamF.ModelClasses.Node;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;

public class CSVManipulator {
  private NodeFactory nodeFactory = new NodeFactory();
  private EdgeFactory edgeFactory = new EdgeFactory();
  /**
   * reads a csv file that contains nodes and inserts the data in the file into the correct place in
   * the database
   */
  public void readCSVFileNode(Path path) {
    String row = "";
    ArrayList<String> data = new ArrayList<>();
    try {
      // goes to get the file
      BufferedReader csvReader = new BufferedReader(new FileReader(path.toFile()));
      /*BufferedWriter csvWriter = new BufferedWriter(new FileWriter(path.toFile(), true));
      csvWriter.newLine();
      csvWriter.write("end");
      csvWriter.close();*/
      while ((row = csvReader.readLine()) != null) {
        data.addAll(Arrays.asList(row.split(",")));
      }

      int i = 9;
      while (i < (data.size() - 1) && !data.get(i).equals("end")) {
        Node node =
            new Node(
                data.get(i), // name
                Short.parseShort(data.get(i + 1)), // xcoord
                Short.parseShort(data.get(i + 2)), // ycoord
                data.get(i + 4), // building
                data.get(i + 6), // longname
                data.get(i + 7), // shortname
                Node.NodeType.getEnum(data.get(i + 5)), // nodetype
                Short.parseShort(data.get(i + 3)));
        System.out.println("Created Node on line " + i / 9 + ", " + node.getName());
        nodeFactory.create(node); // floor
        i = i + 9;
      }

    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File Not found!");
    } catch (EOFException e) {
      // Expected use to end read csv
    } catch (IOException e) {

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getClass());
    }
  }

  /** Writes to the CSV file so that it can become persistant */
  public void writeCSVFileNode(Path path) {
    // writing to the file
    ObservableList<Node> nodes = nodeFactory.getAllNodes();
    for (Node n : nodes) {

      // csvString = csvString + formatNode(n);
      formatNode(n);
    }
    try (FileWriter fw = new FileWriter(path.toString());
        BufferedWriter bw = new BufferedWriter(fw); ) {

      bw.write("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName,teamAssigned");

      for (Node n : nodes) {
        bw.newLine();
        bw.write(formatNode(n));
      }
      bw.close();
    } catch (IOException e) {
      // exception handling left as an exercise for the reader
    }
  }

  /**
   * formats a node into csv
   *
   * @param node
   * @return returns a node in the form of a string
   */
  public String formatNode(Node node) {
    String string = "";
    string = node.getName() + ",";
    string = string + String.valueOf(node.getXCoord()) + ",";
    string = string + String.valueOf(node.getYCoord()) + ",";
    string = string + String.valueOf(node.getFloor()) + ",";
    string = string + node.getBuilding() + ",";
    string = string + node.getType().getTypeString() + ",";
    string = string + node.getLongName() + ",";
    string = string + node.getShortName() + ",";
    string = string + "dataWritten";

    return string;
  }

  /**
   * reads a csv file that contains edges and inserts the data in the file into the correct place in
   * the database
   */
  public void readCSVFileEdge(Path path) {
    String row = "";
    ArrayList<String> data = new ArrayList<>();
    try {
      // goes to get the file
      BufferedReader csvReader = new BufferedReader(new FileReader(path.toFile()));
      while ((row = csvReader.readLine()) != null) {
        data.addAll(Arrays.asList(row.split(",")));
      }

      for (int i = 0; i < data.size(); i = i + 2) {
        //  edgeFactory.create(data.get(i), data.get(i + 1), data.get(i + 2));
      }

    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File Not found!");
    } catch (EOFException e) {
      // Expected use to end read csv
    } catch (IOException e) {

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /** Writes to the CSV file so that it can become persistant */
  /*public void writeCSVFileEdge() throws IOException {
  String csvString = "";
  // writing to the file


  /*for(Node n: Node) {

  try (FileWriter fw = new FileWriter("download.txt", true);
  for(Node n: Node) {

    csvString = csvString + formatNode(n);
  }*/

}
