package com.swmansion.reanimated.sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.WindowManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.swmansion.reanimated.nativeProxy.SensorSetter;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class ReanimatedSensor {
    int interval;
    ReanimatedSensorListener listener;
    Sensor sensor;
    SensorManager sensorManager;
    ReanimatedSensorType sensorType;

    public ReanimatedSensor(WeakReference<ReactApplicationContext> weakReference, ReanimatedSensorType reanimatedSensorType, int i, SensorSetter sensorSetter) {
        this.listener = new ReanimatedSensorListener(sensorSetter, i, ((WindowManager) weakReference.get().getSystemService("window")).getDefaultDisplay());
        weakReference.get();
        this.sensorManager = (SensorManager) weakReference.get().getSystemService("sensor");
        this.sensorType = reanimatedSensorType;
        if (i == -1) {
            this.interval = 2;
        } else {
            this.interval = i;
        }
    }

    public boolean initialize() {
        Sensor defaultSensor = this.sensorManager.getDefaultSensor(this.sensorType.getType());
        this.sensor = defaultSensor;
        if (defaultSensor != null) {
            this.sensorManager.registerListener(this.listener, defaultSensor, this.interval * 1000);
            return true;
        }
        return false;
    }

    public void cancel() {
        this.sensorManager.unregisterListener(this.listener, this.sensor);
    }
}
