package be.fastrada.networking.sending;

import be.fastrada.networking.Packet;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacketGrouper implements Runnable {
    public static final int MAX = 50;

    private List<Packet> packets;
    private int amount;
    private long latestReceived;
    private String raceName;
    private long startTime;
    public static final int TIMEOUT = 900;

    public PacketGrouper() {
        this.packets = Collections.synchronizedList(new ArrayList<Packet>());
        this.startTime = System.currentTimeMillis();
    }

    public int getAmount() {
        return amount;
    }

    public void add(byte[] bytes) {
        amount++;
        latestReceived = System.currentTimeMillis();

        Packet p = new Packet(bytes, new DateTime());
        packets.add(p);
        if (amount == MAX) send();
    }

    public byte[] getContent(int i) {
        return packets.get(i).getContent();
    }

    public DateTime getTimestamp(int i) {

        return packets.get(i).getTimestamp();
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getRaceName() {
        return raceName;
    }

    @Override
    public void run() {
        while (true) {
            long timeout = System.currentTimeMillis() - latestReceived;

            if (timeout > TIMEOUT && packets.size() > 0) send();
        }
    }

    private void send() {
        final List<Packet> packetCopy = new ArrayList<Packet>(packets);
        new Thread(new PacketSender(raceName, startTime, packetCopy)).start();
        packets.clear();
        amount = 0;
    }
}
