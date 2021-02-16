package basic.iostream;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileClassDemo {

	public static void main(String[] args) throws IOException {
		File file = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\network Nodes.xlsx");
		// ����ϵͳ�ָ���
		separator();
		//File�๹�췽��
		defineFile();
		//File�ೣ�÷���
		generalMethodofFile(file);
//		//�����ļ������ļ���
//		CreateFileorDir(file);
//		//�����ƶ�Ŀ¼�µ��ļ����ļ���
//		filelist(file);
//		//���й������ı���
//		ListFileFilter(file);


	}

	private static void separator() {
		//static String separator��ϵͳ�йص�Ĭ�����Ʒָ��� 
		System.out.println(File.separator);
		//static String pathSeparator��ϵͳ�йص�·���ָ��� 
		System.out.println(File.pathSeparator);
	}

	private static void defineFile() {
		//File(String pathname) ͨ��������·�����ַ���ת��Ϊ����·����������һ���� File ʵ��
		File file1 = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest");
		File file2 = new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest","Hello.java");
		//File(File parent, String child) ���� parent����·������ child·�����ַ�������һ���� File ʵ��
		File file3 =new File(file1,"Hello.Java");
		File file4 =new File("D:\\Users\\Lenovo\\eclipse_workspace\\iotest\\Hello.java");
		System.out.println("file1:"+file1);
		System.out.println("file2:"+file2);
		System.out.println("file3:"+file3);
		System.out.println("file4:"+file4);
	}

	private static void generalMethodofFile(File file) {
		System.out.println("Fuile AbsolutePath:"+file.getAbsolutePath());
		System.out.println("File Name:"+file.getName());
		System.out.println("File Path:"+file.getPath());
		System.out.println("File length: "+file.length());
	}

	private static void CreateFileorDir(File file) throws IOException {
			File file2 = new File(file,"NewFolerCreatedByJava\\abc\\a\\b\\c");
			File file3 = new File(file,"NewFolerCreatedByJava2");
			//����Folder
			file2.mkdirs();
			file3.mkdir();
//			System.out.println(file2.getPath()+file2.isDirectory());
	//		file2.delete();
			
			File file1 = new File(file2,"ExcelfileCreatedByJava.xlsx");
			//boolean createNewFile() ����ļ������ڣ��򴴽� ����true�� ����ļ����ڣ��򲻴��� ����false�� ���·���������׳�IOException
			boolean b = file1.createNewFile();
			System.out.println(file1.getName()+b);
	//		file1.delete();
	//		System.out.println(file1.exists());
			
			
		}

	private static void filelist(File file) {
		//listfile������һ��File���͵����飬����ָ���ļ����¶���ľ���·��
		File[] arry = file.listFiles();
		for(File i : arry) {
			System.out.println("file.listFiles(): "+i);
		}
		//List������һ���ַ������͵����飬���ص���ָ���ļ����µĶ������ơ�
		String[] s =file.list();
		for(String i : s) {
			System.out.println("file.list :"+i);
		}
	}

	private static void ListFileFilter(File file) {
		//����xlsx�ļ�������:�ڲ�����󣬿ɽ�����Ϊ��������listFiles(filterjava)��
		xlsxfilter xlsxfilter = new FileClassDemo().new xlsxfilter();
		//�����ļ��й�����:�ڲ�����󣬿ɽ�����Ϊ��������listFiles(filterdir)
		directoryfilter dirfilte = new FileClassDemo().new directoryfilter();
		//��file.listFiles����
		File[] xlsxFiles = file.listFiles(xlsxfilter);
		File[] fileFiles = file.listFiles(dirfilte);
		for(File files :xlsxFiles) {
			System.out.println("listfile:"+files.getName());
		}
			
		//�õݹ����Ƕ��Ŀ¼���򲻿���ʹ�ù�����
		File[] arry = file.listFiles();
		for(File f : arry) {
			//�������������Ŀ¼�򡰵����Լ��� �ݹ鷽��
			if(f.isDirectory()) {
				ListFileFilter(f);
			//�����ɸѡǶ��Ŀ¼�µ�ָ�������ļ��������ʹ��getName().endsWith(".�ļ�����")
			}else if(f.getName().endsWith(".xlsx")) {
			System.out.println("�ݹ�"+f.getName());
			}
		}
		
	}


	class xlsxfilter implements FilenameFilter{
		//ֻ���ҡ�.java���ļ��Ĺ�����
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xlsx");
		}
	}
	
	class directoryfilter implements FileFilter{
		//ֻ�����ļ��еĹ�����
		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	}
}
