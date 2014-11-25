package be.fastrada.controller;

import be.fastrada.model.Race;
import be.fastrada.service.PacketService;
import be.fastrada.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("api")
public class RaceController {

    @Autowired
    private RaceService raceService;
    @Autowired
    private PacketService packetService;

    @RequestMapping(value = "race", method = RequestMethod.GET)
    @ResponseBody
    public List getRaces() {
        return raceService.getAllRaces();
    }

    @RequestMapping(value = "race/{raceId}", method = RequestMethod.GET)
    @ResponseBody
    public Race getRaceById(@PathVariable(value = "raceId") String raceId) {
        return raceService.getRaceById(raceId);
    }

    @RequestMapping(value = "race/{raceId}/data", method = RequestMethod.GET)
    @ResponseBody
    public List getRaceDataById(@PathVariable(value = "raceId") String raceId) {
        return packetService.getPacketsByRaceId(raceId, "");
    }

    @RequestMapping(value = "race/{raceId}/data/{filter}", method = RequestMethod.GET)
    @ResponseBody
    public List getRaceDataById(@PathVariable(value = "raceId") String raceId, @PathVariable("filter") String filterKey) {
        return packetService.getPacketsByRaceId(raceId, filterKey);
    }
}
