package DmN.ICA.vodka.impl;

import DmN.ICA.vodka.api.VodkaMod;
import DmN.ICA.vodka.json.api.JsonObjectParser;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class VodkaLoader extends DmN.ICA.vodka.VodkaLoader {
    public VodkaLoader(VodkaClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public void firstInit() {
        super.firstInit();
        try {
            Enumeration<URL> enumeration = this.getClass().getClassLoader().getResources("vodka.mod.json");
            while (enumeration.hasMoreElements()) {
                JsonObjectParser jparser = JsonObjectParser.create(new JsonParser().parse(new FileReader(enumeration.nextElement().getFile())).getAsJsonObject());
                VodkaModImpl mod = new VodkaModImpl();
                mod.id = jparser.getString("id");
                mod.version = jparser.getInt("version");
                AtomicBoolean skip = new AtomicBoolean(false);
                this.mods.stream().filter(m -> m.id().equals(mod.id)).findFirst().ifPresent(m -> skip.set(m.version() >= mod.version));
                if (skip.get())
                    continue;
                mod.type = VodkaMod.Type.valueOf(jparser.getString("type").toUpperCase());
                mod.modClass = jparser.getString("class");
                mod.name = jparser.getString("name");
                mod.description = jparser.getString("description");
                mod.authors = new ArrayList<>();
                jparser.getArrIterator("authors").forEachRemaining(e -> mod.authors.add(e.getAsString()));
                mod.highMCVersion = new MinecraftVersion(jparser.getString("highMCVersion"));
                mod.lowMCVersion = new MinecraftVersion(jparser.getString("lowMCVersion"));
                mod.loadPost = new ArrayList<>();
                jparser.getArrIterator("loadPost").forEachRemaining(e -> mod.loadPost.add(e.getAsString()));
                mod.loadPrev = new ArrayList<>();
                jparser.getArrIterator("loadPrev").forEachRemaining(e -> mod.loadPrev.add(e.getAsString()));
                mod.dependencies = new ArrayList<>();
                jparser.getArrIterator("dependencies").forEachRemaining(e -> mod.dependencies.add(e.getAsString()));
                this.mods.add(mod);
            }

            List<VodkaMod> loads = new ArrayList<>();
            for (VodkaMod mod : this.mods)
                sortForLoad(loads, mod);
            this.mods = loads;

            for (VodkaMod mod : this.mods)
                if (mod.modClass() != null && Class.forName(mod.modClass(), true, loader).isAssignableFrom(VodkaMod.FirstInitializer.class))
                    ((VodkaMod.FirstInitializer) mod.instance()).firstInit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commonInit() {
        super.commonInit();
        try {
            for (VodkaMod mod : this.mods)
                if (mod.modClass() != null && Class.forName(mod.modClass(), true, loader).isAssignableFrom(VodkaMod.CommonInitializer.class))
                    ((VodkaMod.CommonInitializer) mod.instance()).commonInit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientInit() {
        super.clientInit();
        try {
            for (VodkaMod mod : this.mods)
                if (mod.modClass() != null && Class.forName(mod.modClass(), true, loader).isAssignableFrom(VodkaMod.ClientInitializer.class))
                    ((VodkaMod.ClientInitializer) mod.instance()).clientInit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void serverInit() {
        super.serverInit();
        try {
            for (VodkaMod mod : this.mods)
                if (mod.modClass() != null && Class.forName(mod.modClass(), true, loader).isAssignableFrom(VodkaMod.ServerInitializer.class))
                    ((VodkaMod.ServerInitializer) mod.instance()).serverInit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void sortForLoad(List<VodkaMod> loads, VodkaMod last) {
        if (loads.stream().anyMatch(mod -> mod.id().equals(last.id())))
            return;
        for (String mod : last.loadPost())
            this.sortForLoad(loads, this.mods.stream().filter(m -> m.id().equals(mod)).findFirst().get());
        if (!last.loadPrev().isEmpty()) {
            int minId = loads.size() - 1;
            for (String mod : last.loadPrev())
                minId = Math.min(minId, this.mods.indexOf(this.mods.stream().filter(m -> m.id().equals(mod)).findFirst().get()));
            List<VodkaMod> tmp0 = new ArrayList<>();
            List<VodkaMod> tmp1 = new ArrayList<>();
            for (int i = 0; i < minId; i++)
                tmp0.add(loads.get(i));
            for (int i = minId; i < loads.size(); i++)
                tmp1.add(loads.get(i));
            loads.clear();
            loads.addAll(tmp0);
            loads.add(last);
            loads.addAll(tmp1);
        } else
            loads.add(last);
    }

    @Override
    public @NotNull MinecraftVersion getMCVersion() {
        return new MinecraftVersion("1.18.2");
    }

    @Override
    public @NotNull LoaderType getLoaderType() {
        return LoaderType.Fabric;
    }

    @Override
    public @NotNull EnvType getEnvironment() {
        return EnvType.valueOf(FabricLoader.getInstance().getEnvironmentType().name());
    }

    @Override
    public @NotNull Path getModsDir() {
        return new File(FabricLoader.getInstance().getGameDir().toString() + File.separator + "vodka_mods").toPath();
    }

    @Override
    public @NotNull Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
