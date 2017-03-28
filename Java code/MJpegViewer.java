import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;



public class MJpegViewer extends JComponent{
	
	AffineTransform rotacion=new AffineTransform();//Se usa para rotar la imagen 90º
	Point pos;//Posicion de la imagen
	Dimension dim;//Dimension de la ventana
	BufferedImage imagen;//Frame actual
	String mensajes[]=new String[4]; 
	Color colores[]=new Color[mensajes.length];
	
	
	MJpegViewer(int x, int y, int ancho, int alto){
		pos=new Point(x,y);
		dim=new Dimension(ancho, alto);
		rotacion.rotate(Math.PI/2) ;//Rota la imagen 90º	
		rotacion.translate(0, -ancho);//Al rotar con centro en origen la 
											//imagen quedaría fuera de la pantalla.
											//Esta translación lo corrige
	}
	
	@Override
	/*
	 * Cada vez que se actualiza la imagen con repaint()
	 * se hace una llamada a este procedimiento.
	 * En el se aplica la transformación "rotacion" y se
	 * dibuja la imagen. Si la imagen no ha sido inicializada 
	 * dibuja un rectangulo negro.
	 */
	public void paint(Graphics g) {
		AffineTransform reset=((Graphics2D)g).getTransform();
		((Graphics2D)g).setTransform(rotacion);//Aplicamos la rotacion
		setBounds(new Rectangle(pos, dim));		//Establecemos la posicion y las dimensiones
												//de la imagen.
		if(imagen==null){
			g.setColor(Color.BLACK);
			g.fillRect(0,0, dim.height, dim.width);
		}
		else{
			g.drawImage(imagen, 0, 0, dim.height, dim.width,null);
		}	
		
		((Graphics2D)g).setTransform(reset);
		for(int i=0;i<mensajes.length;i++)
			if(mensajes[i]!=null){
				g.setColor(colores[i]);
				g.drawString(mensajes[i], 0, dim.height-20*(mensajes.length-i));
			}
	}
	
	
	/*
	 * Este método sirve para que un objeto MjpegRunner 
	 * pase una imagen leida.
	 */
	void setBufferedImage(BufferedImage bf){
		imagen=bf;
	}
	
	
	/*
	 * El metodo mensaje sirve para pasar
	 * un mensaje y mostrarlo en la parte 
	 * de abajo de la imagen.
	 */
	void mensaje(String msg, Color color){
		int i;
		for(i=0;i<mensajes.length-1;i++){
			mensajes[i]=mensajes[i+1];
			colores[i]=colores[i+1];
		}
		mensajes[i]=msg;
		colores[i]=color;
		paint(getGraphics());
	}
}
