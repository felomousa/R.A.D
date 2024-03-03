package com.felomousa.rad.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class radclient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(radclient.class);

    private double lastHealth = -1.0; // Initialize to a value that's unlikely to be a real health value

    @Override
    public void onInitializeClient() {
        LOGGER.info("RAD mod initializing..."); // Verify initialization

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                double currentHealth = client.player.getHealth();
                if (currentHealth != lastHealth) {
                    LOGGER.info("Player Health Changed: " + currentHealth); // Use LOGGER here
                    lastHealth = currentHealth;
                }
            } else {
                LOGGER.debug("Client player is null"); // This line is for debugging purposes
            }
        });
    }
}