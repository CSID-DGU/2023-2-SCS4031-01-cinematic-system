#include <WiFi.h>
#include <IOXhop_FirebaseESP32.h>
#define WIFI_SSID "KT_GiGA_8EF1"
#define WIFI_PASSWORD "a1fb44dc38"

#define FIREBASE_HOST "fir-phoneauth-97f7e-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "tAIzaSyAv3nj7dj9ugsavaDrV4POupMFydjHhsF0"


#define DOOR_SENSOR_PIN 19 // ESP32 pin GPIO19 connected to door sensor's pin

String doorValue; // 문자열로 Firebase에 업로드할 변수
bool doorOpen = false; // 이전에 문이 열렸는지 여부를 나타내는 플래그

void setup()
{
  Serial.begin(9600);

  // connect to WiFi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected IP: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  pinMode(DOOR_SENSOR_PIN, INPUT_PULLUP);
}

void loop()
{
  // 도어 센서 상태 감지
  int doorState = digitalRead(DOOR_SENSOR_PIN);

  if (doorState == HIGH && !doorOpen)
  {
    // 문이 열린 경우에만 실행
    doorValue = "1"; // "1"을 문자열로 설정
    Firebase.set("/CareReceiver_list/abcd/ActivityData/door/checkouting", doorValue);

    // Handle error
    if (Firebase.failed())
    {
      Serial.print("Setting /Door failed:");
      Serial.println(Firebase.error());
      return;
    }

    Serial.println("Door open");
    doorOpen = true; // 플래그를 true로 설정
    // TODO: 문이 열렸을 때 수행할 동작 추가
  }
  else if (doorState == LOW && doorOpen)
  {
    // 문이 닫힌 경우에만 실행
    Serial.println("Door close");
    doorOpen = false; // 플래그를 false로 설정
    // TODO: 문이 닫혔을 때 수행할 동작 추가
  }

  delay(100); // Add a small delay for stability
}
