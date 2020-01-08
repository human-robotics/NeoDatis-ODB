/**
 * 
 */
package org.neodatis.odb.test.fromusers.luhan;

/**
 * @author olivier
 *
 */

import org.neodatis.odb.NeoDatis;
import org.neodatis.odb.ODB;
import org.neodatis.odb.Objects;
import org.neodatis.odb.Query;

public class ClientThread2 extends Thread {

	private int consto;

	public ClientThread2(int consto) {
		this.consto = consto;
	}

	public void run() {
		
		if (consto == 1) {
			ODB odb = NeoDatis.openClient("localhost", 8989, "base1");
			Account account = new Account(200621639);
			account.setCurrentDeposit(1000);
			account.setFixedDeposit(0);
			odb.store(account);
			odb.close();
			
		} else {
			System.out.println(consto);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


		for (int i = 0; i < 10; i++) {
			ODB odb = NeoDatis.openClient("localhost", 8989, "base1");
			Objects objects = odb.getObjects(Account.class);
			while(objects.isEmpty()){
				odb.close();
				odb = NeoDatis.openClient("localhost", 8989, "base1");
				Query q  = odb.query(Account.class);
				q.getQueryParameters().setInMemory(false);
				objects = odb.getObjects(q);
			}
			while (objects.hasNext()) {
				Account a = (Account) objects.next();
				System.out.println((i+1)+ ":(" + consto + ") current deposit =" + a.getCurrentDeposit());
				a.setCurrentDeposit(a.getCurrentDeposit() + consto);
				odb.store(a);
			}
			odb.close();
		}
	}
}