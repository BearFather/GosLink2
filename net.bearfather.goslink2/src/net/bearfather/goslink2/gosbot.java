package net.bearfather.goslink2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class gosbot {
	private TelnetService TN;
	public ArrayList<String> enters =new ArrayList<String>();
	
	public gosbot(){
		GosLink2.dw.append("GosBot Started.");
	}
	public void tele(String plr,int num) throws InterruptedException, IOException{
		TN=GosLink2.TNH.get(num);
		String msg=TN.readUntil("\n");
		String cmd="";
		String rtn;
		String tmsg[]=msg.split("<4;2>0:");
		msg = tmsg[1];
		String broken[]=msg.trim().split(" ");
		String chk=broken[0].trim().toLowerCase();
		boolean denyall=Boolean.parseBoolean(GosLink2.props("deny"));
		if (chk.startsWith("@")){
			if (chk.equals("@abils")){
				TN.write("sys god "+plr+" abil");
				TN.readit("\n","Room error");
				rtn=TN.readit("\n","Room error");
				cmd="/"+plr+" "+rtn;
			}
			else if (chk.equals("@help")){
				TN.write("/"+plr+" Hello "+plr+" Commands are:");
				cmd="/"+plr+" @abils,@good,@neutral,@evil,@retrain, and @home room# map#";
			}
			else if (chk.equals("@neutral")){
				if (!denyall&&!GosLink2.deny.contains(plr)){
					TN.write("sys god "+plr+" neutral");
					TN.readit("\n","Room error");
					rtn=TN.readit("\n","Room error");
				}else{rtn="You are denied!";}
				cmd="/"+plr+" "+rtn;
			}
			else if (chk.equals("@good")){
				if (!denyall&&!GosLink2.deny.contains(plr)){
					TN.write("sys god "+plr+" good");
					TN.readit("\n","Room error");
					rtn=TN.readit("\n","Room error");
				}else{rtn="You are denied!";}
				cmd="/"+plr+" "+rtn;
			}
			else if (chk.equals("@evil")){
				if (!denyall&&!GosLink2.deny.contains(plr)){
					TN.write("sys god "+plr+" neutral");
					TN.write("sys god "+plr+" add evil 150");
					TN.readit("\n","Room error");
					TN.readit("\n","Room error");
					rtn=TN.readit("\n","Room error");
				}else{rtn="You are denied!";}
				cmd="/"+plr+" "+rtn;
			}
			else if (chk.equals("@retrain")){
				cmd="sys god "+plr+" retrain";
				GosLink2.dw.append(cmd);
				TN.write(cmd);
				TN.readit("\n","Room error");
				rtn=TN.readit("\n","Room error");
				cmd="/"+plr+" "+rtn;
			}
			else if(chk.equals("@home")){
				String line= "blank";
				String line2 = "blank";
				int alive=0;
				if (broken.length < 3) {cmd="/"+plr+" not enough info";}
				else{
					TN.write("sys st room "+broken[1]+" "+broken[2]);
					rtn=TN.readit("Monsters:","Room error");
					if (rtn.equals("Room error")){cmd="/"+plr+" Room error!";}
					else{
						String broken2[]=rtn.split("\n");
						for (int i=0;i<broken2.length;i++){
							String mmsg=broken2[i];
							if (mmsg.contains("Specific Monster:")){line=mmsg;}
							else if (mmsg.contains("Specific Monster is Alive")){
								line2=mmsg;
								alive=1;
							}
						}
						if (alive==1){cmd="/"+plr+" "+line2;}
						else if (!line.equals("blank")){cmd="/"+plr+" "+line;}
						else{cmd="/"+plr+" You have an invalid room.";}
					}
				}
			}
			else if(chk.startsWith("@score")){
				int max=10;
				if (broken.length>1){
					max=Integer.parseInt(broken[1])-1;
				}
				ICheaker.max=max;
				ICheaker.TN=TN;
				ICheaker ic=new ICheaker();
				ic.run();
			}
			else{cmd="/"+plr+" Invalid command:"+msg;}
			if (GosLink2.TC1!=null&&GosLink2.TC1.ghost ==1 || GosLink2.TC2!=null&&GosLink2.TC2.ghost == 1 || GosLink2.TC3!=null&&GosLink2.TC3.ghost == 1){cmd=cmd+"\n";}
			TN.write(cmd);
			}
		//else{} //this would be a telepath without a @
	}
	
	public void enter(String plr,int num) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException{
		String gname;
		int found=0;
		int amt=0;
		int write=0;
		int cnt=0;
		String nme;
		while (found==0){
			cnt=GosLink2.names.size();
			for (int i=0;i<cnt;i++){
				if(GosLink2.names.get(i)!=null){
					nme=GosLink2.names.get(i);
					if (plr.equals(nme.substring(1))){
						amt=Integer.parseInt(nme.substring(0,1));
						if (amt<5){
							amt=amt+1;
							GosLink2.names.remove(i);
							GosLink2.names.add(i,amt+plr);
							write=1;
							found=1;
						}
						else{found=2;}
						i=cnt+1200;
					}	
				}	
			}
			if (found==0){found=3;}
		}
		
		if (found==3){
			GosLink2.names.add("1"+plr);
			write=1;
			found=1;
		}
		if (write==1){GosLink2.filewrt();}
		TN=GosLink2.TNH.get(num);
		gname=GosLink2.props("muser"+num);
		String blah=num+",/"+plr+" Hello "+plr+".  My name is "+gname+".  I am a GosLink Bot.  Please Telepath me @help for commands.";
		if (found==1){enters.add(blah);}
	}

	public static void enterchk(){
		for (String value:GosLink2.gb.enters){
			GosLink2.dw.append(value);
			int num=Integer.parseInt(value.substring(0,1));
			GosLink2.TNH.get(num).write(value.substring(2));
		}
		GosLink2.gb.enters.clear();
	}
}
