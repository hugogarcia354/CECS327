import java.net.*;
import java.io.*;
import java.util.HashMap;
public class SERVER_PROGRAM{
    public static void main(String args[]){
    DatagramSocket aSocket = null;
    try{
        aSocket = new DatagramSocket(32710);
        while(true){
            byte[] buffer = new byte[1000];
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(request);
            System.out.println("Received request from: " + request.getAddress().toString());
            request.setData(pastryTable(new String(request.getData(), "UTF-8")).getBytes()); 
            DatagramPacket reply = new DatagramPacket(request.getData(),request.getLength(), request.getAddress(), request.getPort());
            aSocket.send(reply);
        }
    } catch (SocketException e){System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {System.out.println("IO: " + e.getMessage());
    } finally {if (aSocket != null){
        System.out.print("Socket failed");
        aSocket.close();}
    } 
    }

    public static String pastryRoute(HashMap<String,String> pastryRouting,String input) {
		String first = Character.toString(input.charAt(0));
		if(pastryRouting.containsKey(first)) {
			if(input.length() > 1) {
				String second = Character.toString(input.charAt(1));
				if(pastryRouting.containsKey(first+second)) {
					if(input.length()>2) {
						String third = Character.toString(input.charAt(2));
						if(pastryRouting.containsKey(first+second+third)) {
							if(input.length()>3) {
								if(pastryRouting.containsKey(input)) {
                                    String found = pastryRouting.get(input);
                                    String reply = (input+":"+found);
									return reply;
								}
								else {
									input = input.substring(0,3);
									return pastryRoute(pastryRouting,input);
								}
							}
							else {
								String found = pastryRouting.get(first+second+third);
                                String reply = (first+second+third+found);
                                return reply;
							}
						}
						else {
							input = input.substring(0,2);
							return pastryRoute(pastryRouting,input);
						}
					}
					else {
						String found = pastryRouting.get(first+second);
                        String reply = (first+second+found);
                        return reply;
					}
				}
				else {
					input = input.substring(0,1);
					return pastryRoute(pastryRouting,input);
				}
			}
			else {
				String found = pastryRouting.get(first);
                String reply = (first+found);
                return reply;
			}
        }
		return input + ":NULL";
    }
    public static String pastryTable(String args){
		args = args.trim();
		boolean valid = true;
		try{
			for (char ch : args.toCharArray()) {
				int value = Integer.parseInt(Character.toString(ch));
				if(value > 3){
					valid = false;
					break;
				}
			}
		}
		catch (Exception e){
			System.out.println("Invalid request: " + args);
			return ("INVALID REQUEST");
		}
        if(args.length() > 4 || args.length()<1 || !valid) {
            System.out.println("Invalid request: " + args);
			return ("INVALID REQUEST");
		}
		String input = args;
		HashMap< String,String> leafSet = new HashMap< String,String>();
		leafSet.put("1312","54.153.51.70");
		leafSet.put("2020", "18.219.20.177");
		leafSet.put("2101","54.177.110.97");
		leafSet.put("2102","54.219.136.134");
		
		HashMap<String,String> pastryRouting = new HashMap<String,String>();
		pastryRouting.put("0","011:52.8.111.209");
		pastryRouting.put("1","123:13.57.66.133");
		pastryRouting.put("2","022:18.218.254.202");
		pastryRouting.put("3","023:18.144.55.5");
		
		pastryRouting.put("20","22:18.218.254.202");
		pastryRouting.put("21","01:54.177.110.97");
		pastryRouting.put("22","33:52.14.38.156");
		pastryRouting.put("23","21:54.177.170.153");
		
		pastryRouting.put("200","x:NULL");
		pastryRouting.put("201","x:NULL");
		pastryRouting.put("202","2:18.218.254.202");
		pastryRouting.put("203","0:NULL");
		
		pastryRouting.put("2020","18.219.20.177");
		pastryRouting.put("2021","NULL");
		pastryRouting.put("2022","18.218.254.202");
		pastryRouting.put("2023","NULL");
		
		if(input.equals("2022")) {
            String reply = (input+":18.218.254.202");
            return reply;
		}
		else if(leafSet.containsKey(input)) {
			String found = leafSet.get(input);
            String reply = (input+":"+found);
            return reply;
		}
		else {
			return pastryRoute(pastryRouting,input);
			}
        }
}
