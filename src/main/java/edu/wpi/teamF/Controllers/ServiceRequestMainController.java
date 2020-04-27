package edu.wpi.teamF.Controllers;

import edu.wpi.teamF.App;
import java.io.IOException;
import javafx.scene.input.MouseEvent;

public class ServiceRequestMainController {
  SceneController sceneController = App.getSceneController();

  public void switchToComputerService(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("ComputerServiceRequest");
  }

  public void medicineService(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("MedicineDeliveryRequest");
  }

  public void languageService(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("LanguageServiceController2");
  }

  public void sanitationRequest(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("SanitationServiceRequest");
  }

  public void mariachiService(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("MariachiRequest");
  }
}
