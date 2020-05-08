package edu.wpi.teamF.Controllers.MapEditor;

import edu.wpi.teamF.ModelClasses.Edge;
import lombok.Data;

@Data
public class EdgeLine {
    Edge edge;
    String tempNode1;
    String tempNode2;



    public EdgeLine(Edge edge) {
        this.edge = edge;
        tempNode1 = edge.getNode1();
        tempNode2 = edge.getNode2();

    }

    public void setNode1(String node1ID) {
        tempNode1 = node1ID;
    }

    public void setNode2(String node2ID) {
        tempNode2 = node2ID;
    }

    public String getNode1(){
        return edge.getNode1();
    }

    public String getNode2(){
        return edge.getNode2();
    }

    public String getEdgeID() {
        return this.edge.getId();
    }
}
