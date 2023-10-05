package com.amazonaws;

import android.util.Base64;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.events.TouchesHelper;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/* loaded from: classes.dex */
public class RNAWSCognitoModule extends ReactContextBaseJavaModule {
    private static final String HEX_N = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E088A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3DC2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AAAC42DAD33170D04507A33A85521ABDF1CBA64ECFB850458DBEF0A8AEA71575D060C7DB3970F85A6E1E4C7ABF5AE8CDB0933D71E8C94E04A25619DCEE3D2261AD2EE6BF12FFA06D98A0864D87602733EC86A64521F2B18177B200CBBE117577A615D6C770988C0BAD946E208E24FA074E5AB3143DB5BFCE0FD108E4B82D120A93AD2CAFFFFFFFFFFFFFFFF";
    private static final BigInteger N = new BigInteger(HEX_N, 16);
    private final ReactApplicationContext reactContext;

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "RNAWSCognito";
    }

    public RNAWSCognitoModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        this.reactContext = reactApplicationContext;
    }

    @ReactMethod(isBlockingSynchronousMethod = true)
    public String getRandomBase64(int i) throws NoSuchAlgorithmException {
        byte[] bArr = new byte[i];
        new SecureRandom().nextBytes(bArr);
        return Base64.encodeToString(bArr, 2);
    }

    @ReactMethod
    public void computeModPow(ReadableMap readableMap, Callback callback) {
        try {
            callback.invoke(null, new BigInteger(readableMap.getString(TouchesHelper.TARGET_KEY), 16).modPow(new BigInteger(readableMap.getString("value"), 16), new BigInteger(readableMap.getString("modifier"), 16)).toString(16));
        } catch (Exception e) {
            callback.invoke(e.getMessage(), null);
        }
    }

    @ReactMethod
    public void computeS(ReadableMap readableMap, Callback callback) {
        try {
            BigInteger bigInteger = new BigInteger(readableMap.getString("g"), 16);
            BigInteger bigInteger2 = new BigInteger(readableMap.getString("x"), 16);
            BigInteger bigInteger3 = new BigInteger(readableMap.getString("k"), 16);
            BigInteger bigInteger4 = new BigInteger(readableMap.getString("a"), 16);
            BigInteger bigInteger5 = new BigInteger(readableMap.getString("b"), 16);
            BigInteger add = bigInteger4.add(new BigInteger(readableMap.getString("u"), 16).multiply(bigInteger2));
            BigInteger bigInteger6 = N;
            callback.invoke(null, bigInteger5.subtract(bigInteger3.multiply(bigInteger.modPow(bigInteger2, bigInteger6))).modPow(add, bigInteger6).mod(bigInteger6).toString(16));
        } catch (Exception e) {
            callback.invoke(e.getMessage(), null);
        }
    }
}
