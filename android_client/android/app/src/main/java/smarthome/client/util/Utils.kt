package smarthome.client.util

object Utils {

    fun calculateRGB(r: String, g: String, b: String): Int {
        return Utils.calculateRGB(r.toInt(), g.toInt(), b.toInt())
    }

    fun calculateRGB(r: Int, g: Int, b: Int): Int {
        return r * 65536 + g * 256 + b
    }

}