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
    public String cacheDir;

    public CacheClassLoader(URL[] urls, ClassLoader parent, ClassLoader knotLoader, EnvType envType, String cacheDir) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        super(urls, parent, knotLoader, envType);

        var cacheDirF = new File(cacheDir);
        if (!cacheDirF.exists())
            cacheDirF.mkdirs();
        this.cacheDir = cacheDir.endsWith(File.separator) ? cacheDir : cacheDir + File.separator;
    }

    public static CacheClassLoader create(File modsDir, ClassLoader parent, ClassLoader knotLoader, EnvType envType, String cacheDir) throws MalformedURLException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        return new CacheClassLoader(buildModsDir(modsDir), parent, knotLoader, envType, cacheDir);
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
        var file = new File(this.cacheDir + name);

        if (file.exists()) {
            try (var stream = new FileInputStream(file)) {
                byte[] bytes = stream.readAllBytes();
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
