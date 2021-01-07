
/*3. Implement an Ethernet LAN using n nodes and set multiple traffic
  nodes and plot congestion window for different source/destination.


Networktopology
n0  n1  n2  n3 
|   |   |   |
===================== 
CSMA channel with base IP 10.1.1.0
Source node–n0 sink node -n1

In this program we have created 4 CSMA nodes n0, n1, n2 and n3 with
 IP addresses 10.1.1.1, 10.1.1.2, 10.1.1.3 and 10.1.1.4 respectively.
 Data transmission is simulated between nodes n0 and n1 .
 Once the cwnd values are generated, they are exported to ".dat" file and congestion graph is plot using gnuplot.

*/

#include "ns3/core-module.h"
#include "ns3/network-module.h"
#include "ns3/internet-module.h"
#include "ns3/applications-module.h"
#include "ns3/csma-module.h"
#include "ns3/network-application-helper.h"

using namespace ns3;

NS_LOG_COMPONENT_DEFINE ("3rd Lab Program");

int 
main (int argc, char *argv[])
{
  CommandLine cmd;
  cmd.Parse (argc, argv);
  
  NS_LOG_INFO ("Create nodes.");
  NodeContainer nodes;
  nodes.Create (4);//4 csma nodes are created

  CsmaHelper csma;
  csma.SetChannelAttribute ("DataRate", StringValue ("5Mbps"));
  csma.SetChannelAttribute ("Delay", TimeValue (MilliSeconds (0.0001)));

  NetDeviceContainer devices;
  devices = csma.Install (nodes);

    //RateErrorModel allows us to introduce errors into a Channel at a given rate.  
  Ptr<RateErrorModel> em = CreateObject<RateErrorModel> ();
  em->SetAttribute ("ErrorRate", DoubleValue (0.00001));
  devices.Get (1)->SetAttribute ("ReceiveErrorModel", PointerValue (em));

  InternetStackHelper stack;
  stack.Install (nodes);

  Ipv4AddressHelper address;
  address.SetBase ("10.1.1.0", "255.255.255.0");
  Ipv4InterfaceContainer interfaces = address.Assign (devices);

  uint16_t sinkPort = 8080;

  Address sinkAddress (InetSocketAddress (interfaces.GetAddress (1), sinkPort));

  //PacketSink Application is used on the destination node to receiveTCP
  //connections and data. Creates objects in an abstract way and associates
  //type-id along with the object.
  PacketSinkHelper packetSinkHelper ("ns3::TcpSocketFactory", InetSocketAddress (Ipv4Address::GetAny (), sinkPort));

  //Install sinkapp on node 1
  ApplicationContainer sinkApps = packetSinkHelper.Install (nodes.Get (1));
  sinkApps.Start (Seconds (0.));
  sinkApps.Stop (Seconds (20.));

  //next two lines of code will create the socket and connect the trace source.
  Ptr<Socket> ns3TcpSocket = Socket::CreateSocket (nodes.Get (0), TcpSocketFactory::GetTypeId ());
  ns3TcpSocket->TraceConnectWithoutContext ("CongestionWindow", MakeCallback (&CwndChange));

  //creates an Object of type NetworkApplication (Class present innetwork-application-helper.h)
  Ptr<NetworkApplication> app = CreateObject<NetworkApplication> ();

  /*Next statement tells the Application
  1. what Socket to use, 
  2.what address to connect to,
  3. how much data to send at each send event,
  4. how many send events to generate and
  5. The rate at which to produce data from those events.*/
  app->Setup (ns3TcpSocket, sinkAddress, 1040, 1000, DataRate ("50Mbps"));
  nodes.Get (0)->AddApplication (app);
  app->SetStartTime (Seconds (1.));
  app->SetStopTime (Seconds (20.));

  //Displays packet drop msg
  devices.Get (1)->TraceConnectWithoutContext ("PhyRxDrop", MakeCallback (&RxDrop));

  AsciiTraceHelper ascii;
  csma.EnableAsciiAll (ascii.CreateFileStream ("3lan.tr"));

  Simulator::Stop (Seconds (20));
  Simulator::Run ();
  Simulator::Destroy ();

  return 0;
}

/* TO RUN THE PROGRAM USE --
./waf - - run scratch/Program3 - -vis

Redirect the output to a file called cwnd.dat
(TYPE IN TERMINAL THE BELOW STATEMENTS)
./waf --run scratch/Program3 > cwnd.dat 2>&1 Now run gnuplot
gnuplot> set terminal png size 640,480 gnuplot> set output "cwnd.png"
gnuplot> plot "cwnd.dat" using 1:2 title 'Congestion Window' with linespointsgnuplot> exit

*/