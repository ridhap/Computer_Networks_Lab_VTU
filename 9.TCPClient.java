/* 9. Using TCP/IP sockets , write a client – server program to make the client send the file
 name and to make the server send back the contents of the requested file if present.

*/

 //TCP CLIENT PROGRAM
 

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TCPClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			
			Socket client=new Socket("localhost",1537);
			
			Scanner sc=new Scanner(System.in);
			System.out.println("Enter the full path of the the file to be displayed");
			String filename=sc.next();
			
			DataOutputStream sendToServer = new DataOutputStream(client.getOutputStream());
			sendToServer.writeBytes(filename+"\n");
			
			BufferedReader br = new BufferedReader( new InputStreamReader(client.getInputStream()));
			String str = br.readLine();
			if(str.equals("Yes"))
			{
				while((str=br.readLine())!=null)
					System.out.println(str);
			}
			else
			{
				System.out.println("File not found");
			}
			client.close();
			sc.close();
			br.close();
			sendToServer.close();
		}
		catch(IOException e)
		{
			System.out.println("IO: "+e.getMessage());
		}
		finally
		{
			System.out.println("\n Connection Terminated");
		}
	}
		
}

/*
OUTPUT:-

Enter the full path of the the file to be displayed
C:\Users\ridha\Desktop\connection.txt
hey hey!!
The TCP connection establishment was a succesful trial!

 Connection Terminated
*/
