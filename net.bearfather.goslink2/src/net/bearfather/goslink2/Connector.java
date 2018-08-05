package net.bearfather.goslink2;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.SocketException;
import org.apache.commons.net.telnet.TelnetClient;
/*
 * 		This Class was made to do a rewrite,
 * 		I have haven't got far but maybe used
 * 		in the future.
 *							Bear 
 *
 */
public class Connector implements Runnable {

	public Connector() {
	}

	@Override
	public void run() {

	}
	public class TelnetService {
		private final String server;
		private final int port;
		public TelnetClient telnet = new TelnetClient();
		private InputStream dataIn;
		private PrintStream dataOut;
		public int loggedin=0;
		public int runner=1;
		public int mynum=0;
		String hangup=GosLink2.props("cleanup");
	    String nonstop="(N)onstop, (Q)uit, or (C)ontinue?";
	    String ghosts="Enter your password to end the other connection and log on.";
	   	int cnt;
		public int ghost=0;

		public TelnetService(String server, int port) {
			this.server = server.replace("http://", "");
			this.port = port;
		}
		public void killme() throws IOException{
			GosLink2.dw.append("Killme Called");
			telnet.disconnect();
		}
		private void startTelnetSession() throws SocketException, IOException {
			//mh=new Bot(npc,this);
			telnet.connect(server, port);
			dataIn = telnet.getInputStream();
			dataOut = new PrintStream(telnet.getOutputStream());
		}
		public String getTelnetSessionAsString(String tcn) throws SocketException, IOException, InterruptedException {
	        startTelnetSession();
	        String rtn="blah";
	        GosLink2.dw.append("Logging into server "+tcn+".");
	        rtn=loginUser(GosLink2.props("user"+tcn),GosLink2.props("game"+tcn),GosLink2.props("pass"+tcn));
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
			write(cmd);
			readUntil(GosLink2.props("pmud"));
			write("e");
			write("\n");
			write("set statline off");
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
					else{if(!value.equals("ping")){GosLink2.dw.append(mynum+"->"+value);}}
				}
			} catch (Exception e) {
				e.printStackTrace();
				loggedin=0;
			}
		}
		public String readUntil(String pattern) throws InterruptedException, IOException {
			cnt=0;
			char lastChar = pattern.charAt(pattern.length() - 1);
	        StringBuffer buffer = new StringBuffer();
	        char ch = (char) dataIn.read();
	        String msg;
	        while (runner==1) {
	            buffer.append(ch);
	        	msg=buffer.toString();
	        	String chk=msg.trim();
	        	String rtn=msgchk(chk);
	        	if (rtn != null){
	            	if (rtn.equals("!OffLINE+02")){
	            		loggedin=0;
	            		return "!OffLINE+02";
	            	}
	        		return rtn;
	        	}
	        	if (ch == lastChar) {
	            	if (GosLink2.dw.dbm){GosLink2.dw.append(msg.trim());}
	            	if (buffer.toString().endsWith(pattern)) {
	                    return buffer.toString().trim();
	                }
	            }
	            ch = (char) dataIn.read();
	        }
	       	return null;
	        }
		public String readit(String kill) throws InterruptedException, IOException {
			cnt=0;
			char lastChar = 'n';
	        StringBuffer buffer = new StringBuffer();
	        
	        char ch = (char) dataIn.read();
	        while (runner==1) {
	            buffer.append(ch);
	            String msg=buffer.toString().trim();
	            String results=msgchk(msg);
	        	if (results != null){
	            	if (results.equals("!OffLINE+02")){
	            		loggedin=0;
	            		return "!OffLINE+02";
	            	}
	        		return results;
	        	}
	        	if (msg.endsWith("The gods have punished you appropriately.")){write("\n");}
	            if (msg.contains(kill)){return kill;}
	        	if (ch == lastChar) {
	        		if (buffer.toString().endsWith("\n")) {return buffer.toString();}
	            }
	            ch = (char) dataIn.read();
	        }
	       	return null;
		}

		private String msgchk(String chk) throws InterruptedException, IOException{
			if (chk.endsWith(ghosts)){ghost=1;}
	    	else if (chk.endsWith(nonstop)){
	    	  if (cnt == 0){
	        	dataOut.print("n\b");
				dataOut.flush();
	    	  }
	    	  cnt++;
	    	}else if (chk.endsWith(hangup)){
	        	GosLink2.dw.append("BBS shutdown detected!");
	        	loggedin=0;
	        	return "!OffLINE+02";
	        }else if (chk.contains("You say \"ping\"")){
	        	//HeartBeat.ping=true;
	        	return "flush";
	        }
	        return null;
		}
	}
}
