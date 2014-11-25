package be.fastrada;

import be.fastrada.pojo.PostPacketList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by bavo on 14/03/14.
 */
public class PostPacketListTest
{
    @Test
    public void testRace()
    {
        PostPacketList list = new PostPacketList();
        list.setRaceId("1234");

        assertEquals("1234", list.getRaceId());
    }
}
