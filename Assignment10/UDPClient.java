import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.ArrayList;
public class UDPClient{
    public DatagramSocket aSocket = null;
    public String pastry;
    public String ip;
    public ArrayList<String> previousID = new ArrayList<String>();

    public String randomGenerator(){
        String rand = "";
        Random r = new Random();
        int result = 0;
        for(int i = 0; i < 4; i++){
            result = r.nextInt(3);
            rand = rand + Integer.toString(result);
        }
        return rand;
    }

    public boolean validateID(String replyID){
        boolean valid = true;
        for (char ch : replyID.toCharArray()) {
            int value = Integer.parseInt(Character.toString(ch));
            if(value > 3){
                valid = false;
                break;
            }
        }
        return valid;
    }

    public String requestIP(String pastry, String ip){
        try {
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(3000);
            byte [] m = pastry.getBytes();
            InetAddress aHost = InetAddress.getByName(ip);
            int serverPort = 32710;
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            String replyString = new String(reply.getData());
            System.out.println("Reply: " + replyString);
            return replyString;
            } catch (SocketException e){return ("Socket: " + e.getMessage());
            } catch (IOException e){return ("IO: " + e.getMessage());
            } finally { if(aSocket != null) aSocket.close();}
    }
    
    public static int route(){
        UDPClient client = new UDPClient();
        client.pastry = client.randomGenerator();
        client.previousID.add(client.pastry) ;
        client.ip = "18.218.254.202";
        boolean found = false;
        int count = 0;
        String replyID = "2022";
        String replyIP = "";
        while(found == false){
            System.out.println("Requested Pastry ID: " + client.pastry);
            System.out.println("Checking in Pastry ID: "+ replyID);
            String reply = client.requestIP(client.pastry,client.ip);
            count++;
            replyID = reply.substring(0, 4);
            replyID = replyID.trim();
            replyIP = reply.substring(5);
            replyIP = replyIP.replace(":","");
            replyIP = replyIP.trim();
            replyIP = replyIP.toLowerCase();
            System.out.println("Reply ip: " +replyIP );
            if(replyID.equals(client.pastry)){
                System.out.println("Found ID");
                found = true;
            } else if(replyID.equals("NULL") || replyIP.equals("null")){
                System.out.println("No routing found");
                found = true;
            } else if(client.previousID.contains(replyID)){
                System.out.println("Stuck in a loop!");
                return -1;
            } else if(replyIP.equals("eceive timed out")){
                System.out.println("Server Not Found!");
                return -1;
            }
             else{
                client.ip = replyIP;
            }
            client.previousID.add(replyID);
        }
        System.out.println("Number of hops:" + count);
        return count;
    } 
    public static void main(String args[]){
        int runs = 1000;
        int hops[] = new int[runs];
        for(int i = 0; i < runs; i++){
            int count = route();
            if(count < 0 ){
                i--;
            }
            else{
                hops[i] = count;
            }
        }
        for(int i = 0; i < runs; i++){
            System.out.print(hops[i] + " ");
        }
    }
}