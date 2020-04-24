package edu.wpi.teamF.ModelClasses;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamF.ModelClasses.ServiceRequest.ComputerServiceRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UIComputerServiceRequest extends RecursiveTreeObject<UIComputerServiceRequest> {
  public String location;
  public String make;
  public String OS;
  public String HardwareSoftware;
  public String description;
  public String priority;
  public String assignee;
  public String dateSubmitted;
  public String completed;

  public UIComputerServiceRequest(ComputerServiceRequest csr) {
    DateFormat date = new SimpleDateFormat("yyyy-mm-dd");
    boolean completed = csr.getComplete();
    String cmp;
    if(completed){
      cmp = "T";
    }else{
      cmp = "F";
    }

    this.location = csr.getLocation().getId();
    this.make = csr.getMake();
    this.OS = csr.getOS();
    this.HardwareSoftware = csr.getHardwareSoftware();
    this.description = csr.getDescription();
    this.priority = "" + csr.getPriority();
    this.assignee = csr.getAssignee();
    this.dateSubmitted = date.format(csr.getDateTimeSubmitted());
    this.completed = cmp;
  }
}
