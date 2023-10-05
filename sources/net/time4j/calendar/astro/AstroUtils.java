package net.time4j.calendar.astro;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AstroUtils {
    AstroUtils() {
    }

    public static double gmst(double d) {
        double floor = Math.floor(d);
        double d2 = (d - 51544.5d) / 36525.0d;
        double d3 = ((((((floor - 51544.5d) / 36525.0d) * 8640184.812866d) + 24110.54841d) + (((d - floor) * 86400.0d) * 1.0027379093d)) + (((0.093104d - (6.2E-6d * d2)) * d2) * d2)) / 86400.0d;
        return (d3 - Math.floor(d3)) * 2.0d * 3.141592653589793d;
    }

    public static double getRefraction(double d) {
        return (1.02d / Math.tan(Math.toRadians((10.3d / (5.11d + d)) + d))) + 0.0019279d;
    }

    public static double refractionFactorOfStdAtmosphere(int i) {
        return Math.pow(1.0d - ((i * 0.0065d) / 288.15d), 4.255d);
    }

    public static double toRange_0_360(double d) {
        while (Double.compare(0.0d, d) > 0) {
            d += 360.0d;
        }
        while (Double.compare(d, 360.0d) >= 0) {
            d -= 360.0d;
        }
        return d;
    }

    public static int hashCode(double d) {
        long doubleToLongBits = Double.doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }
}
