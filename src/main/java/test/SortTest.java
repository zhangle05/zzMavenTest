/**
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangle
 *
 */
public class SortTest {

    public static void main(String[] args) {
        List<String> cagList = new ArrayList<String>();
        cagList.add("E");
        cagList.add("A");
        cagList.add("F");
        cagList.add("D");
        Collections.sort(cagList);
        for (String c : cagList) {
            System.out.println(c);
        }
    }
}
