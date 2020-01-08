package org.neodatis.odb.test.fromusers.bogdan.floccus;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;








class Foo {
    private String name;
    private String surname;
    private long id;

    
    public Foo(String name, String surname, long id) {
        this.name = name;
        this.surname = surname;
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}


/**
 *
 * @author bo
 */
public class Main {
    static int trs = 0;
    int threads = 0;


    public synchronized void makeProcess() {
        String id = UUID.randomUUID().toString();
        System.err.println("Running ID " + id);
        FloccusClient c = new FloccusClient("foobar");
        int count = 0;
        while (true) {
            c.store(new Foo("John", "Smith", new Date().getTime()));
//            c.close(); // here it will die eventually
            count++;
            Main.trs++;
            if (count > 500) {
                count = 0;
                c.close(); // here it will die, but somewhat later. :-)
            }
        }
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Args: -server or -client");
        } else {
            if (args[0].equals("-server")) {
                new FloccusServer("foobar").start();

                System.err.println("Exit");
            } else {
                for (int i = 0; i < 2; i++) {
                    final int id = Integer.parseInt(i + "");
                    Thread p = new Thread(new Runnable() {
                        public void run() {
                            new Main().makeProcess();
                        }
                    });
                    p.start();
                    System.err.println("Started " + id);
                }

                while (true) {
                    int before = Main.trs;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.err.println(String.format("%s TPS", (Main.trs - before)));
                 }
            }
        }
    }
}
