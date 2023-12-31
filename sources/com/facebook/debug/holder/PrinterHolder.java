package com.facebook.debug.holder;

/* loaded from: classes.dex */
public class PrinterHolder {
    private static Printer sPrinter = NoopPrinter.INSTANCE;

    public static void setPrinter(Printer printer) {
        if (printer == null) {
            sPrinter = NoopPrinter.INSTANCE;
        } else {
            sPrinter = printer;
        }
    }

    public static Printer getPrinter() {
        return sPrinter;
    }
}
