package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamF.App;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.DatabaseManipulators.ServiceRequestStats;
import edu.wpi.teamF.ModelClasses.ServiceRequest.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;

public class DataViewController implements Initializable {

  public JFXComboBox<String> serviceChoice;
  // Maintenance Request
  public BarChart<?, ?> completedChartMain;
  public PieChart pieChartTotalMain;
  public BarChart<?, ?> serviceLocationBarMain;
  public CategoryAxis xAxisMain;
  public NumberAxis yAxisMain;
  public CategoryAxis yAxisLoc;
  public NumberAxis xAxisLoc;
  public PieChart pieChartMainComp;

  // Transport Request

  // Sanitation Reques

  // Traffic

  public AnchorPane rootPane;

  ServiceRequestStats serviceRequestStats = new ServiceRequestStats();
  DatabaseManager databaseManager = DatabaseManager.getManager();
  public List<MaintenanceRequest> mR = databaseManager.getAllMaintenanceRequests();
  public List<TransportRequest> tR = databaseManager.getAllTransportRequests();
  public List<SanitationServiceRequest> sR = databaseManager.getAllSanitationRequests();
  public SceneController sceneController = App.getSceneController();
  DirectoryChooser backup = new DirectoryChooser();

  public DataViewController() throws Exception {}

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    List<ReportsClass> rC = databaseManager.getAllReports();

    // Maintenance

    // Transport
    List<String> data1;
    List<String> data2;
    XYChart.Series dataSeries1 = new XYChart.Series<>();
    XYChart.Series dataSeries2 = new XYChart.Series<>();
    data1 = serviceRequestStats.getMaintenanceEmployeeNumbersGraphs(mR);
    data2 = serviceRequestStats.getMaintenanceLocationNumbersGraphs(mR);
    String avgTime1 = serviceRequestStats.CalculateAverageMaintenanceTimeGraphs(mR);

    // completed Graph to show number of completed requests per employee

    for (int i = 0; i < data1.size(); i += 2) {
      dataSeries1
          .getData()
          .add(new XYChart.Data<>(data1.get(i), Integer.parseInt(data1.get(i + 1))));
    }

    for (int i = 0; i < data2.size(); i += 2) {
      dataSeries2
          .getData()
          .add(new XYChart.Data<>(data2.get(i), Integer.parseInt(data2.get(i + 1))));
    }

    completedChartMain.getData().add(dataSeries1);
    serviceLocationBarMain.getData().add(dataSeries2);

    // Pie Chart to compared visually which employee did the most service requests
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    for (int i = 0; i < data1.size(); i += 2) {
      pieChartData.add(new PieChart.Data(data1.get(i), Integer.parseInt(data1.get(i + 1))));
    }
    pieChartTotalMain.setData(pieChartData);


  }

  public void back(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("Accounts");
  }

  public void backupServiceRequests(ActionEvent actionEvent) {
    backup.setTitle("Select Where to Backup Database");
    File selDir = backup.showDialog(rootPane.getScene().getWindow());
    serviceRequestStats.downloadStatistics(selDir.toPath());
  }
}
