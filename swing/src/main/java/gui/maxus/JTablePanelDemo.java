package gui.maxus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ImageIcon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JTablePanelDemo extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static Boolean isCancel = false;
	public int pageIndex = -1;

	/**
	 * Create the dialog.
	 * 
	 * @param countDownLatch
	 */
	public JTablePanelDemo(final CountDownLatch countDownLatch) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isCancel = true;
				dispose();
				countDownLatch.countDown();
			}
		});
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		String imagePath = null;
		try {
			String path = this.getClass().getResource("/").getPath();
			String wsPath = path.substring(0, path.indexOf("bin"));
			imagePath = wsPath + "resource/images/";
		} catch (Exception e) {
		}

		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
			tabbedPane.setSelectedIndex(-1);
			tabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					pageIndex = tabbedPane.getSelectedIndex();
					System.out.println("���ǵڣ�" + (tabbedPane.getSelectedIndex() + 1) + "  ����ǩҳ: ");
				}
			});
			tabbedPane.setBounds(0, 0, 432, 216);
			// ���ñ�ǩҳ���ֵ�����
			Font font = new Font("΢���ź�", Font.BOLD, 15);
			tabbedPane.setFont(font);
			// ���ñ�ǩҳ�ı���ɫ
			tabbedPane.setBackground(Color.white);
			// ���ñ�ǩҳ��ǰ��ɫ
			tabbedPane.setForeground(Color.black);

			contentPanel.add(tabbedPane);
			{
				JPanel panel1 = new JPanel();
				panel1.setBackground(Color.MAGENTA);
				tabbedPane.addTab("Panel1", new ImageIcon(imagePath + "New.png"), panel1, "panel 1 test");
			}

			{
				JPanel panel2 = new JPanel();
				panel2.setBackground(Color.BLUE);
				tabbedPane.addTab("Panel2", new ImageIcon(imagePath + "Open.png"), panel2, "panel 2 test");
			}

			{
				JPanel panel3 = new JPanel();
				panel3.setBackground(Color.GREEN);
				tabbedPane.addTab("Panel3", new ImageIcon(imagePath + "Save.png"), panel3, "panel 3 test");
			}
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				countDownLatch.countDown();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				isCancel = true;
				dispose();
				countDownLatch.countDown();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
}
