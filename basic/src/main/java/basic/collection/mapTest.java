/**
 * 
 */
package basic.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Administrator
 *
 */
public class mapTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map<String,String> map = new HashMap<>();
		map.put("key", "value");
		try {
			Entry<String, String>  next = map.entrySet().iterator().next();
			String key = next.getKey();
			String value = next.getValue();
			System.out.println(key+"\t"+value);
		} catch (Exception e) {
		}
		
		System.out.println("End");
	}
	

}
