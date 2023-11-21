#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"

int inputPIN = 26;
int statusPIR = 0;
int valueRead = 0;
int motionCount = 0;

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

  pinMode(inputPIN, INPUT);
}

void loop() {
  valueRead = digitalRead(inputPIN);

  if ((valueRead == HIGH) && (statusPIR == LOW)) {
    Serial.println("Motion Detected!");
    statusPIR = HIGH;
    motionCount++;  // 움직임이 탐지되면 카운트 증가

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
  } else {
    if ((valueRead == LOW) && (statusPIR == HIGH)) {
      Serial.println("Motion Ended!");
      statusPIR = LOW;
    }
  }

  delay(1000);
}
