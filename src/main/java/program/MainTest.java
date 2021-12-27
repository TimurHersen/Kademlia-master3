package program;

import javafx.geometry.HPos;
import javafx.scene.paint.Color;
import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.KadContent;
import kademlia.dht.KademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;
import system.Route;
import system.Vehicle;


import java.io.IOException;
import java.util.ArrayList;

public class MainTest {

    private static int identifierSize = 4;

    private static ArrayList<JKademliaNode> nodeList = new ArrayList<>();
    private static ArrayList<DHTContentImpl> contentList = new ArrayList<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ContentNotFoundException {
        createNodesByCoor();
        bootstrapNodes();
        Thread.sleep(5000);
        startJourney("x=0y=2");




    }
    public static void createNodes() throws IOException, ClassNotFoundException, InterruptedException {
        for(int i = 0; i < Math.pow(2, identifierSize); i ++){
            String ownerId = String.valueOf(i);
            JKademliaNode currentNode = new JKademliaNode(ownerId, new KademliaId(), 1222 + i);
            nodeList.add(currentNode);
            Thread.sleep(600);




        }
        System.out.println(nodeList.size() + " Nodes created");

    }
    public static void createNodesByCoor() throws IOException, ContentNotFoundException {
        int portNumber = 1220;
        for(int x = 1; x < identifierSize+1; x++){
            for(int y = 0; y < identifierSize; y++){

                //Creating a String variable from coordinates
                String ownerId = "x=" + Integer.toString(x) + "y=" + Integer.toString(y);
                String data = "EMPTY";

                //Creating the node
                JKademliaNode currentNode = new JKademliaNode(ownerId, new KademliaId(), portNumber);

                //Creating and adding content to the node
                DHTContentImpl c = new DHTContentImpl(currentNode.getOwnerId(), data);
                currentNode.put(c);
                currentNode.refresh();
                contentList.add(c);


                nodeList.add(currentNode);


                portNumber += 20;


            }
        }
        System.out.println(nodeList.size() + " Nodes created");
    }
    public static void bootstrapNodes() throws IOException, InterruptedException {

        for(int i = 0; i < nodeList.size(); i++){
            System.out.println("Working on node: " + i);
            for(int x = 0; x < identifierSize; x++){
                int result = (int) Math.pow(2, x);
                int ownerIdInt = i;
                int XOR = (ownerIdInt^result);
                System.out.println("Strapping: " + XOR);
                nodeList.get(XOR).bootstrap(nodeList.get(i).getNode());
                Thread.sleep(500);






            }

        }

    }
    public static void startJourney(String startingPoint) throws IOException, ClassNotFoundException {
        for(DHTContentImpl c : contentList){
            GetParameter gp = new GetParameter(c);            // Lets look for content by key
            gp.setType(DHTContentImpl.TYPE);                  // We also only want content of this type
            gp.setOwnerId(c.getOwnerId());
            System.out.println("Content found:" + gp );

            //Räkna ut nästkommande steg som ska tas
            //Kontaktar den noden, kollar vilket content som finns i EMPTY/FULL


        }




    }





    public static void lookForCoordinates(String coordinates) throws ContentNotFoundException, IOException {
        for(JKademliaNode node : nodeList){
            GetParameter gp = new GetParameter(node.getNode().getNodeId(), DHTContentImpl.TYPE);
            gp.setOwnerId(node.getOwnerId());
            KademliaStorageEntry conte = node.get(gp);
            if(gp.getOwnerId().equalsIgnoreCase(coordinates)){
                System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
                System.out.println("Content Metadata: " + conte.getContentMetadata());
            }


        }



    }
    public static void printList(){
        for(JKademliaNode node : nodeList){
            System.out.println(node.getOwnerId());
            for(int i = 0; i < node.getRoutingTable().getAllNodes().size(); i++){
                System.out.println(node.getRoutingTable().getAllNodes().get(i));

            }

        }
    }
    public ArrayList<JKademliaNode> returnList(){
        return nodeList;
    }

    /*for(int x = 1; x < 4+1; x++){
            for(int y = 0; y < 4; y++){

                //Creating a String variable from coordinates
                String data = "x=" + Integer.toString(x) + "y=" + Integer.toString(y) + ":EMPTY";

                //Creating and adding content to the node
                DHTContentImpl c = new DHTContentImpl(nodeList.get(nodeToGet).getOwnerId(), data);
                nodeList.get(nodeToGet).put(c);
                contentList.add(c);
                GetParameter gp = new GetParameter(c.getKey(), DHTContentImpl.TYPE);
                gp.setType(DHTContentImpl.TYPE);
                gp.setOwnerId(c.getOwnerId());
                KademliaStorageEntry conte = nodeList.get(nodeToGet).get(gp);

                System.out.println("Content Found: " + new DHTContentImpl().fromSerializedForm(conte.getContent()));
                System.out.println("Content Metadata: " + conte.getContentMetadata());

                nodeToGet++;
            }
        }*/

}
