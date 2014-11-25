package be.fastrada.networking.sending;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import be.fastrada.networking.listening.PacketListener;

public class PacketSenderService extends Service {
    private PacketGrouper packetGrouper;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        this.packetGrouper = new PacketGrouper();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (packetGrouper.getRaceName() == null) {
            packetGrouper.setRaceName(intent.getAction());
            new Thread(packetGrouper).start();
        }else{
            final Bundle bundle = intent.getExtras();
            if (bundle != null) {
                final byte[] bytes = bundle.getByteArray(PacketListener.BUNDLE_BYTES_KEY);
                packetGrouper.add(bytes);
            }
        }

        return START_STICKY;
    }
}
