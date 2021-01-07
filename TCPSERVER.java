/* 9. Using TCP/IP sockets , write a client – server program to make the client send the file
 name and to make the server send back the contents of the requested file if present.

*/

//TCP SERVER PROGRAM

import java.net.*;
import java.io.*;

public class TCPSERVER {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
				ServerSocket socket=new ServerSocket(1537);
				System.out.println("SERVER READY");
				
				Socket server =socket.accept();
				System.out.println("CONNECTION ESTABLISHED");
				
				BufferedReader br=new BufferedReader( new InputStreamReader(server.getInputStream()));
				String filename=br.readLine();
				
				FileReader fr=null;
				BufferedReader b=null;
				
				DataOutputStream sendToClient = new DataOutputStream(server.getOutputStream());
				File f=new File(filename);  
				System.out.println("The entered file path => "+f);
				System.out.println(f);
				
				if(f.exists())
				{
					sendToClient.writeBytes("Yes\n");
					fr=new FileReader(filename);
					b=new BufferedReader(fr);
					String str;
					while((str=b.readLine())!=null)
					{
						sendToClient.writeBytes(str+"\n");
					}
				}
				else
					sendToClient.writeBytes("No\n");
				server.close();
				socket.close();
				sendToClient.close();
				br.close();
			}
			
		catch(Exception e) {
			System.out.println("IO: "+e.getMessage());
		}
		finally
		{
			System.out.println("\n Connection Terminated");
		}
	}
}
/*
OUTPUT :-
SERVER READY
CONNECTION ESTABLISHED
The entered file path => C:\Users\ridha\Desktop\connection.txt
C:\Users\ridha\Desktop\connection.txt

 Connection Terminated
*/