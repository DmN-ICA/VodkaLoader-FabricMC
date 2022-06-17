package DmN.ICA.vodka.impl.loader;

import DmN.ICA.vodka.api.EnvType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class CacheClassLoader extends VodkaClassLoader {
    public String cacheDIr;

    public CacheClassLoader(URL[] urls, ClassLoader parent, ClassLoader knotLoader, EnvType envType, String cacheDIr) throws ReflectiveOperationException {
        super(urls, parent, knotLoader, envType);
        var cacheDirF = new File(cacheDIr);
        if (!cacheDirF.exists())
            cacheDirF.mkdirs();
        this.cacheDIr = cacheDIr.endsWith(File.separator) ? cacheDIr : cacheDIr + File.separator;
    }

    public static CacheClassLoader create(File modsDir, ClassLoader parent, ClassLoader knotLoader, EnvType envType, String cacheDIr) throws MalformedURLException, ReflectiveOperationException {
        return new CacheClassLoader(buildModsDir(modsDir), parent, knotLoader, envType, cacheDIr);
    }

    @Override
    public byte[] getBytes(String name, boolean allowFromParents) {
        try {
            return loadCachedBytes(name, () -> super.getBytes(name, allowFromParents));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] loadCachedBytes(String name, Supplier<byte[]> e) throws IOException {
        var file = new File(cacheDIr + name);

        if (file.exists()) {
            try (var stream = new FileInputStream(file)) {
                var bytes = stream.readAllBytes();
                if (bytes.length == 0)
                    return null;
                return bytes;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        var bytes = e.get();
        if (bytes == null || bytes.length == 0)
            return null;

        try (var stream = new FileOutputStream(file)) {
            stream.write(bytes);
        }

        return bytes;
    }
}
