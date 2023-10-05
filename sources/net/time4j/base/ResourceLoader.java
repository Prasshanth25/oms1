package net.time4j.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ServiceLoader;

/* loaded from: classes2.dex */
public abstract class ResourceLoader {
    private static final boolean ANDROID;
    private static final boolean ENFORCE_USE_OF_CLASSLOADER;
    public static final String EXTERNAL_RESOURCE_LOADER = "net.time4j.base.ResourceLoader";
    private static final ResourceLoader INSTANCE;
    public static final String USE_OF_CLASSLOADER_ONLY = "net.time4j.base.useClassloaderOnly";

    public abstract InputStream load(URI uri, boolean z);

    public abstract URI locate(String str, Class<?> cls, String str2);

    public abstract <S> Iterable<S> services(Class<S> cls);

    static {
        boolean equalsIgnoreCase = "Dalvik".equalsIgnoreCase(System.getProperty("java.vm.name"));
        ANDROID = equalsIgnoreCase;
        ENFORCE_USE_OF_CLASSLOADER = !equalsIgnoreCase && Boolean.getBoolean(USE_OF_CLASSLOADER_ONLY);
        String property = System.getProperty(EXTERNAL_RESOURCE_LOADER);
        if (property == null) {
            INSTANCE = new StdResourceLoader();
            return;
        }
        try {
            INSTANCE = (ResourceLoader) Class.forName(property).newInstance();
        } catch (Exception e) {
            throw new AssertionError("Wrong configuration of external resource loader: " + e.getMessage());
        }
    }

    public static ResourceLoader getInstance() {
        return INSTANCE;
    }

    public final InputStream load(Class<?> cls, String str, boolean z) throws IOException {
        if (ANDROID) {
            throw new FileNotFoundException(str);
        }
        URL resource = cls.getClassLoader().getResource(str);
        if (resource != null) {
            if (z) {
                URLConnection openConnection = resource.openConnection();
                openConnection.setUseCaches(false);
                openConnection.connect();
                return openConnection.getInputStream();
            }
            return resource.openStream();
        }
        throw new FileNotFoundException(str);
    }

    /* loaded from: classes2.dex */
    private static class StdResourceLoader extends ResourceLoader {
        StdResourceLoader() {
            if (ResourceLoader.ANDROID) {
                throw new IllegalStateException("The module time4j-android is not active. Check your configuration.");
            }
        }

        @Override // net.time4j.base.ResourceLoader
        public URI locate(String str, Class<?> cls, String str2) {
            String str3;
            try {
                try {
                    ProtectionDomain protectionDomain = cls.getProtectionDomain();
                    CodeSource codeSource = protectionDomain == null ? null : protectionDomain.getCodeSource();
                    if (codeSource != null) {
                        String externalForm = codeSource.getLocation().toExternalForm();
                        try {
                            if (externalForm.endsWith(".jar")) {
                                externalForm = "jar:" + externalForm + "!/";
                            }
                            str3 = externalForm + str2;
                            return new URI(str3);
                        } catch (URISyntaxException unused) {
                            System.err.println("Warning: malformed resource path = " + str3);
                            return null;
                        }
                    }
                } catch (SecurityException unused2) {
                }
            } catch (URISyntaxException unused3) {
                str3 = null;
            }
            return null;
        }

        @Override // net.time4j.base.ResourceLoader
        public InputStream load(URI uri, boolean z) {
            if (uri != null && !ResourceLoader.ENFORCE_USE_OF_CLASSLOADER) {
                try {
                    URL url = uri.toURL();
                    if (z) {
                        URLConnection openConnection = url.openConnection();
                        openConnection.setUseCaches(false);
                        openConnection.connect();
                        return openConnection.getInputStream();
                    }
                    return url.openStream();
                } catch (IOException e) {
                    if (uri.toString().contains(".repository")) {
                        PrintStream printStream = System.err;
                        printStream.println("Warning: Loading of resource " + uri + " failed (" + e.getMessage() + "). Consider setting the system property \"net.time4j.base.useClassloaderOnly\" for reducing overhead.");
                        e.printStackTrace(System.err);
                    }
                }
            }
            return null;
        }

        @Override // net.time4j.base.ResourceLoader
        public <S> Iterable<S> services(Class<S> cls) {
            return ServiceLoader.load(cls, cls.getClassLoader());
        }
    }
}
