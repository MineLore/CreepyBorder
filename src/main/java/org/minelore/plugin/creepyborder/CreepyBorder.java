package org.minelore.plugin.creepyborder;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.minelore.plugin.creepyborder.component.*;
import org.minelore.plugin.creepyborder.config.*;
import org.minelore.plugin.creepyborder.config.spongepowered.*;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.BukkitColorSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.PotionEffectTypeSerializer;
import org.minelore.plugin.creepyborder.config.spongepowered.serializer.WrapperConfigSerializer;
import org.minelore.plugin.creepyborder.manager.BorderManager;
import org.minelore.plugin.creepyborder.nms.BiomeRedWaterListener_1_21_R1;
import org.minelore.plugin.creepyborder.nms.BiomeWrap_1_21_R1;
import org.minelore.plugin.creepyborder.util.BiomeData;
import org.minelore.plugin.creepyborder.util.DataOfEnableWrapper;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.awt.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    //todo: Вынести регестрацию BiomeWrapper  отдельный менеджер для уменьшения связанности
    private BiomeWrap_1_21_R1 biomeWrap;
    private final Logger LOG = getLogger();
    private ProtocolManager protocolManager;
    private MainConfig mainConfig;
    private YamlConfigurationLoader loader;
    private PaperCommandManager<CommandSourceStack> commandManager;
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
        loadCommandManager();
        loadBorderManager();
        borderManager.start();
    }

    public void loadWrapperFactory() {
        wrapperFactory = new WrapperFactory();

        wrapperFactory.<WBiomeConfig>registerWrapper(BiomeWrapper.NAME, config -> {
            BiomeData biomeData = new BiomeData(
                    config.getWaterColor(),
                    config.getSkyColor(),
                    config.getFogColor(),
                    config.getWaterFogColor()
            );
            if (biomeWrap == null) {
                biomeWrap = new BiomeWrap_1_21_R1(biomeData);
            }
            else biomeWrap.replaceBiome(biomeData);
            return new BiomeWrapper(
                this,
                new BiomeRedWaterListener_1_21_R1(
                        biomeWrap
                ),
                protocolManager);
        });
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

        loader = YamlConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .serializers(builder -> builder
                                .register(new BukkitColorSerializer())
                                .register(new PotionEffectTypeSerializer())
                                .register(WrapperConfig.class, wrapperConfigSerializer)
                        )
                )
                .nodeStyle(NodeStyle.BLOCK)
                .path(getDataFolder().toPath().resolve("config.yml"))
                .build();
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

    public void reload() throws ConfigurateException {
        CommentedConfigurationNode node = loader.load();
        mainConfig = node.get(MainConfigImpl.class);
        if (mainConfig == null) {
            mainConfig = new MainConfigImpl();
            node.set(mainConfig);
        }
        loader.save(node);
        loadWrapperFactory();
        loadBorderManager();
        borderManager.start();
    }

    public void loadCommandManager() {
        commandManager = PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.<CommandSourceStack>builder().commonPoolExecutor().build())
                .buildOnEnable(this);
        loadReloadCommand();

    }

    public void loadReloadCommand() {
        commandManager.command(commandManager
                .commandBuilder("creepyborder", "cp")
                .literal("reload", "r")
                .permission((sender) -> PermissionResult.of (sender.getSender().hasPermission(mainConfig.getReloadPermission()), Permission.of(mainConfig.getReloadPermission())))
                .handler(ctx -> {
                    CommandSender commandSender = ctx.sender().getSender();
                    try {
                        reload();
                        commandSender.sendMessage(Component.text("Config reloaded!").color(TextColor.color(Color.GREEN.getRGB())));
                    } catch (ConfigurateException e) {
                        e.printStackTrace();
                        commandSender.sendMessage(Component.text("Config has not been reloaded! Check console!").color(TextColor.color(Color.RED.getRGB())));
                    }
                }));
    }

    private void loadBorderManager() {
        if (borderManager != null) {
            borderManager.stop();
        }
        borderManager = new BorderManager(this, "world", mainConfig.getWrapperConfigs().stream().<DataOfEnableWrapper>mapMulti((wrapperConfig, consumer) -> {
            var wrapperOptional = wrapperFactory.toWrapper(wrapperConfig);
            if (wrapperOptional.isPresent()) {
                consumer.accept(new DataOfEnableWrapper(wrapperConfig.getDistToBorder(), wrapperOptional.get()));
            }
            else {
                LOG.warning("Can't create wrapper for config: " + wrapperConfig);
            }
        }).toList(), player -> player.hasPermission(mainConfig.getImmunityPermission()));
    }
}
