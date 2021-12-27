package system;

import kademlia.JKademliaNode;

import java.util.ArrayList;

public class Vehicle {
    private JKademliaNode currentCoor;
    private Route route;

    public Vehicle(Route route, JKademliaNode currentCoor){
        this.route = route;
        this.currentCoor = currentCoor;
    }
}
