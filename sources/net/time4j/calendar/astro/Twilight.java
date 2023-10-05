package net.time4j.calendar.astro;

/* loaded from: classes2.dex */
public enum Twilight {
    BLUE_HOUR(4.0d),
    CIVIL(6.0d),
    NAUTICAL(12.0d),
    ASTRONOMICAL(18.0d);
    
    private final transient double angle;

    Twilight(double d) {
        this.angle = d;
    }

    public double getAngle() {
        return this.angle;
    }
}
