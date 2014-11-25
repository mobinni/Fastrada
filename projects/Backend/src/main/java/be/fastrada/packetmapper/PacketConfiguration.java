package be.fastrada.packetmapper;

import be.fastrada.Exception.FastradaException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PacketConfiguration {
    private JSONObject configFile;

    public PacketConfiguration(InputStream packetMappingPath) throws FastradaException {
        try {
            this.configFile = (JSONObject) new JSONParser().parse(new InputStreamReader(packetMappingPath));
        } catch (ParseException e) {
            final String message = "Error parsing json";
            throw new FastradaException(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getConfigFile() {

        return configFile;
    }
}
