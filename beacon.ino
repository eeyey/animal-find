  #include "sys/time.h"

  #include "BLEDevice.h"
  #include "BLEServer.h"
  #include "BLEUtils.h"
  #include "esp_sleep.h"

  #define GPIO_DEEP_SLEEP_DURATION     1  // sleep 4 seconds and then wake up
  RTC_DATA_ATTR static time_t last;        // remember last boot in RTC Memory
  RTC_DATA_ATTR static uint32_t bootcount; // remember number of boots in RTC Memory

  BLEAdvertising *pAdvertising;
  struct timeval now;
    
  void setBeacon() {
    char beacon_data[22];
    uint16_t beconUUID = 0xFEAA;

    BLEAdvertisementData oAdvertisementData = BLEAdvertisementData();
    
    oAdvertisementData.setFlags(0x06); 
    oAdvertisementData.setCompleteServices(BLEUUID(beconUUID));

      beacon_data[0] = 0x10;  // Eddystone Frame Type (Eddystone-URL)
      beacon_data[1] = 0x20;  // Beacons TX power at 0m
      beacon_data[2] = 0x03;  // URL Scheme 'https://'
      beacon_data[3] = 'f';  
      beacon_data[4] = 'a';  
      beacon_data[5] = 'n';  
      beacon_data[6] = 'i';  
      beacon_data[7] = 'm';  
      beacon_data[8] = '.';  
      beacon_data[9] = 'r';  
      beacon_data[10] = 'u';  
      beacon_data[11] = '/';  
      beacon_data[12] = 'A';
      beacon_data[13] = '6';
      beacon_data[14] = 'K';
      beacon_data[15] = 'X';
    
    oAdvertisementData.setServiceData(BLEUUID(beconUUID), std::string(beacon_data, 16));
    pAdvertising->setScanResponseData(oAdvertisementData);
  }

  void setup() {
    Serial.begin(115200);
    gettimeofday(&now, NULL);

    last = now.tv_sec;
    
    BLEDevice::init("ESP32");
    BLEServer *pServer = BLEDevice::createServer();

    pAdvertising = pServer->getAdvertising();
    
    setBeacon();

    pAdvertising->start();
    delay(100);
    pAdvertising->stop();
    esp_deep_sleep(1000000LL * GPIO_DEEP_SLEEP_DURATION);
  }

  void loop() {
  }