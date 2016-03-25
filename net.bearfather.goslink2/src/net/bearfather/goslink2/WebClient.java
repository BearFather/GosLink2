package net.bearfather.goslink2;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;


public class WebClient implements Runnable {
	private String user="";
	private String pass="hey";
	private String name="default";
	public Socket client;
	private InputStream dataIn;
	private PrintStream dataOut;
	public boolean live=false;
	public int channel=-1;
	public WebTimer timer;
	WebClient(Socket client) {
		this.client = client;
	}
	@Override
	public void run() {
		timer=new WebTimer(WebSocket.channels[channel]);
		Thread t=new Thread(timer);
		t.start();
		GosLink2.dw.append("Web user connected on channel "+channel+".");

		if (channel>-1){live=true;}
		//i'M RUNNING
		try {
			dataIn = client.getInputStream();
			dataOut = new PrintStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			live=false;
		}
		if (live){live=login();}
		else{dataOut.println("no lines available");close();}
		if (live){
			// we are connected and talking.
			dataOut.println(":!:connected:!:");
			GosLink2.sayit(name+" has entered webchat.");
		}
		while (live){
			String msg="none";
			try {
				int rtn=0;
				msg=readit();
				rtn=WebSocket.checkCmd(msg,WebSocket.channels[channel]);
				if (rtn!=2){timer.reset();}
				else if(rtn==3){close();}
				if (rtn==0&&!msg.equals("\r\n")&&!msg.equals("\n")){
					String smsg="";
					if (smsg.length()>4){msg.substring(0,4);}
					if (smsg.equals("gos ")){
						msg=msg.substring(4);}
					GosLink2.dw.append("Web:"+name+" gossips: "+msg);
					GosLink2.sayit("W: "+name+": "+msg);
					for (WebClient value:WebSocket.channels){
						if (value!=null&&!value.name.equals(name)){value.send("W: "+name+": "+msg);}
					}

				}
			} catch (InterruptedException | IOException e) {e.printStackTrace();live=false;}
		}

	}
	public boolean login(){
		boolean rtn=false;
		while (live){
			try {
				dataOut.println(":!:user:!:");
				String tuser=readit();
				if (tuser!=null&&tuser.contains(":!:user:!:")){
					tuser=tuser.replace(":!:user:!:", "").trim();
					if (!tuser.equals(user)){
						close();
						return false;
					}
				}else{
					close();
					return false;
				}
				dataOut.println(":!:pass:!:");
				String tpass=readit();
				if (tpass.contains(":!:pass:!:")){
					tpass=tpass.replace(":!:pass:!:", "").trim();
					if (!tpass.equals(pass)){
						close();
						return false;
					}
				}else{
					close();
					return false;
				}
				dataOut.println(":!:name:!:");
				String tname=readit().trim();
				if (tname!=null&&!tname.equals("")){
					name=tname;
					timer.setName(name);
				}else{
					dataOut.print("bad username");
					close();
					return false;
				}
				return true;
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
				GosLink2.dw.append("lost socket");
				close();
				return false;
			}
		}
		return rtn;
	}


	public String readit(String pattern, String kill) throws InterruptedException, IOException {
		char lastChar = pattern.charAt(pattern.length() - 1);
        StringBuffer buffer = new StringBuffer();
        char ch = (char) dataIn.read();
        while (live) {
            buffer.append(ch);
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
	public String readit(String pattern) throws InterruptedException, IOException {
		char lastChar = pattern.charAt(pattern.length() - 1);
        StringBuffer buffer = new StringBuffer();
        char ch = (char) dataIn.read();

        while (live) {
            buffer.append(ch);
        	if (ch == lastChar) {
        		if (buffer.toString().endsWith(pattern)) {
                    return buffer.toString();
                }
            }
            ch = (char) dataIn.read();
            
        }
       	return null;
            
    }
	public String readit() throws InterruptedException, IOException {
		String pattern="\n";
		char lastChar = pattern.charAt(pattern.length() - 1);
        String buffer = "";
        char ch = (char) dataIn.read();
        while (live) {
            buffer=buffer+ch;//TODO need to change timeout, its crashing when client closes
        	if (ch == lastChar) {
        		if (buffer.endsWith(pattern)) {
        			return buffer;
                }
            }
            ch = (char) dataIn.read();
            //GosLink2.dw.append("buffer:"+buffer+": ch:"+ch+":"); //show everything coming in char at a time
        }
       	return null;
            
    }
	public void write(String line){
		dataOut.print(line);
	}
	public void writeln(String line){
		dataOut.println(line);
	}
	public void close(){
		try {
			GosLink2.dw.append("WebSocket closing.");
			live=false;
			if (timer!=null){timer.run=false;}
			client.close();
		} catch (IOException e) {GosLink2.dw.append("we blew a fuse!");
}
		finally{
			if (channel>-1){WebSocket.channels[channel]=null;}
		}
		
	}
	public void send(String msg){
		dataOut.println(msg);
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
