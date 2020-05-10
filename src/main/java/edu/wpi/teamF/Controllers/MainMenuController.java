package edu.wpi.teamF.Controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamF.App;
import edu.wpi.teamF.DatabaseManipulators.DatabaseManager;
import edu.wpi.teamF.ModelClasses.Account.Account;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MainMenuController implements Initializable {

  public AnchorPane root;
  public ImageView background;
  public AnchorPane anchorPane;
  public Label welcomeLabel;
  public JFXButton loginButton;
  public JFXButton pathfinderButton;
  public JFXButton helpButton;
  public GridPane gridPane;
  public Pane rectanglePane;
  public JFXButton serviceRequestButton;
  public Rectangle blueRectangle;
  public DatabaseManager dbm = DatabaseManager.getManager();
  public Label time;
  public Label date;

  SceneController sceneController = App.getSceneController();

  public void login(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("Login");
  }

  public void pathfinder(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("Pathfinder");
  }

  public void serviceRequest(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("ServiceRequestMain");
  }

  public void admin(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("DataManipulator");
  }

  public void help(ActionEvent actionEvent) throws IOException {
    sceneController.switchScene("HelpMain");
  }

  public void about(ActionEvent actionEvent) {}

  public void credits(ActionEvent actionEvent) {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Account.Type userLevel = dbm.getPermissions();
    if (userLevel == Account.Type.USER) {
      // set to user
    } else if (userLevel == Account.Type.STAFF) {
      // set to staff
    } else if (userLevel == Account.Type.ADMIN) {
      // set to admin perms
    }
    background.fitWidthProperty().bind(anchorPane.widthProperty());
    background.fitHeightProperty().bind(anchorPane.heightProperty());
    time();
  }

  // Time
  @FXML private Label Time;

  private int minute;
  private int hour;
  private int second;

  @FXML
  public void time() {
    Calendar calendar = new GregorianCalendar();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);

    Timeline clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  LocalTime currentTime = LocalTime.now();
                  time.setText(
                      currentTime.getHour()
                          + ": "
                          + currentTime.getMinute()
                          + ": "
                          + currentTime.getSecond());
                  date.setText(month + "/" + day + "/" + year);
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }
}
