package net.bearfather.goslink2;

import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;

public class DebugConsole {
	public boolean dbm=false;
	DebugWindow dw;
	public DebugConsole() {
	    try {
	    	if (!GraphicsEnvironment.isHeadless()) {
	    		dw=new DebugWindow();
	    	}
	    } catch (final HeadlessException ignored) {
	    	System.out.println("GUI DETECTION FAILED");
	    }
	}
	public void append(String msg) {
		if (dw==null) {
			System.out.println(msg);
		}else {
			dw.append(msg);
		}
	}
}
