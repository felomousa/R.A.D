from flask import Flask, request, jsonify
from rgbmatrix import RGBMatrix, RGBMatrixOptions
from PIL import Image
import threading
import time
import os

# Flask app initialization
app = Flask(__name__)

# Default health data
health_data = {"health": 100}

# Matrix configuration
options = RGBMatrixOptions()
options.rows = 32
options.cols = 64
options.chain_length = 3
options.hardware_mapping = 'regular'
options.brightness = 50
options.pwm_bits = 11
options.pwm_lsb_nanoseconds = 130
options.led_rgb_sequence = "RGB"
options.scan_mode = 1
options.gpio_slowdown = 3

# Create LED matrix
matrix = RGBMatrix(options=options)

# Route to update or get health data
@app.route('/update_health', methods=['POST', 'GET'])
def handle_health():
    global health_data
    # Update health data if POST request
    if request.method == 'POST':
        health_data = request.json
        return jsonify({"message": "Health data updated"}), 200
    # Return current health data if GET request
    return jsonify(health_data)

# Previous health value for comparison
prev_health = 20

# Display health data on LED matrix
def display_health_data():
    global prev_health
    while True:
        health = int(health_data.get("health", 20))
        health = min(health, 20)  # Cap health at 20

        # Load heart images
        empty_container_image = Image.open("container.png").convert("RGBA")
        full_heart_image = Image.open("full.png").convert("RGBA")
        half_heart_image = Image.open("half.png").convert("RGBA")
        
        # Resize images to fit display
        heart_size = 17
        empty_container_image = empty_container_image.resize((heart_size, heart_size))
        full_heart_image = full_heart_image.resize((heart_size, heart_size))
        half_heart_image = half_heart_image.resize((heart_size, heart_size))

        # Display blinking effect if health decreases
        if health < prev_health:
            blinking_images = [Image.open(img).convert("RGBA").resize((heart_size, heart_size)) 
                               for img in ["container_blinking.png", "full_blinking.png", "half_blinking.png"]]
            final_image = display_health(health, *blinking_images, heart_size)
            matrix.SetImage(final_image.convert('RGB'))
            time.sleep(0.2)  # Blink duration

        # Update display with current health
        final_image = display_health(health, empty_container_image, full_heart_image, half_heart_image, heart_size)
        matrix.SetImage(final_image.convert('RGB'))

        prev_health = health  # Update previous health value
        time.sleep(0.01)  # Short delay for loop iteration

# Compose health display image
def display_health(health, empty_container_image, full_heart_image, half_heart_image, heart_size):
    y = 6  # Vertical offset
    final_image = Image.new("RGB", (options.cols * options.chain_length, options.rows), (0, 0, 0))
    # Base layer of empty containers
    for i in range(10):
        x_position = i * (heart_size + 2)
        final_image.paste(empty_container_image, (x_position, y), empty_container_image.split()[3])
    # Add full and half hearts based on health
    num_full_hearts = health // 2
    for i in range(num_full_hearts):
        x_position = i * (heart_size + 2)
        final_image.paste(full_heart_image, (x_position, y), full_heart_image.split()[3])
    if health % 2 == 1:
        x_position = num_full_hearts * (heart_size + 2)
        final_image.paste(half_heart_image, (x_position, y), half_heart_image.split()[3])
    return final_image

# Start Flask app in a separate thread
def start_flask_app():
    os.environ['FLASK_SKIP_DOTENV'] = '1'  # Skip .env file for Flask settings
    app.run(host='0.0.0.0', port=5001, debug=False, use_reloader=False)

if __name__ == "__main__":
    flask_thread = threading.Thread(target=start_flask_app)
    flask_thread.start()
    display_health_data()
