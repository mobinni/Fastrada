package be.fastrada.activities;

import android.app.Activity;
import android.os.Bundle;
import be.fastrada.R;

public class Settings extends Activity {
    // When changing these, do not forget to change in xml/xml_settings.xml as well
    public static final int DEFAULT_MAX_TEMP = 120;
    public static final int DEFAULT_MAX_SPEED = 300;
    public static final int DEFAULT_MAX_RPM = 6000;
    public static final int DEFAULT_ALARM_TEMP = 90;

    // When changing these, do not forget to change in xml/xml_settings.xml AND layout/main.xml as well
    public static final boolean DEFAULT_SHOW_GEAR = true;
    public static final boolean DEFAULT_HOLO_STYLE = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
}