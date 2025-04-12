# Minecraft Health Display

Syncs Minecraft player health with a physical RGB LED matrix display in real time, using a Fabric mod and Flask server on a Raspberry Pi.

![img](https://github.com/felomousa/R.A.D/assets/149443735/10891976-59bf-4817-855f-8574e1e70712)

## Features
- real-time health data sync from Minecraft to hardware
- Fabric mod tracks health and sends JSON payloads to Flask
- LED matrix displays heart icons based on player health
- blinking effect when health drops

## Architecture

### Minecraft Client (Fabric Mod)
- hooks into client tick events via `ClientTickEvents.END_CLIENT_TICK`
- monitors `client.player.getHealth()`
- sends health as JSON via HTTP POST to Pi server
- logs changes and responses

### Raspberry Pi Backend (Flask + rgbmatrix)
- Flask server listens for `/update_health` POST requests
- parses JSON and updates internal state
- display thread renders current health using preloaded heart images
- supports blinking frames for damage feedback

## Setup

### Requirements
- Minecraft Fabric 1.20.4
- Raspberry Pi with connected RGB LED matrix
- Python 3
- Libraries: `Flask`, `Pillow`, `rgbmatrix`

### Install

1. **Minecraft Mod:**
   - Place compiled RAD mod `.jar` into `~/.minecraft/mods/`

2. **Raspberry Pi:**
   ```bash
   sudo apt-get install python3-pip
   pip3 install flask pillow
   ```

   - clone and configure `rgbmatrix`:  
     https://github.com/hzeller/rpi-rgb-led-matrix

3. **Run Flask Server:**
   ```bash
   python3 RAD.py
   ```

4. start Minecraft and monitor LED output live

## Technical Highlights / Skills Demonstrated

- event-driven game state tracking via Fabric modding API
- HTTP-based coms protocol between game client and device
- real-time rendering and compositing using `Pillow`
- Linux service deployment on Raspberry Pi
- Python & Java
