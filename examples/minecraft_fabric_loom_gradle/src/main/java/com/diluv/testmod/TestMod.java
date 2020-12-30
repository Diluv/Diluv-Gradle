package com.diluv.testmod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class TestMod implements ModInitializer {
    
    private final Logger log = LogManager.getLogger("DiluvGradleTest");
    
    @Override
    public void onInitialize () {
        
        this.log.info("Hello again, friend of a friend.");
    }
}