/**
 * 
 */
package swing.maxus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JSeparator;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Administrator
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DiagCanMsgInputDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public boolean isCancel = false;
	public int phyRequestID = -1;
	public int phyResponseID = -1;
	public int funRequesID = -1;
	public String byteOrder = null;
	public String comType = null;
	public Object gwinterface = null;
	public Object targetNetwork = null;
	public List<Object> receicedECUs = null;

	private JTextField textField_PhyRequestID;
	private JTextField textField_PhyResponseID;
	private JTextField textField_FunRequestID;
	private JComboBox comboBox_ByteOder = new JComboBox();
	private JComboBox comboBox_Gateway = new JComboBox();
	private JComboBox comboBox_TargetNet = new JComboBox();
	private JList list_ReceivedECUs = new JList();
	private JScrollPane scrollPane = new JScrollPane();
	private JRadioButton rdbtn_CANType = new JRadioButton("CAN");
	private JRadioButton rdbtn_CANFDType = new JRadioButton("CAN FD");
	private final ButtonGroup btnGrp_COMType = new ButtonGroup();

	/**
	 * Create the dialog.
	 */
	public DiagCanMsgInputDialog(final Object[] ecuInterfaces, final Thread callerThread) {
		if (ecuInterfaces == null) {
			return;
		}
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				isCancel = true;
				dispose();
				synchronized (callerThread) {
					callerThread.notify();
				}
			}
		});

		setSize(480, 559);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("Diagnostic CAN Message Creater");
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) screensize.getWidth() / 2 - getWidth() / 2;
		int y = (int) screensize.getHeight() / 2 - getHeight() / 2;
		setLocation(x, y);
		// setBounds(200, 200, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lbl_PhyRequestID = new JLabel("Phy Request ID(Hex):");
		lbl_PhyRequestID.setBounds(25, 13, 199, 37);
		lbl_PhyRequestID.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_PhyRequestID);

		textField_PhyRequestID = new JTextField();
		textField_PhyRequestID.setBounds(238, 13, 196, 37);
		textField_PhyRequestID.setToolTipText(
				"ID\u8303\u56F4\u4E3A0x700~0x7EF\uFF0C0x\u524D\u7F00\u53EF\u8981\u53EF\u4E0D\u8981\uFF01");
		textField_PhyRequestID.setText("");
		textField_PhyRequestID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		contentPanel.add(textField_PhyRequestID);
		textField_PhyRequestID.setColumns(10);

		JLabel lbl_PhyResponseID = new JLabel("Phy Response ID(Hex):");
		lbl_PhyResponseID.setBounds(25, 63, 199, 37);
		lbl_PhyResponseID.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_PhyResponseID);

		textField_PhyResponseID = new JTextField();
		textField_PhyResponseID.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				int phyResID = hextoDec(textField_PhyRequestID.getText());
				if (phyResID >= Integer.decode("0x700") && phyResID <= (Integer.decode("0x7EF") - 128)) {
					textField_PhyResponseID.setText(Integer.toHexString(phyResID + 128));
				}
			}
		});
//		textField_PhyResponseID.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				int phyResID = hextoDec(textField_PhyRequestID.getText())+128;
//				textField_PhyResponseID.setText(Integer.toHexString(phyResID));
//			}
//		});
		textField_PhyResponseID.setBounds(238, 63, 196, 37);
		textField_PhyResponseID.setToolTipText(
				"ID\u8303\u56F4\u4E3A0x700~0x7EF\uFF0C0x\u524D\u7F00\u53EF\u8981\u53EF\u4E0D\u8981\uFF01");
		textField_PhyResponseID.setText("");
		textField_PhyResponseID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		textField_PhyResponseID.setColumns(10);
		contentPanel.add(textField_PhyResponseID);

		JLabel lbl_FunRequestID = new JLabel("Fun Request ID(Hex):");
		lbl_FunRequestID.setBounds(25, 113, 199, 37);
		lbl_FunRequestID.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_FunRequestID);

		textField_FunRequestID = new JTextField();
		textField_FunRequestID.setBounds(238, 113, 196, 37);
		textField_FunRequestID.setText("0x7DF");
		textField_FunRequestID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		textField_FunRequestID.setColumns(10);
		contentPanel.add(textField_FunRequestID);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 263, 440, 2);
		contentPanel.add(separator);

		JLabel lbl_ComType = new JLabel("Communication Type:");
		lbl_ComType.setBounds(25, 213, 199, 37);
		lbl_ComType.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_ComType);
		rdbtn_CANType.setBounds(238, 219, 76, 27);

		rdbtn_CANType.setFont(new Font("Arial", Font.BOLD, 15));
		btnGrp_COMType.add(rdbtn_CANType);
		contentPanel.add(rdbtn_CANType);
		rdbtn_CANType.setSelected(true);
		rdbtn_CANFDType.setBounds(348, 219, 86, 27);

		rdbtn_CANFDType.setFont(new Font("Arial", Font.BOLD, 15));
		btnGrp_COMType.add(rdbtn_CANFDType);
		contentPanel.add(rdbtn_CANFDType);

		JLabel lbl_SelectGw = new JLabel("Select GateWay:");
		lbl_SelectGw.setBounds(25, 278, 199, 37);
		lbl_SelectGw.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_SelectGw);

		JLabel lbl_SelectTargetNet = new JLabel("Select Target Network:");
		lbl_SelectTargetNet.setBounds(25, 329, 199, 37);
		lbl_SelectTargetNet.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_SelectTargetNet);

		JLabel lbl_SelectReceivedECUs = new JLabel("Select Received ECUs:");
		lbl_SelectReceivedECUs.setBounds(25, 381, 199, 37);
		lbl_SelectReceivedECUs.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_SelectReceivedECUs);
		comboBox_Gateway.setBounds(238, 278, 197, 37);

		// �������������б�
		comboBox_Gateway.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		comboBox_Gateway.setModel(new DefaultComboBoxModel(ecuInterfaces));
		comboBox_Gateway.setSelectedItem(ecuInterfaces[ecuInterfaces.length - 1]);
		contentPanel.add(comboBox_Gateway);

		JLabel lbl_ByteOder = new JLabel("Byte Order:");
		lbl_ByteOder.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		lbl_ByteOder.setBounds(25, 163, 121, 37);
		contentPanel.add(lbl_ByteOder);

		comboBox_ByteOder.setModel(new DefaultComboBoxModel(
				new String[] { "Intel Standard", "Motorola Forward LSB", "Motorola Forward MSB" }));
		comboBox_ByteOder.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		comboBox_ByteOder.setBounds(238, 163, 196, 37);
		comboBox_ByteOder.setSelectedIndex(1);
		contentPanel.add(comboBox_ByteOder);

		comboBox_Gateway.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					Object[] channelCommunications = getAllChannelCommunications(item);

					// �������������б�
//					comboBox_TargetNet.setModel(new DefaultComboBoxModel(new String[] { "Body", "Chassis", "Diagnostic" }));
					comboBox_TargetNet.setModel(new DefaultComboBoxModel(channelCommunications));
					comboBox_TargetNet.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
					comboBox_TargetNet.setBounds(238, 329, 199, 37);
					contentPanel.add(comboBox_TargetNet);
					comboBox_TargetNet.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								Object item = e.getItem();
								targetNetwork = item;

								// �����ɶ�ѡECU Interface��Jlist
								DefaultListModel listModel = createDefaultListModel(item.toString());
								scrollPane.setBounds(238, 381, 199, 80);
								contentPanel.add(scrollPane);

								list_ReceivedECUs.setModel(listModel);
								scrollPane.setViewportView(list_ReceivedECUs);
							}
						}
					});

				}
			}

		});

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("OK");
			okButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
					getJDialogInput();
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
					isCancel = true;
					dispose();
					synchronized (callerThread) {
						callerThread.notify();
					}
				}

			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
	}

	private Object[] getAllChannelCommunications(Object item) {
		List<Object> list = new ArrayList<Object>();
		list.add("Body");
		list.add("Chassis");
		list.add("Info");
		list.add("Powertrain");
		list.add("Diagnostic");

		Object[] channelCommunications = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			channelCommunications[i] = list.get(i);
		}

		return channelCommunications;
	}

	private DefaultListModel createDefaultListModel(String string) {
		DefaultListModel dlm = new DefaultListModel();
		dlm.addElement("ECU1");
		dlm.addElement("ECU2");
		dlm.addElement("ECU3");
		dlm.addElement("ECU4");
		dlm.addElement("ECU5");

		return dlm;
	}

	private void getJDialogInput() {
		gwinterface = comboBox_Gateway.getSelectedItem();
		phyRequestID = hextoDec(textField_PhyRequestID.getText());
		phyResponseID = hextoDec(textField_PhyResponseID.getText());
		funRequesID = hextoDec(textField_FunRequestID.getText());
		byteOrder = (String) comboBox_ByteOder.getSelectedItem();
		comType = rdbtn_CANType.isSelected() ? rdbtn_CANType.getText() : rdbtn_CANFDType.getText();
		receicedECUs = list_ReceivedECUs.getSelectedValuesList();
	}

	private int hextoDec(String str) {
		if (str == null || str.equals("")) { //$NON-NLS-1$
			return -1;
		}
		String hexadecimal = str.replaceAll("0x", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (hexadecimal.equals("")) {
			return -1;
		}
		Integer integer = Integer.valueOf(hexadecimal, 16);
		int decimal = integer.intValue();
		return decimal;
	}
}
