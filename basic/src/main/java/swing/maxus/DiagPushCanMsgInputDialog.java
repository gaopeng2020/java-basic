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
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


/******************************************************************************************
 *   ������ͱ����û�������壻
 * Gao Peng gaopeng@e-planet.cn
 ******************************************************************************************/
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DiagPushCanMsgInputDialog extends JDialog {

	 //�����ĳ�Ա����
	public boolean isCancel = false;
	public int messageID = -1;
	public String comType = null;
	public String byteOder = null;
	public Object gwInterface = null;
	public Object targetNetwork = null;
	public List<Object> receicedECUs = null;

	//��ʼ�����
	private final JPanel contentPanel = new JPanel();
	private JTextField textField_MessageID;
	private JComboBox comboBox_Gateway = new JComboBox();
	private JComboBox comboBox_TargetNet = new JComboBox();
	private JComboBox comboBox_ByteOder = new JComboBox();
	private JList list_ReceivedECUs = new JList();
	private JScrollPane scrollPane = new JScrollPane();
	private JRadioButton rdbtn_CANType = new JRadioButton("CAN");
	private JRadioButton rdbtn_CANFDType = new JRadioButton("CAN FD");
	private final ButtonGroup btnGrp_COMType = new ButtonGroup();

	/**
	 * Create the dialog.
	 * 
	 * @param ecuInterfaces
	 * @param callerThread
	 */
	public DiagPushCanMsgInputDialog(final Object[] ecuInterfaces, final Thread callerThread) {
		if (ecuInterfaces == null) {
			synchronized (callerThread) {
				callerThread.notify();
			}
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
		setSize(480, 480);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("DTC Push CAN Message Creater");
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) screensize.getWidth() / 2 - getWidth() / 2;
		int y = (int) screensize.getHeight() / 2 - getHeight() / 2;
		setLocation(x, y);
		// setBounds(200, 200, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lbl_MessageID = new JLabel("Message ID(Hex):");
		lbl_MessageID.setBounds(26, 34, 199, 37);
		lbl_MessageID.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_MessageID);

		textField_MessageID = new JTextField();
		textField_MessageID.setToolTipText("ID\u8303\u56F4\u4E3A0x490~0x4FF\uFF0C0x\u524D\u7F00\u53EF\u8981\u53EF\u4E0D\u8981\uFF01");
		textField_MessageID.setBounds(239, 34, 208, 37);
		textField_MessageID.setText("");
		textField_MessageID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		contentPanel.add(textField_MessageID);
		textField_MessageID.setColumns(10);

		JLabel lbl_ComType = new JLabel("Communication Type:");
		lbl_ComType.setBounds(26, 84, 199, 37);
		lbl_ComType.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_ComType);
		
		rdbtn_CANType.setBounds(239, 90, 76, 27);
		rdbtn_CANType.setFont(new Font("Arial", Font.BOLD, 15));
		rdbtn_CANType.setSelected(true);
		btnGrp_COMType.add(rdbtn_CANType);
		contentPanel.add(rdbtn_CANType);
		
		rdbtn_CANFDType.setBounds(356, 90, 86, 27);
		rdbtn_CANFDType.setFont(new Font("Arial", Font.BOLD, 15));
		btnGrp_COMType.add(rdbtn_CANFDType);
		contentPanel.add(rdbtn_CANFDType);

		JLabel lbl_Gateway = new JLabel("Select GateWay:");
		lbl_Gateway.setBounds(26, 194, 199, 37);
		lbl_Gateway.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_Gateway);

		JLabel lbl_ByteOrder = new JLabel("Byte Order:");
		lbl_ByteOrder.setBounds(26, 134, 199, 37);
		lbl_ByteOrder.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_ByteOrder);

		comboBox_ByteOder.setBounds(239, 134, 208, 37);
		comboBox_ByteOder.setModel(new DefaultComboBoxModel(
				new String[] { "Intel Standard", "Motorola Forward LSB", "Motorola Forward MSB" }));
		comboBox_ByteOder.setSelectedIndex(1);
		comboBox_ByteOder.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		contentPanel.add(comboBox_ByteOder);
		comboBox_ByteOder.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				byteOder = (String) comboBox_ByteOder.getSelectedItem();
				
			}
		});

		JLabel lbl_TargetNet = new JLabel("Select Target Network:");
		lbl_TargetNet.setBounds(26, 245, 199, 37);
		lbl_TargetNet.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_TargetNet);

		JLabel lbl_ReceivedECUs = new JLabel("Select Received ECUs:");
		lbl_ReceivedECUs.setBounds(26, 304, 199, 37);
		lbl_ReceivedECUs.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
		contentPanel.add(lbl_ReceivedECUs);

		// �������������б�
		comboBox_Gateway.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		comboBox_Gateway.setModel(new DefaultComboBoxModel(ecuInterfaces));
		contentPanel.add(comboBox_Gateway);
		comboBox_Gateway.setBounds(239, 194, 208, 37);
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
					comboBox_TargetNet.setBounds(239, 250, 208, 37);
					contentPanel.add(comboBox_TargetNet);
					comboBox_TargetNet.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							if (e.getStateChange() == ItemEvent.SELECTED) {
								Object item = e.getItem();
								targetNetwork = item;

								// �����ɶ�ѡECU Interface��Jlist
								DefaultListModel listModel = createDefaultListModel(item.toString());
								scrollPane.setBounds(239, 310, 208, 80);
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
		messageID = hextoDec(textField_MessageID.getText());
		gwInterface = comboBox_Gateway.getSelectedItem();
		comType = rdbtn_CANType.isSelected() ? rdbtn_CANType.getText() : rdbtn_CANFDType.getText();
		byteOder = (String) comboBox_ByteOder.getSelectedItem();
		gwInterface = comboBox_TargetNet.getSelectedItem();
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
