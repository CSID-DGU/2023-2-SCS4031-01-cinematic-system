//화염센서 실험
int flame=A5; //A5 아날로그 포트를 센서입력으로
int Beep=8; //8번 포트를 버저 출력으로
int val=0; //변수정의

void setup() 
{
  pinMode(Beep,OUTPUT); //출력모드
  pinMode(flame,INPUT); //입력모드

  Serial.begin(9600); //시리얼 전송속도 9600bps

  val=analogRead(flame); //아날로그포트 전압값 읽기
}
 
void loop() 
{  
  Serial.println(analogRead(flame)); //아날로그 값을 시리얼로 출력
  if((analogRead(flame)-val)>=5) //아날로그 값이 5보다 크거나 같은지 확인
    digitalWrite(Beep,HIGH); //버저 켜기
  else //5보다 작으면
    digitalWrite(Beep,LOW); //버저 끄기
   
  
}

