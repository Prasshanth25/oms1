package androidx.core.location;

import android.location.LocationListener;
import android.os.Bundle;
import com.android.tools.r8.annotations.SynthesizedClassV2;

/* loaded from: classes.dex */
public interface LocationListenerCompat extends LocationListener {

    @SynthesizedClassV2(kind = 8, versionHash = "7a5b85d3ee2e0991ca3502602e9389a98f55c0576b887125894a7ec03823f8d3")
    /* renamed from: androidx.core.location.LocationListenerCompat$-CC */
    /* loaded from: classes.dex */
    public final /* synthetic */ class CC {
        public static void $default$onProviderDisabled(LocationListenerCompat _this, String str) {
        }

        public static void $default$onProviderEnabled(LocationListenerCompat _this, String str) {
        }

        public static void $default$onStatusChanged(LocationListenerCompat _this, String str, int i, Bundle bundle) {
        }
    }

    @Override // android.location.LocationListener
    void onProviderDisabled(String str);

    @Override // android.location.LocationListener
    void onProviderEnabled(String str);

    @Override // android.location.LocationListener
    void onStatusChanged(String str, int i, Bundle bundle);
}
