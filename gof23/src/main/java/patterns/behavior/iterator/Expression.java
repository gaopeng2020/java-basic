package patterns.behavior.iterator;

import java.util.HashMap;

/**
 * 鎶借薄绫昏〃杈惧紡锛岄€氳繃HashMap 閿€煎, 鍙互鑾峰彇鍒板彉閲忕殑鍊
 * 
 * @author Administrator
 *
 */
public abstract class Expression {
	// a + b - c
	// 瑙ｉ噴鍏紡鍜屾暟鍊 key 灏辨槸鍏紡(琛ㄨ揪寮 鍙傛暟[a,b,c], value灏辨槸灏辨槸鍏蜂綋鍊
	// HashMap {a=10, b=20}
	public abstract int interpreter(HashMap<String, Integer> var);
}
