vlhkost = 0
teplota_old = 0
teplota = 100
data = 0
OLED.init(128, 64)
ESP8266_IoT.init_wifi(SerialPin.P8, SerialPin.P12, BaudRate.BAUD_RATE115200)
ESP8266_IoT.connect_wifi("WiFi-Repeater", "c0p3rmin3")
if ESP8266_IoT.wifi_state(True):
    basic.show_icon(IconNames.YES)
    basic.pause(500)

def on_forever():
    global teplota, teplota_old, vlhkost, data
    teplota = Environment.dht11value(Environment.DHT11Type.DHT11_TEMPERATURE_C, DigitalPin.P15)
    if teplota != teplota_old:
        teplota_old = teplota
        vlhkost = Environment.dht11value(Environment.DHT11Type.DHT11_HUMIDITY, DigitalPin.P15)
        ESP8266_IoT.connect_thing_speak()
        if ESP8266_IoT.thing_speak_state(True):
            basic.show_icon(IconNames.HAPPY)
            basic.pause(100)
        ESP8266_IoT.set_data("2XCLNYUKIK1J31E2", teplota, vlhkost, 0)
        ESP8266_IoT.upload_data()
        data = data + 1
        OLED.clear()
        OLED.write_string("Teplota: ")
        OLED.write_num_new_line(teplota)
        OLED.write_string("Vlhkost: ")
        OLED.write_num_new_line(vlhkost)
        OLED.new_line()
        OLED.write_string("Odoslane: ")
        OLED.write_num_new_line(data)
    else:
        basic.clear_screen()
    if teplota > 24:
        ESP8266_IoT.connect_thing_speak()
        smarthome.motor_fan(AnalogPin.P1, True)
        ESP8266_IoT.set_data("2XCLNYUKIK1J31E2", teplota, vlhkost, 1)
        ESP8266_IoT.upload_data()
    else:
        ESP8266_IoT.connect_thing_speak()
        smarthome.motor_fan(AnalogPin.P1, False)
        ESP8266_IoT.set_data("2XCLNYUKIK1J31E2", teplota, vlhkost, 0)
        ESP8266_IoT.upload_data()
basic.forever(on_forever)
