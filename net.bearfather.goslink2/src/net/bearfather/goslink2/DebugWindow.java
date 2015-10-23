package net.bearfather.goslink2;


import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
 
public class DebugWindow extends JFrame {
private static final long serialVersionUID = 1L;
TextArea textarea = new TextArea();
private boolean win;
 public DebugWindow(){
	 win=Boolean.valueOf(GosLink2.prps("window"));
	 if (win==true){
		 String imagePath = "smokin.png";
		 InputStream imgStream = DebugWindow.class.getResourceAsStream(imagePath);
		 try{BufferedImage myImg = ImageIO.read(imgStream);
		 setIconImage(myImg);
		 }catch (Exception e){e.printStackTrace();}
		 setTitle("Gossip Link");
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setLayout(new FlowLayout());
		 add(textarea);
		 textarea.setEditable(false);
		 pack();
		 setVisible(true);
	 }
   }
public void append(String msg) {
	if (win==true){textarea.append(msg.trim()+"\n");}
	else{System.out.println(msg);}
	
}
}