package net.bearfather.goslink2;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;

public class WebSocket implements Runnable{
	private static int port=3000;
	private static String user=GosLink2.prps("webuser");
	private static String pass=GosLink2.prps("webpass");
	static ServerSocket server;
	public static boolean run=false;
	public static WebClient[] channels=new WebClient[50];//TODO need to flesh out channels like removing them
	public static String[] players=new String[4];
		
	public WebSocket(){
		GosLink2.dw.append("Web started!");
		Arrays.fill(channels,null);
		
	}
	@Override
	public void run() {
		run=true;
		listenSocket();
	}
	
	public static void listenSocket() {
		try{server = new ServerSocket(port);} catch (IOException e) {System.out.println("Could not listen on port "+port);run=false;}
		while(run==true){
			WebClient w;
			try{
				w = new WebClient(server.accept());
				w.setPass(pass);
				w.setUser(user);
			    Thread t = new Thread(w);
			    findChannel(w);
			    t.start();
				System.out.println("started connection");
			} catch (IOException e) {
				System.out.println("Accept failed: 3000");
				}
		}
	}		
	
	
	
	private static int findChannel(WebClient client){
		int rtn=-1;
			int c=0;
			for (WebClient value:channels){
				if (value==null){
					channels[c]=client;
					client.channel=c;
					return c;
				}
				c++;
			}
		return rtn;
	}
	public static int checkCmd(String line,WebClient w){//0=no command, 1=command, 2=auto message(don't reset timer),3=hangup
		int rtn=0;
		if (line==null){
			return 99;
		}
		else if (line.startsWith("/who")){
			checkWho();
			w.write(":!:server1:!:"+players[1]+":!:server2:!:"+players[2]+":!:server3:!:"+players[3]+":!:web:!:"+players[0]);
			return 1;
		}
		else if (line.trim().equals(":!:pong:!:")){
        	w.timer.ping=false;
        	return 2;
        }
		else if (line.trim().equals(":!:hangup:!:")){
			GosLink2.dw.append("hanging up");
			return 3;
		}

		return rtn;
	}
	public static void checkWho(){
		try {
			players[0]="Web users to be done still.";
			if (GosLink2.TC1!=null&&GosLink2.TC1.loggedin==1){players[1]=ICheaker.whoCheck(GosLink2.TNH.get(1));}else{players[1]="offline";}
			if (GosLink2.TC2!=null&&GosLink2.TC2.loggedin==1){players[2]=ICheaker.whoCheck(GosLink2.TNH.get(2));}else{players[2]="offline";}
			if (GosLink2.TC3!=null&&GosLink2.TC3.loggedin==1){players[3]=ICheaker.whoCheck(GosLink2.TNH.get(3));}else{players[3]="offline";}
		} catch (InterruptedException | IOException e) {}
		
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		WebSocket.port = port;
	}

}
