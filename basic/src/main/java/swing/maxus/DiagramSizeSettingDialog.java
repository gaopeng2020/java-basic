package swing.maxus;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class DiagramSizeSettingDialog extends JDialog {
	private final JPanel jPanel = new JPanel();
	private JComboBox comboBox_DiagramSize = new JComboBox();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField_DiagramName;

	public String diagramSize = "";
	public boolean isLandscape = true;
	public String diagramName = "";
	public boolean isCancel = false;

	/**
	 * Create the dialog.
	 */
	public DiagramSizeSettingDialog(final Thread callerThread) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				dispose();
				isCancel = true;
				synchronized (callerThread) {
					callerThread.notify();
				}
			}
		});

		setAlwaysOnTop(true);
		setTitle("Diagram Configuration");
		setBounds(600, 300, 394, 276);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		jPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(jPanel, BorderLayout.CENTER);

		comboBox_DiagramSize.setBounds(173, 28, 190, 34);
		comboBox_DiagramSize
				.setModel(new DefaultComboBoxModel(new String[] { "ISO A4", "ISO A3", "ISO A2", "ISO A1", "ISO A0" }));
		comboBox_DiagramSize.setFont(new Font("Arial", Font.BOLD, 16));
		comboBox_DiagramSize.setToolTipText("Select Diagram Size");
		comboBox_DiagramSize.setSelectedIndex(1);

		JLabel lbl_DiagramSize = new JLabel("ISO Diagram Size:");
		lbl_DiagramSize.setBounds(14, 25, 149, 33);
		lbl_DiagramSize.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel lbl_DiagramDirection = new JLabel("Diagram Direction:");
		lbl_DiagramDirection.setBounds(14, 78, 149, 33);
		lbl_DiagramDirection.setFont(new Font("Arial", Font.BOLD, 16));

		JLabel lbl_DiagramName = new JLabel("Diagram Name:");
		lbl_DiagramName
				.setToolTipText("Diagram Name is Optional, if no input, Diagram will be same with Component Pacakge.");
		lbl_DiagramName.setBounds(14, 131, 149, 33);
		lbl_DiagramName.setFont(new Font("Arial", Font.BOLD, 16));
		jPanel.add(lbl_DiagramName);

		textField_DiagramName = new JTextField();
		textField_DiagramName.setFont(new Font("Arial", Font.BOLD, 16));
		textField_DiagramName.setBounds(173, 130, 190, 36);
		jPanel.add(textField_DiagramName);
		textField_DiagramName.setColumns(10);
		// get text of jTextField
		textField_DiagramName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					dispose();
					getDialogInput();
					synchronized (callerThread) {
						callerThread.notify();
					}
				}
			}
		});

		JRadioButton rdbtn_Portrait = new JRadioButton("Portrait");
		rdbtn_Portrait.setBounds(281, 82, 79, 27);
		buttonGroup.add(rdbtn_Portrait);
		rdbtn_Portrait.setFont(new Font("Arial", Font.PLAIN, 16));

		JRadioButton rdbtn_Landscape = new JRadioButton("Landscape");
		rdbtn_Landscape.setBounds(173, 81, 107, 27);
		buttonGroup.add(rdbtn_Landscape);
		rdbtn_Landscape.setFont(new Font("Arial", Font.PLAIN, 16));

		jPanel.setLayout(null);
		jPanel.add(comboBox_DiagramSize);
		jPanel.add(lbl_DiagramSize);
		jPanel.add(lbl_DiagramDirection);
		jPanel.add(rdbtn_Portrait);
		jPanel.add(rdbtn_Landscape);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
						getDialogInput();
						synchronized (callerThread) {
							callerThread.notify();
						}
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dispose();
						isCancel = true;
						synchronized (callerThread) {
							callerThread.notify();
						}
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * @Note Get Dialog Input Contents;
	 */
	private void getDialogInput() {
		// get value of button
		String buttonText = "";
		Enumeration<AbstractButton> btnGroup = buttonGroup.getElements();
		while (btnGroup.hasMoreElements()) {
			AbstractButton btn = btnGroup.nextElement();
			if (btn.isSelected()) {
				buttonText = btn.getText();
				break;
			}
		}
		// Default direction is landscape
		if (buttonText.equalsIgnoreCase("Portrait") || buttonText.equals("")) {
			isLandscape = false;
		} else {
			isLandscape = true;
		}

		// get value of comboBox
		diagramSize = ((String) comboBox_DiagramSize.getSelectedItem()).substring(4);

		// get value of diagram name
		diagramName = textField_DiagramName.getText();
	}
}
