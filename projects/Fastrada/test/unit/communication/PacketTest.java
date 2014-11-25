package unit.communication;

import be.fastrada.networking.Packet;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class PacketTest
{
    @Test
    public void testPacket(){
        byte[] bytes = new byte[10];
        DateTime instant = new DateTime();

        bytes[0] = (byte) 9;
        Packet p = new Packet(bytes, instant);

        assertEquals(bytes, p.getContent());
        assertEquals(instant, p.getTimestamp());
    }
}
