//UDP SERVER PROGRAM


import java.io.IOException;
import java.net.*;
import java.util.*;

public class UDPServer {
	
	public static void main(String[] args)
	{
		try
		{
			DatagramSocket server = new DatagramSocket(1537);
			
			System.out.println("Enter server message:");
			Scanner sc = new Scanner(System.in);
			
			while(true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				server.receive(request);
				
				String message = sc.nextLine();
				byte[] sendMessage = message.getBytes();
				DatagramPacket reply = new DatagramPacket(sendMessage, sendMessage.length, request.getAddress(), request.getPort());
				server.send(reply);
				server.close();
				sc.close();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("IO: "+e.getMessage());
		}
		finally
		{
			System.out.println("\n Connection Terminated");
		}	}
}


/*

OUTPUT-

Enter server message:
Hello udpclient
IO: Socket closed

 Connection Terminated

*/