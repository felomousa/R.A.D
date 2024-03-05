# Real Time Avatar Display (RAD) Mod 1.0.0 💖

Welcome to RAD (Real Time Avatar Display), where your Minecraft health jumps out of the screen and into the real world! This project combines the thrill of Minecraft with the hands-on fun of Raspberry Pi and LED displays. Perfect for gamers, makers, and anyone in between.


![IMG](https://github.com/felomousa/R.A.D/assets/149443735/10891976-59bf-4817-855f-8574e1e70712)

## Features 🎮

- **Real-Time Health Tracking**: Keeps an eye on your health bar so you don't have to.
- **LED Matrix Display**: Lights up your room with your current health status.
- **DIY Satisfaction**: Mix coding and electronics to create something truly unique.
- **Personal Touch**: Customize how you want your health displayed.

## How It Works 🛠

### The Easy Explanation

1. **In Minecraft**: The mod watches your health.
2. **Sending Data**: Whenever your health changes, the mod tells the Raspberry Pi about it.
3. **Display Time**: The Raspberry Pi then updates an LED matrix to show your health in real-time.

### A Bit More Technical

1. **Minecraft Mod**: Utilizes game events to monitor real-time health changes. It captures health data and sends it over to a Flask web server running on the Raspberry Pi through HTTP POST requests, formatted in JSON.
2. **Data Handling on Raspberry Pi**: The Flask server listens for incoming data, parses the JSON payload to extract health information, and processes it to control the LED matrix. The `rgbmatrix` library is used alongside Python imaging libraries to create and update heart icons on the display.

## Getting Started 🚀

### What You Need

- Minecraft with Fabric installed
- Raspberry Pi (any model that's handy)
- RGB LED matrix compatible with Raspberry Pi
- Basic knowledge of your Raspberry Pi's terminal

### Installation Guide

1. **Install the RAD Mod**:
   - Drop the RAD mod file into your Minecraft `mods` folder.

2. **Set Up Your Raspberry Pi**:
   - Ensure Python and Flask are installed.
   - Connect your LED matrix to the Raspberry Pi.

3. **Run the RAD.py Script**:
   - Copy `RAD.py` to your Raspberry Pi.
   - Run `python RAD.py` in your terminal to start the Flask server.

4. **Play Minecraft and Watch**:
   - As you play, watch your health status update live on the LED matrix!




Enjoy RAD, and may your health bar always be full! 💖
