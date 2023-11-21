#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"

#define Beep 18 // ESP32 pin GPIO18, which is connected to the button
#define flame 21 // ESP32 pin GPIO21, which is connected to the LED

int val = 0;
String fireValue;  // 문자열로 Firebase에 업로드할 변수

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

  pinMode(flame, INPUT);
  pinMode(Beep, OUTPUT);

  val = analogRead(flame);
}

void loop() {
  Serial.println(analogRead(flame)); // 아날로그 값을 시리얼로 출력

  if ((analogRead(flame) - val) >= 5) {
    digitalWrite(Beep, HIGH); // 버저 켜기
    fireValue = "1";  // "1"을 문자열로 설정

    // Firebase에 "1" 업로드
    Firebase.set("/CareReceiver_list/abcd/ActivityData/fire", fireValue);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Fire failed:");
      Serial.println(Firebase.error());
      return;
    }
  } else {
    digitalWrite(Beep, LOW); // 버저 끄기
    fireValue = "0";  // "0"을 문자열로 설정

    // Firebase에 "0" 업로드
    Firebase.set("/CareReceiver_list/abcd/ActivityData/fire", fireValue);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Fire failed:");
      Serial.println(Firebase.error());
      return;
    }
  }

  delay(100);  // Add a small delay for stability
}
