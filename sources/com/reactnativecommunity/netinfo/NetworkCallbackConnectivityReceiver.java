package com.reactnativecommunity.netinfo;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;
import com.facebook.react.bridge.ReactApplicationContext;

/* loaded from: classes.dex */
public class NetworkCallbackConnectivityReceiver extends ConnectivityReceiver {
    private static final int DELAY_MS = 250;
    private NetworkCapabilities mCapabilities;
    private Network mNetwork;
    private final ConnectivityNetworkCallback mNetworkCallback;

    public NetworkCallbackConnectivityReceiver(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.mNetwork = null;
        this.mCapabilities = null;
        this.mNetworkCallback = new ConnectivityNetworkCallback();
    }

    @Override // com.reactnativecommunity.netinfo.ConnectivityReceiver
    public void register() {
        try {
            this.mNetwork = getConnectivityManager().getActiveNetwork();
            asyncUpdateAndSend(0);
            getConnectivityManager().registerDefaultNetworkCallback(this.mNetworkCallback);
        } catch (SecurityException unused) {
        }
    }

    @Override // com.reactnativecommunity.netinfo.ConnectivityReceiver
    public void unregister() {
        try {
            getConnectivityManager().unregisterNetworkCallback(this.mNetworkCallback);
        } catch (IllegalArgumentException | SecurityException unused) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:140:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0083  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    void updateAndSend() {
        /*
            r10 = this;
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.UNKNOWN
            android.net.Network r1 = r10.mNetwork
            android.net.NetworkCapabilities r2 = r10.mCapabilities
            r3 = 0
            r4 = 0
            if (r2 == 0) goto L9b
            r5 = 2
            boolean r5 = r2.hasTransport(r5)
            r6 = 4
            r7 = 1
            if (r5 == 0) goto L16
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.BLUETOOTH
            goto L3a
        L16:
            boolean r5 = r2.hasTransport(r4)
            if (r5 == 0) goto L1f
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.CELLULAR
            goto L3a
        L1f:
            r5 = 3
            boolean r5 = r2.hasTransport(r5)
            if (r5 == 0) goto L29
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.ETHERNET
            goto L3a
        L29:
            boolean r5 = r2.hasTransport(r7)
            if (r5 == 0) goto L32
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.WIFI
            goto L3a
        L32:
            boolean r5 = r2.hasTransport(r6)
            if (r5 == 0) goto L3a
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.VPN
        L3a:
            if (r1 == 0) goto L46
            android.net.ConnectivityManager r5 = r10.getConnectivityManager()     // Catch: java.lang.SecurityException -> L45
            android.net.NetworkInfo r5 = r5.getNetworkInfo(r1)     // Catch: java.lang.SecurityException -> L45
            goto L47
        L45:
        L46:
            r5 = r3
        L47:
            int r8 = android.os.Build.VERSION.SDK_INT
            r9 = 28
            if (r8 < r9) goto L55
            r8 = 21
            boolean r8 = r2.hasCapability(r8)
            r8 = r8 ^ r7
            goto L68
        L55:
            if (r1 == 0) goto L67
            if (r5 == 0) goto L67
            android.net.NetworkInfo$DetailedState r8 = r5.getDetailedState()
            android.net.NetworkInfo$DetailedState r9 = android.net.NetworkInfo.DetailedState.CONNECTED
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L67
            r8 = 1
            goto L68
        L67:
            r8 = 0
        L68:
            r9 = 12
            boolean r9 = r2.hasCapability(r9)
            if (r9 == 0) goto L7c
            r9 = 16
            boolean r9 = r2.hasCapability(r9)
            if (r9 == 0) goto L7c
            if (r8 != 0) goto L7c
            r8 = 1
            goto L7d
        L7c:
            r8 = 0
        L7d:
            boolean r6 = r2.hasTransport(r6)
            if (r6 == 0) goto L8d
            if (r8 == 0) goto L8e
            int r2 = r2.getLinkDownstreamBandwidthKbps()
            if (r2 == 0) goto L8e
            r4 = 1
            goto L8e
        L8d:
            r4 = r8
        L8e:
            if (r1 == 0) goto L9d
            com.reactnativecommunity.netinfo.types.ConnectionType r1 = com.reactnativecommunity.netinfo.types.ConnectionType.CELLULAR
            if (r0 != r1) goto L9d
            if (r4 == 0) goto L9d
            com.reactnativecommunity.netinfo.types.CellularGeneration r3 = com.reactnativecommunity.netinfo.types.CellularGeneration.fromNetworkInfo(r5)
            goto L9d
        L9b:
            com.reactnativecommunity.netinfo.types.ConnectionType r0 = com.reactnativecommunity.netinfo.types.ConnectionType.NONE
        L9d:
            r10.updateConnectivity(r0, r3, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.reactnativecommunity.netinfo.NetworkCallbackConnectivityReceiver.updateAndSend():void");
    }

    public void asyncUpdateAndSend(int i) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.reactnativecommunity.netinfo.NetworkCallbackConnectivityReceiver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NetworkCallbackConnectivityReceiver.this.m383xb719608a();
            }
        }, i);
    }

    /* renamed from: lambda$asyncUpdateAndSend$0$com-reactnativecommunity-netinfo-NetworkCallbackConnectivityReceiver */
    public /* synthetic */ void m383xb719608a() {
        try {
            this.mCapabilities = getConnectivityManager().getNetworkCapabilities(this.mNetwork);
            updateAndSend();
        } catch (SecurityException unused) {
        }
    }

    /* loaded from: classes.dex */
    public class ConnectivityNetworkCallback extends ConnectivityManager.NetworkCallback {
        private ConnectivityNetworkCallback() {
            NetworkCallbackConnectivityReceiver.this = r1;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver.this.asyncUpdateAndSend(250);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLosing(Network network, int i) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLost(Network network) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = null;
            NetworkCallbackConnectivityReceiver.this.mCapabilities = null;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onUnavailable() {
            NetworkCallbackConnectivityReceiver.this.mNetwork = null;
            NetworkCallbackConnectivityReceiver.this.mCapabilities = null;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            NetworkCallbackConnectivityReceiver.this.mCapabilities = networkCapabilities;
            NetworkCallbackConnectivityReceiver.this.updateAndSend();
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            if (NetworkCallbackConnectivityReceiver.this.mNetwork != null) {
                NetworkCallbackConnectivityReceiver.this.mNetwork = network;
            }
            NetworkCallbackConnectivityReceiver.this.asyncUpdateAndSend(250);
        }
    }
}
