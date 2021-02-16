package patterns.behavior.iterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class ClientTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String expStr = getExpStr(); // a+b
		HashMap<String, Integer> var = getValue(expStr);// var {a=10, b=20}
		Calculator calculator = new Calculator(expStr);
		System.out.println("杩愮畻缁撴灉锛" + expStr + "=" + calculator.run(var));
	}

	// 鑾峰緱琛ㄨ揪寮
	public static String getExpStr() throws IOException {
		System.out.print("璇疯緭鍏ヨ〃杈惧紡锛");
		return (new BufferedReader(new InputStreamReader(System.in))).readLine();
	}

	// 鑾峰緱鍊兼槧灏
	public static HashMap<String, Integer> getValue(String expStr) throws IOException {
		HashMap<String, Integer> map = new HashMap<>();

		for (char ch : expStr.toCharArray()) {
			if (ch != '+' && ch != '-') {
				if (!map.containsKey(String.valueOf(ch))) {
					System.out.print("璇疯緭鍏" + String.valueOf(ch) + "鐨勫€硷細");
					String in = (new BufferedReader(new InputStreamReader(System.in))).readLine();
					map.put(String.valueOf(ch), Integer.valueOf(in));
				}
			}
		}

		return map;
	}
}
