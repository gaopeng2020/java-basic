package patterns.behavior.iterator;

import java.util.HashMap;

/**
 * 鎶借薄杩愮畻绗﹀彿瑙ｆ瀽鍣杩欓噷锛屾瘡涓繍绠楃鍙凤紝閮藉彧鍜岃嚜宸卞乏鍙充袱涓暟瀛楁湁鍏崇郴锛
 * 浣嗗乏鍙充袱涓暟瀛楁湁鍙兘涔熸槸涓€涓В鏋愮殑缁撴灉锛屾棤璁轰綍绉嶇被鍨嬶紝閮芥槸Expression绫荤殑瀹炵幇绫
 * 
 * @author Administrator
 *
 */
public class SymbolExpression extends Expression {

	protected Expression left;
	protected Expression right;

	public SymbolExpression(Expression left, Expression right) {
		this.left = left;
		this.right = right;
	}

	//鍥犱负 SymbolExpression 鏄鍏跺瓙绫绘潵瀹炵幇锛屽洜姝interpreter 鏄竴涓粯璁ゅ疄鐜
	@Override
	public int interpreter(HashMap<String, Integer> var) {
		// TODO Auto-generated method stub
		return 0;
	}
}
