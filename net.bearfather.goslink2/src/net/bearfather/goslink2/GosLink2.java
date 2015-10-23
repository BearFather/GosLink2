package net.bearfather.goslink2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
public class GosLink2 implements Runnable{
public static Properties prop = new Properties();
public static int time;
static{
			InputStream input = null;
			try {
				input = new FileInputStream("config.properties");
				prop.load(input);
			} catch (IOException ex) {
				JFrame frame = null;
				JOptionPane.showMessageDialog(frame, "Can't find config.properties!","No Config File",JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} finally {
				if (input != null) {
					try {
						input.close();
						time=Integer.parseInt(prop.getProperty("time"));
						time=time*60*1000;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	 }
	public static DebugWindow dw=new DebugWindow();  //Non-Linux
//	public static DebugConsole dw=new DebugConsole();//Linux
//	public static outputWindow ow=new outputWindow();  //debug windows
//	public static outputWindow ow2=new outputWindow();  //debug windows
	public static gosbot gb=new gosbot();
	static TelnetService TC1;
	static TelnetService TC2;
	static TelnetService TC3;
	public static HashMap<Integer,TelnetService> TNH=new HashMap<Integer,TelnetService>();
	public static Thread server1;
	public static Thread server2;
	public static Thread server3;
	public static Thread wst;
    public static HashMap<Integer,Thread> SH=new HashMap<Integer,Thread>();
    public static File fnames = new File("names.txt");
    public static File dnames = new File("deny.txt");
    public static Thread HB= new Thread (new HeartBeat());
    public static ArrayList<String> names =new ArrayList<String>();
    public static ArrayList<String> deny =new ArrayList<String>();
    private int tcn;
    static int look=1;

    public static void main(String[] args) {
    	if (prop.getProperty("webchat")!=null&&prop.getProperty("webchat").equals("true")){
    		wst=new Thread(new WebSocket());
    		wst.start();
    	}
    	TC1 = new TelnetService(prop.getProperty("server1"), 23);
    	server1 = new Thread (new GosLink2(1));
    	TNH.put(1, TC1);
    	server1.start();
		SH.put(1,server1);
    	if (!prop.getProperty("server2").equals("none")){
        	TC2 = new TelnetService(prop.getProperty("server2"), 23);
        	server2 = new Thread (new GosLink2(2));
        	TNH.put(2, TC2);
    		server2.start();
    		SH.put(2,server2);
    	}
    	if (!prop.getProperty("server3").equals("none")){
    		TC3 = new TelnetService(prop.getProperty("server3"), 23);
    		server3 = new Thread (new GosLink2(3));
    		TNH.put(3, TC3);
    		server3.start();
    		SH.put(3,server3);
    	}
		HB.start();
		try {
			filerdr();
			denyRdr();
		} catch (IOException e) {e.printStackTrace();}
	}
	@Override
	public void run() {
			while (!SH.get(tcn).isInterrupted()){
				try {
					String rtn=Tclient(tcn);
					if (rtn.equals("reload")){
						TNH.get(tcn).loggedin=0;
						SH.get(tcn).interrupt();
						TNH.get(tcn).killme();
					}
				} catch (SocketException e) {
					dw.append("Server "+tcn+" offline.");
					TNH.get(tcn).loggedin=0;
					SH.get(tcn).interrupt();
				}catch (IOException | InterruptedException e) {e.printStackTrace();System.out.println("thats me");}
			}
	SH.remove(tcn);
	}
	
	public String Tclient(int num) throws SocketException, IOException, InterruptedException{
		TelnetService TC=TNH.get(num);
		TNH.get(num).mynum=num;
		TNH.get(num).myname=prop.getProperty("server"+num+"name");
		String rtn=TC.getTelnetSessionAsString(Integer.toString(num));
		if (rtn.equals("reload")){return rtn;}
		TC.readit(" ","Room error");
		dw.append("Server "+num+": ");
		TC.write("gos Goslink is enabled.");
		TC.readit("\n","Room error");
		TC.write("\n");
		String msg = null;
		TC.whoCheck=true;
		while (TC.loggedin == 1){
			TC.readUntil("gossips:");
			msg=TC.readUntil("\n");
			if (msg.equals("!OffLINE+02")){
			}else if(msg.contains("users in the game.")){
			}else{
				sayit(num,msg);
			}
		}
		dw.append("Server "+num+" is offline.");
		killme(num);
		return "reload";
	}
	public static void sayit(int tc,String msg){
		String tmsg[]=msg.split("<4;2>0:");
		String player = tmsg[0];
		player=player.toLowerCase().trim();
		String u1=GosLink2.prps("muser1").toLowerCase();
		String u2=GosLink2.prps("muser2").toLowerCase();
		String u3=GosLink2.prps("muser3").toLowerCase();
		String sname=prop.getProperty("server"+tc+"name");
		if (TC1.ghost ==1 || TC2!=null&&TC2.ghost == 1 || TC3!=null&&TC3.ghost == 1){tmsg[1]=tmsg[1]+"\n";}
		if (!player.equals(u1)&&!player.equals(u2)&&!player.equals(u3)){
			//dw.append("Server "+tc+" :");
			for(Entry<Integer, TelnetService> t:TNH.entrySet()){
				if (tc!=t.getValue().mynum){
//					TNH.get(t.getKey()).write("gos  "+player+": "+tmsg[1].trim());  //original replace if it fails
					TNH.get(t.getKey()).write("gos  "+sname.substring(0,1)+": "+tmsg[0].trim()+": "+tmsg[1].trim());
				}
			}
			for (WebClient value:WebSocket.channels){
				if (value!=null){value.send(sname+": "+tmsg[0].trim()+": "+tmsg[1].trim());}
			}
		}

	}
	public static void startit(int num){
		if (!TNH.containsKey(num)){
			SH.put(num, new Thread (new GosLink2(num)));
			SH.get(num).start();
			
		}else{
			SH.remove(num);
			SH.put(num, new Thread (new GosLink2(num)));
			SH.get(num).start();
			
		}
	}
    public static void killme(int num) {
    	if (SH.containsKey(num)){
    		SH.get(num).interrupt();
    	}
    }
    @SuppressWarnings("resource")
	public static void filerdr() throws IOException{
    	if (!fnames.exists()){fnames.createNewFile();}
    	BufferedReader rfile = new BufferedReader(new FileReader(fnames));
    	String nme=null;
    	for (int i=0;i<6;i++){
    		nme=rfile.readLine();
    		if (nme!=null && !nme.isEmpty()){
    			names.add(nme);
    		}
    	}
    }
    public static void filewrt() throws FileNotFoundException, UnsupportedEncodingException{
    	PrintWriter wfile = new PrintWriter(fnames, "UTF-8");
    	for (int i=0;i<names.size();i++){
    		wfile.println(names.get(i));
    	}
    	wfile.close();
    }
    @SuppressWarnings("resource")
	public static void denyRdr() throws IOException{
    	if (!dnames.exists()){dnames.createNewFile();}
    	BufferedReader rfile = new BufferedReader(new FileReader(dnames));
    	String nme=null;
    	for (int i=0;i<6;i++){
    		nme=rfile.readLine();
    		if (nme!=null && !nme.isEmpty()){
    			deny.add(nme.toLowerCase().trim());
    		}
    	}
    }
    public int getTcn() {
		return tcn;
	}
	public GosLink2(int set) {
		this.tcn = set;
	}
	public static String prps(String name) {
		return prop.getProperty(name);
	}
	public static int Pi(String line){
		int rtn=0;
		try{rtn=Integer.parseInt(line);} catch (NumberFormatException nfe) {return 0;}
		return rtn;
	}
}