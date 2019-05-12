/*
by Anthony Stump
Created: 12 May 2019
Updated: on Creation
 */

package asUtilsPorts.Tests;

import java.util.ArrayList;

public class ArrayListTest {
    
    
        private ArrayList<Integer> heights(String detail) {
            ArrayList<Integer> heights = new ArrayList<Integer>();
            heights.add(0);
            switch(detail) {
                case "L": for(int i = 100; i <= 1000; i=i+50) { heights.add(i); } break;
                case "H": for(int i = 100; i <= 1000; i=i+25) { heights.add(i); } break;
            }
            return heights;
        }
    
        public static void main(String[] args) {
            ArrayListTest t = new ArrayListTest();
            ArrayList<Integer> tHeights = t.heights("H");
            System.out.println(tHeights.toString());            
        }
    
}
