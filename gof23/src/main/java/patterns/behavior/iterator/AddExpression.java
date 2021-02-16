package patterns.behavior.iterator;

import java.util.HashMap;

/**
 * 鍔犳硶瑙ｉ噴鍣
 * @author Administrator
 *
 */
public class AddExpression extends SymbolExpression  {

	public AddExpression(Expression left, Expression right) {
		super(left, right);
	}

	//澶勭悊鐩稿姞
	//var 浠嶇劧鏄{a=10,b=20}..
	//涓€浼氭垜浠琩ebug 婧愮爜,灏眔k
	public int interpreter(HashMap<String, Integer> var) {
		//super.left.interpreter(var) 锛杩斿洖 left 琛ㄨ揪寮忓搴旂殑鍊a = 10
		//super.right.interpreter(var): 杩斿洖right 琛ㄨ揪寮忓搴斿€b = 20
		return super.left.interpreter(var) + super.right.interpreter(var);
	}
}
