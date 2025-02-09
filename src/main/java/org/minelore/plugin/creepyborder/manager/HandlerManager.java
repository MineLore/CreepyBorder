package org.minelore.plugin.creepyborder.manager;

import com.comphenix.protocol.ProtocolLibrary;
import org.minelore.plugin.creepyborder.CreepyBorder;
import org.minelore.plugin.creepyborder.config.spongepowered.*;
import org.minelore.plugin.creepyborder.handler.*;
import org.minelore.plugin.creepyborder.handler.factory.BiomeHandlerFactory;
import org.minelore.plugin.creepyborder.handler.factory.HandlerFactory;
import org.minelore.plugin.creepyborder.handler.factory.HandlerFactoryContainer;
import org.minelore.plugin.creepyborder.nms.BiomePacketEvent_1_21_R1;
import org.minelore.plugin.creepyborder.nms.BiomeWrapper_1_21_R1;

import java.util.Map;

/**
 * @author TheDiVaZo
 * created on 30.01.2025
 */
public class HandlerManager {
    private final CreepyBorder creepyBorder;
    private HandlerFactoryContainer handlerFactoryContainer;

    private BiomeHandlerFactory biomeHandlerFactory;

    public HandlerManager(CreepyBorder creepyBorder) {
        this.creepyBorder = creepyBorder;
        loadBiomeHandlerFactory();
    }

    private void loadBiomeHandlerFactory() {
        BiomeWrapper_1_21_R1 biomeWrapper_1_21_r1 = new BiomeWrapper_1_21_R1();
        biomeHandlerFactory = new BiomeHandlerFactory(creepyBorder, biomeWrapper_1_21_r1, new BiomePacketEvent_1_21_R1(biomeWrapper_1_21_r1), ProtocolLibrary.getProtocolManager());
    }

    public void loadHandlerFactories() {
        handlerFactoryContainer = new HandlerFactoryContainer(
                Map.of(
                        MagmaGrabHandler.NAME,
                        (HandlerFactory<MagmaGrabHandler, HMagmaGrabConfig>) config -> new MagmaGrabHandler(creepyBorder, config.getVectorLength()),
                        PotionEffectHandler.NAME,
                        (HandlerFactory<PotionEffectHandler, HPotionEffectConfig>) config -> new PotionEffectHandler(creepyBorder, config.getEffectDatas()),
                        SoundHandler.NAME,
                        (HandlerFactory<SoundHandler, HSoundConfig>) config -> new SoundHandler(creepyBorder, config.getSounds(), config.getPitch(), config.getVolume(), config.getPeriodInTick()),
                        TimedKillHandler.NAME,
                        (HandlerFactory<TimedKillHandler, HTimedKillConfig>) config -> new TimedKillHandler(creepyBorder, config.getTickToKill()),
                        BiomeHandler.NAME,
                        (HandlerFactory<BiomeHandler, HBiomeConfig>) config -> biomeHandlerFactory.loadWrapper(config)
                )
        );
    }

    public HandlerFactoryContainer getHandlerFactoryContainer() {
        return handlerFactoryContainer;
    }
}
