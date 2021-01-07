
/*1. Implement three nodes point – to – point network with duplex links between them. 
Set the queue size, vary the bandwidth and find the number of packetsdropped.*/



#include "ns3/core-module.h"
#include "ns3/network-module.h"
#include "ns3/internet-module.h"
#include "ns3/point-to-point-module.h"
#include "ns3/applications-module.h"
#include "ns3/traffic-control-module.h"

// Network topology
//
//       10.1.1.0       10.1.2.0
// n0 -------------- n1..........n2
//    point-to-point
//
/*     In this program we have created 3 point to point nodes n0,n1,n2. 
Node n0 has IP address 10.1.1.1 and n3 has 10.1.2.2. Node n1 has 2 interfaces(10.1.1.2 and 10.1.2.1). 
On-Off-Helper application is used to generate the traffic at source node n0 .
Packets move from n0 to n2 via n1. Acknowledgment is sent from n2 to n0 via n1.  
Details of the flow(Number of packets sent, received and dropped) can be verified by using tracemetrics(lab1.trfile). */

using namespace ns3;

NS_LOG_COMPONENT_DEFINE ("P2P-LAB1");


int main (int argc, char *argv[])
{
    std::string socketType= "ns3::UdpSocketFactory";;

    CommandLine cmd;
    cmd.Parse (argc, argv);

    NodeContainer nodes;
    nodes.Create (3);//3 point-to-point nodes are created
   
    InternetStackHelper stack;
    stack.Install (nodes); //TCP-IP layer functionality configured on all nodes
   
   //Bandwidth and delay set for the point-to-point channel.
   // Vary these parameters to see the variation in number of packets sent/received/dropped.
    PointToPointHelper p2p1;
    p2p1.SetDeviceAttribute ("DataRate", StringValue ("5Mbps"));
    p2p1.SetChannelAttribute ("Delay", StringValue ("1ms"));

    //Set the base address for the first network(nodes n0 andn1)
    Ipv4AddressHelper address;
    address.SetBase ("10.1.1.0", "255.255.255.0");
   
    NetDeviceContainer devices;
    devices = p2p1.Install (nodes.Get (0), nodes.Get (1));
    Ipv4InterfaceContainer interfaces = address.Assign (devices);
   
    //Set the base address for the second network (nodes n1 and n2)
    devices = p2p1.Install (nodes.Get (1), nodes.Get (2));
    address.SetBase ("10.1.2.0", "255.255.255.0");
    interfaces = address.Assign (devices);


    //RateErrorModel allows us to introduce errors into a Channel at a given rate.
    //Vary the error rate value to see the variation in number of packets dropped
    Ptr<RateErrorModel> em = CreateObject<RateErrorModel> ();
    //Introduce error model to drop packets
    em->SetAttribute ("ErrorRate", DoubleValue (0.00002));
    devices.Get (1)->SetAttribute ("ReceiveErrorModel", PointerValue (em));   
    
    //create routing table at all nodes
    Ipv4GlobalRoutingHelper::PopulateRoutingTables ();
    
    uint32_t payloadSize = 1448;
    OnOffHelper onoff (socketType, Ipv4Address::GetAny ());

    //Generate traffic by using OnOff application
    onoff.SetAttribute ("OnTime",  StringValue ("ns3::ConstantRandomVariable[Constant=1]"));    
    onoff.SetAttribute ("OffTime", StringValue ("ns3::ConstantRandomVariable[Constant=0]"));
    onoff.SetAttribute ("PacketSize", UintegerValue (payloadSize));     
    onoff.SetAttribute ("DataRate", StringValue ("50Mbps")); //bit/s
     
    uint16_t port = 7;

     //1. Install receiver (for packetsink) on node 2
    Address localAddress1 (InetSocketAddress (Ipv4Address::GetAny (), port));
    PacketSinkHelper packetSinkHelper1 (socketType, localAddress1);
    ApplicationContainer sinkApp1 = packetSinkHelper1.Install (nodes.Get (2));
    sinkApp1.Start (Seconds (0.0));
    sinkApp1.Stop (Seconds (10)); 

    //2. Install sender app on node 0
    ApplicationContainer apps;
    AddressValue remoteAddress (InetSocketAddress (interfaces.GetAddress (1), port));
    onoff.SetAttribute ("Remote", remoteAddress);
    apps.Add (onoff.Install (nodes.Get (0)));
    apps.Start (Seconds (1.0));
    apps.Stop (Seconds (10));

    Simulator::Stop (Seconds (10));

    //Generate trace file
    AsciiTraceHelper ascii;
    p2p1.EnableAsciiAll (ascii.CreateFileStream ("P2ptracefile.tr"));

    //Run the simulator
    Simulator::Run ();
    Simulator::Destroy ();

  
    return 0;
}

/*        To Run the program :-
          ./waf - - run scratch/Program1 - -vis            */

