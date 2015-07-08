package net.bearfather.goslink2;


import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
 
public class outputWindow extends JFrame {
private static final long serialVersionUID = 1L;
TextArea textarea = new TextArea(20,80);
private boolean win;
 public outputWindow(){
	 win=Boolean.valueOf(GosLink2.prps("window"));
	 if (win==true){
		 String imagePath = "smokin.png";
		 InputStream imgStream = outputWindow.class.getResourceAsStream(imagePath);
		 try{BufferedImage myImg = ImageIO.read(imgStream);
		 setIconImage(myImg);
		 }catch (Exception e){e.printStackTrace();}
		 setTitle("Output window");
		 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 setLayout(new FlowLayout());
		 add(textarea);
		 pack();
		 setVisible(true);
	 }
   }
public void append(String msg) {
	if (win==true){textarea.append(msg+"\n");}
	else{System.out.println(msg);}
	
}
}