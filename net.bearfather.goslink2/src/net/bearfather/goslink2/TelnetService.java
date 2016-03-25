package net.bearfather.goslink2;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;
public class TelnetService {
	private final String server;
	private final int port;
	public TelnetClient telnet = new TelnetClient();
	private InputStream dataIn;
	private PrintStream dataOut;
	public static String player;
	public int loggedin=0;
	public static int runner=1;
	public int ghost =0;
	public int mynum=0;
	public String myname="";
	public boolean whoCheck=false;
	public String oPlayers="";
	String hangup=GosLink2.props("cleanup");
    String nonstop="(N)onstop, (Q)uit, or (C)ontinue?";
    String ghosts="Enter your password to end the other connection and log on.";
   	//public boolean ping =true;
   	int cnt;
   	
	public TelnetService(String server, int port) {
		this.server = server.replace("http://", "");
		this.port = port;
	}
	public void killme() throws IOException{
		GosLink2.dw.append("Ghost detected.  Reloging.");
		telnet.disconnect();
	}
	private void startTelnetSession() throws SocketException, IOException {
		telnet.connect(server, port);
		dataIn = telnet.getInputStream();
		dataOut = new PrintStream(telnet.getOutputStream());
	}
	public String getTelnetSessionAsString(String tcb) throws SocketException, IOException, InterruptedException {
        startTelnetSession();
        String rtn="blah";
        GosLink2.dw.append("Logging into server "+tcb+".");
        if (tcb.equals("1")){
        	rtn=loginUser(GosLink2.props("user1"),GosLink2.props("game1"),GosLink2.props("pass1"));
        }
        else if (tcb.equals("2")){
        	rtn=loginUser(GosLink2.props("user2"),GosLink2.props("game2"),GosLink2.props("pass2"));
        }
        else{
        	rtn=loginUser(GosLink2.props("user3"),GosLink2.props("game3"),GosLink2.props("pass3"));
        }
        if (rtn.equals("reload")){return rtn;}
        loggedin=1;
        return "Logged in";
}
	private String loginUser(String name, String cmd,String pass) throws InterruptedException, IOException {
		if (GosLink2.props("nonstop")!=null){nonstop=GosLink2.props("nonstop");}
		readUntil(GosLink2.props("puser"));
		write(name);
		readUntil(GosLink2.props("ppass"));
		write(pass+"\r\n");
		readUntil(GosLink2.props("pmenu"));
		if (ghost == 1){
			write("=x\n");
			ghost=0;
			return "reload";
		}
		else {write(cmd);}
		readUntil(GosLink2.props("pmud"));
		write("e");
		write("\n");
		if(GosLink2.ansi){write("=a");}
		return "blah";
}
	public void write(String value) {
		try {
			dataOut.println(value);
			dataOut.flush();
			if (loggedin==1){
				if (value.equals("\n")){}
				else if(value.equals("")){}
				else{if(!value.equals("ping")){GosLink2.dw.append(myname+"->"+value);}}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String readUntil(String pattern) throws InterruptedException, IOException {
		cnt=0;
		char lastChar = pattern.charAt(pattern.length() - 1);
        StringBuffer buffer = new StringBuffer();
        char ch = (char) dataIn.read();
        String msg;
        String broken[];
        while (runner==1) {
        	if (whoCheck){oPlayers=ICheaker.whoCheck(this);whoCheck=false;}
            buffer.append(ch);
        	msg=buffer.toString();

        	String chk=msg.trim();
        	String rtn=msgchk(chk,msg);
        	if (rtn != null){return rtn;}
        	if (ch == lastChar) {
            	if (GosLink2.dw.dbm){GosLink2.dw.append(msg.trim());}
         		if (buffer.toString().endsWith(pattern)) {
                	broken=msg.split(" ");
                	for (int i=0;i<broken.length;i++ ) {
                		if (broken[i].equals("gossips:")){
                			player=broken[i-1];
                		}
                	}
                    return player+"<4;2>0:"+buffer.toString();
                    
                }
         		
         		if (buffer.toString().endsWith("telepaths:")) {
         			broken=msg.split(" ");
                	for (int i=0;i<broken.length;i++ ) {
                		if (broken[i].equals("telepaths:")){
                			player=broken[i-1];
                		}
                	}
                    GosLink2.gb.tele(player.trim().toLowerCase(),mynum);
                }
            }
            ch = (char) dataIn.read();

            /*
            int chn=Character.getNumericValue(ch);
            if (!ping){System.out.println("!"+chn);}
            if (!ping && chn!=-1){ping=true;}
*/
        }
       	return null;
            
        }
	public String readit(String pattern, String kill) throws InterruptedException, IOException {
		cnt=0;
		char lastChar = pattern.charAt(pattern.length() - 1);
        StringBuffer buffer = new StringBuffer();
        char ch = (char) dataIn.read();
        while (runner==1) {
            buffer.append(ch);
            //System.out.print(ch);
            String msg=buffer.toString().trim();
            if (msg.contains(kill)){
     			return kill;
     		}
        	if (ch == lastChar) {
        		if (buffer.toString().endsWith(pattern)) {
                    return buffer.toString();
                }
            }
            ch = (char) dataIn.read();
            
        }
       	return null;
            
        }

	private String msgchk(String chk, String msg) throws InterruptedException, IOException{
        	String broken[];
    		
        	if (chk.endsWith(ghosts)){
        		ghost=1;
        	}
        	else if (chk.endsWith(nonstop)){
        	  if (cnt == 0){
            	dataOut.print("n\b");
    			dataOut.flush();
        	  }
        	  cnt++;
        	}
        	else if (chk.endsWith("Room error")){
     			return "Room error";
     		}
        	else if (msg.endsWith("just entered the Realm.")){
     			broken=msg.split(" ");
            	for (int i=0;i<broken.length;i++ ) {
            		if (broken[i].equals("just")){
            			player=broken[i-1];
            		}
            	}
                GosLink2.gb.enter(player.trim(),mynum);
     			oPlayers=ICheaker.whoCheck(this);
     			whoCheck=false;
     		}
        	else if (chk.endsWith(hangup)){
            	GosLink2.dw.append("BBS shutdown detected!");
            	loggedin=0;
            	return "!OffLINE+02";
            }
        	else if(chk.endsWith("just hung up!!!")||chk.endsWith("just disconnected!!!")||chk.endsWith("just left the Realm.")){
     			oPlayers=ICheaker.whoCheck(this);
     			whoCheck=false;
     			return ":!:whocheck:!:";
        	}
            return null;
        }
}