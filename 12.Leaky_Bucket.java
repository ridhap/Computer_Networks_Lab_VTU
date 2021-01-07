//12. Write a program for congestion control using leaky bucket algorithm.


import java.util.Scanner;

public class Leaky_Bucket {
	public static void main(String[] args) 
	{
		Scanner sc= new Scanner(System.in);
		int n,l,i,lct,x1,x=0;
		System.out.println("Enter the Number of packets");
		n=sc.nextInt();
		int ta[]=new int[n];
		System.out.println("Enter Arrival Times of Packets");
		for(int k=0;k<n;k++)
		{
			ta[k]=sc.nextInt();
		}
		System.out.println("Enter the Bucket size");
		l=sc.nextInt();
		System.out.println("Enter the rate of the packet time");
		i=sc.nextInt();
		//Initializing
		lct=ta[0];
		for(int k=0;k<n;k++)
		{
			
			x1=x-(ta[k]-lct);
			
			if(x1<=l)
			{
				System.out.println("Packet " + (k+1) + " confirmed");
				x=x1+i;
				lct=ta[k];
			}
			else
				System.out.println("Packet " + (k+1) + " dropped");
		}
		sc.close();
	}

}
/* OUTPUT

Enter the Number of packets
11
Enter Arrival Times of Packets
1 2 3 5 6 8 11 12 13 15 19
Enter the Bucket size
4
Enter the rate of the packet time
4
Packet 1 confirmed
Packet 2 confirmed
Packet 3 dropped
Packet 4 confirmed
Packet 5 dropped
Packet 6 dropped
Packet 7 confirmed
Packet 8 dropped
Packet 9 confirmed
Packet 10 dropped
Packet 11 confirmed


*/
