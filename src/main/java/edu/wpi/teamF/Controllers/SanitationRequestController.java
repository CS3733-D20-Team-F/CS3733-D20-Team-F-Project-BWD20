package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamF.Controllers.UISettings.UISetting;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.ServiceRequest.SanitationServiceRequest;
import edu.wpi.teamF.ModelClasses.UIClasses.UISanitationServiceRequest;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import lombok.SneakyThrows;

public class SanitationRequestController implements Initializable {
  public GridPane optionBar;
  public AnchorPane mainMenu;
  public Label locationLabel;
  public Label descriptionLabel;
  public Label priorityLabel;
  public Label sanitationTypeLabel;
  public JFXButton requestServiceButton;
  public JFXButton checkStatusButton;
  public JFXButton submitButton;
  public JFXButton cancelButton;
  public JFXButton updateButton;
  public JFXButton deleteButton;
  public JFXTextField deleteText;
  public GridPane deletePane;
  public JFXComboBox<String> locationComboBox;
  public JFXComboBox<String> priorityComboBox;
  public JFXComboBox<String> sanitationTypeComboBox;
  public JFXTreeTableView<UISanitationServiceRequest> table;
  public GridPane servicePane;
  public AnchorPane checkStatusPane;
  public AnchorPane anchorPane;
  DatabaseManager databaseManager = DatabaseManager.getManager();
  List<SanitationServiceRequest> sanitationRequestList = databaseManager.getAllSanitationRequests();
  ObservableList<UISanitationServiceRequest> uiSanitationRequests =
      FXCollections.observableArrayList();

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // add the different choices to the choicebox
    // Replace this with long names, linked to IDs

    UISetting uiSetting = new UISetting();
    uiSetting.setAsLocationComboBox(locationComboBox);
    priorityComboBox.setItems(FXCollections.observableArrayList("Low", "Medium", "High"));
    sanitationTypeComboBox.setItems(
        FXCollections.observableArrayList("Spill", "Odor", "Cleanliness"));

    JFXTreeTableColumn<UISanitationServiceRequest, String> ID = new JFXTreeTableColumn<>("ID");
    ID.setPrefWidth(100);
    ID.setCellValueFactory(param -> param.getValue().getValue().getID());

    JFXTreeTableColumn<UISanitationServiceRequest, String> loc =
        new JFXTreeTableColumn<>("Location");
    loc.setPrefWidth(100);
    loc.setCellValueFactory(param -> param.getValue().getValue().getLocation());

    JFXTreeTableColumn<UISanitationServiceRequest, String> priority =
        new JFXTreeTableColumn<>("Priority");
    priority.setPrefWidth(100);
    priority.setCellValueFactory(param -> param.getValue().getValue().getPriority());

    JFXTreeTableColumn<UISanitationServiceRequest, String> date = new JFXTreeTableColumn<>("Date");
    date.setPrefWidth(100);
    date.setCellValueFactory(param -> param.getValue().getValue().getDateSubmitted());

    JFXTreeTableColumn<UISanitationServiceRequest, String> sanitationType =
        new JFXTreeTableColumn<>("Sanitation Type");
    sanitationType.setPrefWidth(100);
    sanitationType.setCellValueFactory(param -> param.getValue().getValue().getType());

    JFXTreeTableColumn<UISanitationServiceRequest, String> description =
        new JFXTreeTableColumn<>("Description");
    description.setPrefWidth(100);
    description.setCellValueFactory(param -> param.getValue().getValue().getAssignee());

    JFXTreeTableColumn<UISanitationServiceRequest, String> assignee =
        new JFXTreeTableColumn<>("Assignee");
    assignee.setPrefWidth(100);
    assignee.setCellValueFactory(param -> param.getValue().getValue().getAssignee());

    JFXTreeTableColumn<UISanitationServiceRequest, String> completed =
        new JFXTreeTableColumn<>("Compeleted");
    completed.setPrefWidth(100);
    completed.setCellValueFactory(param -> param.getValue().getValue().getCompleted());

    for (SanitationServiceRequest sr : sanitationRequestList) {
      uiSanitationRequests.add(new UISanitationServiceRequest(sr));
    }
    final TreeItem<UISanitationServiceRequest> root =
        new RecursiveTreeItem<>(uiSanitationRequests, RecursiveTreeObject::getChildren);

    table
        .getColumns()
        .setAll(ID, date, loc, priority, sanitationType, description, assignee, completed);

    assignee.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    completed.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

    table.setRoot(root);
    table.setEditable(true);
    table.setShowRoot(false);

    resize(anchorPane.getWidth());
    anchorPane
        .widthProperty()
        .addListener(
            (observable, oldWidth, newWidth) -> {
              if (newWidth.doubleValue() != oldWidth.doubleValue()) {
                resize(newWidth.doubleValue());
              }
            });
  }

  private void resize(double width) {
    System.out.println(width);
    Font newFont = new Font(width / 50);
    locationLabel.setFont(newFont);
    priorityLabel.setFont(newFont);

    sanitationTypeLabel.setFont(newFont);
    descriptionLabel.setFont(newFont);
    submitButton.setFont(newFont);
    cancelButton.setFont(newFont);
    updateButton.setFont(newFont);
  }

  public void submit(ActionEvent actionEvent) throws Exception {
    String location = locationComboBox.getValue();
    String nodeID = location.substring(location.length() - 10);
    System.out.println(location);
    System.out.println(nodeID);
    Node node = databaseManager.readNode(nodeID);
    String priorityString = priorityComboBox.getValue();
    int priority = 0;
    if (priorityString.equals("Low")) {
      priority = 1;
    } else if (priorityString.equals("Medium")) {
      priority = 2;
    } else if (priorityString.equals("High")) {
      priority = 3;
    }
    String sanitationType = sanitationTypeComboBox.getValue();

    Date date = new Date(System.currentTimeMillis());
    SanitationServiceRequest sanitationRequest =
        new SanitationServiceRequest(
            node, "Not Assigned", "No Description", date, priority, sanitationType);
    databaseManager.manipulateServiceRequest(sanitationRequest);
    uiSanitationRequests.add(new UISanitationServiceRequest(sanitationRequest));
    table.refresh();
    resetRequest();
  }

  private void resetRequest() {
    locationComboBox.setValue(null);
    priorityComboBox.setValue(null);
    sanitationTypeComboBox.setValue(null);
  }

  public void cancel(ActionEvent actionEvent) {
    resetRequest();
  }

  public void request(ActionEvent actionEvent) {
    servicePane.setVisible(true);
    checkStatusPane.setVisible(false);
  }

  public void update(ActionEvent actionEvent) throws Exception {
    for (UISanitationServiceRequest uiSR : uiSanitationRequests) {
      SanitationServiceRequest toUpdate =
          databaseManager.readSanitationServiceRequest(uiSR.getID().get());
      if (!uiSR.equalsCSR(toUpdate)) {
        toUpdate.setAssignee(uiSR.getAssignee().get());
        String completed = uiSR.getCompleted().get();
        if (completed.equals("Incomplete")) {
          toUpdate.setComplete(false);
        } else if (completed.equals("Complete")) {
          toUpdate.setComplete(true);
        }
        databaseManager.manipulateServiceRequest(toUpdate);
      }
    }
    table.refresh();
  }

  public void delete(ActionEvent actionEvent) {
    String toDelete = deleteText.getText();
    databaseManager.deleteSanitationService(toDelete);
    uiSanitationRequests.removeIf(
        sanitationRequest -> sanitationRequest.getID().get().equals(toDelete));
  }

  public void checkStatus(ActionEvent actionEvent) {
    servicePane.setVisible(false);
    checkStatusPane.setVisible(true);
  }
}
