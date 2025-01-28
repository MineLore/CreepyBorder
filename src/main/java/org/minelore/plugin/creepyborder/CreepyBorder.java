package org.minelore.plugin.creepyborder;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.minelore.plugin.creepyborder.component.*;
import org.minelore.plugin.creepyborder.config.*;
import org.minelore.plugin.creepyborder.config.spongepowered.*;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.BukkitColorSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.PotionEffectTypeSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.WrapperConfigSerializer;
import org.minelore.plugin.creepyborder.manager.BorderManager;
import org.minelore.plugin.creepyborder.nms.BiomeRedWaterListener_1_21_R1;
import org.minelore.plugin.creepyborder.util.DataOfEnableWrapper;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.Map;
import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    private final Logger LOG = getLogger();
    private ProtocolManager protocolManager;
    private MainConfig mainConfig;
    private WrapperFactory wrapperFactory;
    private BorderManager borderManager;

    @Override
    public void onDisable() {
        super.onDisable();
        LOG.info("CreepyBorder plugin is disabled! Development by TheDiVaZo. For MineLoreSMP");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        LOG.info("CreepyBorder plugin is enabled! Development by TheDiVaZo. For MineLoreSMP");
        protocolManager = ProtocolLibrary.getProtocolManager();
        loadWrapperFactory();
        loadConfig();
        loadBorderManager();
        borderManager.start();
    }

    public void loadWrapperFactory() {
        wrapperFactory = new WrapperFactory();

        wrapperFactory.<WBiomeConfig>registerWrapper(BiomeWrapper.NAME, config -> new BiomeWrapper(
                this,
                new BiomeRedWaterListener_1_21_R1(
                        config.getWaterColor(),
                        config.getSkyColor(),
                        config.getFogColor(),
                        config.getWaterFogColor()
                ),
                protocolManager));
        wrapperFactory.<WMagmaGrabConfig>registerWrapper(MagmaGrabWrapper.NAME, config -> new MagmaGrabWrapper(this, config.getVectorLength()));
        wrapperFactory.<WPotionEffectConfig>registerWrapper(PotionEffectWrapper.NAME, config -> new PotionEffectWrapper(this, config.getEffectDatas()));
        wrapperFactory.<WSoundConfig>registerWrapper(SoundWrapper.NAME, config -> new SoundWrapper(this, config.getSounds(), config.getPitch(), config.getVolume(), config.getPeriodInTick()));
        wrapperFactory.<WTimedKillConfig>registerWrapper(TimedKillWrapper.NAME, config -> new TimedKillWrapper(this, config.getTickToKill()));
    }

    public void loadConfig() {
        WrapperConfigSerializer wrapperConfigSerializer = new WrapperConfigSerializer(Map.of(
                BiomeWrapper.NAME, WBiomeConfigImpl.class,
                MagmaGrabWrapper.NAME, WMagmaGrabConfigImpl.class,
                PotionEffectWrapper.NAME, WPotionEffectConfigImpl.class,
                SoundWrapper.NAME, WSoundConfigImpl.class,
                TimedKillWrapper.NAME, WTimedKillConfigImpl.class
        ));

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .serializers(builder -> builder
                                .register(new BukkitColorSerializer())
                                .register(new PotionEffectTypeSerializer())
                                .register(WrapperConfig.class, wrapperConfigSerializer)
                        )
                )
                .path(getDataFolder().toPath().resolve("config.yml"))
                .build();
        loader.createNode();
        try {
            CommentedConfigurationNode node = loader.load();
            mainConfig = node.get(MainConfigImpl.class);
            if (mainConfig == null) {
                mainConfig = new MainConfigImpl();
                node.set(mainConfig);
            }
            loader.save(node);
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadBorderManager() {
        borderManager = new BorderManager(this, "world", mainConfig.getWrapperConfigs().stream().<DataOfEnableWrapper>mapMulti((wrapperConfig, consumer) -> {
            var wrapperOptional = wrapperFactory.toWrapper(wrapperConfig);
            if (wrapperOptional.isPresent()) {
                consumer.accept(new DataOfEnableWrapper(wrapperConfig.getDistToBorder(), wrapperOptional.get()));
            }
            else {
                LOG.warning("Can't create wrapper for config: " + wrapperConfig);
            }
        }).toList(), player -> false);
    }
}
