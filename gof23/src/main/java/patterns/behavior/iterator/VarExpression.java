package patterns.behavior.iterator;

import java.util.HashMap;


/**
 * 鍙橀噺鐨勮В閲婂櫒
 * @author Administrator
 *
 */
public class VarExpression extends Expression {

	private String key; // key=a,key=b,key=c

	public VarExpression(String key) {
		this.key = key;
	}

	// var 灏辨槸{a=10, b=20}
	// interpreter 鏍规嵁 鍙橀噺鍚嶇О锛岃繑鍥炲搴斿€
	@Override
	public int interpreter(HashMap<String, Integer> var) {
		return var.get(this.key);
	}
}
