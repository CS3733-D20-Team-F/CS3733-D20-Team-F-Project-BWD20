package edu.wpi.cs3733.d20.teamF.Controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.d20.teamF.Controllers.UISettings.UISetting;
import edu.wpi.cs3733.d20.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.cs3733.d20.teamF.ModelClasses.Account.Account;
import edu.wpi.cs3733.d20.teamF.ModelClasses.MaintenanceRequest;
import edu.wpi.cs3733.d20.teamF.ModelClasses.UIAccount;
import edu.wpi.cs3733.d20.teamF.ModelClasses.UIMaintenenceRequest;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import lombok.SneakyThrows;

public class MaintenanceRequestController implements Initializable {
  public AnchorPane anchorPane;
  public GridPane optionBar;
  public JFXButton requestServiceButton;
  public GridPane servicePane;
  public Label locationLabel;
  public Label makeLabel;
  public JFXButton submitButton;
  public JFXButton cancelButton;
  public Label typeLabel;
  public JFXComboBox<String> issueChoice;
  public Label securityRequestLabel;
  public Label OSLabel;
  public Label descLabel;
  public JFXTextField descText;
  public Label prioLabel;
  public AnchorPane checkStatusPane;
  public JFXButton update;
  public GridPane deletePane;
  public JFXTextField deleteText;
  public JFXButton backButton;
  public JFXButton checkStatusButton;
  public JFXComboBox<String> locationComboBox;
  public Label priorityLabel;
  public JFXComboBox<String> priorityComboBox;
  public JFXComboBox<String> completedComboBox;
  public JFXTextField desText;
  public JFXComboBox<String> assigneeChoice;
  public JFXTreeTableView<UIMaintenenceRequest> treeTableMaintenance;
  public AnchorPane accountTablePane;
  public JFXTreeTableView<UIAccount> treeTableAccounts;
  public JFXButton addAccountButton;
  public JFXButton deleteAccountButton;
  public JFXButton updateAccountButton;
  public Label locationLabel1;
  public JFXComboBox<String> typeComboBox;
  public JFXButton deleteButton;
  public Label completionDateLabel;
  public JFXDatePicker estCompletion;
  public JFXButton accountsButton;
  public JFXButton updateButton;
  public JFXTextField estimatedCost;

  ObservableList<UIMaintenenceRequest> mrUI = FXCollections.observableArrayList();
  ObservableList<UIAccount> acts = FXCollections.observableArrayList();
  DatabaseManager databaseManager = DatabaseManager.getManager();
  List<MaintenanceRequest> maintenanceRequests = databaseManager.getAllMaintenanceRequests();
  List<Account> accounts = databaseManager.getAllAccounts();

  public MaintenanceRequestController() throws Exception {
    try {
      maintenanceRequests = databaseManager.getAllMaintenanceRequests();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // add the different choices to the choicebox
    // Replace this with long names, linked to IDs

    UISetting uiSetting = new UISetting();

    priorityComboBox.getItems().add("Low");
    priorityComboBox.getItems().add("Medium");
    priorityComboBox.getItems().add("High");

    typeComboBox.getItems().add("Elevator");
    typeComboBox.getItems().add("Electrical");
    typeComboBox.getItems().add("Plumbing");
    typeComboBox.getItems().add("Grounds Keeping");

    assigneeChoice.getItems().add("");

    // ID
    JFXTreeTableColumn<UIMaintenenceRequest, String> ID = new JFXTreeTableColumn<>("ID");
    ID.setPrefWidth(100);
    ID.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getID();
          }
        });

    // Location column
    JFXTreeTableColumn<UIMaintenenceRequest, String> loc = new JFXTreeTableColumn<>("Location");
    loc.setPrefWidth(100);
    loc.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String>,
            ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getLocation();
          }
        });
    // desc column
    JFXTreeTableColumn<UIMaintenenceRequest, String> desc = new JFXTreeTableColumn<>("Description");
    desc.setPrefWidth(80);
    desc.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getDescription();
          }
        });
    // priority column
    JFXTreeTableColumn<UIMaintenenceRequest, String> priority =
        new JFXTreeTableColumn<>("Priority");
    priority.setPrefWidth(50);
    priority.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String>,
            ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getPriority();
          }
        });

    // Assignee choicebox
    JFXTreeTableColumn<UIMaintenenceRequest, String> column = new JFXTreeTableColumn<>("Assignee");
    column.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) ->
            param.getValue().getValue().getAssignee());
    column.setCellFactory(
        new Callback<
            TreeTableColumn<UIMaintenenceRequest, String>,
            TreeTableCell<UIMaintenenceRequest, String>>() {
          @Override
          public TreeTableCell<UIMaintenenceRequest, String> call(
              TreeTableColumn<UIMaintenenceRequest, String> param) {
            return new TextFieldTreeTableCell<>();
          }
        });
    column.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    column.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<UIMaintenenceRequest, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<UIMaintenenceRequest, String> event) {
            TreeItem<UIMaintenenceRequest> current =
                treeTableMaintenance.getTreeItem(event.getTreeTablePosition().getRow());
            current.getValue().setAssignee(new SimpleStringProperty(event.getNewValue()));
          }
        });
    ObservableList<String> completedList = FXCollections.observableArrayList();
    completedList.add("Complete");
    completedList.add("Incomplete");

    // estimated cost
    JFXTreeTableColumn<UIMaintenenceRequest, String> estCost =
        new JFXTreeTableColumn<>("Estimated Cost");
    estCost.setPrefWidth(100);
    estCost.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getEstimatedCost();
          }
        });

    // completion date
    JFXTreeTableColumn<UIMaintenenceRequest, String> CompleteDate =
        new JFXTreeTableColumn<>("Completion Date");
    CompleteDate.setPrefWidth(100);
    CompleteDate.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getDateCompleted();
          }
        });

    // date submitted
    JFXTreeTableColumn<UIMaintenenceRequest, String> date =
        new JFXTreeTableColumn<>("Date Submitted");
    date.setPrefWidth(100);
    date.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getDateSubmitted();
          }
        });
    ObservableList<String> typeList = FXCollections.observableArrayList();
    typeList.add("Admin");
    typeList.add("Staff");

    ObservableList<String> specialList = FXCollections.observableArrayList();
    specialList.add("Elevator");
    specialList.add("Electrical");
    specialList.add("Plumbing");
    specialList.add("Grounds Keeping");

    // User type
    JFXTreeTableColumn<UIAccount, String> userType = new JFXTreeTableColumn<>("User Type");
    userType.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UIAccount, String> param) ->
            param.getValue().getValue().getType());
    userType.setCellFactory(
        new Callback<TreeTableColumn<UIAccount, String>, TreeTableCell<UIAccount, String>>() {
          @Override
          public TreeTableCell<UIAccount, String> call(TreeTableColumn<UIAccount, String> param) {
            return new TextFieldTreeTableCell<>();
          }
        });
    userType.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    userType.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(typeList));
    userType.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<UIAccount, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<UIAccount, String> event) {
            TreeItem<UIAccount> current =
                treeTableAccounts.getTreeItem(event.getTreeTablePosition().getRow());
            current.getValue().setType(new SimpleStringProperty(event.getNewValue()));
          }
        });

    // estimated complete date
    JFXTreeTableColumn<UIMaintenenceRequest, String> estCompleteDate =
        new JFXTreeTableColumn<>("Estimated Completion Date");
    estCompleteDate.setPrefWidth(100);
    estCompleteDate.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getEstimatedCompletionDate();
          }
        });

    // completed
    JFXTreeTableColumn<UIMaintenenceRequest, String> completed =
        new JFXTreeTableColumn<>("Completed");
    completed.setPrefWidth(200);
    completed.setCellValueFactory(
        (JFXTreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) ->
            param.getValue().getValue().getCompleted());
    completed.setCellFactory(
        new Callback<
            TreeTableColumn<UIMaintenenceRequest, String>,
            TreeTableCell<UIMaintenenceRequest, String>>() {
          @Override
          public TreeTableCell<UIMaintenenceRequest, String> call(
              TreeTableColumn<UIMaintenenceRequest, String> param) {
            return new TextFieldTreeTableCell<>();
          }
        });
    completed.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    completed.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(completedList));
    completed.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<UIMaintenenceRequest, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<UIMaintenenceRequest, String> event) {
            TreeItem<UIMaintenenceRequest> current =
                treeTableMaintenance.getTreeItem(event.getTreeTablePosition().getRow());
            current.getValue().setCompleted(new SimpleStringProperty(event.getNewValue()));
          }
        });

    // username
    JFXTreeTableColumn<UIAccount, String> username = new JFXTreeTableColumn<>("Username");
    username.setPrefWidth(100);
    username.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIAccount, String> param) {
            return param.getValue().getValue().getUserName();
          }
        });

    // first name
    JFXTreeTableColumn<UIAccount, String> firstName = new JFXTreeTableColumn<>("First Name");
    firstName.setPrefWidth(100);
    firstName.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIAccount, String> param) {
            return param.getValue().getValue().getFirstName();
          }
        });

    // last name
    JFXTreeTableColumn<UIAccount, String> lastName = new JFXTreeTableColumn<>("Last Name");
    lastName.setPrefWidth(100);
    lastName.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIAccount, String> param) {
            return param.getValue().getValue().getLastName();
          }
        });

    // type
    JFXTreeTableColumn<UIMaintenenceRequest, String> type = new JFXTreeTableColumn<>("Type");
    type.setPrefWidth(100);
    type.setCellValueFactory(
        new Callback<>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<UIMaintenenceRequest, String> param) {
            return param.getValue().getValue().getType();
          }
        });

    // specialty
    JFXTreeTableColumn<UIAccount, String> specialty = new JFXTreeTableColumn<>("Specialty");
    specialty.setPrefWidth(100);
    specialty.setCellValueFactory(
        (TreeTableColumn.CellDataFeatures<UIAccount, String> param) ->
            param.getValue().getValue().getSpecialty());
    specialty.setCellFactory(
        new Callback<TreeTableColumn<UIAccount, String>, TreeTableCell<UIAccount, String>>() {
          @Override
          public TreeTableCell<UIAccount, String> call(TreeTableColumn<UIAccount, String> param) {
            return new TextFieldTreeTableCell<>();
          }
        });
    specialty.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    specialty.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(typeList));
    specialty.setOnEditCommit(
        new EventHandler<TreeTableColumn.CellEditEvent<UIAccount, String>>() {
          @Override
          public void handle(TreeTableColumn.CellEditEvent<UIAccount, String> event) {
            TreeItem<UIAccount> current =
                treeTableAccounts.getTreeItem(event.getTreeTablePosition().getRow());
            current.getValue().setSpecialty(new SimpleStringProperty(event.getNewValue()));
          }
        });

    // Load the database into the tableview
    if (maintenanceRequests != null) {
      for (MaintenanceRequest csr : maintenanceRequests) {
        mrUI.add(new UIMaintenenceRequest(csr));
      }
    } else {
      System.out.println("Get all maintence Requests is returning null");
    }
    if (accounts != null) {
      for (Account act : accounts) {
        acts.add(new UIAccount(act));
      }
    } else {
      System.out.println("Get all accounts is returning null");
    }

    final TreeItem<UIMaintenenceRequest> root =
        new RecursiveTreeItem<UIMaintenenceRequest>(mrUI, RecursiveTreeObject::getChildren);

    final TreeItem<UIAccount> root1 =
        new RecursiveTreeItem<UIAccount>(acts, RecursiveTreeObject::getChildren);
    // set the columns for the tableview

    treeTableMaintenance
        .getColumns()
        .setAll(
            ID,
            loc,
            column,
            priority,
            type,
            desc,
            estCost,
            estCompleteDate,
            completed,
            CompleteDate,
            date);
    treeTableAccounts.getColumns().setAll(username, firstName, lastName, userType, specialty);

    column.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    completed.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    priority.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    firstName.setCellFactory((TextFieldTreeTableCell.forTreeTableColumn()));
    lastName.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    userType.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    specialty.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

    treeTableMaintenance.setRoot(root);
    treeTableMaintenance.setEditable(true);
    treeTableMaintenance.setShowRoot(false);
    treeTableAccounts.setRoot(root1);
    treeTableAccounts.setEditable(true);
    treeTableAccounts.setShowRoot(false);
  }

  public void submit(ActionEvent actionEvent) throws Exception {
    // Get the values
    String location = locationComboBox.getValue();
    String nodeID = location.substring(location.length() - 10);
    String desc = desText.getText();
    String priority = priorityComboBox.getValue();
    String assignee = assigneeChoice.getValue();
    Double estCost = Double.parseDouble(estimatedCost.getText());
    Date estDate =
        Date.from(estCompletion.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    String type = typeComboBox.getValue();
    String complete = completedComboBox.getValue();

    System.out.println(priority);
    int priorityDB = 1;
    if (priority.equals("Low")) {
      priorityDB = 1;
    } else if (priority.equals("Medium")) {
      priorityDB = 2;
    } else if (priority.equals("High")) {
      priorityDB = 3;
    }
    LocalDateTime now = LocalDateTime.now();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    Date date = new Date(System.currentTimeMillis());
    MaintenanceRequest csRequest =
        new MaintenanceRequest(
            nodeID, desc, assignee, date, priorityDB, type, estDate, estCost, null);
    databaseManager.manipulateServiceRequest(csRequest);
    mrUI.add(new UIMaintenenceRequest(csRequest));
    treeTableMaintenance.refresh();
    locationComboBox.setValue(null);
    priorityComboBox.setValue(null);
    assigneeChoice.setValue(null);
    desText.setText("");
    estimatedCost.setText("");
    estCompletion.setValue(null);
  }

  public void cancel(ActionEvent actionEvent) {
    locationComboBox.setValue(null);
    priorityComboBox.setValue(null);
    assigneeChoice.setValue(null);
    desText.setText("");
    estimatedCost.setText("");
    estCompletion.setValue(null);
  }

  public void update(ActionEvent actionEvent) throws Exception {
    for (UIMaintenenceRequest mrui : mrUI) {
      MaintenanceRequest toUpdate = databaseManager.readMaintenanceRequest(mrui.getID().get());
      boolean isSame = mrui.equalsCSR(toUpdate);
      if (!isSame) {
        toUpdate.setAssignee(mrui.getAssignee().get());
        toUpdate.setCompleted(new Date());
        String completed = mrui.getCompleted().get();
        if (completed.equals("Complete")) {
          Date date = new Date();
          toUpdate.setDateTimeSubmitted(date);
          toUpdate.setComplete(true);
        } else if (completed.equals("Incomplete")) {
          toUpdate.setComplete(false);
        } else {
          throw new IllegalArgumentException(
              "This doesn't belong in the completed attribute: " + completed);
        }
        databaseManager.manipulateServiceRequest(toUpdate);
      }
    }
    treeTableMaintenance.refresh();
  }

  public void delete(ActionEvent actionEvent) throws Exception {
    String toDelete = deleteText.getText();
    mrUI.removeIf(transportRequest -> transportRequest.getID().get().equals(toDelete));
    deleteText.setText("");
    treeTableMaintenance.refresh();
  }

  public void request(ActionEvent actionEvent) {
    servicePane.setVisible(true);
    checkStatusPane.setVisible(false);
    accountTablePane.setVisible(false);
  }

  public void statusView(ActionEvent actionEvent) {
    servicePane.setVisible(false);
    checkStatusPane.setVisible(true);
    accountTablePane.setVisible(false);
  }

  public void accountView(MouseEvent mouseEvent) {
    servicePane.setVisible(false);
    checkStatusPane.setVisible(false);
    accountTablePane.setVisible(true);
  }

  private void resize(double width) {
    System.out.println(width);
    Font newFont = new Font(width / 50);
    locationLabel.setFont(newFont);
    makeLabel.setFont(newFont);
    typeLabel.setFont(newFont);
    OSLabel.setFont(newFont);
    descLabel.setFont(newFont);
    prioLabel.setFont(newFont);
    securityRequestLabel.setFont(new Font(width / 20));
    submitButton.setFont(newFont);
    cancelButton.setFont(newFont);
    // deleteButton.setFont(new Font(width / 50));
    update.setFont(newFont);
    backButton.setFont(newFont);
  }

  public void updateAccount(ActionEvent actionEvent) throws Exception {
    for (UIAccount acc : acts) {
      Account toUpdate = databaseManager.readAccount(acc.getUserName().get());
      boolean isSame = acc.equalsAccount(toUpdate);
      if (!isSame) {
        toUpdate.setFirstName(acc.getFirstName().get());
        toUpdate.setLastName(acc.getLastName().get());
        toUpdate.setType(Account.Type.getEnum(acc.getType().get()));
        toUpdate.setSpecialty(Account.Specialty.getEnum(acc.getSpecialty().get()));
      } else {
        throw new IllegalArgumentException("This doesn't belong in the completed attribute: ");
      }
      databaseManager.manipulateAccount(toUpdate);
    }
    treeTableAccounts.refresh();
  }
}