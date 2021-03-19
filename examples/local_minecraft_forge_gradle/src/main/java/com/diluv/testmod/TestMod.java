package com.diluv.testmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.common.Mod;

@Mod("diluvtestmod")
public class TestMod {
    
    private static final Logger LOGGER = LogManager.getLogger();
    
    public TestMod() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
    }
    
    private void onPlayerTick(TickEvent.PlayerTickEvent event) {
        
        if (event.phase == Phase.END) {
            
            LOGGER.info(event.player.toString());
        }
    }
}