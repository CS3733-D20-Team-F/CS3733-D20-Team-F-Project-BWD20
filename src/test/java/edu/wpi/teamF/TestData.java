package edu.wpi.teamF;

import edu.wpi.teamF.ModelClasses.Account.*;
import edu.wpi.teamF.ModelClasses.Appointment;
import edu.wpi.teamF.ModelClasses.Edge;
import edu.wpi.teamF.ModelClasses.Node;
import edu.wpi.teamF.ModelClasses.ServiceRequest.ComputerServiceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.MaintenanceRequest;
import edu.wpi.teamF.ModelClasses.ServiceRequest.SecurityRequest;
import java.util.Date;

public class TestData {

  public String[] validEdgeIDs = {
    "nodeA_nodeH", "nodeB_nodeG", "nodeC_nodeF", "nodeD_nodeE", "nodeT_nodeT"
  };

  public String[] invalidEdgeIDs = {
    "nodeA", "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public String[] validNodeIDs = {
    "nodeA", "nodeB", "nodeC", "nodeD", "nodeE", "nodeF", "nodeG", "nodeH"
  };

  public String[] invalidNodeIDS = {
    "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public Short[] validCoordinates = {10, 20, 100, 2};

  public Short[] invalidCoordinates = {0, -100, null};

  public String[] validBuildings = {"Building A", "Building B", "Evil Headquarters", "That one"};

  public String[] invalidBuildings = {
    "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public String[] validLongNames = {
    "Intensive Care Unit",
    "Administrative offices",
    "Department of Computer Science",
    "Hallway outside everything"
  };

  public String[] invalidLongNames = {
    "",
    null,
    "Did you ever hear the Tragedy of Darth Plagueis the wise? I thought not. It’s not a story the Jedi would tell you. It’s a Sith legend. Darth Plagueis was a Dark Lord of the Sith, so powerful and so wise he could use the Force to influence the midichlorians to create life... He had such a knowledge of the dark side that he could even keep the ones he cared about from dying. The dark side of the Force is a pathway to many abilities some consider to be unnatural. He became so powerful... the only thing he was afraid of was losing his power, which eventually, of course, he did. Unfortunately, he taught his apprentice everything he knew, then his apprentice killed him in his sleep. It’s ironic he could save others from death, but not himself."
  };

  public String[] validShortNames = {"ICU", "Admin Offices", "Dept of CS", "HallOutside"};

  public String[] invalidShortNames = {
    "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public Short[] validFloors = {1, 5, 10, 3};

  public Short[] invalidFloors = {null, -3, 0};

  public Node.NodeType[] validNodeTypes = Node.NodeType.values();

  public Node.NodeType[] invalidNodeTypes = {null};

  public Node[] validNodes = {
    new Node(
        validNodeIDs[0],
        validCoordinates[0],
        validCoordinates[0],
        validBuildings[0],
        validLongNames[0],
        validShortNames[0],
        validNodeTypes[0],
        validFloors[0]),
    new Node(
        validNodeIDs[1],
        validCoordinates[1],
        validCoordinates[1],
        validBuildings[1],
        validLongNames[1],
        validShortNames[1],
        validNodeTypes[1],
        validFloors[1]),
    new Node(
        validNodeIDs[2],
        validCoordinates[2],
        validCoordinates[2],
        validBuildings[2],
        validLongNames[2],
        validShortNames[2],
        validNodeTypes[2],
        validFloors[2]),
    new Node(
        validNodeIDs[3],
        validCoordinates[3],
        validCoordinates[3],
        validBuildings[3],
        validLongNames[3],
        validShortNames[3],
        validNodeTypes[3],
        validFloors[3]),
    new Node(
        validNodeIDs[4],
        validCoordinates[0],
        validCoordinates[0],
        validBuildings[0],
        validLongNames[0],
        validShortNames[0],
        validNodeTypes[4],
        validFloors[0]),
    new Node(
        validNodeIDs[5],
        validCoordinates[1],
        validCoordinates[1],
        validBuildings[1],
        validLongNames[1],
        validShortNames[1],
        validNodeTypes[5],
        validFloors[1]),
    new Node(
        validNodeIDs[6],
        validCoordinates[2],
        validCoordinates[2],
        validBuildings[2],
        validLongNames[2],
        validShortNames[2],
        validNodeTypes[6],
        validFloors[2]),
    new Node(
        validNodeIDs[7],
        validCoordinates[3],
        validCoordinates[3],
        validBuildings[3],
        validLongNames[3],
        validShortNames[3],
        validNodeTypes[7],
        validFloors[3])
  };

  public Node[] validNodesWithEdges = validNodes;

  public Edge[] validEdges = {
    new Edge(validEdgeIDs[0], validNodeIDs[0], validNodeIDs[7]),
    new Edge(validEdgeIDs[1], validNodeIDs[1], validNodeIDs[6]),
    new Edge(validEdgeIDs[2], validNodeIDs[2], validNodeIDs[5]),
    new Edge(validEdgeIDs[3], validNodeIDs[3], validNodeIDs[4]),
    new Edge("nodeT_nodeT", "nodeT", "nodeG")
  };

  public String[] validRooms = {"White Room", "Red room", "OR", "OP"};

  public String[] validUserIDs = {"12345632", "12300220", "20120323", "dsjfe1232"};
  public String[] validPCPs = {"John", "Jack", "Jill", "Jimmy"};
  public Appointment[] validAppointments = {
    new Appointment(validNodeIDs[0], validNodes[0], validRooms[0], validUserIDs[0], validPCPs[0]),
    new Appointment(validNodeIDs[1], validNodes[1], validRooms[1], validUserIDs[1], validPCPs[1]),
    new Appointment(validNodeIDs[2], validNodes[2], validRooms[2], validUserIDs[2], validPCPs[2]),
    new Appointment(validNodeIDs[3], validNodes[3], validRooms[3], validUserIDs[3], validPCPs[3]),
  };

  public String[] validUsernames = {"This one", "TheCuddleMonster", "sjmulhern", "Snuggles"};

  public String[] invalidUsernames = {
    "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public String[] validPasswords = {
    "Areally$tr0ngPassw@rd", "aWeakerPassw0rd", "weakPassword", "password"
  };

  public String[] invalidPasswords = {
    "weakPWD", "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public String[] validNames = {"Ferdinand", "Nikolas", "Constantine", "Nero"};

  public String[] invalidNames = {
    "", null, "this string is waaaaaaaaay toooooooo loooooooong to be valid"
  };

  public String[] validEmails = {
    "sjmulhern@wpi.edu", "joeBiden@usa.gov", "putin@rus.ru", "wack@giggle.pig"
  };

  public String[] invalidEmails = {"sjmulhern", "@wpi.edu", "", null};

  public Account.Type[] validAccountTypes = Account.Type.values();

  public Account.Type[] invalidAccountTypes = null;

  public Account[] validAccounts = {
    new Admin(validNames[0], validNames[0], validEmails[0], validUsernames[0], validPasswords[0]),
    new Staff(validNames[1], validNames[1], validEmails[1], validUsernames[1], validPasswords[1]),
    new User(validNames[2], validNames[2], validEmails[2], validUsernames[2], validPasswords[2]),
    new User(validNames[3], validNames[3], validEmails[3], validUsernames[3], validPasswords[3])
  };

  public Date[] validDates = {new Date(0), new Date(10), new Date(110), new Date(1110)};
  public String[] validIDs = {"12312442", "123124134", "124134123412", "213124124"};
  public String[] validDescriptions = {"12312442", "123124134", "124134123412", "213124124"};
  public String[] validAssignees = {"Roman", "Sully", "Denver", "Charles"};

  public SecurityRequest[] validSecurityRequests = {
    new SecurityRequest(
        validIDs[0],
        validNodes[0],
        validAssignees[0],
        validDescriptions[0],
        validDates[0],
        2,
        false),
    new SecurityRequest(
        validIDs[1],
        validNodes[1],
        validAssignees[1],
        validDescriptions[1],
        validDates[1],
        2,
        true),
    new SecurityRequest(
        validIDs[2],
        validNodes[2],
        validAssignees[2],
        validDescriptions[2],
        validDates[2],
        2,
        false),
    new SecurityRequest(
        validIDs[3],
        validNodes[3],
        validAssignees[3],
        validDescriptions[3],
        validDates[3],
        2,
        true),
  };
  public MaintenanceRequest[] validMaintenanceRequests = {
    new MaintenanceRequest(
        validIDs[0],
        validNodes[0],
        validAssignees[0],
        validDescriptions[0],
        validDates[0],
        2,
        true),
    new MaintenanceRequest(
        validIDs[1],
        validNodes[1],
        validAssignees[1],
        validDescriptions[1],
        validDates[1],
        2,
        false),
    new MaintenanceRequest(
        validIDs[2],
        validNodes[2],
        validAssignees[2],
        validDescriptions[2],
        validDates[2],
        2,
        true),
    new MaintenanceRequest(
        validIDs[3],
        validNodes[3],
        validAssignees[3],
        validDescriptions[3],
        validDates[3],
        2,
        false),
  };
  public String[] validOS = {
      "Mac", "Linux", "Windows", "Newton"
  };
  public String[] validHardwareSoftware = {
      "Soft", "Hard", "Soft", "Hard"
  };
  public String[] validMake = {
      "Mac 10", "Linux 11", "Windows 12", "Newton 23"
  };


  public ComputerServiceRequest[] validComputerServiceRequests ={
      new ComputerServiceRequest(
        validIDs[0],
          validNodes[0],
          validAssignees[0],
          validDescriptions[0],
          validDates[0],
          2,
          false,
          validMake[0],
          validHardwareSoftware[0],
          validOS[0]
          ),
      new ComputerServiceRequest(
          validIDs[1],
          validNodes[1],
          validAssignees[1],
          validDescriptions[1],
          validDates[1],
          1,
          true,
          validMake[1],
          validHardwareSoftware[1],
          validOS[1]
      ),
      new ComputerServiceRequest(
          validIDs[2],
          validNodes[2],
          validAssignees[2],
          validDescriptions[2],
          validDates[2],
          2,
          false,
          validMake[2],
          validHardwareSoftware[2],
          validOS[2]
      ),
      new ComputerServiceRequest(
          validIDs[3],
          validNodes[3],
          validAssignees[3],
          validDescriptions[3],
          validDates[3],
          2,
          false,
          validMake[3],
          validHardwareSoftware[3],
          validOS[3]
      ),
  };



  public TestData() throws Exception {}
}
