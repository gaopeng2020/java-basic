package patterns.structure.bridge;

import patterns.structure.bridge.abst.FoldedPhone;
import patterns.structure.bridge.abst.UpRightPhone;
import patterns.structure.bridge.impl.HuaweiPhone;
import patterns.structure.bridge.impl.VivoPhone;
import patterns.structure.bridge.impl.XiaoMiPhone;

public class Client {

	public static void main(String[] args) {

		// 获取折叠式手机 (样式 + 品牌 )

		FoldedPhone xiaomiFolderPhone = new FoldedPhone(new XiaoMiPhone());
		xiaomiFolderPhone.open();
		xiaomiFolderPhone.call();
		xiaomiFolderPhone.close();

		System.out.println("=======================");

		FoldedPhone vivoFolderPhone = new FoldedPhone(new VivoPhone());
		vivoFolderPhone.open();
		vivoFolderPhone.call();
		vivoFolderPhone.close();
		
		System.out.println("=======================");
		
		FoldedPhone huaweiFolderPhone = new FoldedPhone(new HuaweiPhone());
		huaweiFolderPhone.open();
		huaweiFolderPhone.call();
		huaweiFolderPhone.close();

		System.out.println("==============");

		UpRightPhone xiaomiUpRightPhone = new UpRightPhone(new XiaoMiPhone());
		xiaomiUpRightPhone.open();
		xiaomiUpRightPhone.call();
		xiaomiUpRightPhone.close();

		System.out.println("==============");

		UpRightPhone vivoUpRightPhone = new UpRightPhone(new VivoPhone());
		vivoUpRightPhone.open();
		vivoUpRightPhone.call();
		vivoUpRightPhone.close();
	}

}
