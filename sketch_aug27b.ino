int ledPin1 = 2; // Pino digital onde o LED está conectado
int ledPin2 = 3;
bool shouldTurnOn1 = false; // Variável para controlar se o LED deve ligar
bool shouldTurnOn2 = false;
void setup() {
  pinMode(ledPin1, OUTPUT);
  pinMode(ledPin2, OUTPUT);
  Serial.begin(9600);
  digitalWrite(ledPin1, LOW);
  digitalWrite(ledPin2, LOW);
}

void loop() {
  if (shouldTurnOn1) {
    digitalWrite(ledPin1, HIGH); // Ligar o LED
  } else {
    digitalWrite(ledPin1, LOW); // Desligar o LED
  }

    if (shouldTurnOn2) {
    digitalWrite(ledPin2, HIGH); // Ligar o LED
  } else {
    digitalWrite(ledPin2, LOW); // Desligar o LED
  }

  if (Serial.available()) {
    char command = Serial.read();
    Serial.print("Recebeu o caractere: ");
    Serial.println(command);

    if (command == '1') {
      shouldTurnOn1 = true; // Configurar para ligar o LED quando necessário
      delay(50);
      shouldTurnOn1 = false;
      delay(50);
      shouldTurnOn1 = true;
    } else if (command =='0'){
      shouldTurnOn1 = false;
    }

        if (command == '2') {
      shouldTurnOn2 = true; // Configurar para ligar o LED quando necessário
      delay(50);
      shouldTurnOn2 = false;
      delay(50);
      shouldTurnOn2 = true;
    } else if (command =='3'){
      shouldTurnOn2 = false;
    }

    
  }
}
