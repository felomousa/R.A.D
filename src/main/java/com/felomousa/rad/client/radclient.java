package com.felomousa.rad.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class radclient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(radclient.class);

    private double lastHealth = -1.0;
    private static final String PI_SERVER_URL = "http://your_pi_ip_address:your_port/update_health";

    @Override
    public void onInitializeClient() {
        LOGGER.info("RAD mod initializing...");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                double currentHealth = client.player.getHealth();
                if (currentHealth != lastHealth) {
                    LOGGER.info("Player Health Changed: " + currentHealth);
                    sendHealthData(currentHealth);
                    lastHealth = currentHealth;
                }
            } else {
                LOGGER.debug("Client player is null");
            }
        });
    }

    private void sendHealthData(double health) {
        try {
            URL url = new URL(PI_SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonPayload = "{\"health\": " + health + "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            LOGGER.info("HTTP Response Code: " + responseCode);

            conn.disconnect();
        } catch (Exception e) {
            LOGGER.error("Error sending health data: " + e.getMessage());
        }
    }
}
