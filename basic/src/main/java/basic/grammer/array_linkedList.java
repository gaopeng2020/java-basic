package basic.grammer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class array_linkedList {
	private static List<Integer> list = null;
	
	public static void main(String[] args) {
		
			//arraylist();
			linkedlist();
		
		System.out.println(list);
	}

	private static void linkedlist() {
		list = new ArrayList<>();
		for(int i=0;i<30;i++) {
		list.add(i);
		}
	}

	private static void arraylist() {
		list = new LinkedList<>();
		for(int i=0;i<30;i++) {
			list.add(i);
			}
	}

}
