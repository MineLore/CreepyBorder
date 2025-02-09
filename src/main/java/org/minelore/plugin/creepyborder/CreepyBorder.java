package org.minelore.plugin.creepyborder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.thedivazo.libs.dvzconfig.core.config.ConfigContainer;
import me.thedivazo.libs.dvzconfig.core.config.ConfigLoader;
import me.thedivazo.libs.dvzconfig.core.manager.ConfigManager;
import me.thedivazo.libs.dvzconfig.core.manager.ConfigManagerImpl;
import me.thedivazo.libs.dvzconfig.core.serializer.HierarchyClassSerializer;
import me.thedivazo.libs.dvzconfig.paper.serializer.PotionEffectTypeSerializer;
import me.thedivazo.libs.dvzconfig.spigot.serializer.BukkitColorSerializer;
import me.thedivazo.libs.dvzconfig.yaml.YamlConfigLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.minelore.plugin.creepyborder.border.Border;
import org.minelore.plugin.creepyborder.config.spongepowered.*;
import org.minelore.plugin.creepyborder.handler.*;
import org.minelore.plugin.creepyborder.manager.BorderManager;
import org.minelore.plugin.creepyborder.manager.HandlerManager;
import org.minelore.plugin.creepyborder.util.EnableHandlerData;
import org.popcraft.chunkyborder.ChunkyBorder;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.awt.*;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    private final Logger log = getLogger();
    private PaperCommandManager<CommandSourceStack> commandManager;
    private HandlerManager handlerManager;
    private ConfigManager configManager;
    private BorderManager borderManager;

    @Override
    public void onDisable() {
        super.onDisable();
        log.info("CreepyBorder plugin is disabled! Development by TheDiVaZo for MineLoreSMP");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("CreepyBorder plugin is enabled! Development by TheDiVaZo for MineLoreSMP");
        handlerManager = new HandlerManager(this);

        handlerManager.loadHandlerFactories();
        loadConfigManager();
        loadCommandManager();
        loadBorderManager();
        borderManager.start();
    }

    public void reload() {
        configManager.load();
        borderManager.stop();
        loadBorderManager();
        borderManager.start();
    }

    public void loadConfigManager() {
        TypeSerializer<HandlerConfig> handlerConfigTypeSerializer = new HierarchyClassSerializer<>(
                "name",
                Map.of(
                        HBiomeConfig.class, BiomeHandler.NAME,
                        HMagmaGrabConfig.class, MagmaGrabHandler.NAME,
                        HPotionEffectConfig.class, PotionEffectHandler.NAME,
                        HSoundConfig.class, SoundHandler.NAME,
                        HTimedKillConfig.class, TimedKillHandler.NAME
                )
        );

        TypeSerializerCollection typeSerializerCollection = TypeSerializerCollection.builder()
                .register(HandlerConfig.class, handlerConfigTypeSerializer)
                .register(org.bukkit.Color.class, BukkitColorSerializer.DEFAULT)
                .register(PotionEffectType.class, PotionEffectTypeSerializer.DEFAULT)
                .build();

        ConfigLoader configLoader = YamlConfigLoader.getConfigLoader(typeSerializerCollection);

        ConfigContainer configContainer = ConfigContainer.builder()
                .loader(configLoader)
                .addConfig(getDataFolder().toPath().resolve("config.yml"), MainConfig.class)
                .build();
        configManager = new ConfigManagerImpl(configContainer);
    }

    public void loadCommandManager() {
        MainConfig mainConfig = configManager.getConfig(MainConfig.class);
        commandManager = PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.<CommandSourceStack>builder().commonPoolExecutor().build())
                .buildOnEnable(this);

        commandManager.command(commandManager
                .commandBuilder("creepyborder", "cb")
                .literal("reload", "r")
                .permission((sender) -> PermissionResult.of(sender.getSender().hasPermission(mainConfig.getReloadPermission()), Permission.of(mainConfig.getReloadPermission())))
                .handler(ctx -> Bukkit.getScheduler().runTask(this, () -> {
                    CommandSender commandSender = ctx.sender().getSender();
                    try {
                        reload();
                        commandSender.sendMessage(Component.text("Конфигурация перезагружена! Для того, чтобы увидеть изменения биома - перезайдите").color(TextColor.color(Color.GREEN.getRGB())));
                    } catch (Exception e) {
                        commandSender.sendMessage(Component.text("Config has not been reloaded! Check console!").color(TextColor.color(Color.RED.getRGB())));
                        log.warning(e.getMessage());
                        e.printStackTrace();
                    }
                })));

    }

    private void loadBorderManager() {
        Border border;
        if (Bukkit.getPluginManager().isPluginEnabled("ChunkyBorder")) {
            border = Border.createChunkyBorder("world");
        }
        else {
            border = Border.createBukkitBorder("world");
        }

        MainConfig mainConfig = configManager.getConfigContainer().getConfig(MainConfig.class);
        borderManager = new BorderManager(
                this,
                border,
                mainConfig.getWrapperConfigs().stream().<EnableHandlerData>mapMulti((wrapperConfig, consumer) -> {
                    var wrapperOptional = handlerManager.getHandlerFactoryContainer().toWrapper(wrapperConfig);
                    if (wrapperOptional.isPresent()) {
                        consumer.accept(new EnableHandlerData(wrapperConfig.getDistToBorder(), wrapperOptional.get()));
                    } else {
                        log.warning("Can't create wrapper for config: " + wrapperConfig);
                    }
                }).toList(),
                player -> player.hasPermission(mainConfig.getImmunityPermission())
        );
    }
}
