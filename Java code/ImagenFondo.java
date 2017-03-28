
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author msi
 */
public class ImagenFondo extends javax.swing.JPanel{
    
    @Override
        public void paintComponent(Graphics g) {
            
            
        Dimension tamano=Toolkit.getDefaultToolkit().getScreenSize();
        ImageIcon fondo = new ImageIcon(getClass().getResource("fondoapp.jpg"));
        g.drawImage(fondo.getImage(), 0, 0, tamano.width, tamano.height, null);
        setOpaque(false);
        super.paintComponent(g);
        
        
}
    
}
