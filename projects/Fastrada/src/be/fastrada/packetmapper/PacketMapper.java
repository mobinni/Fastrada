package be.fastrada.packetmapper;


import android.util.Log;
import be.fastrada.Exception.FastradaException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Class that represents a packet.
 */
public class PacketMapper {
    private ByteBuffer byteBuffer;
    private PacketConfiguration packetConfiguration;

    public PacketMapper(PacketConfiguration packetConfiguration) {
        this.packetConfiguration = packetConfiguration;
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

    public int getSize(String name) {
        for (Object o : this.getStructure()) {
            JSONObject obj = (JSONObject) o;
            if (obj.get("name").equals(name)) {
                return ((Long) obj.get("size")).intValue();
            }
        }
        return -1;
    }

    public boolean invokeMethod(JSONObject jo) throws FastradaException {
        if (jo == null) {
            return true;
        }

        String methodToInvoke = (String) jo.get("name");
        int byteSize = Integer.parseInt(jo.get("size").toString());
        double offset = Double.parseDouble(jo.get("offset").toString());
        double factor = Double.parseDouble(jo.get("factor").toString());

        try {
            Class cls = Class.forName(packetConfiguration.getClassPath());
            Object obj = packetConfiguration.getClassObject();

            for (Method m : cls.getMethods()) {
                if (m.getName().equals(methodToInvoke)) {
                    switch (byteSize) {
                        case 8:
                            short value1 = (short) (byteBuffer.get() & 0xff);

                            value1 = (short) ((value1 * factor) - offset);

                            m.invoke(obj, value1);
                            break;
                        case 16:
                            int value2 = byteBuffer.getShort() & 0xffff;

                            value2 = (int) (((double) value2 * factor) - offset);

                            m.invoke(obj, value2);
                            break;
                        case 32:
                            long value3 = (long) (byteBuffer.getInt() & 0xffffffffL);

                            value3 = (long) ((value3 * factor) - offset);

                            m.invoke(obj, value3); //kan dit niet coveren omdat dashboard geen parameter voor double heeft
                            break;
                    }
                    return true;
                }
            }

            return true;
        } catch (InvocationTargetException e) {
            final String message = "Invocation target, exception";
            Log.e("Fastrada", message);

            throw new FastradaException(message);

        } catch (IllegalAccessException e) {
            final String message = "ClassObject illegal!";
            Log.e("Fastrada", message);

            throw new FastradaException(message);

        } catch (ClassNotFoundException e) {
            final String message = "Class not found!";
            Log.e("Fastrada", message);

            throw new FastradaException(message);
        }
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