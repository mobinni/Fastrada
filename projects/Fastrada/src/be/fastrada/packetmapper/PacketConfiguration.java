package be.fastrada.packetmapper;

import android.util.Log;
import be.fastrada.Exception.FastradaException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PacketConfiguration {
    private JSONObject configFile;
    private String classPath;
    private PacketInterface classObject;

    public PacketConfiguration(InputStream packetMappingPath, String classPath, PacketInterface classObject) throws FastradaException {
        this.classPath = classPath;
        this.classObject = classObject;

        // Validate
        if (this.classObject == null) {
            final String message = "ClassObject can not be null!";
            Log.e("Fastrada", message);

            throw new FastradaException(message);
        }

        try {
            this.configFile = (JSONObject) new JSONParser().parse(new InputStreamReader(packetMappingPath));
        } catch (ParseException e) {
            final String message = "Error parsing json";
            Log.e("Fastrada", message);

            throw new FastradaException(message);
        } catch (IOException e) {
            final String message = "File not found";
            Log.e("Fastrada", message);

            throw new FastradaException(message);
        }
    }

    public JSONObject getConfigFile() {

        return configFile;
    }

    public String getClassPath() {
        return classPath;
    }

    public PacketInterface getClassObject() {
        return classObject;
    }
}
