package patterns.behavior.iterator;

import java.util.HashMap;

public class SubExpression extends SymbolExpression {

	public SubExpression(Expression left, Expression right) {
		super(left, right);
	}

	//姹傚嚭left 鍜right 琛ㄨ揪寮忕浉鍑忓悗鐨勭粨鏋
	public int interpreter(HashMap<String, Integer> var) {
		return super.left.interpreter(var) - super.right.interpreter(var);
	}
}
