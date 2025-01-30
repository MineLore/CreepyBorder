package org.minelore.plugin.creepyborder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.permission.Permission;
import org.incendo.cloud.permission.PermissionResult;
import org.minelore.plugin.creepyborder.manager.BorderManager;
import org.minelore.plugin.creepyborder.manager.ConfigManager;
import org.minelore.plugin.creepyborder.manager.HandlerManager;
import org.minelore.plugin.creepyborder.util.EnableHandlerData;

import java.awt.*;
import java.util.logging.Logger;

/**
 * @author TheDiVaZo
 * created on 19.01.2025
 */
public class CreepyBorder extends JavaPlugin {
    private final Logger LOG = getLogger();
    private PaperCommandManager<CommandSourceStack> commandManager;
    private HandlerManager handlerManager;
    private ConfigManager configManager;
    private BorderManager borderManager;

    @Override
    public void onDisable() {
        super.onDisable();
        LOG.info("CreepyBorder plugin is disabled! Development by TheDiVaZo for MineLoreSMP");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        LOG.info("CreepyBorder plugin is enabled! Development by TheDiVaZo for MineLoreSMP");
        handlerManager = new HandlerManager(this);
        configManager = new ConfigManager(getDataFolder().toPath().resolve("config.yml"), LOG);

        configManager.loadConfig();
        handlerManager.loadHandlerFactories();
        loadCommandManager();
        loadBorderManager();
        borderManager.start();
    }

    public boolean reload() {
        boolean isReload = configManager.loadConfig();
        borderManager.stop();
        loadBorderManager();
        borderManager.start();
        return isReload;
    }

    public void loadCommandManager() {
        commandManager = PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.<CommandSourceStack>builder().commonPoolExecutor().build())
                .buildOnEnable(this);

        commandManager.command(commandManager
                .commandBuilder("creepyborder", "cp")
                .literal("reload", "r")
                .permission((sender) -> PermissionResult.of(sender.getSender().hasPermission(configManager.getMainConfig().getReloadPermission()), Permission.of(configManager.getMainConfig().getReloadPermission())))
                .handler(ctx -> {
                    CommandSender commandSender = ctx.sender().getSender();
                    if (reload()) {
                        commandSender.sendMessage(Component.text("Config reloaded!").color(TextColor.color(Color.GREEN.getRGB())));
                    }
                    else {
                        commandSender.sendMessage(Component.text("Config has not been reloaded! Check console!").color(TextColor.color(Color.RED.getRGB())));
                    }
                }));

    }

    private void loadBorderManager() {
        borderManager = new BorderManager(
                this,
                "world",
                configManager.getMainConfig().getWrapperConfigs().stream().<EnableHandlerData>mapMulti((wrapperConfig, consumer) -> {
                    var wrapperOptional = handlerManager.getHandlerFactoryContainer().toWrapper(wrapperConfig);
                    if (wrapperOptional.isPresent()) {
                        consumer.accept(new EnableHandlerData(wrapperConfig.getDistToBorder(), wrapperOptional.get()));
                    } else {
                        LOG.warning("Can't create wrapper for config: " + wrapperConfig);
                    }
                }).toList(),
                player -> player.hasPermission(configManager.getMainConfig().getImmunityPermission())
        );
    }
}
