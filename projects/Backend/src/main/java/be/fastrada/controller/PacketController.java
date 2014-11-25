package be.fastrada.controller;

import be.fastrada.Exception.FastradaException;
import be.fastrada.model.Race;
import be.fastrada.packetmapper.PacketConfiguration;
import be.fastrada.packetmapper.PacketMapper;
import be.fastrada.pojo.Packet;
import be.fastrada.pojo.PostPacketList;
import be.fastrada.service.PacketService;
import be.fastrada.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("api")
public class PacketController {

    @Autowired
    private PacketService packetService;
    @Autowired
    private RaceService raceService;

    @RequestMapping(value = "packet", method = RequestMethod.POST)
    @ResponseBody
    public String addPacket(@RequestBody PostPacketList packetList) {
        PacketConfiguration configuration = null;
        try {
            configuration = new PacketConfiguration(this.getClass().getResourceAsStream("/structure.json"));
        } catch (FastradaException e) {
            e.printStackTrace();
        }

        PacketMapper packetMapper = new PacketMapper(configuration);

        Race race = raceService.getRaceByNameAndTime(packetList.getRaceName(), packetList.getStartTime());
        if (race == null) {
            raceService.addRace(packetList.getRaceName(), packetList.getStartTime());
            race = raceService.getRaceByNameAndTime(packetList.getRaceName(), packetList.getStartTime());
        }

        for (Packet p : packetList.getPackets()) {
            packetMapper.clearArrays();
            packetMapper.setContent(p.getContent());
            packetMapper.process();

            ArrayList<Double> values = packetMapper.getValues();
            ArrayList<String> types = packetMapper.getTypes();

            for (int i = 0; i < values.size(); i++) {
                be.fastrada.model.Packet packet = new be.fastrada.model.Packet(values.get(i), p.getTimestamp(), race.getId(), types.get(i));
                packetService.addPacket(packet);
            }

        }

        return "Created " + packetList.getPackets().size() + " packets.";
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
