package gui.sdudy;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;

public class windowsOpenClose {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File homeDirectory = fsv.getHomeDirectory();
		JFileChooser fileChooser =new JFileChooser(homeDirectory);
		
//		String wsPath = System.getProperty("user.dir");
//		JFileChooser fileChooser = new JFileChooser(wsPath);
		
//		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
//		fileChooser.setFileFilter(filter);
		
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser .showOpenDialog(null);
		File file = fileChooser .getSelectedFile();

		String fileName = file.getName();
		String path = file.getPath();
		String absolutePath = file.getAbsolutePath();
		final String parent = file.getParent();

		System.out.println("fileName:"+fileName);
		System.out.println("path:"+path);
		System.out.println("absolutePath:"+absolutePath);
		System.out.println("dir:"+parent);
	}

}
