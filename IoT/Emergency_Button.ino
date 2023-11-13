// 응급 버튼 실험
int Button = 3;
int LED = 13;
boolean onoff = LOW;

void setup() {
  pinMode(Button, INPUT);
  pinMode(LED, OUTPUT);
  Serial.begin(9600); // 시리얼 전송 속도 9600bps
}

void loop() {
  if (digitalRead(Button) == LOW) { // 상승 엣지 검출 (먼저 초기에 LOW 레벨 여부 확인, 버튼 누르기 전)
    delay(10); // 10ms 딜레이
    if (digitalRead(Button) == HIGH) { // HIGH 레벨 여부 확인 (버튼이 눌러진 상태)
      onoff = !onoff; // LED 점등 상태를 반전시킴 (LOW -> HIGH or HIGH -> LOW)
      digitalWrite(LED, onoff); // LED에 LED 점등 상태 값 출력
      Serial.println(onoff); // 현재 상태를 시리얼 모니터에 출력
      delay(10); // 10ms 딜레이
      while (digitalRead(Button) == HIGH) { // 버튼이 떨어질 때까지 대기
        delay(1);
      }
    }
  }
}
