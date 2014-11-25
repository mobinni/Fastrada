package be.fastrada.networking.listening;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.SocketException;

public class PacketListenerService extends Service {
    private PacketListener packetListener;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        try {
            this.packetListener = new PacketListener();
        } catch (SocketException e) {
            Log.d("[PacketListenerService]", e.getMessage());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(packetListener).start();
        return START_STICKY;
    }
}
