package packetSender;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;



public class PacketSender {

    private InetAddress adres;
    private DatagramSocket socket;
    private int port;
    private int sendPackets;

    public PacketSender(String host, int port) {
        this.port = port;
        try {
            adres = InetAddress.getByName(host);
            socket = new DatagramSocket();
        } catch (UnknownHostException e) {
            throw new Error("unknown host");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9000;
        String url = "192.168.43.1";

        PacketSender sender = new PacketSender(url, port);

        sender.runSimulator();
    }

    public void sendByte(byte[] bytes) {
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, adres, port);
        try {
            socket.send(packet);
            Thread.sleep(50);
        } catch (IOException e) {
            throw new Error("IOException");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendPackets++;
    }


    public int getSendPackets() {
        return sendPackets;
    }

    public int runSimulator() {

        int linesRead = 0;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("src/resources/CANData"));
            String line = br.readLine();
            line = line.replaceAll("\t", "");

            while (!line.equals("") || line != null) {
                linesRead++;
                System.out.println(line);
                byte[] bytes = hexStringToByteArray(line);
                this.sendByte(bytes);
                line = br.readLine();
                if (line == null) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(linesRead);

        return linesRead;
    }

    /*
    public static byte[] hexStringToByteArray(String s) {
        int byteLength = 2; // A byte is 2 chars long
        byte[] array = new byte[s.length() / byteLength];

        for (int i = 0; i < array.length; i ++) {
            int startIndex = i * byteLength;
            int endIndex = startIndex + byteLength;

            String subString = s.substring(startIndex, endIndex);

            try {
                // Set to short first, else we can't check what the value is
                short value = Short.parseShort(subString, 16);

                // If value is smaller then send it directly parsed as byte
                if (value < 256) {
                    int b = Byte.parseByte(subString, 16);
                    array[i] = Byte.parseByte(subString, 16);
                } else {
                    // Else we need to set it to negative - cap of byte
                    // Example, we want to send 129, this is bigger then 128, so we send -1
                    // Binary: 129 (unsigned) == 10000001 == -1 (signed)
                    // @todo: Temporary solution, we need to be able to send integers, shorts, ...
                    int i1 = Integer.parseInt(subString, 16);

                }

                System.out.println();

            } catch (NumberFormatException e) {
                // Overflow? (0x80 --> 128, cap decimal byte signed)

            }
        }

        return array;
    } */

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}