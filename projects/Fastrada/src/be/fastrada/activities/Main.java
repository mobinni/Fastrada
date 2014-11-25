package be.fastrada.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import be.fastrada.Dashboard;
import be.fastrada.Exception.FastradaException;
import be.fastrada.R;
import be.fastrada.components.HoloCircularProgressBar;
import be.fastrada.components.Speedometer;
import be.fastrada.networking.hotspot.WifiApManager;
import be.fastrada.networking.listening.PacketListener;
import be.fastrada.networking.listening.PacketListenerService;
import be.fastrada.networking.sending.PacketSenderService;
import be.fastrada.packetmapper.PacketConfiguration;
import be.fastrada.packetmapper.PacketMapper;

import java.io.InputStream;

/**
 * Activity with all the visualized data.
 */
public class Main extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Dashboard dashboard;
    private ProgressBar rpmIndicator;
    private HoloCircularProgressBar holoSpeedMeter, holoTempMeter;
    private Speedometer tempoMeter, speedoMeter;
    private TextView tvCurrentTemp, tvCurrentSpeed, tvGear;
    private PacketConfiguration packetConfiguration;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private Intent senderServiceIntent, listenerServiceIntent;

    private PacketMapper packetMapper;
    private SharedPreferences sharedPreferences;
    public static Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        rpmIndicator = (ProgressBar) findViewById(R.id.rpmIndicator);
        holoSpeedMeter = (HoloCircularProgressBar) findViewById(R.id.speedIndicator);
        holoTempMeter = (HoloCircularProgressBar) findViewById(R.id.thermometer);
        tvCurrentTemp = (TextView) findViewById(R.id.tvTemperature);
        tvCurrentSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvGear = (TextView) findViewById(R.id.tvGear);
        tempoMeter = (Speedometer) findViewById(R.id.tempometer);
        speedoMeter = (Speedometer) findViewById(R.id.speedometer);

        initialise();
        initDashboard();
        initHandler();
        /*
         * Initialise packetConfiguration for each packet to be received
         * Make sure that dashboard is initialised ( see initialise() )
         */
        InputStream res = getResources().openRawResource(R.raw.structure);
        try {
            packetConfiguration = new PacketConfiguration(res, "be.fastrada.packetmapper.PacketInterface", dashboard);
        } catch (FastradaException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        // Init packetMapper after packetConfiguration
        packetMapper = new PacketMapper(packetConfiguration);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    private void initHandler() {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.d("mymsghandler", "message");
                final Bundle bundle = msg.getData();
                final byte[] bytes = bundle.getByteArray(PacketListener.BUNDLE_BYTES_KEY);

                packetMapper.setContent(bytes);
                packetMapper.process();

                senderServiceIntent.putExtras(bundle);
                startService(senderServiceIntent);
                Log.d("PACKETRECEIVED", bytes.toString());
            }
        };
    }

    public void initialise() {
        this.dashboard = new Dashboard(tvCurrentTemp, tvCurrentSpeed, holoTempMeter, holoSpeedMeter, rpmIndicator, speedoMeter, tempoMeter, tvGear);
        dashboard.setMaxSpeed(sharedPreferences.getInt(getString(R.string.prefs_max_speed), Settings.DEFAULT_MAX_SPEED));
        dashboard.setMaxRPM(sharedPreferences.getInt(getString(R.string.prefs_max_rpm), Settings.DEFAULT_MAX_RPM));
        dashboard.setMaxTemperature(sharedPreferences.getInt(getString(R.string.prefs_max_temp), Settings.DEFAULT_MAX_TEMP));
        dashboard.setAlarmingTemperature(sharedPreferences.getInt(getString(R.string.prefs_alarm_temp), Settings.DEFAULT_ALARM_TEMP));

        rpmIndicator.setMax(dashboard.getMaxRPM());

        this.senderServiceIntent = new Intent(this, PacketSenderService.class);
        this.listenerServiceIntent = new Intent(this, PacketListenerService.class);
        startService(listenerServiceIntent);

        if (!isHotspotRunning()) Toast.makeText(this, getString(R.string.noHotspot), Toast.LENGTH_LONG).show();
    }

    public void initDashboard() {
        holoSpeedMeter.setProgress(0.0f);
        holoTempMeter.setProgress(0.0f);
        rpmIndicator.setProgress(0);
        tempoMeter.setCurrentSpeed(0.0f);
        speedoMeter.setCurrentSpeed(0.0f);

        tvCurrentSpeed.setText(String.format("%d", 0));
        tvCurrentTemp.setText(String.format("%d", 0));
        tvGear.setText(String.format("%d", 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initHandler();
    }

    @SuppressWarnings("unused")
    public void onSettingsClick(View v) {
        final Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void onToggleClick(final View v) {
        final boolean on = ((ToggleButton) v).isChecked();
        final EditText input = new EditText(this);

        if (on) {
            new AlertDialog.Builder(Main.this)
                    .setCancelable(false)
                    .setTitle(getString(R.string.dialogTitle))
                    .setMessage(getString(R.string.dialogMessage))
                    .setView(input)
                    .setPositiveButton(getString(R.string.dialogPosButton), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String raceName = input.getText().toString();
                            if (raceName != null && !raceName.isEmpty()) {
                                senderServiceIntent.setAction(raceName);
                                startService(senderServiceIntent);
                            } else {
                                Toast.makeText(Main.this, getString(R.string.emptyRacename), Toast.LENGTH_LONG).show();
                                ((ToggleButton) v).setChecked(false);
                            }

                        }
                    }).setNegativeButton(getString(R.string.dialogNegButton), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ((ToggleButton) v).setChecked(false);
                }
            }).show();
        } else {
            stopService(senderServiceIntent);
        }
    }

    private void customVisibility() {
        final RelativeLayout rlGear = (RelativeLayout) findViewById(R.id.rlGear);
        final RelativeLayout holoSpeed = (RelativeLayout) findViewById(R.id.speedMeterHolo);
        final RelativeLayout barsSpeed = (RelativeLayout) findViewById(R.id.speedMeterBars);
        final RelativeLayout holoTemp = (RelativeLayout) findViewById(R.id.tempMeterHolo);
        final RelativeLayout barsTemp = (RelativeLayout) findViewById(R.id.tempMeterBars);

        boolean showGear = sharedPreferences.getBoolean(getString(R.string.prefs_show_gear), Settings.DEFAULT_SHOW_GEAR);
        boolean holoStyle = sharedPreferences.getBoolean(getString(R.string.prefs_holo_style), Settings.DEFAULT_HOLO_STYLE);

        rlGear.setVisibility(showGear ? View.VISIBLE : View.INVISIBLE);

        if (holoStyle) {
            holoSpeed.setVisibility(View.VISIBLE);
            holoTemp.setVisibility(View.VISIBLE);

            barsSpeed.setVisibility(View.INVISIBLE);
            barsTemp.setVisibility(View.INVISIBLE);
            rpmIndicator.setProgressDrawable(getResources().getDrawable(R.drawable.rpmindicator));
        } else {
            holoSpeed.setVisibility(View.INVISIBLE);
            holoTemp.setVisibility(View.INVISIBLE);

            barsSpeed.setVisibility(View.VISIBLE);
            barsTemp.setVisibility(View.VISIBLE);
            rpmIndicator.setProgressDrawable(getResources().getDrawable(R.drawable.rpmindicator2));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        final String alarmTempKey = getString(R.string.prefs_alarm_temp);
        final String showGearKey = getString(R.string.prefs_show_gear);
        final String holoStyleKey = getString(R.string.prefs_holo_style);
        final int maxTemp = sharedPreferences.getInt(getString(R.string.prefs_max_temp), Settings.DEFAULT_MAX_TEMP);
        final int maxSpeed = sharedPreferences.getInt(getString(R.string.prefs_max_speed), Settings.DEFAULT_MAX_SPEED);
        final int maxRpm = sharedPreferences.getInt(getString(R.string.prefs_max_rpm), Settings.DEFAULT_MAX_SPEED);
        int alarmTemp = sharedPreferences.getInt(alarmTempKey, Settings.DEFAULT_ALARM_TEMP);

        if (s.equals(alarmTempKey) && alarmTemp > maxTemp) {
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            Toast.makeText(this, getString(R.string.alarmTempTooHigh), Toast.LENGTH_LONG).show();
            alarmTemp = maxTemp;
            editor.putInt(alarmTempKey, alarmTemp);
            editor.commit();
        }
        if (s.equals(showGearKey) || s.equals(holoStyleKey)) {
            customVisibility();
        }

        dashboard.setAlarmingTemperature(alarmTemp);
        dashboard.setMaxTemperature(maxTemp);
        dashboard.setMaxSpeed(maxSpeed);
        dashboard.setMaxRPM(maxRpm);
    }

    @Override
    protected void onDestroy() {
        stopService(listenerServiceIntent);
        stopService(senderServiceIntent);
        super.onDestroy();
    }

    private boolean isHotspotRunning() {
        final WifiApManager wifiApManager = new WifiApManager(this);
        return wifiApManager.isWifiApEnabled();
    }
}
