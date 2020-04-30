package edu.wpi.teamF.Controllers;

import edu.wpi.teamF.App;
import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ServiceRequestMainController {
  public Label serviceRequestLabel;
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

  public void securityRequest(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("SecurityRequest");
  }

  public void switchToLaundryService(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("LaundryServiceRequest");
  }

  public void flowerRequest(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("FlowerRequestInfo");
  }

  public void maintenanceRequest(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("MaintenenceRequest");
  }

  public void transportRequest(MouseEvent mouseEvent) throws IOException {
    sceneController.switchScene("TransportRequest");
  }

  private void resize(double width) {
    System.out.println(width);
    Font newFont = new Font(width / 50);
    serviceRequestLabel.setFont(newFont);
  }
}
