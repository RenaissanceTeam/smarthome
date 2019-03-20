package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils;

public class Utils {
    public static long calculateRGB(byte r, byte g, byte b) {
        return 0xFF000000 | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
    }
}
