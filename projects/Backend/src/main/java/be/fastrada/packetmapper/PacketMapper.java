package be.fastrada.packetmapper;

import be.fastrada.Exception.FastradaException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PacketMapper {
    private ByteBuffer byteBuffer;
    private PacketConfiguration packetConfiguration;

    private ArrayList<Double> values;
    private ArrayList<String> types;

    public PacketMapper(PacketConfiguration packetConfiguration) {
        this.packetConfiguration = packetConfiguration;
        values = new ArrayList<Double>();
        types = new ArrayList<String>();
    }

    public JSONArray getStructure() {
        int id = this.getId();
        JSONObject packets = (JSONObject) packetConfiguration.getConfigFile().get("packets");
        JSONObject packet = (JSONObject) packets.get("" + id);

        if (packet == null) {
            return new JSONArray();
        }

        return (JSONArray) packet.get("struct");
    }

    public int getId() {
        return byteBuffer.getShort();
    }

    public boolean invokeMethod(JSONObject jo) throws FastradaException {
        if (jo == null) {
            return true;
        }

        String type = (String) jo.get("type");
        int byteSize = Integer.parseInt(jo.get("size").toString());
        double offset = Double.parseDouble(jo.get("offset").toString());
        double factor = Double.parseDouble(jo.get("factor").toString());


        types.add(type);
        switch (byteSize) {
            case 8:
                short value1 = (short) (byteBuffer.get() & 0xff);
                value1 = (short) ((value1 * factor) - offset);
                values.add((double) value1);
                break;
            case 16:
                int value2 = byteBuffer.getShort() & 0xffff;
                value2 = (int) (((double)value2 * factor) - offset);
                values.add((double) value2);
                break;
            case 32:
                long value3 = (long) (byteBuffer.getInt() & 0xffffffffL);
                value3 = (long) ((value3 * factor) - offset);
                values.add((double) value3);
                break;
        }
        return true;
    }

    public void clearArrays() {
        values.clear();
        types.clear();
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public boolean process() {
        JSONArray structure = getStructure();

        for (int i = 0; i < structure.size(); i++) {
            JSONObject jo = (JSONObject) structure.get(i);
            try {
                invokeMethod(jo);
            } catch (FastradaException e) {
                return false;
            }
        }
        return true;
    }

    public ByteBuffer getBuffer() {
        return byteBuffer;
    }

    public void setContent(byte[] content) {
        this.byteBuffer = ByteBuffer.wrap(content); // Wrap the byte array in the buffer, BIG ENDIAN!
    }
}