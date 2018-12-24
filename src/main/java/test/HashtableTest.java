/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * hashMap,concurrentHashMap,hashtable比较
 * 
 * @author zhangle
 *
 */
public class HashtableTest {

    public static void main(String[] args) {
        HashtableTest ht = new HashtableTest();
        ht.testHashMap();
        ht.testHashMapInMT();
    }

    public void testHashMap() {
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put(1, "11111");
        map.put(0, "00000");
        map.put('a', "aaaaa");
        map.put(null, "null,null");
        Iterator<Object> it = map.keySet().iterator();
        for (; it.hasNext();) {
            Object key = it.next();
            System.out.println("key is: " + key + ", value is: " + map.get(key));
        }
    }

    public void testHashMapInMT() {
        final HashMap<Object, Object> map = new HashMap<Object, Object>();
        class R implements Runnable {
            private int id = 0;

            public R(int id) {
                this.id = id;
            }

            @Override
            public void run() {
                System.out.println("###r1-" + this.id + ", put <" + id + ", " + id + ">");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.put(id, id);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("###r1-" + this.id + ", get <" + this.id + ", " + map.get(id) + ">");
            }
        }
        List<Thread> list = new ArrayList<Thread>();
        for (int i = 1; i < 1000; i++) {
            Thread t = new Thread(new R(i));
            list.add(t);
        }
        for (Thread t : list) {
            t.start();
        }
        for (Thread t : list) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("final map size: " + map.size());
    }

    public void testHashtableInMT() {
        final Hashtable<Object, Object> map = new Hashtable<Object, Object>();
        class R implements Runnable {
            private int id = 0;

            public R(int id) {
                this.id = id;
            }

            @Override
            public void run() {
                System.out.println("###r1-" + this.id + ", put <" + id + ", " + id + ">");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                map.put(id, id);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("###r1-" + this.id + ", get <" + this.id + ", " + map.get(id) + ">");
            }
        }
        List<Thread> list = new ArrayList<Thread>();
        for (int i = 1; i < 1000; i++) {
            Thread t = new Thread(new R(i));
            list.add(t);
        }
        for (Thread t : list) {
            t.start();
        }
        for (Thread t : list) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("final map size: " + map.size());
    }

    public void testConcurrentHashMapInMT() {
        final ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();
        class R implements Runnable {
            private int id = 0;

            public R(int id) {
                this.id = id;
            }

            @Override
            public void run() {
                System.out.println("###r1-" + this.id + ", put <1, " + this.id + ">");
                map.put(1, this.id);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("###r1-" + this.id + ", get <1, " + map.get(1) + ">");
            }
        }
        List<Thread> list = new ArrayList<Thread>();
        for (int i = 1; i < 10; i++) {
            Thread t = new Thread(new R(i));
            list.add(t);
        }
        for (Thread t : list) {
            t.start();
        }
        System.out.println("final map size: " + map.size());
    }
}
