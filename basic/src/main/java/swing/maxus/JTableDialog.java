/**
 * 
 */
package swing.maxus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * @author Administrator
 *
 */
public class JTableDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Create the dialog.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public JTableDialog() {
		
		//=====================UI Style ================
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JDialog.setDefaultLookAndFeelDecorated(true);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		//================Dialog properties==============================
		setSize(574, 495);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) screensize.getWidth() / 2 - getWidth() / 2;
		int y = (int) screensize.getHeight() / 2 - getHeight() / 2;
		setLocation(x, y);
		setResizable(false);
		setAlwaysOnTop(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		String imagePath = null;
		try {
			String path = this.getClass().getResource("/").getPath();
			String wsPath = path.substring(0, path.indexOf("bin"));
			imagePath = wsPath + "resource/images/";
		} catch (Exception e) {
		}

		 //============================JTable==============================
		String[] columnNames = { "����", "ѧ��", "�Ա�", "����", "רҵ", "ѧУ" };
//		Object[][] obj = new Object[1][6];  
		final DefaultTableModel tableModel = new DefaultTableModel();
		for (int i = 0; i < columnNames.length; i++) {
			tableModel.addColumn(columnNames[i]);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(57, 69, 471, 335);
		contentPanel.add(scrollPane);
		table = new JTable(tableModel);
		table.setRowHeight(25);
		scrollPane.setViewportView(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedColumn = table.getSelectedColumn();
				int selectedRow = table.getSelectedRow();
				Object value = table.getValueAt(selectedRow, selectedColumn);
			}
		});

		JButton btn_New = new JButton("");
		btn_New.setIcon(new ImageIcon(imagePath + "New.png"));
		btn_New.setBounds(20, 95, 35, 35);
		contentPanel.add(btn_New);
		btn_New.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tableModel.addRow(new Object[] { "GAO", "26", "Male", "6Mounth", "M", "NWAFU" });
			}
		});

		JButton btn_Delete = new JButton("");
		btn_Delete.setIcon(new ImageIcon(imagePath + "delete.png"));
		btn_Delete.setBounds(20, 140, 35, 35);
		contentPanel.add(btn_Delete);
		btn_Delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (table.getSelectedRow() != -1)
					tableModel.removeRow(table.getSelectedRow());
			}
		});

		FileSystemView fsv = FileSystemView.getFileSystemView();
		final File homeDirectory = fsv.getHomeDirectory();

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBounds(0, 0, 35, 26);
		contentPanel.add(menuBar);

		
		// ====================File Menu================================
		JMenu mn_File = new JMenu("File");
		menuBar.add(mn_File);

		JMenuItem mnItm_New = new JMenuItem("New");
		mnItm_New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnItm_New.setIcon(new ImageIcon(imagePath + "/New.png"));
		mn_File.add(mnItm_New);
		mnItm_New.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tableModel.addRow(new Object[] { "GAO", "26", "Male", "6Mounth", "M", "NWAFU" });
			}
		});

		JMenuItem mnItm_Open = new JMenuItem("Open");
		mnItm_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnItm_Open.setIcon(new ImageIcon(imagePath + "Open.png"));
		mn_File.add(mnItm_Open);
		mnItm_Open.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser(homeDirectory);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				fileChooser.showOpenDialog(contentPanel);
				File file = fileChooser.getSelectedFile();

				String fileName = file.getName();
				System.out.println("fileName:" + fileName);
			}
		});

		JMenuItem mnItm_Sava = new JMenuItem("Sava");
		mnItm_Sava.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnItm_Sava.setIcon(new ImageIcon(imagePath + "Save.png"));
		mn_File.add(mnItm_Sava);
		mnItm_Sava.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser(homeDirectory);
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.showSaveDialog(contentPanel);
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile.isDirectory()) {
					String absolutePath = selectedFile.getAbsolutePath();
					System.out.println("fileName:" + absolutePath);
				}
			}
		});

		/*
		 * ===================ON / CANCEL Button=======================
		 */
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
	}

}
