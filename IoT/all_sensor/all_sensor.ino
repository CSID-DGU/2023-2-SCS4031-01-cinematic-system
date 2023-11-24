#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"
#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"

#define D0_PIN 13 // ESP32 pin GPIO21, which is connected to the LED
#define PIR_PIN 16 // PIR sensor pin
#define BUTTON_PIN 33 // ESP32 pin GPIO18, which is connected to the button
#define LED_PIN 27 // ESP32 pin GPIO21, which is connected to the LED
#define DOOR_SENSOR_PIN 19 // ESP32 pin GPIO19 connected to door sensor's pin

int flameState = 0;
int statusPIR = 0;
int motionCount = 0;
int valueRead = 0;
bool ledState = false;
String fireValue; // String to upload to Firebase
String ledStateStr; // Variable to store LED state as a string
String doorValue; // String to upload to Firebase
bool doorOpen = false; // Flag to indicate if the door was opened

unsigned long lastUploadTime = 0;
unsigned long lastResetTime = 0;
#define UPLOAD_INTERVAL 600000 // 10 minutes in milliseconds
#define RESET_INTERVAL 3600000 // 1 hour in milliseconds

void setup() {
  Serial.begin(9600);

  // Connect to WiFi
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

  pinMode(D0_PIN, INPUT);
  pinMode(PIR_PIN, INPUT);
  pinMode(BUTTON_PIN, INPUT_PULLUP);
  pinMode(LED_PIN, OUTPUT);
  pinMode(DOOR_SENSOR_PIN, INPUT_PULLUP);
}

void loop() {
  // Flame sensor
  flameState = digitalRead(D0_PIN);

  if (flameState == HIGH) {
    fireValue = "0";
    //Serial.println("No Flame detected => The fire is NOT detected");
  } else {
    fireValue = "1";
    //Serial.println("Flame detected => The fire is detected");
  }

  // Upload flame status to Firebase
  Firebase.set("/CareReceiver_list/abcd/ActivityData/fire", fireValue);

  // Handle error
  if (Firebase.failed()) {
    Serial.print("Setting /Fire failed:");
    Serial.println(Firebase.error());
    return;
  }

  // PIR motion sensor
  valueRead = digitalRead(PIR_PIN);

  if ((valueRead == HIGH) && (statusPIR == LOW)) {
    Serial.println("Motion Detected!");
    statusPIR = HIGH;
    motionCount++;  // 움직임이 탐지되면 카운트 증가
    Serial.println(motionCount);
  } else {
    if ((valueRead == LOW) && (statusPIR == HIGH)) {
      Serial.println("Motion Ended!");
      statusPIR = LOW;
    }
  }

  // 1시간마다 초기화 및 업로드
  if (millis() - lastResetTime >= RESET_INTERVAL) {
    if (motionCount > 0) {
      // motionCount를 문자열로 변환
      String motionCountStr = String(motionCount);

      // Firebase에 motionCountStr을 업데이트
      Firebase.set("/CareReceiver_list/abcd/ActivityData/activity/cnt", motionCountStr);

      // Handle error
      if (Firebase.failed()) {
        Serial.print("Setting /motionCount failed:");
        Serial.println(Firebase.error());
        return;
      }

      motionCount = 0; // 움직임 횟수 초기화
    }

    lastResetTime = millis(); // 현재 시간을 저장
  }

  // 10분마다 Firebase에 업로드
  if (millis() - lastUploadTime >= UPLOAD_INTERVAL) {
    if (motionCount > 0) { // 움직임이 감지된 경우에만 업로드
      // motionCount를 문자열로 변환
      String motionCountStr = String(motionCount);

      // Firebase에 motionCountStr을 업데이트
      Firebase.set("/CareReceiver_list/abcd/ActivityData/activity/cnt", motionCountStr);

      // Handle error
      if (Firebase.failed()) {
        Serial.print("Setting /motionCount failed:");
        Serial.println(Firebase.error());
        return;
      }
    }

    lastUploadTime = millis(); // 현재 시간을 저장
  }
  // Button and LED control
  static int lastButtonState = HIGH;
  int buttonState = digitalRead(BUTTON_PIN);

  if (lastButtonState == HIGH && buttonState == LOW) {
    // Toggle state of LED
    ledState = !ledState;

    // Control LED according to the toggled state
    digitalWrite(LED_PIN, ledState);

    // Convert LED state to string
    ledStateStr = String(ledState);

    // Set the LED state to Firebase "emergency" path
    Firebase.set("/CareReceiver_list/abcd/ActivityData/emergency", ledStateStr);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Emergency_Button failed:");
      Serial.println(Firebase.error());
      return;
    }
  }

  lastButtonState = buttonState;  // Update last button state

  // Door sensor
  int doorState = digitalRead(DOOR_SENSOR_PIN);

  if (doorState == HIGH && !doorOpen) {
    // Door is opened
    doorValue = "1";
    Firebase.set("/CareReceiver_list/abcd/ActivityData/door/checkouting", doorValue);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Door failed:");
      Serial.println(Firebase.error());
      return;
    }

    Serial.println("Door open");
    doorOpen = true;
    // TODO: Add actions to perform when the door is opened
  } else if (doorState == LOW && doorOpen) {
    // Door is closed
    Serial.println("Door close");
    doorOpen = false;
    // TODO: Add actions to perform when the door is closed
  }

  delay(100);  // Add a small delay for stability
}
