package dom4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class dom4jTest {

	public static void main(String[] args) {

		File file = new File(
				"D:\\Users\\Lenovo\\Documents\\Vector\\CANoe Demo\\Reference\\ARXMLSimulation\\ARXML\\RootComposition.arxml");

		List<String> list = getARXMLInfo(file, "SoftwareTypes", "Interfaces");
		for (String string : list) {
			System.out.println(string);
		}

	}

	private static List<String> getARXMLInfo(File file, String... args) {
		if (file == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();

		// 1.创建Reader对象
		SAXReader reader = new SAXReader();

		// 2.加载xml
		Document document = null;
		try {
			document = reader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		// 3.获取根节点
		Element rootElement = document.getRootElement();

		// 遍历根节点
		Iterator<Element> iterator = rootElement.elementIterator();
		while (iterator.hasNext()) {
			Element level1Element = (Element) iterator.next();
//        	System.out.println("--------level1 Element节点名："+level1Element.getName()+"---level1 Element节点值："+level1Element.getStringValue());

			Iterator<Element> iterator1 = level1Element.elementIterator();
			while (iterator1.hasNext()) {
				Element level2Element = (Element) iterator1.next();
				String level2ElementValue = level2Element.getStringValue();
				String[] level2ElementValues = level2ElementValue.split("\n");
				if (level2ElementValue.indexOf(args[0]) == -1) {
					continue;
				}
				System.out.println("####################"+level2ElementValues[3]);
				for (String str : level2ElementValues) {
//					String value = EPTUtils.stringFormat(str);
//					if (value.equals(""))
//						continue;
//					list.add(value);
				}
				
				Iterator<Element> iterator2 = level2Element.elementIterator();
				while (iterator2.hasNext()) {
					Element level3Element = iterator2.next();
					String level3ElementValue = level3Element.getStringValue();
					String[] level3ElementValues = level3ElementValue.split("\n");
					if (level3ElementValues[0].indexOf(args[1]) == -1) {
						continue;
					}

				}
			}
		}

		return list;
	}

}
