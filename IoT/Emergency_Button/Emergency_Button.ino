#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"

#define BUTTON_PIN  18 // ESP32 pin GPIO18, which is connected to the button
#define LED_PIN     21 // ESP32 pin GPIO21, which is connected to the LED

String led_state_str; // Variable to store led_state as a string

void setup() {
  Serial.begin(9600);

  // connect to WiFi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected IP: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  pinMode(BUTTON_PIN, INPUT_PULLUP);
  pinMode(LED_PIN, OUTPUT);
}

void loop() {
  int button_state = digitalRead(BUTTON_PIN);

  if (last_button_state == HIGH && button_state == LOW) {
    // Toggle state of LED
    led_state = !led_state;

    // Control LED according to the toggled state
    digitalWrite(LED_PIN, led_state);

    // Convert led_state to string
    led_state_str = String(led_state);

    // Set the LED state to Firebase "led_state" path
    Firebase.set("/CareReceiver_list/abcd/ActivityData/emergency", led_state_str);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Emergency_Button failed:");
      Serial.println(Firebase.error());
      return;
    }
  }

  last_button_state = button_state;  // Update last button state

  delay(100);  // Add a small delay for stability
}
