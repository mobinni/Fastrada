package be.fastrada;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class PacketControllerTests {
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testAddPackets() throws Exception {
        String packets = "{\"raceName\":\"test\", \"startTime\" : 1394619337349,\"packets\":[{\"timestamp\":1394619337349,\"content\":\"0101AAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"}]}";
        this.mockMvc.perform(
                post("/api/packet")
                        .content(packets)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void testNullRace() throws Exception {
        Random rand = new Random();
        String packets = "{\"raceName\":\""+ rand.nextFloat() + "\", \"startTime\" : 1394619337349,\"packets\":[{\"timestamp\":1394619337349,\"content\":\"0101AAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337349,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"},{\"timestamp\":1394619337350,\"content\":\"AAAAAAAAAAAAAA==\"}]}";
        this.mockMvc.perform(
                post("/api/packet")
                        .content(packets)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

    }
}
