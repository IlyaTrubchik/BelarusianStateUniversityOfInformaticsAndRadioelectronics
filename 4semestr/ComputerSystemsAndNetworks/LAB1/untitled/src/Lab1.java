import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public class Lab1 {
    public static void main(String[] args) {
        Enumeration<NetworkInterface> networkInterfaces;
        ArrayList<NetInterface> adaptersList = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        try {
            //get the list of all network interfaces
            networkInterfaces = NetworkInterface.getNetworkInterfaces();

            //loop for retrieved network interfaces
            for (Iterator<NetworkInterface> it = networkInterfaces.asIterator(); it.hasNext(); ) {
                NetworkInterface e = it.next();

                //loop for network interface ip addresses
                for (InterfaceAddress address : e.getInterfaceAddresses()) {
                    //choose only non-virtual private IPv4 local addresses
                    if (address.getAddress().isSiteLocalAddress()) adaptersList.add(new NetInterface(e, address));
                }

            }

            int networkAdapters = 0;
            byte[] mac;

            //print local host name and MAC
            System.out.println("Local host name: "+InetAddress.getLocalHost().getHostName());

            //Show list of appropriate network interfaces
            for (NetInterface item : adaptersList) {
                System.out.print(++networkAdapters + ") " + item.networkInterface().getDisplayName() + ";\t");

                //print mac address
                printMAC(item.networkInterface().getHardwareAddress());
            }

            //working with selected interface
            if (networkAdapters != 0) {
                System.out.print("Choose network interface: ");
                if (in.hasNextInt()) {
                    int interfaceNumber = in.nextInt();
                    if (interfaceNumber > 0 && interfaceNumber <= networkAdapters) {
                        System.out.print("\nInterface " + interfaceNumber + "(" + adaptersList.get(interfaceNumber - 1).networkInterface().getDisplayName() + ")" + " was chosen ");
                        printMAC(adaptersList.get(interfaceNumber - 1).networkInterface().getHardwareAddress());

                        NetInterface netInterface = adaptersList.get(interfaceNumber - 1);
                        checkAllAddresses(netInterface);

                    } else System.out.println("Incorrect network interface number!");
                } else System.out.println("Not an integer!");
            }
            in.close();
        } catch (IOException e) {
            System.out.print("I/O Error occurred :(");
        }
    }

    public static void printMAC(byte[] mac){
        System.out.print("MAC: ");
        for (int i = 0; i < 6; i++) System.out.printf("%02x%s", mac[i], (i == 5) ? " " : "-");
        System.out.println();
    }

    public static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static byte[] toByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public static void checkAllAddresses(NetInterface netInterface) throws IOException {
        byte[] ip_address = netInterface.interfaceAddress().getAddress().getAddress();
        int mask = (1 << 31) >> (netInterface.interfaceAddress().getNetworkPrefixLength() - 1); //get subnet mask as int
        int ms_timeout;
        long maxAddressValue = ~mask;
        InetAddress inetAddressByIp;
        LinkedList<String> notReachableToCheck = new LinkedList<>();

        Scanner in = new Scanner(System.in);

        ip_address = toByteArray(fromByteArray(ip_address) & mask);//subnet address as array of bytes
        System.out.print("Subnet address: "); for (int i = 0;i<ip_address.length; i++) System.out.print((0xFF & ip_address[i]) + ((i == ip_address.length - 1) ? "":"."));
        System.out.print("\nPing timeout: ");
        if (!in.hasNextInt()) return;
        ms_timeout = in.nextInt();

        System.out.println("Available nodes: ");
        for (long i = 1; i < maxAddressValue; i++) {
            ip_address = toByteArray(fromByteArray(ip_address) + 1);
            inetAddressByIp = InetAddress.getByAddress(ip_address);

            if (inetAddressByIp.isReachable(ms_timeout)) System.out.print("Reachable");//'\r'+getMACSusingARP(inetAddressByIp.getHostAddress()));
            else notReachableToCheck.add(inetAddressByIp.getHostAddress());

            updateProgress(i,maxAddressValue-1);
        }

        //print all founded devices
        String temp_address;
        System.out.println("\nAnother arp cache data: ");
        for (String deviceInformation: notReachableToCheck) {

            System.out.print(temp_address = getMACSusingARP(deviceInformation));
            if (temp_address.equals("")) Runtime.getRuntime().exec("echo wasd121212 | sudo -S -k arp -d "+deviceInformation);
        }
    }
    public static void updateProgress(long current, long max){
        final int width = 50;
        int i = 0;

        System.out.print('\r');
        for (i = 0; i<(int)((double)current/max*width);i++) System.out.print("#");
        for (; i<width;i++) System.out.print(".");
        System.out.print("["+current+"/"+max+"]");
        if (current == max) System.out.print("\rPinging finished!\n");
    }
    public static String getMACSusingARP(String HostAddress) throws IOException{
        String command = "arp -a "+HostAddress;
        Scanner virtualCMD = new Scanner(Runtime.getRuntime().exec(command).getInputStream());
        String[] fields = new String[4];
        String result="";

        for (int i = 0; i<4 && virtualCMD.hasNext();i++) fields[i] = virtualCMD.next();
        if (fields[3].length() == 17) {
            if (fields[0].length() != 1) result = result.concat(fields[0]);
            result = result.concat(fields[1]).concat(" ").concat(fields[3]).concat("\n");
        }
        return result;
    }
}
record NetInterface(NetworkInterface networkInterface, InterfaceAddress interfaceAddress){}
