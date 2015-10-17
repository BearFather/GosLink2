package net.bearfather.goslink2;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ICheaker  implements Runnable{
	public static int max=5;
	public static TelnetService TN;
	public static boolean check=false;
	public static String whoCheck(TelnetService TN) throws InterruptedException, IOException{
		String rtn="";
		TN.write("sys list users");
		String line=TN.readit("There are", "skjafhdkhfaldsfh");
		String[] b=line.split("\n");
		boolean start=false;
		for (String value:b){
			if (value.trim().startsWith("=-=-=-")){start=true;}
			if (start){
				if (!value.trim().startsWith(">sys list")&&!value.trim().startsWith("Userid")&&!value.trim().startsWith("There are")&&!value.trim().startsWith("=-=-=-")){
					value=value.replace("", "").trim();
					while (value.contains("  ")){
						value=value.replaceAll("  ", " ");
					}
					String[] v=value.split(" ");
					if (v.length>2&&!v[1].equals(GosLink2.prps("muser2"))){rtn=rtn+","+v[1];}
				}
			}
		}
		if (rtn.startsWith(",")){rtn=rtn.substring(1);}
		return rtn;		
	}
	public static int invCheck(String name,TelnetService TN) throws InterruptedException, IOException{
		int cnt=0;
		int amt=0;
		TN.write("sys god "+name+" inv");
		String in=TN.readit("Wealth:", "Cannot find user");
		if (!in.equals("Cannot find user")&&!in.contains("You are carrying Nothing!")){
			String[] b=in.split(",");
			for (String value:b){
				value=value.replace("\n", "");
				value=value.replace("\r", " ");
				if (value.contains("")){
					int n=value.indexOf("");
					value=value.substring(1,n);
				}
				//GosLink2.dw.append(value.trim());
				if (value.contains("PVP")&&value.contains("token")){
					int pamt=1;
					value=value.trim();
					if (!value.startsWith("P")){
						String[] temp=value.split(" ");
						pamt=Integer.parseInt(temp[0]);
						value=value.substring(temp[0].length()+1);
					}
					cnt=cnt+pamt;
					String[] temp=value.split(" ");
					int type=Integer.parseInt(temp[temp.length-1]);
					amt=amt+(pamt*type);
					//GosLink2.dw.append("found: "+pamt+" "+value.trim()+" = "+amt);
				}
			}
			return amt/2;
		}
		return amt/2;

	}
	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> m1, Map.Entry<String, Integer> m2) {
                return (m2.getValue()).compareTo(m1.getValue());
            }
        });
        
        HashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        
        return result;
    }
	@Override
	public void run() {
		try {
			HashMap<String,Integer> map=new HashMap<String,Integer>();
			String n=whoCheck(TN);
			String say="";
			for (String value:n.split(",")){
				map.put(value, invCheck(value,TN));
			}
			map=ICheaker.sortByValue(map);
			int cnt=0;
			for (String v:map.keySet()){
				say=say+", #"+(cnt+1)+" "+v+":"+map.get(v);
				cnt++;
				if(cnt>max){break;}
			}
			if (say.startsWith(",")){say=say.substring(1);}
			TN.write("gos Top players at this time:");
			TN.write("gos "+say);
		} catch (InterruptedException | IOException e) {e.printStackTrace();}
		check=false;
		
	}
}
