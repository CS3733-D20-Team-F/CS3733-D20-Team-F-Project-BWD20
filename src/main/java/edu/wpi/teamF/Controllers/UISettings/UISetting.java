package edu.wpi.teamF.Controllers.UISettings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;

public class UISetting {

  private Zoom zoom = new Zoom();
  private ButtonFormatter buttonFormatter = new ButtonFormatter();
  private LocationComboBox locationComboBox = new LocationComboBox();

  public UISetting() throws Exception {}

  public void makeZoomable(ScrollPane scrollPane, StackPane stackPane, double maxScaleFactor) {
    zoom.makeZoomable(scrollPane, stackPane, maxScaleFactor);
  }

  public void setAsLocationComboBox(JFXComboBox<String> comboBox) throws Exception {

    locationComboBox.setAsLocationComboBox(comboBox);
  }

  public void setLightModeButton(JFXButton button) {
    buttonFormatter.setLightFormat(button);
  }

  public void setDarkModeButton(JFXButton button) {
    buttonFormatter.setDarkFormat(button);
  }
}
