package com.felomousa.rad.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// Mod entry point
public class radclient implements ClientModInitializer {
    // Logger setup
    private static final Logger LOGGER = LogManager.getLogger(radclient.class);
    // Track health changes
    private double lastHealth = -1.0;
    // Server URL
    private static final String PI_SERVER_URL = "http://192.168.0.196:5001/update_health";

    @Override
    public void onInitializeClient() {
        LOGGER.info("RAD mod initializing...");
        // Register tick event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Check if player exists
            if (client.player != null) {
                double currentHealth = client.player.getHealth();
                // If health changed
                if (currentHealth != lastHealth) {
                    LOGGER.info("Player Health Changed: " + currentHealth);
                    // Send health to server
                    sendHealthData(currentHealth);
                    // Update last health
                    lastHealth = currentHealth;
                }
            } else {
                LOGGER.debug("Client player is null");
            }
        });
    }

    // Send health data
    private void sendHealthData(double health) {
        new Thread(() -> {
            try {
                URL url = new URL(PI_SERVER_URL);
                HttpURLConnection conn = getHttpURLConnection(health, url);
                int responseCode = conn.getResponseCode();
                LOGGER.info("HTTP Response Code: " + responseCode);

                conn.disconnect();
            } catch (Exception e) {
                LOGGER.error("Error sending health data: " + e.getMessage());
            }
        }).start();
    }

    // Setup HTTP connection
    @NotNull
    private static HttpURLConnection getHttpURLConnection(double health, URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Set request method
        conn.setRequestMethod("POST");
        // Set content type
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        String jsonPayload = "{\"health\": " + health + "}";

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }
}
