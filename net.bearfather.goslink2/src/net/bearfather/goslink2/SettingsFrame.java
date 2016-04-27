package net.bearfather.goslink2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.util.Properties;

import javax.swing.JButton;

import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SettingsFrame extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	JDialog dialog=new JDialog(GosLink2.dw,true);//for gui
	//JDialog dialog=new JDialog(); //for non gui
	JMenuItem mmain = new JMenuItem("General");
	JMenuItem mall = new JMenuItem("All");
	JMenuItem mserver1 = new JMenuItem("Server1");
	JMenuItem mserver2 = new JMenuItem("Server2");
	JMenuItem mserver3 = new JMenuItem("Server3");
	JButton bok = new JButton("Ok");
	JButton bcancel = new JButton("Cancel");
	JCheckBox cwindow = new JCheckBox("Windows Mode");
	JCheckBox cdeny = new JCheckBox("Deny alignment");
	JCheckBox cgosbot = new JCheckBox("Admin Bot");
	JCheckBox cwebchat = new JCheckBox("WebChat");
	JCheckBox cdebug = new JCheckBox("Debug Mode");
	JCheckBox ctimestamp = new JCheckBox("TimeStamp");
	JCheckBox cansi = new JCheckBox("Ansi Command");
	private JTextField twebuser;
	private JTextField twebpass;
	private JTextField tkey;
	private JTextField twebport;
	private JTextField tpuser;
	private JTextField tppass;
	private JTextField tpmenu;
	private JTextField tpmud;
	private JTextField tcleanup;
	private JTextField ttime;
	CardLayout cl=new CardLayout(0, 0);
	JPanel panel = new JPanel();
	private JTextField s1servername;
	private JTextField s1server;
	private JTextField s1user;
	private JTextField s1pass;
	private JTextField s1game;
	private JTextField s1muser;
	private JTextField s2muser;
	private JTextField s2game;
	private JTextField s2pass;
	private JTextField s2user;
	private JTextField s2server;
	private JTextField s2servername;
	private JTextField s3muser;
	private JTextField s3game;
	private JTextField s3pass;
	private JTextField s3user;
	private JTextField s3server;
	private JTextField s3servername;
	private String clplace="";
	public static Properties prop = GosLink2.prop;
	private String blanks="";
	boolean loadprops;
	public SettingsFrame(boolean loadprops) {
		this.loadprops=loadprops;
		setupPanel();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object e=arg0.getSource();
		if (e.equals(mmain)){cl.show(panel, "main");clplace="main";}
		else if (e.equals(mall)){cl.show(panel, "all");clplace="all";}
		else if (e.equals(mserver1)){cl.show(panel, "server1");clplace="server1";}
		else if (e.equals(mserver2)){cl.show(panel, "server2");clplace="server2";}
		else if (e.equals(mserver3)){cl.show(panel, "server3");clplace="server3";}
		else if (e.equals(bcancel)){dialog.dispose();}
		else if (e.equals(bok)){
			if (chkFields()){
				save();
				JOptionPane.showMessageDialog(dialog, "You have to restart GosLink for settings to take effect.","Restart GosLink",JOptionPane.ERROR_MESSAGE);
				dialog.dispose();
			}else{JOptionPane.showMessageDialog(dialog, "You have critical settings not complete.\nPlease check these fields:\n"+blanks,"Settings not complete.",JOptionPane.ERROR_MESSAGE);}
		}
		shader();
	}
	private void save(){
		prop.clear();
		prop.setProperty("server1name", s1servername.getText());
		prop.setProperty("server1", s1server.getText());
		prop.setProperty("user1", s1user.getText());
		prop.setProperty("pass1", s1pass.getText());
		prop.setProperty("muser1", s1muser.getText());
		prop.setProperty("game1", s1game.getText());
		prop.setProperty("server2name", s2servername.getText());
		prop.setProperty("server2", s2server.getText());
		prop.setProperty("user2", s2user.getText());
		prop.setProperty("pass2", s2pass.getText());
		prop.setProperty("muser2", s2muser.getText());
		prop.setProperty("game2", s2game.getText());
		prop.setProperty("server3name", s3servername.getText());
		prop.setProperty("server3", s3server.getText());
		prop.setProperty("user3", s3user.getText());
		prop.setProperty("pass3", s3pass.getText());
		prop.setProperty("muser3", s3muser.getText());
		prop.setProperty("game3", s3game.getText());
		prop.setProperty("puser", tpuser.getText());
		prop.setProperty("ppass", tppass.getText());
		prop.setProperty("pmenu", tpmenu.getText());
		prop.setProperty("pmud", tpmud.getText());
		prop.setProperty("cleanup", tcleanup.getText());
		prop.setProperty("time", ttime.getText());
		prop.setProperty("webuser", twebuser.getText());
		prop.setProperty("webpass", twebpass.getText());
		String chk="false";
		if (cwindow.isSelected()){chk="true";}
		prop.setProperty("window", chk);
		chk="false";
		if (cwebchat.isSelected()){chk="true";}
		prop.setProperty("webchat", chk);
		chk="false";
		if (cdeny.isSelected()){chk="true";}
		prop.setProperty("deny", chk);
		//Optional settings.
		if (!cgosbot.isSelected()){prop.setProperty("gosbot", "false");}
		if (tkey.getText()!=null&&!tkey.getText().equals("")){prop.setProperty("key", tkey.getText());}
		if (twebport.getText()!=null&&!twebport.getText().equals("")&&!twebport.equals("3000")){prop.setProperty("webport", twebport.getText());}
		if (cansi.isSelected()){prop.setProperty("ansi", "true");}
		if (ctimestamp.isSelected()){prop.setProperty("timestamp", "true");}
		if (cdebug.isSelected()){
			prop.setProperty("debug", "true");
			}
		
		
		saveProps();
	}
	private void load(){
		s1servername.setText(prop.getProperty("server1name"));
		s1server.setText(prop.getProperty("server1"));
		s1user.setText(prop.getProperty("user1"));
		s1pass.setText(prop.getProperty("pass1"));
		s1muser.setText(prop.getProperty("muser1"));
		s1game.setText(prop.getProperty("game1"));
		s2servername.setText(prop.getProperty("server2name"));
		s2server.setText(prop.getProperty("server2"));
		s2user.setText(prop.getProperty("user2"));
		s2pass.setText(prop.getProperty("pass2"));
		s2muser.setText(prop.getProperty("muser2"));
		s2game.setText(prop.getProperty("game2"));
		s3servername.setText(prop.getProperty("server3name"));
		s3server.setText(prop.getProperty("server3"));
		s3user.setText(prop.getProperty("user3"));
		s3pass.setText(prop.getProperty("pass3"));
		s3muser.setText(prop.getProperty("muser3"));
		s3game.setText(prop.getProperty("game3"));
		tpuser.setText(prop.getProperty("puser"));
		tppass.setText(prop.getProperty("ppass"));
		tpmenu.setText(prop.getProperty("pmenu"));
		tpmud.setText(prop.getProperty("pmud"));
		tcleanup.setText(prop.getProperty("cleanup"));
		ttime.setText(prop.getProperty("time"));
		twebuser.setText(prop.getProperty("webuser"));
		twebpass.setText(prop.getProperty("webpass"));
		if (prop.getProperty("window").toLowerCase().equals("true")){cwindow.setSelected(true);}
		if (prop.getProperty("webchat").toLowerCase().equals("true")){cwebchat.setSelected(true);}
		if (prop.getProperty("deny").toLowerCase().equals("true")){cdeny.setSelected(true);}
		if (prop.getProperty("gosbot")!=null&&prop.getProperty("gosbot").toLowerCase().equals("false")){cgosbot.setSelected(false);}
		if (prop.getProperty("key")!=null){tkey.setText(prop.getProperty("key"));}
		if (prop.getProperty("webport")!=null){twebport.setText(prop.getProperty("webport"));}
		if (prop.getProperty("ansi")!=null&&prop.getProperty("ansi").toLowerCase().equals("true")){cansi.setSelected(true);}
		if (prop.getProperty("timestamp")!=null&&prop.getProperty("timestamp").toLowerCase().equals("true")){ctimestamp.setSelected(true);}
		if (prop.getProperty("debug")!=null&&prop.getProperty("debug").toLowerCase().equals("true")){cdebug.setSelected(true);}

	}
	private boolean chkFields(){
		boolean empty=false;
		if (cwebchat.isSelected()){
			if (twebuser.getText().equals("")){empty=true;blanks+="WebUser\n";}
			if (twebpass.getText().equals("")){empty=true;blanks+="Webpass\n";}
		}
		if (tpuser.getText().equals("")){empty=true;blanks+="BBS User\n";}
		if (tppass.getText().equals("")){empty=true;blanks+="BBS P/W\n";}
		if (tpmenu.getText().equals("")){empty=true;blanks+="BBS Menu\n";}
		if (tpmud.getText().equals("")){empty=true;blanks+="Mud Menu\n";}
		if (tcleanup.getText().equals("")){empty=true;blanks+="Cleanup Msg\n";}
		if (ttime.getText().equals("")){empty=true;blanks+="Cleanup Time\n";}
		if (!ttime.getText().equals("")){
			int chk=Pi(ttime.getText(), -1);
			if (chk==-1){empty=true;blanks+="Cleanup Time must be a number\n";}
		}
		if (s1servername.getText().equals("")){empty=true;blanks+="Server1 Server Name\n";}
		if (s1server.getText().equals("")){empty=true;blanks+="Server1 Server Addy\n";}
		if (s1user.getText().equals("")){empty=true;blanks+="Server1 BBS User\n";}
		if (s1pass.getText().equals("")){empty=true;blanks+="Server1 BBS Pass\n";}
		if (s1game.getText().equals("")){empty=true;blanks+="Server1 BBS Menu\n";}
		if (s1muser.getText().equals("")){empty=true;blanks+="Server1 Mud User\n";}
		
		if (empty){return false;}
		return true;
	}
	private void shader(){
		mmain.setBackground(null);
		mall.setBackground(null);
		mserver1.setBackground(null);
		mserver2.setBackground(null);
		mserver3.setBackground(null);
		if (clplace.equals("main")){mmain.setBackground(new Color(190,190,190));}
		else if(clplace.equals("all")){mall.setBackground(new Color(190,190,190));}
		else if(clplace.equals("server1")){mserver1.setBackground(new Color(190,190,190));}
		else if(clplace.equals("server2")){mserver2.setBackground(new Color(190,190,190));}
		else if(clplace.equals("server3")){mserver3.setBackground(new Color(190,190,190));}
	}
	private void listeners(){
		mmain.addActionListener(this);
		mall.addActionListener(this);
		mserver1.addActionListener(this);
		mserver2.addActionListener(this);
		mserver3.addActionListener(this);
		bcancel.addActionListener(this);
		bok.addActionListener(this);
	}
	public void setupPanel(){
		dialog.getContentPane().setLayout(new BorderLayout(0, 0));
		JMenuBar menuBar = new JMenuBar();
		dialog.getContentPane().add(menuBar, BorderLayout.NORTH);
		menuBar.setBounds(0, 0, 449, 21);
		
		mmain.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mmain);
		mmain.setBackground(new Color(190,190,190));
		mall.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mall);
		mserver1.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mserver1);
		mserver2.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mserver2);
		mserver3.setHorizontalAlignment(SwingConstants.LEFT);
		menuBar.add(mserver3);
		
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(cl);
		
		JPanel pmain = new JPanel();
		pmain.setBorder(null);
		panel.add(pmain, "main");
		pmain.setLayout(null);
		
		cwindow.setBounds(22, 7, 114, 23);
		pmain.add(cwindow);
		
		cdeny.setBounds(138, 33, 114, 23);
		pmain.add(cdeny);
		
		cgosbot.setBounds(138, 7, 114, 23);
		pmain.add(cgosbot);
		cgosbot.setSelected(true);
		cwebchat.setBounds(254, 7, 114, 23);
		pmain.add(cwebchat);
		
		cdebug.setBounds(157, 134, 114, 23);
		pmain.add(cdebug);
		
		ctimestamp.setBounds(22, 33, 114, 23);
		pmain.add(ctimestamp);
		
		cansi.setToolTipText("Issue '=a' after login.\r\nOnly use if you can't disable in account settings");
		cansi.setBounds(254, 33, 114, 23);
		pmain.add(cansi);
		
		twebuser = new JTextField();
		twebuser.setBounds(99, 63, 86, 20);
		pmain.add(twebuser);
		twebuser.setColumns(10);
		
		JLabel lblWebuser = new JLabel("Web User:");
		lblWebuser.setHorizontalAlignment(SwingConstants.TRAILING);
		lblWebuser.setBounds(22, 66, 67, 14);
		pmain.add(lblWebuser);
		
		JLabel lblWebPassword = new JLabel("Web P/W:");
		lblWebPassword.setBounds(32, 91, 57, 14);
		pmain.add(lblWebPassword);
		
		twebpass = new JTextField();
		twebpass.setBounds(99, 94, 86, 20);
		pmain.add(twebpass);
		twebpass.setColumns(10);
		
		JLabel lblGlobalKey = new JLabel("Global Key:");
		lblGlobalKey.setBounds(225, 97, 67, 14);
		pmain.add(lblGlobalKey);
		
		tkey = new JTextField();
		tkey.setBounds(292, 94, 86, 20);
		pmain.add(tkey);
		tkey.setColumns(10);
		
		JLabel lblWebPort = new JLabel("Web Port:");
		lblWebPort.setHorizontalAlignment(SwingConstants.TRAILING);
		lblWebPort.setBounds(215, 66, 67, 14);
		pmain.add(lblWebPort);
		
		twebport = new JTextField();
		twebport.setBounds(292, 63, 86, 20);
		pmain.add(twebport);
		twebport.setColumns(10);
		JPanel pall = new JPanel();
		panel.add(pall, "all");
		pall.setLayout(null);
		
		JLabel lblBbsUserPrompt = new JLabel("BBS User Prompt:");
		lblBbsUserPrompt.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsUserPrompt.setBounds(10, 14, 115, 14);
		pall.add(lblBbsUserPrompt);
		
		tpuser = new JTextField();
		tpuser.setText("Otherwise type \\\"\u001B[36mnew\u001B[32m\\\":");
		tpuser.setBounds(135, 11, 304, 20);
		pall.add(tpuser);
		tpuser.setColumns(10);
		
		JLabel lblBbsPwPromp = new JLabel("BBS P/W Promp:");
		lblBbsPwPromp.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsPwPromp.setBounds(20, 39, 105, 14);
		pall.add(lblBbsPwPromp);
		
		tppass = new JTextField();
		tppass.setText("Enter your password:");
		tppass.setBounds(135, 36, 304, 20);
		pall.add(tppass);
		tppass.setColumns(10);
		
		tpmenu = new JTextField();
		tpmenu.setText("help, or,X to exit");
		tpmenu.setBounds(135, 61, 304, 20);
		pall.add(tpmenu);
		tpmenu.setColumns(10);
		
		tpmud = new JTextField();
		tpmud.setText("[MAJORMUD]:");
		tpmud.setBounds(135, 86, 304, 20);
		pall.add(tpmud);
		tpmud.setColumns(10);
		
		tcleanup = new JTextField();
		tcleanup.setText("Sorry to interrupt here, but the server will be shutting");
		tcleanup.setBounds(135, 111, 304, 20);
		pall.add(tcleanup);
		tcleanup.setColumns(10);
		
		JLabel lblBbsMainMenu = new JLabel("BBS Main Menu:");
		lblBbsMainMenu.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsMainMenu.setBounds(10, 64, 115, 14);
		pall.add(lblBbsMainMenu);
		
		JLabel lblMajormudMenu = new JLabel("MajorMud Menu:");
		lblMajormudMenu.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMajormudMenu.setBounds(20, 89, 105, 14);
		pall.add(lblMajormudMenu);
		
		JLabel lblCleanupMessage = new JLabel("Cleanup Message:");
		lblCleanupMessage.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCleanupMessage.setBounds(10, 114, 114, 14);
		pall.add(lblCleanupMessage);
		
		JLabel lblCleanupTime = new JLabel("Cleanup Time:");
		lblCleanupTime.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCleanupTime.setBounds(20, 139, 105, 14);
		pall.add(lblCleanupTime);
		
		ttime = new JTextField();
		ttime.setText("1");
		ttime.setBounds(135, 136, 28, 20);
		pall.add(ttime);
		ttime.setColumns(10);
		
		JPanel pserver1 = new JPanel();
		panel.add(pserver1, "server1");
		pserver1.setLayout(null);
		
		JLabel lblServerName = new JLabel("Server Name:");
		lblServerName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblServerName.setBounds(83, 14, 114, 14);
		pserver1.add(lblServerName);
		
		JLabel lblServerAddress = new JLabel("Server Address:");
		lblServerAddress.setHorizontalAlignment(SwingConstants.TRAILING);
		lblServerAddress.setBounds(83, 39, 114, 14);
		pserver1.add(lblServerAddress);
		
		JLabel lblBbsUsername = new JLabel("BBS Username:");
		lblBbsUsername.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsUsername.setBounds(83, 64, 114, 14);
		pserver1.add(lblBbsUsername);
		
		JLabel lblBbsPassword = new JLabel("BBS Password:");
		lblBbsPassword.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsPassword.setBounds(83, 89, 114, 14);
		pserver1.add(lblBbsPassword);
		
		JLabel lblMajormudUser = new JLabel("MajorMud User:");
		lblMajormudUser.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMajormudUser.setBounds(83, 139, 114, 14);
		pserver1.add(lblMajormudUser);
		
		JLabel lblBbsMenuCommand = new JLabel("BBS Menu Command:");
		lblBbsMenuCommand.setHorizontalAlignment(SwingConstants.TRAILING);
		lblBbsMenuCommand.setBounds(21, 114, 176, 14);
		pserver1.add(lblBbsMenuCommand);
		
		s1servername = new JTextField();
		s1servername.setBounds(207, 11, 136, 20);
		pserver1.add(s1servername);
		s1servername.setColumns(10);
		
		s1server = new JTextField();
		s1server.setBounds(207, 36, 136, 20);
		pserver1.add(s1server);
		s1server.setColumns(10);
		
		s1user = new JTextField();
		s1user.setBounds(207, 61, 136, 20);
		pserver1.add(s1user);
		s1user.setColumns(10);
		
		s1pass = new JTextField();
		s1pass.setBounds(207, 86, 136, 20);
		pserver1.add(s1pass);
		s1pass.setColumns(10);
		
		s1game = new JTextField();
		s1game.setBounds(207, 111, 136, 20);
		pserver1.add(s1game);
		s1game.setColumns(10);
		
		s1muser = new JTextField();
		s1muser.setBounds(207, 136, 136, 20);
		pserver1.add(s1muser);
		s1muser.setColumns(10);
		
		JPanel pserver2 = new JPanel();
		panel.add(pserver2, "server2");
		pserver2.setLayout(null);
		
		JLabel label = new JLabel("Server Name:");
		label.setHorizontalAlignment(SwingConstants.TRAILING);
		label.setBounds(83, 14, 114, 14);
		pserver2.add(label);
		
		JLabel label_1 = new JLabel("Server Address:");
		label_1.setHorizontalAlignment(SwingConstants.TRAILING);
		label_1.setBounds(83, 39, 114, 14);
		pserver2.add(label_1);
		
		JLabel label_2 = new JLabel("BBS Username:");
		label_2.setHorizontalAlignment(SwingConstants.TRAILING);
		label_2.setBounds(83, 64, 114, 14);
		pserver2.add(label_2);
		
		JLabel label_3 = new JLabel("BBS Password:");
		label_3.setHorizontalAlignment(SwingConstants.TRAILING);
		label_3.setBounds(83, 89, 114, 14);
		pserver2.add(label_3);
		
		JLabel label_4 = new JLabel("BBS Menu Command:");
		label_4.setHorizontalAlignment(SwingConstants.TRAILING);
		label_4.setBounds(27, 114, 170, 14);
		pserver2.add(label_4);
		
		JLabel label_5 = new JLabel("MajorMud User:");
		label_5.setHorizontalAlignment(SwingConstants.TRAILING);
		label_5.setBounds(83, 139, 114, 14);
		pserver2.add(label_5);
		
		s2muser = new JTextField();
		s2muser.setColumns(10);
		s2muser.setBounds(207, 136, 136, 20);
		pserver2.add(s2muser);
		
		s2game = new JTextField();
		s2game.setColumns(10);
		s2game.setBounds(207, 111, 136, 20);
		pserver2.add(s2game);
		
		s2pass = new JTextField();
		s2pass.setColumns(10);
		s2pass.setBounds(207, 86, 136, 20);
		pserver2.add(s2pass);
		
		s2user = new JTextField();
		s2user.setColumns(10);
		s2user.setBounds(207, 61, 136, 20);
		pserver2.add(s2user);
		
		s2server = new JTextField();
		s2server.setColumns(10);
		s2server.setBounds(207, 36, 136, 20);
		pserver2.add(s2server);
		
		s2servername = new JTextField();
		s2servername.setColumns(10);
		s2servername.setBounds(207, 11, 136, 20);
		pserver2.add(s2servername);
		
		JPanel pserver3 = new JPanel();
		panel.add(pserver3, "server3");
		pserver3.setLayout(null);
		
		JLabel label_6 = new JLabel("Server Name:");
		label_6.setHorizontalAlignment(SwingConstants.TRAILING);
		label_6.setBounds(83, 14, 114, 14);
		pserver3.add(label_6);
		
		JLabel label_7 = new JLabel("Server Address:");
		label_7.setHorizontalAlignment(SwingConstants.TRAILING);
		label_7.setBounds(83, 39, 114, 14);
		pserver3.add(label_7);
		
		JLabel label_8 = new JLabel("BBS Username:");
		label_8.setHorizontalAlignment(SwingConstants.TRAILING);
		label_8.setBounds(83, 64, 114, 14);
		pserver3.add(label_8);
		
		JLabel label_9 = new JLabel("BBS Password:");
		label_9.setHorizontalAlignment(SwingConstants.TRAILING);
		label_9.setBounds(83, 89, 114, 14);
		pserver3.add(label_9);
		
		JLabel label_10 = new JLabel("BBS Menu Command:");
		label_10.setHorizontalAlignment(SwingConstants.TRAILING);
		label_10.setBounds(20, 114, 177, 14);
		pserver3.add(label_10);
		
		JLabel label_11 = new JLabel("MajorMud User:");
		label_11.setHorizontalAlignment(SwingConstants.TRAILING);
		label_11.setBounds(83, 139, 114, 14);
		pserver3.add(label_11);
		
		s3muser = new JTextField();
		s3muser.setColumns(10);
		s3muser.setBounds(207, 136, 136, 20);
		pserver3.add(s3muser);
		
		s3game = new JTextField();
		s3game.setColumns(10);
		s3game.setBounds(207, 111, 136, 20);
		pserver3.add(s3game);
		
		s3pass = new JTextField();
		s3pass.setColumns(10);
		s3pass.setBounds(207, 86, 136, 20);
		pserver3.add(s3pass);
		
		s3user = new JTextField();
		s3user.setColumns(10);
		s3user.setBounds(207, 61, 136, 20);
		pserver3.add(s3user);
		
		s3server = new JTextField();
		s3server.setColumns(10);
		s3server.setBounds(207, 36, 136, 20);
		pserver3.add(s3server);
		
		s3servername = new JTextField();
		s3servername.setColumns(10);
		s3servername.setBounds(207, 11, 136, 20);
		pserver3.add(s3servername);
		
		JPanel button = new JPanel();
		dialog.getContentPane().add(button, BorderLayout.SOUTH);
		button.setLayout(new BorderLayout(0, 0));
		
		button.add(bok, BorderLayout.WEST);
		
		button.add(bcancel, BorderLayout.EAST);
		listeners();
		button.setSize(120, 275);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(SettingsFrame.class.getResource("/net/bearfather/goslink2/smokin.png")));
		dialog.setTitle("GosLink Settings");
		dialog.setSize(465,259);
		if (loadprops){load();}
		else{cwindow.setSelected(true);}
		dialog.setVisible(true);
	}
	public static int Pi(String line,int rtn){
		try{rtn=Integer.parseInt(line);} catch (NumberFormatException nfe) {return rtn;}
		return rtn;
	}
    public static void saveProps(){
    	OutputStream coutput = null;
    	try {
    		coutput = new FileOutputStream("config.properties");
    		prop.store(coutput, null);
    	} catch (IOException io) {
    		JFrame frame = null;
			JOptionPane.showMessageDialog(frame, "Can't write config.properties!\nDo you have write permission?","Can't write",JOptionPane.ERROR_MESSAGE);
    	} finally {
    		if (coutput != null) {try {coutput.close();} catch (IOException e) {}}
    	}
    }
}
