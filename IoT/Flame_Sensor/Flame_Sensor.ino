#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"

#define D0_PIN 13 // ESP32 pin GPIO21, which is connected to the LED

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
  pinMode(D0_PIN, INPUT);
  //pinMode(flame, INPUT);
  //pinMode(Beep, OUTPUT);

  //val = analogRead(flame);
}

void loop() {
  int flame_state = digitalRead(D0_PIN);


  if (flame_state == HIGH) {
    
    fireValue = "0";  // "1"을 문자열로 설정
    Serial.println("No Flame detected => The fire is NOT detected");

    // Firebase에 "1" 업로드
    Firebase.set("/CareReceiver_list/abcd/ActivityData/fire", fireValue);

    // Handle error
    if (Firebase.failed()) {
      Serial.print("Setting /Fire failed:");
      Serial.println(Firebase.error());
      return;
    }
  } else {
 
    fireValue = "1";  // "0"을 문자열로 설정
    Serial.println("flame detected => The fire is detected");

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
