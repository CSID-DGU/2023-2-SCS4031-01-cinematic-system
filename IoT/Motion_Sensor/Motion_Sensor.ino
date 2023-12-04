
#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"
int inputPIN = 16;
int statusPIR = 0;
int valueRead = 0;
int motionCount = 0;

unsigned long lastUploadTime = 0;  // 마지막 업로드 시간을 저장하는 변수
unsigned long lastResetTime = 0;   // 마지막 초기화 시간을 저장하는 변수
#define UPLOAD_INTERVAL 6000       // 10분에 한 번 업로드 (10분 = 600000 밀리초)
#define RESET_INTERVAL 36000       // 1시간에 한 번 초기화 (1시간 = 3600000 밀리초)

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

  delay(1000);
}
