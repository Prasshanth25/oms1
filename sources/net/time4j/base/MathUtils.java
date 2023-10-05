package net.time4j.base;

/* loaded from: classes2.dex */
public final class MathUtils {
    private MathUtils() {
    }

    public static int safeCast(long j) {
        if (j < -2147483648L || j > 2147483647L) {
            throw new ArithmeticException("Out of range: " + j);
        }
        return (int) j;
    }

    public static int safeAdd(int i, int i2) {
        if (i2 == 0) {
            return i;
        }
        long j = i + i2;
        if (j < -2147483648L || j > 2147483647L) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Integer overflow: (");
            sb.append(i);
            sb.append(',');
            sb.append(i2);
            sb.append(')');
            throw new ArithmeticException(sb.toString());
        }
        return (int) j;
    }

    public static long safeAdd(long j, long j2) {
        int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
        if (i == 0) {
            return j;
        }
        if (i <= 0 ? j < Long.MIN_VALUE - j2 : j > Long.MAX_VALUE - j2) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Long overflow: (");
            sb.append(j);
            sb.append(',');
            sb.append(j2);
            sb.append(')');
            throw new ArithmeticException(sb.toString());
        }
        return j + j2;
    }

    public static int safeSubtract(int i, int i2) {
        if (i2 == 0) {
            return i;
        }
        long j = i - i2;
        if (j < -2147483648L || j > 2147483647L) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Integer overflow: (");
            sb.append(i);
            sb.append(',');
            sb.append(i2);
            sb.append(')');
            throw new ArithmeticException(sb.toString());
        }
        return (int) j;
    }

    public static long safeSubtract(long j, long j2) {
        int i = (j2 > 0L ? 1 : (j2 == 0L ? 0 : -1));
        if (i == 0) {
            return j;
        }
        if (i <= 0 ? j > Long.MAX_VALUE + j2 : j < Long.MIN_VALUE + j2) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Long overflow: (");
            sb.append(j);
            sb.append(',');
            sb.append(j2);
            sb.append(')');
            throw new ArithmeticException(sb.toString());
        }
        return j - j2;
    }

    public static int safeMultiply(int i, int i2) {
        if (i2 == 1) {
            return i;
        }
        long j = i * i2;
        if (j < -2147483648L || j > 2147483647L) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Integer overflow: (");
            sb.append(i);
            sb.append(',');
            sb.append(i2);
            sb.append(')');
            throw new ArithmeticException(sb.toString());
        }
        return (int) j;
    }

    public static long safeMultiply(long j, long j2) {
        int i;
        if (j2 == 1) {
            return j;
        }
        if (j2 <= 0 ? j2 >= -1 ? !(i == 0 && j == Long.MIN_VALUE) : j <= Long.MIN_VALUE / j2 && j >= Long.MAX_VALUE / j2 : j <= Long.MAX_VALUE / j2 && j >= Long.MIN_VALUE / j2) {
            return j * j2;
        }
        StringBuilder sb = new StringBuilder(32);
        sb.append("Long overflow: (");
        sb.append(j);
        sb.append(',');
        sb.append(j2);
        sb.append(')');
        throw new ArithmeticException(sb.toString());
    }

    public static int safeNegate(int i) {
        if (i != Integer.MIN_VALUE) {
            return -i;
        }
        throw new ArithmeticException("Not negatable: " + i);
    }

    public static long safeNegate(long j) {
        if (j != Long.MIN_VALUE) {
            return -j;
        }
        throw new ArithmeticException("Not negatable: " + j);
    }

    public static int floorDivide(int i, int i2) {
        if (i >= 0) {
            return i / i2;
        }
        return ((i + 1) / i2) - 1;
    }

    public static long floorDivide(long j, int i) {
        if (j >= 0) {
            return j / i;
        }
        return ((j + 1) / i) - 1;
    }

    public static int floorModulo(int i, int i2) {
        return i - (i2 * floorDivide(i, i2));
    }

    public static int floorModulo(long j, int i) {
        return (int) (j - (i * floorDivide(j, i)));
    }
}
