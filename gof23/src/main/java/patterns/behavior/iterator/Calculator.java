package patterns.behavior.iterator;

import java.util.HashMap;
import java.util.Stack;

public class Calculator {

	// 瀹氫箟琛ㄨ揪寮
	private Expression expression;

	// 鏋勯€犲嚱鏁颁紶鍙傦紝骞惰В鏋
	public Calculator(String expStr) { // expStr = a+b
		// 瀹夋帓杩愮畻鍏堝悗椤哄簭
		Stack<Expression> stack = new Stack<>();
		// 琛ㄨ揪寮忔媶鍒嗘垚瀛楃鏁扮粍 
		char[] charArray = expStr.toCharArray();// [a, +, b]

		Expression left = null;
		Expression right = null;
		//閬嶅巻鎴戜滑鐨勫瓧绗︽暟缁勶紝 鍗抽亶鍘 [a, +, b]
		//閽堝涓嶅悓鐨勬儏鍐碉紝鍋氬鐞
		for (int i = 0; i < charArray.length; i++) {
			switch (charArray[i]) {
			case '+': //
				left = stack.pop();// 浠巗tack鍙栧嚭left => "a"
				right = new VarExpression(String.valueOf(charArray[++i]));// 鍙栧嚭鍙宠〃杈惧紡 "b"
				stack.push(new AddExpression(left, right));// 鐒跺悗鏍规嵁寰楀埌left 鍜right 鏋勫缓 AddExpresson鍔犲叆stack
				break;
			case '-': // 
				left = stack.pop();
				right = new VarExpression(String.valueOf(charArray[++i]));
				stack.push(new SubExpression(left, right));
				break;
			default: 
				//濡傛灉鏄竴涓Var 灏卞垱寤鸿缁VarExpression 瀵硅薄锛屽苟push鍒stack
				stack.push(new VarExpression(String.valueOf(charArray[i])));
				break;
			}
		}
		//褰撻亶鍘嗗畬鏁翠釜 charArray 鏁扮粍鍚庯紝stack 灏卞緱鍒版渶鍚嶦xpression
		this.expression = stack.pop();
	}

	public int run(HashMap<String, Integer> var) {
		//鏈€鍚庡皢琛ㄨ揪寮廰+b鍜var = {a=10,b=20}
		//鐒跺悗浼犻€掔粰expression鐨刬nterpreter杩涜瑙ｉ噴鎵ц
		return this.expression.interpreter(var);
	}
}