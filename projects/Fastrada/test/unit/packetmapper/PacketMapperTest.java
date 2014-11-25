package unit.packetmapper;

import be.fastrada.Dashboard;
import be.fastrada.Exception.FastradaException;
import be.fastrada.packetmapper.PacketMapper;
import be.fastrada.packetmapper.PacketConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class PacketMapperTest {
    private byte[] byteArray1 = { 0x01, 0x0, 0x00, 0x28, 0x00, 0x28, 0x00, 0x28, 0x00, 0x28 };
    private PacketMapper packetMapper;
    private PacketMapper packetMapper2;
    private PacketConfiguration configuration;


    public PacketMapperTest() throws FileNotFoundException, FastradaException {
        configuration = new PacketConfiguration(new FileInputStream(new File("res/raw/structure.json")), "be.fastrada.Dashboard", new Dashboard());
        packetMapper = new PacketMapper(configuration);
        packetMapper.setContent(byteArray1);
        packetMapper2 = new PacketMapper(configuration);
        packetMapper2.setContent(byteArray1);
    }


    @Test
    public void createPacket() {
        assertEquals(byteArray1, packetMapper.getBuffer().array());
    }


    @Test
    public void getId() {
        assertEquals(256, packetMapper.getId());
    }


    @Test
    public void getConfiguration() {
        assertFalse(configuration.getConfigFile() == null);
        assertTrue(configuration.getConfigFile() != null);
    }


    @Test
    public void getStructure()  {
        assertEquals(3, packetMapper.getStructure().size());
    }


    @Test
    public void getSizeByMethod() {
        assertEquals(-1, packetMapper.getSize("setCurrentSpeed"));
        assertEquals(16, packetMapper2.getSize("setCurrentRPM"));
        assertEquals(-1, packetMapper2.getSize("setCurrentTemperature"));

    }


    @Test
    public void invokeMethod1() throws FastradaException {
        int vehicleSpeed = 60;
        int maxRPM = 7000;
        double maxSpeed = 150.00;

        JSONArray arr = packetMapper.getStructure();

        JSONObject jsonObject = (JSONObject) arr.get(0);

        assertEquals(true, packetMapper.invokeMethod(jsonObject));
    }


    /*
    @Test
    public void processPacket() {
        assertTrue(packetMapper.process());
    }
    */


    @Test
    public void invokeNullMethod() throws FileNotFoundException, FastradaException {
        PacketMapper packetMapper = new PacketMapper(configuration);
        packetMapper.setContent(byteArray1);
        assertTrue(packetMapper.invokeMethod(null));
    }

}