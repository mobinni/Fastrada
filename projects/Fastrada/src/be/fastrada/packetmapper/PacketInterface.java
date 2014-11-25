package be.fastrada.packetmapper;

public interface PacketInterface {
    public void setCurrentSpeed(int currentSpeed);

    public void setCurrentRPM(int currentRPM);

    public void setCurrentTemperature(int currentTemperature);

    public void setGear(int currentGear);

    public void setThrottlePos(int ThrottlePos);
}
