package org.minelore.plugin.creepyborder.manager;

import org.minelore.plugin.creepyborder.config.HandlerConfig;
import org.minelore.plugin.creepyborder.config.MainConfig;
import org.minelore.plugin.creepyborder.config.spongepowered.*;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.BukkitColorSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.PotionEffectTypeSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.WrapperConfigSerializer;
import org.minelore.plugin.creepyborder.handler.*;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 30.01.2025
 */
public class ConfigManager {
    private final YamlConfigurationLoader loader;
    private final Logger LOG;

    private MainConfig mainConfig;

    public ConfigManager(Path pathToConfig, Logger LOG) {
        this.LOG = LOG;
        WrapperConfigSerializer wrapperConfigSerializer = new WrapperConfigSerializer(Map.of(
                BiomeHandler.NAME, HBiomeConfigImpl.class,
                MagmaGrabHandler.NAME, HMagmaGrabConfigImpl.class,
                PotionEffectHandler.NAME, HPotionEffectConfigImpl.class,
                SoundHandler.NAME, HSoundConfigImpl.class,
                TimedKillHandler.NAME, WTimedKillConfigImpl.class
        ));

        loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .serializers(builder -> builder
                                .register(new BukkitColorSerializer())
                                .register(new PotionEffectTypeSerializer())
                                .register(HandlerConfig.class, wrapperConfigSerializer)
                        )
                )
                .nodeStyle(NodeStyle.BLOCK)
                .path(pathToConfig)
                .build();
        loadConfig();
    }

    public boolean loadConfig() {
        try {
            CommentedConfigurationNode node = loader.load();
            mainConfig = node.get(MainConfigImpl.class);
            if (mainConfig == null) {
                mainConfig = new MainConfigImpl();
                node.set(mainConfig);
            }
            loader.save(node);
            return true;
        } catch (ConfigurateException e) {
            LOG.warning("Failed to load configuration! Config has been default");
            e.printStackTrace();
            mainConfig = new MainConfigImpl();
            return false;
        }
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }
}
