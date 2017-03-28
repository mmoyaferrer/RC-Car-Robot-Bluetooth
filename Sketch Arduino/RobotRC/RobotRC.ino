///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////
/////////                                                                                 ///////// 
/////////            PROYECTO ROBOT-RC CON RETRANSMISIÓN DE VIDEO EN TIEMPO REAL          /////////  
/////////                                                                                 ///////// 
/////////              AUTORES: MANUEL MOYA FERRER E ISMAEL YESTE ESPÍN                   /////////  
/////////          PROYECTO SISTEMAS ELECTRÓNICOS DIGITALES 3º TELECOMUNICACIONES         /////////  
/////////                                                                                 /////////  
/////////                                  ENERO 2015                                     /////////  
///////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


#include <Servo.h>

#define pin_trig 8
#define pin_echo 9
#define pin_adelante 5
#define pin_atras 6


char comando;
byte valor;
byte direccion=0;
int velocidad=0;
int distancia;
boolean autopiloto=false;
Servo miServo;
boolean recuperacion=false;
int cm,duracion;


//El funcionamiento básico del programa es el siguiente: 
//Tenemos dos posibilidades, tener el modo autopiloto activado o no, a partir de esto se formará nuestro programa, de la siguiente manera:
//
//1º Si el modo autopiloto está desactivado (autopiloto==false)  :  Lo que haremos será recibir nuestros comandos bluetooth, los cuales nos indicarán
//   la acción a realizar; hacia delante, hacia la izquierda(los cuales se realizan mediante la funcion mover, que explicaremos a continuación),...() O así mismo el comando para activar el modo autopiloto, por lo que autopiloto 
//   seria un valor true y por tanto pasaremos a la otra opción.
//
//2º Si el modo autopiloto está activado (autopiloto == true)  :  El programa activará las funciones de nuestro sensor de ultrasonidos, el cual nos dará la distancia
//   que hay entre el obstáculo frontal y el coche mediante la funcion medir_distancia que explicaremos a continuación. Si esta distancia es menor a 30 cm, el coche rectificará 
//   su movimiento echando marcha atrás y torciendo. En caso de que la distancia sea mayor, no habrá obstaculos cercanos y por tanto el robot podrá continuar su camino sin girar.
//   
//   Así mismo, en este modo seguiremos leyendo datos recibidos por Bluetooth, puesto que nos pueden mandar el comando para desactivar el modo autopiloto.

//Función mover: Mediante esta función, controlaremos el movimiento del coche tanto en direccion delantera-trasera como hacia los lados.
//               Los argumentos que recibe como entrada son un byte de dirección(comprendido en el rango 0-180 (grados) los cuales nos permite el Servo), 
//               Y un dato entero que será velocidad(1 si va hacia delante, -1 hacia atras, en otro caso los motores pararán.)

//Función medir_distancia: El sensor de ultrasonidos mandará una serie de pulsos que posteriormente volverá a recibir en su patilla echo, mediante el tiempo que ha pasado entre
//                         que los pulsos van y vuelven(dato que nos suministra este sensor), podemos calcular el espacio que hay , puesto que sabemos la velocidad del sonido, por 
//                         lo que la salida de esta función serán los centímetros de distancia que separan el frontal del coche con un obstáculo. Para mas información acerca de este
//                         sensor, podemos ver su datasheet en internet. 


//*Los datos que nos envia el ordenador mediante Bluetooth, los recibimos por el puerto serial del Arduino (patillas Rx y Tx)

void mover(byte dir, int v){     
  
  direccion=dir;
  velocidad=v;
  if(v>0){
    digitalWrite(pin_adelante,HIGH);
    digitalWrite(pin_atras,LOW);
  }
  else if(v<0){
    digitalWrite(pin_adelante,LOW);
    digitalWrite(pin_atras,HIGH);
  }
  else{
    digitalWrite(pin_adelante,LOW);
    digitalWrite(pin_atras,LOW);
  }
  miServo.write(dir);           
}

int medir_distancia(){

  digitalWrite(pin_trig,LOW);
  delayMicroseconds(5);
  digitalWrite(pin_trig,HIGH);
  delayMicroseconds(10);
  duracion=pulseIn(pin_echo,HIGH);  //LA SEÑAL RETORNA AL SENSOR

  cm=int (0.034*(duracion/2)) ;  //Velocidad = cm/us  Tiempo = us
  
  if(cm==0 || cm<0){
  cm=100;
  }
  
  return cm;
}

void setup(){

    Serial.begin(9600);
    miServo.attach(7);

    pinMode(pin_trig,OUTPUT);
    pinMode(pin_echo,INPUT);

    pinMode(pin_adelante, OUTPUT);
    pinMode(pin_atras, OUTPUT);

    mover(90, 0);
}

void loop(){

  if(Serial.available()){              

       comando=Serial.read();

       if(!autopiloto){

         switch(comando){

           case 'f':                      //Comando avanzar
             velocidad=1;
             mover(direccion, velocidad);
             break;

           case 'b':                     //Comando retroceder
             velocidad=-1;
             mover(direccion, velocidad);
             break;

           case 'l':                     //Comando girar a la izquierda
           direccion=20;
             mover(direccion, velocidad);
             break;

           case 'r':                     //Comando girar a la derecha
           direccion=160;
             mover(direccion, velocidad);
             break;

           case 's':                     //Se ha soltado la tecla W ó S, por lo que paramos el motor.
             velocidad=0;
             mover(direccion, velocidad);
             break;

           case 'c':                    //Se ha soltado la tecla de dirección (A ó D), por lo que el servo toma el valor 90º(no hay giro)
             direccion=90;
             mover(direccion, velocidad);
             break;

           case 'a':                    //Comando para activar el modo autopiloto.
             autopiloto=true;
             break;
         }
       }
  }

       else{     //MODO AUTOPILOTO

           while(autopiloto){
             
                distancia=medir_distancia();   
                           
                if(distancia<30){
                     mover(160, -1);
                     delay(2000);
                     mover(20, 1);
                     delay(500);
                }  
                if(distancia>30){
                   mover(90,1);
                }
                
               if(Serial.available()){   
                  comando=Serial.read();
                 
                  if(comando=='m'){       //Comando para desactivar el modo autopiloto.
                  autopiloto=false;
                  direccion=160;
                  velocidad=0;
                  mover(direccion,velocidad);
                  }
                  else{
                  autopiloto=true;
                  }  
                }
          }
      
      }  

}
