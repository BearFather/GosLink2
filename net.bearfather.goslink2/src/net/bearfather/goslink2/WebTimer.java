package net.bearfather.goslink2;

public class WebTimer implements Runnable {
	private int counter=0;
	private WebClient client;
	public boolean run=false;
	public boolean ping=false;
	public WebTimer(WebClient w){
		client=w;
		run=true;
	}
	
	@Override
	public void run() {
		try {
			while (run){
				Thread.sleep(10000);//This has crashed after first ping.  Could talk but not receive talking...weird, but it went away.
				
				client.writeln(":!:ping:!:");
				ping=true;
				Thread.sleep(120000);
				if (ping){
					GosLink2.dw.append("what is ping:"+ping);
					counter=8;
					run=false;
				}
				GosLink2.dw.append("counter:"+counter++);
				if (counter==6){
					client.writeln(":!:info:!:You have been idle for 5 min.  In 1 min this connection will be closed.");
				}
				else if (counter>6){
					client.writeln("goodbye!");
					client.writeln(":!:hangup:!:");
					run=false;
					//client.close();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			run=false;
		}finally{GosLink2.dw.append("Channel "+client.channel+" has been closed from timeout.");client.close();}
	}
	public void reset(){
		counter=0;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}


}
