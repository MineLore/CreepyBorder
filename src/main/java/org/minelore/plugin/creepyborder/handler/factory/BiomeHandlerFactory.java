package org.minelore.plugin.creepyborder.handler.factory;

import com.comphenix.protocol.ProtocolManager;
import org.minelore.plugin.creepyborder.CreepyBorder;
import org.minelore.plugin.creepyborder.config.HBiomeConfig;
import org.minelore.plugin.creepyborder.handler.BiomeHandler;
import org.minelore.plugin.creepyborder.nms.BiomePacketEvent;
import org.minelore.plugin.creepyborder.nms.BiomeWrapper;
import org.minelore.plugin.creepyborder.util.SpecialEffectsData;

/**
 * @author TheDiVaZo
 * created on 30.01.2025
 */
public class BiomeHandlerFactory implements HandlerFactory<BiomeHandler, HBiomeConfig>{
    private final CreepyBorder plugin;
    private final BiomeWrapper biomeWrapper;
    private final BiomePacketEvent biomePacketEvent;
    private final ProtocolManager protocolManager;

    public BiomeHandlerFactory(CreepyBorder plugin, BiomeWrapper biomeWrapper, BiomePacketEvent biomePacketEvent, ProtocolManager protocolManager) {
        this.plugin = plugin;
        this.biomeWrapper = biomeWrapper;
        this.biomePacketEvent = biomePacketEvent;
        this.protocolManager = protocolManager;
    }

    @Override
    public BiomeHandler loadWrapper(HBiomeConfig handlerConfig) {
        biomeWrapper.replaceBiome(new SpecialEffectsData(
                handlerConfig.getWaterColor(),
                handlerConfig.getSkyColor(),
                handlerConfig.getFogColor(),
                handlerConfig.getWaterFogColor())
        );
        return new BiomeHandler(plugin, biomePacketEvent, protocolManager);
    }
}
