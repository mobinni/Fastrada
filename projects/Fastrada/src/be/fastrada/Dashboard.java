package be.fastrada;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;
import be.fastrada.components.HoloCircularProgressBar;
import be.fastrada.components.Speedometer;
import be.fastrada.packetmapper.PacketInterface;

import java.io.Serializable;

/**
 * Class that represents the dashboard.
 */
public class Dashboard implements Serializable, PacketInterface {
    private int maxSpeed;
    private int maxTemperature;
    private int maxRPM;
    private int alarmingTemperature;
    private int currentTemperature;

    private TextView tvCurrentTemp, tvCurrentSpeed, tvGear;
    private HoloCircularProgressBar holoTempMeter, holoSpeedMeter;
    private Speedometer tempoMeter, speedoMeter;
    private ProgressBar rpmIndicator;
    private Animation blinkAnimation;
    private boolean effectOn;

    public Dashboard() {
    }

    public Dashboard(TextView tvCurrentTemp, TextView tvCurrentSpeed, HoloCircularProgressBar holoTempMeter, HoloCircularProgressBar holoSpeedMeter, ProgressBar rpmIndicator, Speedometer speedoMeter, Speedometer tempoMeter, TextView tvGear) {
        this();
        this.blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
        blinkAnimation.setDuration(800);
        blinkAnimation.setStartOffset(20);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        blinkAnimation.setRepeatCount(Animation.INFINITE);

        this.tvCurrentTemp = tvCurrentTemp;
        this.tvCurrentSpeed = tvCurrentSpeed;
        this.holoTempMeter = holoTempMeter;
        this.holoSpeedMeter = holoSpeedMeter;
        this.rpmIndicator = rpmIndicator;
        this.tempoMeter = tempoMeter;
        this.speedoMeter = speedoMeter;
        this.tvGear = tvGear;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMaxRPM(int maxRPM) {
        this.maxRPM = maxRPM;
    }

    public void setAlarmingTemperature(int alarmingTemperature) {
        this.alarmingTemperature = alarmingTemperature;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMaxRPM() {
        return maxRPM;
    }

    public int getAlarmingTemperature() {
        return alarmingTemperature;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentSpeed(int currentSpeed) {

        if (holoSpeedMeter != null) {
            holoSpeedMeter.setProgress(((float) currentSpeed / (float) getMaxSpeed()));
            speedoMeter.onSpeedChanged(currentSpeed);
        }
        if (tvCurrentSpeed != null && currentSpeed <= maxSpeed) {
            tvCurrentSpeed.setText(String.format("%d", currentSpeed));
        } else if (tvCurrentSpeed != null && currentSpeed > maxSpeed) {
            tvCurrentSpeed.setText(String.format("%d", maxSpeed));
        }
    }

    public void setCurrentRPM(int currentRPM) {
        if (rpmIndicator != null && currentRPM <= maxRPM) {
            rpmIndicator.setProgress(currentRPM);
        } else if (rpmIndicator != null && currentRPM > maxRPM) {
            rpmIndicator.setProgress(maxRPM);
        }
    }

    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;

        if (holoTempMeter != null) {
            holoTempMeter.setProgress(((float) currentTemperature / (float) getMaxSpeed()));
            tempoMeter.onSpeedChanged(currentTemperature);
        }
        if (tvCurrentTemp != null && currentTemperature <= maxTemperature) {
            tvCurrentTemp.setText(String.format("%d", currentTemperature));
        } else if (tvCurrentTemp != null && currentTemperature > maxTemperature) {
            tvCurrentTemp.setText(String.format("%d", maxTemperature));
        }

        checkForExceedingTemp();
    }

    public void checkForExceedingTemp() {
        if (getCurrentTemperature() >= getAlarmingTemperature() && !effectOn) {
            effectOn = true;
            holoTempMeter.startAnimation(blinkAnimation);
            tempoMeter.startAnimation(blinkAnimation);
        } else if (getCurrentTemperature() < getAlarmingTemperature()) {
            effectOn = false;
            holoTempMeter.clearAnimation();
            tempoMeter.clearAnimation();
        }
    }


    public void setThrottlePos(int throttle) {
        //to be implemented
    }

    public void setGear(int currentGear) {
        tvGear.setText(String.format("%d", currentGear));
    }
}
