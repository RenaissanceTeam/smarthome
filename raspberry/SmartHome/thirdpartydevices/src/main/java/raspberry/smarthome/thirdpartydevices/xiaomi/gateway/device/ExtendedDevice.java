package raspberry.smarthome.thirdpartydevices.xiaomi.gateway.device;

public abstract class ExtendedDevice extends Device {

    private float voltage;
    private String status = "";

    public ExtendedDevice(String sid, String type) {
        super(sid, type);
    }

    public float getVoltage() {
        return voltage;
    }

    public String getStatus() {
        return status;
    }

    void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + "\nstatus: " + status + ", voltage: " + voltage + "v";
    }
}
