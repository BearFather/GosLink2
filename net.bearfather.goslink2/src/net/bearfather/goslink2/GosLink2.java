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
	public static boolean debug=false;
	public static String entrymsg="GosLink enabled.";
	public static ArrayList<String> msgs=new ArrayList<String>();
	public static int spamtimer=5;
	public static boolean ansi=false;
	public static boolean timestamp=false;
	public static boolean gosbot1=true;
	public static String key="none";
	public static String broadcast="gos";
	public static String admin="sysop";
	static{
				InputStream input = null;
				try {
					input = new FileInputStream("config.properties");
					prop.load(input);
				} catch (IOException ex) {
					System.out.println("No config.properties. Please copy template or launch in a gui.");
					JFrame frame = null;
					JOptionPane.showMessageDialog(frame, "Can't find config.properties!\n Launching Settings...","No Config File",JOptionPane.ERROR_MESSAGE);
					new SettingsFrame(false);
					System.exit(0);
				} finally {
					if (input != null) {
						try {
							input.close();
							time=Integer.parseInt(prop.getProperty("time"));
							time=time*60*1000;
							if(prop.getProperty("debug")!=null){if(prop.getProperty("debug").toLowerCase().equals("true")){debug=true;}}
							if(prop.getProperty("ansi")!=null){if(prop.getProperty("ansi").toLowerCase().equals("true")){ansi=true;}}
							if(prop.getProperty("timestamp")!=null){if(prop.getProperty("timestamp").toLowerCase().equals("true")){timestamp=true;}}
							if(prop.getProperty("key")!=null){key=prop.getProperty("key");}
							if(prop.getProperty("gosbot")!=null){if(prop.getProperty("gosbot").toLowerCase().equals("false")){gosbot1=false;}}
							if(prop.getProperty("broadcast")!=null){broadcast=prop.getProperty("broadcast");}
							if(prop.getProperty("admin")!=null){admin=prop.getProperty("admin").toLowerCase();}
						} catch (IOException e) {e.printStackTrace();}
					}
				}
				InputStream m=null;
				Properties fmsgs = new Properties();
				try{
					m=new FileInputStream("messages.cfg");
					fmsgs.load(m);
				}catch(IOException e){
				}finally{
					if (m!=null){
						try {
							m.close();
							if (fmsgs.getProperty("entrymsg")!=null&&!fmsgs.getProperty("entrymsg").isEmpty()){entrymsg=fmsgs.getProperty("entrymsg");}
							if (fmsgs.getProperty("tmsg1") != null&&!fmsgs.getProperty("tmsg1").isEmpty()){msgs.add(fmsgs.getProperty("tmsg1"));}
							if (fmsgs.getProperty("tmsg2") != null&&!fmsgs.getProperty("tmsg2").isEmpty()){msgs.add(fmsgs.getProperty("tmsg2"));}
							if (fmsgs.getProperty("tmsg3") != null&&!fmsgs.getProperty("tmsg3").isEmpty()){msgs.add(fmsgs.getProperty("tmsg3"));}
							if (fmsgs.getProperty("tmsg4") != null&&!fmsgs.getProperty("tmsg4").isEmpty()){msgs.add(fmsgs.getProperty("tmsg4"));}
							if (fmsgs.getProperty("tmsg5") != null&&!fmsgs.getProperty("tmsg5").isEmpty()){msgs.add(fmsgs.getProperty("tmsg5"));}
							if (fmsgs.getProperty("tmsg6") != null&&!fmsgs.getProperty("tmsg6").isEmpty()){msgs.add(fmsgs.getProperty("tmsg6"));}
							if (fmsgs.getProperty("timer") != null&&!fmsgs.getProperty("timer").isEmpty()){spamtimer=Pi(fmsgs.getProperty("timer"));}
						} catch (IOException e) {}
					}
				}
		 }
	public static DebugConsole dw=new DebugConsole();
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
    public static ArrayList<String> debugmsg=new ArrayList<String>();
    public static String[] log=new String[50];

    public static void main(String[] args) {
    	GosLink2.dw.append("Double Click the window for settings.");
    	if (props("webchat")!=null&&props("webchat").equals("true")){
    		wst=new Thread(new WebSocket());
    		wst.start();
    	}
    	TC1 = new TelnetService(props("server1"), 23);
    	server1 = new Thread (new GosLink2(1));
    	TNH.put(1, TC1);
    	server1.start();
		SH.put(1,server1);
    	if (!props("server2").equals("none")&&!props("server2").equals("")){
        	TC2 = new TelnetService(props("server2"), 23);
        	server2 = new Thread (new GosLink2(2));
        	TNH.put(2, TC2);
    		server2.start();
    		SH.put(2,server2);
    	}
    	if (!props("server3").equals("none")&&!props("server3").equals("")){
    		TC3 = new TelnetService(props("server3"), 23);
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
				}catch (IOException | InterruptedException e) {e.printStackTrace();dw.append("Server"+tcn+": Bad Server Address");if (SH.containsKey(tcn)){SH.get(tcn).interrupt();}}
			}
	SH.remove(tcn);
	}
	
	public String Tclient(int num) throws SocketException, IOException, InterruptedException{
		TelnetService TC=TNH.get(num);
		TNH.get(num).mynum=num;
		TNH.get(num).myname=props("server"+num+"name");
		String rtn=TC.getTelnetSessionAsString(Integer.toString(num));
		if (rtn.equals("reload")){return rtn;}
		TC.readit(" ","Room error");
		dw.append("Server "+num+": ");
		TC.write(broadcast+" "+entrymsg);
		TC.readit("\n","Room error");
		TC.write("\n");
		String msg = null;
		TC.whoCheck=true;
		while (TC.loggedin == 1){
			TC.readUntil("gossips:");
			msg=TC.readUntil("\n");
			if (msg!=null){
				if (msg.equals("!OffLINE+02")){
				}else if(msg.contains("users in the game.")){
				}else{
					TelnetService.player="";
					sayit(num,msg);
				}
			}
		}
		dw.append("Server "+num+" is offline.");
		killme(num);
		return "reload";
	}
	public static void sayit(int tc,String msg){ //this will send on chosen command auc/gos/br
		String tmsg[]=msg.split("<4;2>0:");
		if (tmsg!=null&&tmsg.length>1){
			String player = tmsg[0];
			player=player.toLowerCase().trim();
			String u1=props("muser1").toLowerCase();
			String u2=props("muser2").toLowerCase();
			String u3=props("muser3").toLowerCase();
			String sname=props("server"+tc+"name");
			if (TC1.ghost ==1 || TC2!=null&&TC2.ghost == 1 || TC3!=null&&TC3.ghost == 1){tmsg[1]=tmsg[1]+"\n";}
			if (!player.equals(u1)&&!player.equals(u2)&&!player.equals(u3)){
				for(Entry<Integer, TelnetService> t:TNH.entrySet()){
					if (tc!=t.getValue().mynum&&tmsg!=null&&tmsg.length>1){
						String temp=broadcast+"  "+sname.substring(0,1)+": "+tmsg[0].trim()+": "+tmsg[1].trim();
						TNH.get(t.getKey()).write(temp);
					}
				}
				for (WebClient value:WebSocket.channels){
					if (value!=null){value.send(sname+": "+tmsg[0].trim()+": "+tmsg[1].trim());}
				}
				if (tmsg!=null&&tmsg.length>1){log=shift(log,tmsg[0].trim()+" gossips: "+tmsg[1].trim());}
			}
		}
	}
	public static void sayit(String msg){// this will send to all channels on gos only
		for(Entry<Integer, TelnetService> t:GosLink2.TNH.entrySet()){
			if (GosLink2.TNH.get(t.getKey()).loggedin==1){GosLink2.TNH.get(t.getKey()).write("gos "+msg);}
		}
		log=shift(log,msg.trim());
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
    	while(rfile.ready()){
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
    	while (rfile.ready()){
    		nme=rfile.readLine();
    		if (nme!=null && !nme.isEmpty()){
    			deny.add(nme.toLowerCase().trim());
    		}
    	}
    }
    @SuppressWarnings("resource")
 	public static ArrayList<String> filerdr(String sfile) throws IOException{
    	File file=new File(sfile);
    	ArrayList<String> rtn=new ArrayList<String>();
    	if (!file.exists()){file.createNewFile();}
     	BufferedReader rfile = new BufferedReader(new FileReader(file));
     	String nme=null;
     	while(rfile.ready()){
     		nme=rfile.readLine();
     		if (nme!=null && !nme.isEmpty()){
     			rtn.add(nme);
     		}
     	}
     	rfile.close();
     	return rtn;
     }
     public static void filewrt(String file,ArrayList<String> data,boolean add) throws IOException{
		try {
			if (add) {
				ArrayList<String> ndata;
				ndata = filerdr(file);
				data.addAll(0,ndata);
				
			}
		} catch (IOException e) {}
    	PrintWriter wfile = new PrintWriter(file, "UTF-8");
    	for (int i=0;i<data.size();i++){
    		wfile.println(data.get(i));
    	}
    	wfile.close();
     }
    public int getTcn() {
		return tcn;
	}
	public GosLink2(int set) {
		this.tcn = set;
	}
	public static String props(String name) {
		return prop.getProperty(name);
	}
	public static int Pi(String line){
		int rtn=0;
		try{rtn=Integer.parseInt(line);} catch (NumberFormatException nfe) {return 0;}
		return rtn;
	}
	public static String[] shift(String[] msgs, String msg){
		  int num = msgs.length - 1 ;
		  String[] rtn=new String[num+1];
		   System.arraycopy( msgs, 1, rtn, 0, num);
		   rtn[num]=msg;
		return rtn;
	}
}