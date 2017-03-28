
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

//*Nos hemos ayudado de otros códigos para la implementación de la clase MjpegRunner tal como se indica en el pdf*//

import java.awt.*; 
import java.net.URL;
import java.net.URLConnection;
import jssc.SerialPort;
import jssc.SerialPortException;
import javax.swing.*;
import java.awt.event.*;


public class Coche extends javax.swing.JPanel{
    
	static MJpegViewer visor;//Objeto heredado de JComponent usado para mostrar la imagen.
	static Thread lecturaVideo;//Hilo de ejecucion obtenido a partir de un objeto de la clase MjpegRunner
	static JFrame ventana=new JFrame();
	static Dimension tam=Toolkit.getDefaultToolkit().getScreenSize();//Tamaño de la pantalla
    static SerialPort serialPort = new SerialPort("COM6");//Puerto serie emulado, asociado al módulo bluetooth
    static  JTextPane ip=new JTextPane(),puerto=new JTextPane();//Entradas del formulario que se utilizará para 
    															//conectar con el servidor http.
         

	
	
	static KeyListener TeclasPulsadas=new KeyListener()  {
		/*
		 * Este objeto se encarga de ejecutar las funciones correspondientes
		 * cuande se pulse o se suelte una tecla. Para cada tecla definida envía
		 * un byte por el pueto serie.
		 */
               
	    @Override
	    public void keyTyped(KeyEvent e) {
	    }

        @Override
        public void keyPressed(KeyEvent e) {
            
            if("W".equals(KeyEvent.getKeyText(e.getKeyCode())))
            	comandoBTooth("f");
            
            if("S".equals(KeyEvent.getKeyText(e.getKeyCode())))
            	comandoBTooth("b");
            
            if("A".equals(KeyEvent.getKeyText(e.getKeyCode())))
            	comandoBTooth("l");
            
            if("D".equals(KeyEvent.getKeyText(e.getKeyCode())))
            	comandoBTooth("r");
        }

        @Override
        public void keyReleased(KeyEvent e) {
        	if("W".equals(KeyEvent.getKeyText(e.getKeyCode())))
        		comandoBTooth("s");
        	
        	if("S".equals(KeyEvent.getKeyText(e.getKeyCode())))
        		comandoBTooth("s");
        	
        	if("A".equals(KeyEvent.getKeyText(e.getKeyCode())))
        		comandoBTooth("c");
        	
        	if("D".equals(KeyEvent.getKeyText(e.getKeyCode())))
        		comandoBTooth("c");      
        }
	};
        
	static ActionListener fControles= new ActionListener() {
   	 	@Override
   	 	public void actionPerformed(ActionEvent e){
   	 		comandoBTooth(e.getActionCommand()); 
   	 	}  
	};
	
    public static void comandoBTooth(String comando){//Envía el argumento, 
    												 //como secuencia de bytes, por el puerto serie.
    	try {
    		serialPort.writeBytes(comando.getBytes());
    	} 
    	catch (SerialPortException ex) {}
    }
        
	
    
    
    
	public static void main(String args[]) throws Exception{  
		///////////////////////////////////////////////////////////////////////
		//Definicion de los elementos de la ventana
		Container cBotones=new Container();
		Container filas[]=new Container[20];
		 
		
		JLabel lip=new JLabel("IP:"), 
			   lpuerto=new JLabel("Puerto:");
		
		JButton conectar=new JButton("Conectar"), 
				controles =new JButton("Controles"), 
				adelante=new JButton("Adelante"), 
				atras=new JButton("Atras"), 
				stop=new JButton("Stop") ,
                derecha=new JButton("Derecha"), 
                izquierda=new JButton("Izquierda"), 
                encenderluces=new JButton("Encender luces"), 
                apagarluces=new JButton("Apagar luces"),
                modoautomatico=new JButton("Modo Automático"), 
                apagarmodoautomatico=new JButton("Apagar Modo Automático");
		///////////////////////////////////////////////////////////////////////
		
		
        ///////////////////////////////////////////////////////////////////////
		//Se añaden los elementos a la ventana
		ventana.setSize(tam.width, tam.height);
        ventana.setContentPane(new ImagenFondo());
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setLayout(new BoxLayout(ventana.getContentPane(), BoxLayout.X_AXIS));
		visor=new MJpegViewer(tam.width/12, tam.height/2-2*tam.width/9, tam.width/3, 4*tam.width/9);
		ventana.add(visor);
		ventana.add(cBotones);
		ventana.addMouseListener(new MouseAdapter(){
			@Override
				public void mouseClicked(MouseEvent e) {
					ventana.requestFocusInWindow();
			} 
		});
                
		ventana.addKeyListener(TeclasPulsadas);
		///////////////////////////////////////////////////////////////////////
		
		
		//////////////////////////////////////////////////////////////////////
		//Se ajustan los atributos de los botones
		cBotones.setLayout(new BoxLayout(cBotones, BoxLayout.Y_AXIS));		
		for(int i=0;i<20;i++){
                    
			filas[i]=new Container();
			filas[i].setLayout(null);
                        
			if(i==0){
				filas[i].add(lip);
				filas[i].add(ip);
                lip.setBounds(10, 10, 40, 30);
                ip.setBounds(40,15,150,25);
                ip.setText("192.168.1.");
                lip.setForeground(Color.white);
			}
			else if(i==1){
				filas[i].add(lpuerto);
				filas[i].add(puerto);
                lpuerto.setBounds(10, 10, 100, 30);
				puerto.setBounds(60,10,40,30);
				puerto.setText("8080");
				lpuerto.setForeground(Color.white);
                                
			}
			else if(i==2){
				filas[i].add(conectar);
				filas[i].add(controles);
				conectar.setBounds(20, 10, 400, 30);
			}
			
                        
			else if(i==6){
				filas[i].add(adelante);
				adelante.setBounds(350, 10, 100, 25);
                            
				filas[i].add(stop);
				stop.setBounds(500, 10, 100, 25);
			}
			
			else if(i==7){
				filas[i].add(izquierda);
				izquierda.setBounds(250, 10, 100, 25);
				filas[i].add(atras);
				atras.setBounds(350, 10, 100, 25);
				filas[i].add(derecha);
				derecha.setBounds(450, 10, 100, 25);
			}
			if(i==9){
				filas[i].add(encenderluces);
				encenderluces.setBounds(50, 10, 150, 25);
				
				filas[i].add(modoautomatico);
				modoautomatico.setBounds(250, 10, 150, 25);
			}
			if(i==10){
				filas[i].add(apagarluces);
				apagarluces.setBounds(50, 10, 150, 25);
				
				filas[i].add(apagarmodoautomatico);
				apagarmodoautomatico.setBounds(250, 10, 150, 25);
			}
			cBotones.add(filas[i]);
		}
	
		////////////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////////////
		// Se definen las funciones de los botones
		conectar.addActionListener(new ActionListener() {
       	 	@Override
       	 	public void actionPerformed(ActionEvent e){
       	 		URL urlVideo;
       	 		visor.mensaje("Conectando a sercidor de video...", Color.WHITE);
       	 		try{
       	 			urlVideo=new URL("http://"+ip.getText()+":"+puerto.getText()+"/videofeed");
       	 
       	 			if(lecturaVideo!=null)
       	 				lecturaVideo.stop();
       	 			lecturaVideo=new Thread(new MjpegRunner(visor, urlVideo));
       	 			lecturaVideo.start();
       	 			visor.mensaje("Conectado a servidor de video.", Color.WHITE);
       	 		}
       	 		catch(Exception ex){
       	 			visor.mensaje("Error al conectar con el servidor de video", Color.WHITE);
       	 		}
       	 	}      
		});
		
         adelante.addActionListener(fControles);
         adelante.setActionCommand("f");
         
         atras.addActionListener(fControles);
         atras.setActionCommand("b");
               
         stop.addActionListener(fControles);
         stop.setActionCommand("s");
        
         izquierda.addActionListener(fControles);
         izquierda.setActionCommand("l");
        
         derecha.addActionListener(fControles);
         derecha.setActionCommand("r");
         
         modoautomatico.addActionListener(fControles);
         modoautomatico.setActionCommand("a");
                
         apagarmodoautomatico.addActionListener(fControles);
         apagarmodoautomatico.setActionCommand("m");
         
         encenderluces.addActionListener(new ActionListener(){
        	 @Override
        	 public void actionPerformed(ActionEvent e){
        		 try{
        			 URLConnection con=new URL("http://"+ip.getText()+":"+puerto.getText()+e.getActionCommand()).openConnection();
        			 con.setReadTimeout(500);
        			 con.connect();
        			 con.getInputStream().read();
        		 }
        		 catch(Exception ex){}
                        
        	 }
		
         });
         encenderluces.setActionCommand("/enabletorch");
                
         apagarluces.addActionListener(new ActionListener(){
        	 @Override
        	 public void actionPerformed(ActionEvent e){
        		 try{
        			 URLConnection con=new URL("http://"+ip.getText()+":"+puerto.getText()+e.getActionCommand()).openConnection();
        			 con.setReadTimeout(500);
        			 con.connect();
        			 con.getInputStream().read();
        		 }
        		 catch(Exception ex){}
                        
        	 }
		
         });
         apagarluces.setActionCommand("/disabletorch");  
         //////////////////////////////////////////////////////////////
                
		ventana.setVisible(true);//Muestra la ventana
		long t0=System.currentTimeMillis();
		while(System.currentTimeMillis()-t0<2500)
			;
		
		///////////////////////////////////////////////////////////////
		// Se establece la conexion con el puerto serie asociado al
		// módulo bluetooth.
		visor.mensaje("Conectando a bluetooth...", Color.white);
		try{
        	serialPort.openPort();
        	serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
        	visor.mensaje("Conectado a bluetooh", Color.GREEN);
		}
        catch(Exception ex){
        	visor.mensaje("Error: Dispositivo bluetooth no encontrado", Color.RED);
         }
	}//Fin del main
}//Fin de la clase principal
