package packetSender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpServer implements Runnable {
    private DatagramSocket datagramSocket;
    private int packetsReceived;
    private int byteSize;
    private byte[] buffer;

    public UdpServer(int byteSize, int port) {
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        packetsReceived = 0;
        this.byteSize = byteSize;
        buffer = new byte[byteSize];
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket packet;
            try {
                packet = new DatagramPacket(buffer, byteSize);
                datagramSocket.receive(packet);
                packetsReceived++;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getPacketsReceived() {
        return packetsReceived;
    }
}
