package org.neodatis.odb.test.trigger;

import org.neodatis.odb.core.trigger.CloseListener;

public class MyCloseListener implements CloseListener {
	public int nbCloses;
	public void afterClose() {
		nbCloses++;
		System.out.println("MyCloseListener");
	}

}
