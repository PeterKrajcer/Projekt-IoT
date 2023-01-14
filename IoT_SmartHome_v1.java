let vlhkost = 0
let teplota_old = 0
let teplota = 100
let data = 0
OLED.init(128, 64)
ESP8266_IoT.initWIFI(SerialPin.P8, SerialPin.P12, BaudRate.BaudRate115200)
ESP8266_IoT.connectWifi("WiFi-Repeater", "c0p3rmin3")
if (ESP8266_IoT.wifiState(true)) {
    basic.showIcon(IconNames.Yes)
    basic.pause(500)
}
basic.forever(function () {
    teplota = Environment.dht11value(Environment.DHT11Type.DHT11_temperature_C, DigitalPin.P15)
    if (teplota != teplota_old) {
        teplota_old = teplota
        vlhkost = Environment.dht11value(Environment.DHT11Type.DHT11_humidity, DigitalPin.P15)
        ESP8266_IoT.connectThingSpeak()
        if (ESP8266_IoT.thingSpeakState(true)) {
            basic.showIcon(IconNames.Happy)
            basic.pause(100)
        }
        ESP8266_IoT.setData(
        "2XCLNYUKIK1J31E2",
        teplota,
        vlhkost,
        0
        )
        ESP8266_IoT.uploadData()
        data = data + 1
        OLED.clear()
        OLED.writeString("Teplota: ")
        OLED.writeNumNewLine(teplota)
        OLED.writeString("Vlhkost: ")
        OLED.writeNumNewLine(vlhkost)
        OLED.newLine()
        OLED.writeString("Odoslane: ")
        OLED.writeNumNewLine(data)
    } else {
        basic.clearScreen()
    }
    if (teplota > 24) {
        ESP8266_IoT.connectThingSpeak()
        smarthome.motorFan(AnalogPin.P1, true)
        ESP8266_IoT.setData(
        "2XCLNYUKIK1J31E2",
        teplota,
        vlhkost,
        1
        )
        ESP8266_IoT.uploadData()
    } else {
        ESP8266_IoT.connectThingSpeak()
        smarthome.motorFan(AnalogPin.P1, false)
        ESP8266_IoT.setData(
        "2XCLNYUKIK1J31E2",
        teplota,
        vlhkost,
        0
        )
        ESP8266_IoT.uploadData()
    }
})
