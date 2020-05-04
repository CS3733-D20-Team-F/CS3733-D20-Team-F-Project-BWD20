package edu.wpi.teamF.DatabaseManipulators;

import edu.wpi.teamF.ModelClasses.ServiceRequest.ComputerServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.FlowerRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.LanguageServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.LaundryServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.MaintenanceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.MariachiRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.MedicineDeliveryRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.SanitationServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.SecurityRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.ServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.TransportRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServiceRequestStats {
  MaintenanceRequestFactory maintenanceRequestFactory = MaintenanceRequestFactory.getFactory();
  TransportRequestFactory transportRequestFactory = TransportRequestFactory.getFactory();
  ComputerServiceRequestFactory computerServiceRequestFactory =
      ComputerServiceRequestFactory.getFactory();
  FlowerServiceRequestFactory flowerServiceRequestFactory =
      FlowerServiceRequestFactory.getFactory();
  LanguageServiceRequestFactory languageServiceRequestFactory =
      LanguageServiceRequestFactory.getFactory();
  LaundryServiceRequestFactory laundryServiceRequestFactory =
      LaundryServiceRequestFactory.getFactory();
  MariachiRequestFactory mariachiRequestFactory = MariachiRequestFactory.getFactory();
  MedicineDeliveryRequestFactory medicineDeliveryRequestFactory =
      MedicineDeliveryRequestFactory.getFactory();
  SanitationServiceRequestFactory sanitationServiceRequestFactory =
      SanitationServiceRequestFactory.getFactory();
  SecurityRequestFactory securityRequestFactory = SecurityRequestFactory.getFactory();
  // outputs all of the statistics of a maintenance request
  public void downloadStatistics(Path path) {
    MaintenanceRequestStats(path);
    TransportRequestStats(path);
    ComputerRequestStats(path);
    FlowerRequestStats(path);
    LanguageRequestStats(path);
    LaundryRequestStats(path);
    MariachiRequestStats(path);
    MedicineRequestStats(path);
    SanitationRequestStats(path);
    SecurityRequestStats(path);
  }

  public void MaintenanceRequestStats(Path path) {
    List<MaintenanceRequest> maintenanceRequestList =
        maintenanceRequestFactory.getAllMaintenanceRequests();
    if (maintenanceRequestList.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv");
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("MaintenanceStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getMaintenanceEmployeeNumbersCSV(maintenanceRequestList);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getMaintenanceLocationNumbersCSV(maintenanceRequestList);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("Average time to complete request,");
        bw.write(CalculateAverageMaintenanceTimeCSV(maintenanceRequestList));
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getMaintenanceEmployeeNumbersCSV(
      List<MaintenanceRequest> maintenanceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (MaintenanceRequest m : maintenanceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getMaintenanceLocationNumbersCSV(
      List<MaintenanceRequest> maintenanceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (MaintenanceRequest m : maintenanceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private String CalculateAverageMaintenanceTimeCSV(List<MaintenanceRequest> maintenanceRequest) {
    String total = "";
    Long timeDifference = (long) 0;
    int numOfRequests = 0;
    for (MaintenanceRequest m : maintenanceRequest) {
      if (m.getTimeCompleted() != null) {
        timeDifference = m.getTimeCompleted().getTime() - m.getDateTimeSubmitted().getTime();
        numOfRequests++;
      }
    }
    if (timeDifference == 0) {
      total = "Your average time was 0";
    } else {
      timeDifference = timeDifference / (long) numOfRequests / 1000;
      total = timeDifference / 3600 + " hours";
      timeDifference = timeDifference % 3600;
      total = total + " " + timeDifference / 60 + " Minutes";
    }
    return total;
  }

  public ArrayList<String> getMaintenanceEmployeeNumbersGraphs(
      List<MaintenanceRequest> maintenanceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (MaintenanceRequest m : maintenanceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getMaintenanceLocationNumbersGraphs(
      List<MaintenanceRequest> maintenanceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (MaintenanceRequest m : maintenanceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  private String CalculateAverageMaintenanceTimeGraphs(
      List<MaintenanceRequest> maintenanceRequest) {
    String total = "";
    Long timeDifference = (long) 0;
    int numOfRequests = 0;
    for (MaintenanceRequest m : maintenanceRequest) {
      if (m.getTimeCompleted() != null) {
        timeDifference = m.getTimeCompleted().getTime() - m.getDateTimeSubmitted().getTime();
        numOfRequests++;
      }
    }
    if (timeDifference == 0) {
      total = "Your average time was 0";
    } else {
      total = "" + timeDifference / 60;
    }
    return total;
  }

  public void TransportRequestStats(Path path) {
    List<TransportRequest> transportRequests = transportRequestFactory.getAllTransportRequests();
    if (transportRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("TransportStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getTransportEmployeeNumbers(transportRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("Start Locations");
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getTransportLocationNumbers(transportRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("End Locations");
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getDestinationNumbers(transportRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("Average time to complete request,");
        bw.write(CalculateAverageTransportTime(transportRequests));
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getTransportEmployeeNumbers(List<TransportRequest> transportRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getTransportLocationNumbers(List<TransportRequest> transportRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getDestinationNumbers(List<TransportRequest> transportRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      nodeNum.add(m.getDestination().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }
    return csvStyled;
  }

  private String CalculateAverageTransportTime(List<TransportRequest> transportRequests) {
    String total = "";
    Long timeDifference = (long) 0;
    int numOfRequests = 0;
    for (TransportRequest m : transportRequests) {
      if (m.getDateTimeCompleted() != null) {
        timeDifference = m.getDateTimeCompleted().getTime() - m.getDateTimeSubmitted().getTime();
        numOfRequests++;
      }
    }
    if (timeDifference == 0) {
      total = "Your average time was 0";
    } else {
      timeDifference = timeDifference / (long) numOfRequests / 1000;
      total = timeDifference / 3600 + " hours";
      timeDifference = timeDifference % 3600;
      total = total + " " + timeDifference / 60 + " Minutes";
    }
    return total;
  }

  public ArrayList<String> getTransportEmployeeNumbersGraphs(
      List<TransportRequest> transportRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getTransportLocationNumbersGraph(
      List<TransportRequest> transportRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getDestinationNumbersGraph(List<TransportRequest> transportRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (TransportRequest m : transportRequests) {
      nodeNum.add(m.getDestination().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }
    return csvStyled;
  }

  public String CalculateAverageTransportTimeGraph(List<TransportRequest> transportRequests) {
    String total = "";
    Long timeDifference = (long) 0;
    int numOfRequests = 0;
    for (TransportRequest m : transportRequests) {
      if (m.getDateTimeCompleted() != null) {
        timeDifference = m.getDateTimeCompleted().getTime() - m.getDateTimeSubmitted().getTime();
        numOfRequests++;
      }
    }
    if (timeDifference == 0) {
      total = "Your average time was 0";
    } else {
      total = "" + timeDifference / 60;
    }
    return total;
  }

  public void ComputerRequestStats(Path path) {
    List<ComputerServiceRequest> computerServiceRequestList =
        computerServiceRequestFactory.getAllComputerRequests();
    if (computerServiceRequestList.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("ComputerStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getComputerEmployeeNumbersCSV(computerServiceRequestList);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getComputerLocationNumbersCSV(computerServiceRequestList);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getComputerEmployeeNumbersCSV(
      List<ComputerServiceRequest> computerServiceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ComputerServiceRequest m : computerServiceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getComputerLocationNumbersCSV(
      List<ComputerServiceRequest> computerServiceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : computerServiceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getComputerEmployeeNumbersGraphs(
      List<ComputerServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getComputerLocationNumbersGraphs(
      List<ComputerServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void FlowerRequestStats(Path path) {
    List<FlowerRequest> flowerRequests = flowerServiceRequestFactory.getAllFlowerRequests();
    if (flowerRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("FlowerStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getFlowerEmployeeNumbersCSV(flowerRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getFlowerLocationNumbersCSV(flowerRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getFlowerEmployeeNumbersCSV(List<FlowerRequest> flowerRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : flowerRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getFlowerLocationNumbersCSV(List<FlowerRequest> flowerRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : flowerRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getFlowerEmployeeNumbersGraphs(List<FlowerRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getFlowerLocationNumbersGraphs(List<FlowerRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void LanguageRequestStats(Path path) {
    List<LanguageServiceRequest> serviceRequests =
        languageServiceRequestFactory.getAllLanguageRequests();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("LanguageStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getLanguageEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getLanguageLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getLanguageEmployeeNumbersCSV(
      List<LanguageServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getLanguageLocationNumbersCSV(
      List<LanguageServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getLanguageEmployeeNumbersGraphs(
      List<LanguageServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getLanguageLocationNumbersGraphs(
      List<LanguageServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void LaundryRequestStats(Path path) {
    List<LaundryServiceRequest> serviceRequests =
        laundryServiceRequestFactory.getAllLaundryRequests();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("LaundryStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getLaundryEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getLaundryLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getLaundryEmployeeNumbersCSV(
      List<LaundryServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getLaundryLocationNumbersCSV(
      List<LaundryServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getLaundryEmployeeNumbersGraphs(
      List<LaundryServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getLaundryLocationNumbersGraphs(
      List<LaundryServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void MariachiRequestStats(Path path) {
    List<MariachiRequest> serviceRequests = mariachiRequestFactory.getAllMariachiRequest();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("MariachiStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getMariachiEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getMariachiLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getMariachiEmployeeNumbersCSV(List<MariachiRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getMariachiLocationNumbersCSV(List<MariachiRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getMariachiEmployeeNumbersGraphs(List<MariachiRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getMariachiLocationNumbersGraphs(List<MariachiRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void MedicineRequestStats(Path path) {
    List<MedicineDeliveryRequest> serviceRequests =
        medicineDeliveryRequestFactory.getAllMedicineDeliveryRequests();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("MedicineStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getMedicineEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getMedicineLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getMedicineEmployeeNumbersCSV(
      List<MedicineDeliveryRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getMedicineLocationNumbersCSV(
      List<MedicineDeliveryRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getMedicineEmployeeNumbersGraphs(
      List<MedicineDeliveryRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getMedicineLocationNumbersGraphs(
      List<MedicineDeliveryRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void SanitationRequestStats(Path path) {
    List<SanitationServiceRequest> serviceRequests =
        sanitationServiceRequestFactory.getAllSanitationRequests();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("SanitationStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getSanitationEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getSanitationLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getSanitationEmployeeNumbersCSV(
      List<SanitationServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getSanitationLocationNumbersCSV(
      List<SanitationServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getSanitationEmployeeNumbersGraphs(
      List<SanitationServiceRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getSanitationLocationNumbersGraphs(
      List<SanitationServiceRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public void SecurityRequestStats(Path path) {
    List<SecurityRequest> serviceRequests = securityRequestFactory.getAllSecurityRequest();
    if (serviceRequests.size() != 0) {
      try (FileWriter fw = new FileWriter(path.toString() + "/ServiceRequestReport.csv", true);
          BufferedWriter bw = new BufferedWriter(fw); ) {
        bw.write("SecurityStats");
        bw.newLine();
        bw.write("EmployeeName,NumberOfRequestsAssigned");
        ArrayList<String> stats = new ArrayList<String>();
        stats = getSecurityEmployeeNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
        bw.write("NodeID,NumberOfRequestsAtLocation");
        stats = getSecurityLocationNumbersCSV(serviceRequests);
        for (String s : stats) {
          bw.newLine();
          bw.write(s);
        }
        bw.newLine();
      } catch (IOException e) {
        System.out.println(e.getMessage() + "" + e.getClass());
      }
    }
  }

  private ArrayList<String> getSecurityEmployeeNumbersCSV(List<SecurityRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  private ArrayList<String> getSecurityLocationNumbersCSV(List<SecurityRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey() + "," + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getSecurityEmployeeNumbersGraphs(List<SecurityRequest> serviceRequests) {
    ArrayList<String> employeeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      employeeNum.add(m.getAssignee());
    }
    Map<String, Long> frequency =
        employeeNum.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }

  public ArrayList<String> getSecurityLocationNumbersGraphs(List<SecurityRequest> serviceRequests) {

    ArrayList<String> nodeNum = new ArrayList<String>();
    ArrayList<String> csvStyled = new ArrayList<String>();
    for (ServiceRequest m : serviceRequests) {
      nodeNum.add(m.getLocation().getId());
    }
    Map<String, Long> frequency =
        nodeNum.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    for (Map.Entry<String, Long> entry : frequency.entrySet()) {
      if (entry.getValue() > 0) {
        csvStyled.add(entry.getKey());
        csvStyled.add("" + entry.getValue());
      }
    }

    return csvStyled;
  }
}
