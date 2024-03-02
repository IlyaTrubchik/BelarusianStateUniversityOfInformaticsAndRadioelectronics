import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

public class Main {
    public static void scanNetwork(long startip,long endip,long hostIP) throws IOException {
        int timeout =1000;
        System.out.println("Subnet address:"+getIpString(startip));
        for(long i = startip+1;i<=endip;i++)
        {
            if(i != hostIP && InetAddress.getByName(getIpString(i)).isReachable(timeout))
            {
                System.out.print('\r'+"Name:"+ InetAddress.getByName(getIpString(i)).getCanonicalHostName());
                Process pArp =Runtime.getRuntime().exec("arp -a "+getIpString(i));
                InputStream inCmd = pArp.getInputStream();
                BufferedReader out =new BufferedReader(new InputStreamReader(inCmd,"Cp866"));
                String line;
                int count  = 0;
                while ((line = out.readLine ()) != null) {
                    if (line.endsWith("ий")) {
                        int index = line.indexOf('-');
                        StringBuilder mac = new StringBuilder();
                        for (int k = index - 2; k <= index + 14; k++) {
                            mac.append(line.charAt(k));
                        }
                        System.out.print("\n"+"MAC Address: " + mac.toString());
                        System.out.println("\n");
                    }
                }
            }
            System.out.print('\r'+"["+(i-startip-1)+"/"+(endip-(startip+1))+"]");
        }
    }
    public static void main(String[] args){
        ArrayList<netInterface> netInterfaces = new ArrayList<>();
       try {
           Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
           while (interfaces.hasMoreElements()) {
               NetworkInterface iFace = interfaces.nextElement();
               for (InterfaceAddress address : iFace.getInterfaceAddresses()) {
                   if (address.getAddress().isSiteLocalAddress()) netInterfaces.add(new netInterface(iFace, address));
               }
           }
           printInterfaces(netInterfaces);
           System.out.println("Choose intefrace for scanning:");
           Scanner scanner = new Scanner(System.in);
           int index = scanner.nextInt();
           index--;
           if (index < netInterfaces.size() && index >= 0) {
               System.out.println("Host Name:" + InetAddress.getLocalHost().getHostName());
               System.out.println("Choosen interface:" + netInterfaces.get(index).iFace().getDisplayName());
               printMac(netInterfaces.get(index).iFace().getHardwareAddress());
               byte[] ip = netInterfaces.get(index).address().getAddress().getAddress();
               short prefix = netInterfaces.get(index).address().getNetworkPrefixLength();
               long mask = (0xFFFFFFFF << (32 - prefix));
               long startip = Integer.toUnsignedLong(getIpNumber(ip)) & mask;
               long endip = startip | (Integer.toUnsignedLong(0xFFFFFFFF) >> prefix);
               scanNetwork(startip, endip, Integer.toUnsignedLong(getIpNumber(InetAddress.getLocalHost().getAddress())));
           } else {
               System.out.println("Choosen uncorrect interface");
           }
       }
       catch (IOException e){
            System.out.println("IO error!");
       }
    }
    public static void printInterfaces(ArrayList<netInterface> interfaces) {
        System.out.println("NetworkInterfaces:");
        try {
            for(int i =0;i<interfaces.size();i++)
            {
                System.out.println((i+1)+"."+"Name:"+ interfaces.get(i).iFace().getDisplayName());
                printMac(interfaces.get(i).iFace().getHardwareAddress());
            }
        } catch (SocketException e)
        {
            System.out.println("I/O error occurs");
        }

    }
    //Какие протоколы виндовс используются для обноружения узлов сети
    public static int getIpNumber(byte[] ip)
    {
        int ipnum = 0;
        for(int i = 0;i<3;i++)
        {
            ipnum = (ipnum+ Byte.toUnsignedInt(ip[i]))<<8;

        }
        ipnum = ipnum + Byte.toUnsignedInt(ip[3]);
        return ipnum;
    }
    public static String getIpString(long ip)
    {
        StringBuilder sb = new StringBuilder();
        sb.append((ip & 0xFF000000)>>24);
        sb.append('.');
        sb.append((ip & 0x00FF0000)>>16);
        sb.append('.');
        sb.append((ip & 0x0000FF00)>>8);
        sb.append('.');
        sb.append(ip& 0x000000FF);
        return  sb.toString();
    }
    public static void printMac(byte[] mac)
    {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < mac.length; j++) {
            sb.append(String.format("%02X%s", mac[j], (j < mac.length - 1) ? "-" : ""));
        }
        System.out.println("MAC:" + sb.toString());
    }

}
record netInterface (NetworkInterface iFace,InterfaceAddress address){};