package net.bearfather.goslink2;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Toolkit;

import javax.swing.BoxLayout;
 
public class DebugWindow extends JFrame implements ActionListener,MouseListener{
	private static final long serialVersionUID = 1L;
	JTextArea textarea = new JTextArea();
	JFrame frame=new JFrame();
 	JButton bdebug = new JButton("debug");
	private boolean win=true;
	public boolean dbm=false;
	private final JScrollPane scrollPane = new JScrollPane();
	
	 public DebugWindow(){
		 win=Boolean.valueOf(GosLink2.props("window"));
		 setupPanel();
		 
	   }
	public void append(String msg) {
		if (GosLink2.timestamp){
			Date date=new Date();
			SimpleDateFormat ft =new SimpleDateFormat ("MM/dd hh:mm");
			msg=ft.format(date)+": "+msg;
			textarea.setCaretPosition(textarea.getDocument().getLength());
		}
		if (win==true){textarea.append(msg+"\n");}
		else{System.out.println(msg);}
		
	}
	@SuppressWarnings("unused")
	public void setupPanel(){
	 	getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
	 	getContentPane().add(scrollPane);
	 	scrollPane.setViewportView(textarea);
	 	textarea.setEditable(false);
	 	if(GosLink2.debug){scrollPane.setColumnHeaderView(bdebug);}
	 	textarea.addMouseListener(new ClickListener());
	 	bdebug.addActionListener(this);
		String imagePath = "smokin.png";
		InputStream imgStream = DebugWindow.class.getResourceAsStream(imagePath);
		try{BufferedImage myImg = ImageIO.read(imgStream);}catch (Exception e){}
		setTitle("Gossip Link");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	setIconImage(Toolkit.getDefaultToolkit().getImage(DebugWindow.class.getResource("/net/bearfather/goslink2/smokin.png")));
		if (GosLink2.debug){this.setSize(453,370);}
		else{this.setSize(448,311);}
		if(win){this.setVisible(true);}
	}
	public void debugmode(boolean swch){
		if (swch){
		 	scrollPane.setBounds(0, 0, 834, 674);
		 	bdebug.setBounds(10, 691, 89, 23);
			this.setSize(853,870);
			dbm=true;
		}else{
		 	scrollPane.setBounds(0, 0, 434, 274);
		 	bdebug.setBounds(10, 291, 89, 23);
			this.setSize(453,370);
			dbm=false;
		}
	}
	public void addmsg(String msg){
		if (GosLink2.debugmsg.size()>5){
			GosLink2.debugmsg.remove(0);
			GosLink2.debugmsg.add(msg);
		}else{
			GosLink2.debugmsg.add(msg);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bdebug)){
			if(dbm){debugmode(false);}else{debugmode(true);}
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
}