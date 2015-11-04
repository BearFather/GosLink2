package net.bearfather.goslink2;


import java.util.HashMap;

public class HeartBeat implements Runnable {
	private int ol;
	private int ct;
	private HashMap<Integer, Integer> OLH=new HashMap<Integer,Integer>();
	private HashMap<Integer, Integer> CTH=new HashMap<Integer,Integer>();
	private TelnetService TC;
	private int time=Integer.parseInt(GosLink2.prps("time"));

	public void run(){
		if (time!=0){time=time-1;}
		GosLink2.dw.append("HeartBeat Started.");
		OLH.put(1,0);
		OLH.put(2,0);
		OLH.put(3,0);
		CTH.put(1,0);
		CTH.put(2,0);
		CTH.put(3,0);
		while (true){
			try {
				Thread.sleep(60000);
				for (int v:GosLink2.TNH.keySet()){
					checkserver(v);
				}
				gosbot.enterchk();
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	public void checkserver(int num) throws InterruptedException{
		TC=GosLink2.TNH.get(num);
		ol=OLH.get(num);
		ct=CTH.get(num);

		if (TC.loggedin == 1){
			if (ct==3){
				TC.write("ping");
				ct=0;
			}else{ct++;}
			if (ct >5){ct=0;}
		}else{
			if (ol==0){GosLink2.dw.append("Server "+num+" is offline, waiting to restart.");}
			if (ol == time){
				ol=0;
				GosLink2.dw.append("Starting Server "+num+".");
				GosLink2.startit(num);
			}else{ol++;}
		}
		OLH.put(num, ol);
		CTH.put(num, ct);

	}

}
