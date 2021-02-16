/**
 * 
 */
package gui.maxus;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import javax.swing.JTabbedPane;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Administrator
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class NMCanMsgInutDialog extends JDialog {

	public int messageID = -1;
	public String comType = null;
	public String byteOrder = null;
	public String nmType = null;
	public boolean isAutosarNM = true;
	public boolean isCancel = false;

	public int nmAsrCanMsgCycleOffset = -1;
	public int nmAsrCanMsgReducedTime = -1;
	public int genNodSleepTime = -1;
	public int nmNodeAddress = -1;

	public int baudrate = -1;
	public int nmAsrRepeatMessageTime = -1;
	public int nmAsrTimeoutTime = -1;
	public int nmAsrWaitBusSleepTime = -1;
	public int nmAsrCanMsgCycleTime = -1;
	public int iLTxTimeout = -1;
	public int nmMessageCount = -1;
	public int nmBaseAddress = -1;

	private final JPanel contentPanel = new JPanel();
	private JTextField text_MessageID;
	private JComboBox comboBox_NmType = new JComboBox();
	private JComboBox comboBox_ByteOrder = new JComboBox();
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtn_CANType = new JRadioButton("CAN");
	private JRadioButton rdbtn_CANFDType = new JRadioButton("CANFD");
	private JTextField text_NmAsrCanMsgCycleOffset;
	private JTextField text_NmAsrCanMsgReducedTime;
	private JTextField text_NmNodeAddress;
	private JTextField text_GenNodSleepTime;
	private JTextField text_NmAsrRepeatMessageTime;
	private JTextField text_NmAsrTimeoutTime;
	private JTextField text_NmAsrwaitBusSleepTime;
	private JTextField text_NmMessageCount;
	private JTextField text_NmAsrCanMsgCycleTime;
	private JTextField text_NmBaseAddress;
	private JTextField text_Baudrate;
	private JTextField text_ILTxTimeout;

	/**
	 * Create the dialog.
	 */
	public NMCanMsgInutDialog(final Thread callerThread) {
		// set SystemLookAndFeel style for dialog
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
		setSize(533, 570);
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("Network Management Message Creater");
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) screensize.getWidth() / 2 - this.getWidth() / 2;
		int y = (int) screensize.getHeight() / 2 - this.getHeight() / 2;
		setLocation(x, y);
//		setBounds(200, 200, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(4, 1, 520, 485);
		contentPanel.add(tabbedPane);

		JPanel panel_NmAttr = new JPanel();
		panel_NmAttr.setBackground(Color.WHITE);
		tabbedPane.addTab("NM Attributes", null, panel_NmAttr, null);
		panel_NmAttr.setLayout(null);

		JPanel panel_MsgAttr = new JPanel();
		panel_MsgAttr.setBackground(Color.WHITE);
		panel_MsgAttr.setBounds(11, 12, 490, 206);
		panel_NmAttr.add(panel_MsgAttr);
		panel_MsgAttr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Message Attributes",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_MsgAttr.setLayout(null);
		comboBox_NmType.setBounds(270, 153, 202, 35);
		panel_MsgAttr.add(comboBox_NmType);

		comboBox_NmType.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		comboBox_NmType.setModel(new DefaultComboBoxModel(new String[] { "AUTOSAR", "OSEK" }));
		comboBox_NmType.setSelectedIndex(0);

		JLabel lblSelectNmType = new JLabel("Select NM Type:");
		lblSelectNmType.setBounds(14, 154, 153, 33);
		panel_MsgAttr.add(lblSelectNmType);
		lblSelectNmType.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		JLabel lbl_MessageID = new JLabel("Message ID(Hex):");
		lbl_MessageID.setBounds(14, 24, 184, 33);
		panel_MsgAttr.add(lbl_MessageID);
		lbl_MessageID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_MessageID = new JTextField();
		text_MessageID.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				int phyResID = hextoDec(text_MessageID.getText());
				if (phyResID >= Integer.decode("0x400") && phyResID <= (Integer.decode("0x47F"))) {
					text_NmNodeAddress.setText("0x"+Integer.toHexString(phyResID&0x011));
				}
			}
		});
		text_MessageID.setBounds(270, 24, 202, 33);
		panel_MsgAttr.add(text_MessageID);
		text_MessageID.setToolTipText(
				"ID\u8303\u56F4\u4E3A0x400~0x47F\uFF0C0x\u524D\u7F00\u53EF\u4EE5\u4E0D\u586B\u5199\uFF01");
		text_MessageID.setText("");
		text_MessageID.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_MessageID.setColumns(10);

		buttonGroup.add(rdbtn_CANType);
		rdbtn_CANType.setBackground(Color.WHITE);
		rdbtn_CANType.setBounds(280, 70, 67, 27);
		panel_MsgAttr.add(rdbtn_CANType);
		rdbtn_CANType.setFont(new Font("Arial", Font.PLAIN, 15));
		rdbtn_CANType.setSelected(true);

		buttonGroup.add(rdbtn_CANFDType);
		rdbtn_CANFDType.setBackground(Color.WHITE);
		rdbtn_CANFDType.setBounds(383, 70, 97, 27);
		panel_MsgAttr.add(rdbtn_CANFDType);
		rdbtn_CANFDType.setFont(new Font("Arial", Font.PLAIN, 15));

		JLabel lbl_ComType = new JLabel("Communication Type:");
		lbl_ComType.setBounds(14, 66, 184, 33);
		panel_MsgAttr.add(lbl_ComType);
		lbl_ComType.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		JLabel lbl_ByteOrder = new JLabel("Byte Order:");
		lbl_ByteOrder.setBounds(14, 107, 184, 33);
		panel_MsgAttr.add(lbl_ByteOrder);
		lbl_ByteOrder.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		comboBox_ByteOrder.setBounds(270, 106, 202, 35);
		panel_MsgAttr.add(comboBox_ByteOrder);

		comboBox_ByteOrder.setModel(new DefaultComboBoxModel(
				new String[] { "Intel Standard", "Motorola Forward LSB", "Motorola Forward MSB" }));
		comboBox_ByteOrder.setSelectedIndex(1);
		comboBox_ByteOrder.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		JPanel panel_NodeAttr = new JPanel();
		panel_NodeAttr.setBackground(Color.WHITE);
		panel_NodeAttr.setBounds(10, 237, 490, 200);
		panel_NmAttr.add(panel_NodeAttr);
		panel_NodeAttr.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Node Attributes",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_NodeAttr.setLayout(null);

		JLabel lbl_NmAsrCanMsgCycleOffset = new JLabel("NmAsrCanMsgCycleOffset:");
		lbl_NmAsrCanMsgCycleOffset.setBounds(14, 23, 238, 33);
		panel_NodeAttr.add(lbl_NmAsrCanMsgCycleOffset);
		lbl_NmAsrCanMsgCycleOffset.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrCanMsgCycleOffset = new JTextField();
		text_NmAsrCanMsgCycleOffset.setBounds(270, 18, 201, 33);
		panel_NodeAttr.add(text_NmAsrCanMsgCycleOffset);
		text_NmAsrCanMsgCycleOffset.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrCanMsgCycleOffset.setText("0");
		text_NmAsrCanMsgCycleOffset.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrCanMsgCycleOffset.setColumns(10);

		JLabel lbl_GenNodSleepTime = new JLabel("GenNodSleepTime:");
		lbl_GenNodSleepTime.setBounds(14, 111, 238, 33);
		panel_NodeAttr.add(lbl_GenNodSleepTime);
		lbl_GenNodSleepTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrCanMsgReducedTime = new JTextField();
		text_NmAsrCanMsgReducedTime.setBounds(270, 65, 201, 33);
		panel_NodeAttr.add(text_NmAsrCanMsgReducedTime);
		text_NmAsrCanMsgReducedTime.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrCanMsgReducedTime.setText("0");
		text_NmAsrCanMsgReducedTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrCanMsgReducedTime.setColumns(10);

		JLabel lbl_NmAsrCanMsgReducedTime = new JLabel("NmAsrCanMsgReducedTime(ms):");
		lbl_NmAsrCanMsgReducedTime.setBounds(14, 65, 252, 33);
		panel_NodeAttr.add(lbl_NmAsrCanMsgReducedTime);
		lbl_NmAsrCanMsgReducedTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_GenNodSleepTime = new JTextField();
		text_GenNodSleepTime.setBounds(270, 111, 201, 33);
		panel_NodeAttr.add(text_GenNodSleepTime);
		text_GenNodSleepTime
				.setToolTipText("AUTOSAR/OSEK NM\u5C5E\u6027");
		text_GenNodSleepTime.setText("0");
		text_GenNodSleepTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_GenNodSleepTime.setColumns(10);
		
				JLabel lbl_NmNodeAddress = new JLabel("NmAsrNodeIdentifier\r\n/NmStationAddress(Hex):");
				lbl_NmNodeAddress.setBounds(14, 157, 344, 33);
				panel_NodeAttr.add(lbl_NmNodeAddress);
				lbl_NmNodeAddress.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
				
						text_NmNodeAddress = new JTextField();
						text_NmNodeAddress.setBounds(362, 157, 109, 33);
						panel_NodeAttr.add(text_NmNodeAddress);
						text_NmNodeAddress
								.setToolTipText("\u5341\u516D\u8FDB\u5236  \uFF0C0x\u524D\u7F00\u53EF\u4EE5\u4E0D\u586B\u5199\uFF01");
						text_NmNodeAddress.setText("");
						text_NmNodeAddress.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
						text_NmNodeAddress.setColumns(10);

		JPanel panel_NetAttr = new JPanel();
		panel_NetAttr.setBackground(Color.WHITE);
		tabbedPane.addTab("Network Attributes", null, panel_NetAttr, null);
		panel_NetAttr.setBorder(null);
		panel_NetAttr.setLayout(null);

		JLabel lbl_NmAsrRepeatMessageTime = new JLabel("NmAsrRepeatMessageTime(ms):");
		lbl_NmAsrRepeatMessageTime.setBounds(24, 75, 238, 33);
		panel_NetAttr.add(lbl_NmAsrRepeatMessageTime);
		lbl_NmAsrRepeatMessageTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrRepeatMessageTime = new JTextField();
		text_NmAsrRepeatMessageTime.setBounds(288, 75, 201, 33);
		panel_NetAttr.add(text_NmAsrRepeatMessageTime);
		text_NmAsrRepeatMessageTime.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrRepeatMessageTime.setText("1500");
		text_NmAsrRepeatMessageTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrRepeatMessageTime.setColumns(10);

		JLabel lbl_NmAsrTimeoutTime = new JLabel("NmAsrTimeoutTime(ms):");
		lbl_NmAsrTimeoutTime.setBounds(24, 183, 238, 33);
		panel_NetAttr.add(lbl_NmAsrTimeoutTime);
		lbl_NmAsrTimeoutTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrTimeoutTime = new JTextField();
		text_NmAsrTimeoutTime.setBounds(288, 183, 201, 33);
		panel_NetAttr.add(text_NmAsrTimeoutTime);
		text_NmAsrTimeoutTime.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrTimeoutTime.setText("2000");
		text_NmAsrTimeoutTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrTimeoutTime.setColumns(10);

		JLabel lbl_NmAsrwaitBusSleepTime = new JLabel("NmAsrwaitBusSleepTime(ms):");
		lbl_NmAsrwaitBusSleepTime.setBounds(24, 129, 238, 33);
		panel_NetAttr.add(lbl_NmAsrwaitBusSleepTime);
		lbl_NmAsrwaitBusSleepTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrwaitBusSleepTime = new JTextField();
		text_NmAsrwaitBusSleepTime.setBounds(288, 129, 201, 33);
		panel_NetAttr.add(text_NmAsrwaitBusSleepTime);
		text_NmAsrwaitBusSleepTime.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrwaitBusSleepTime.setText("2000");
		text_NmAsrwaitBusSleepTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrwaitBusSleepTime.setColumns(10);

		JLabel lbl_NmMessageCount = new JLabel("NmAsrMessageCount/NmMessageCount:");
		lbl_NmMessageCount.setBounds(24, 345, 304, 33);
		panel_NetAttr.add(lbl_NmMessageCount);
		lbl_NmMessageCount.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmMessageCount = new JTextField();
		text_NmMessageCount.setBounds(346, 345, 143, 33);
		panel_NetAttr.add(text_NmMessageCount);
		text_NmMessageCount.setToolTipText("AUTOSAR/OSEK NM\u5C5E\u6027");
		text_NmMessageCount.setText("128");
		text_NmMessageCount.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmMessageCount.setColumns(10);

		JLabel lbl_NmAsrCanMsgCycleTime = new JLabel("NmAsrCanMsgCycleTime(ms):");
		lbl_NmAsrCanMsgCycleTime.setBounds(24, 237, 238, 33);
		panel_NetAttr.add(lbl_NmAsrCanMsgCycleTime);
		lbl_NmAsrCanMsgCycleTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmAsrCanMsgCycleTime = new JTextField();
		text_NmAsrCanMsgCycleTime.setBounds(288, 237, 201, 33);
		panel_NetAttr.add(text_NmAsrCanMsgCycleTime);
		text_NmAsrCanMsgCycleTime.setToolTipText("AUTOSAR NM\u5C5E\u6027");
		text_NmAsrCanMsgCycleTime.setText("500");
		text_NmAsrCanMsgCycleTime.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmAsrCanMsgCycleTime.setColumns(10);

		JLabel lbl_NmBaseAddress = new JLabel("NmAsrBaseAddress/NmAsrBaseAddress:");
		lbl_NmBaseAddress
				.setToolTipText("\u5341\u516D\u8FDB\u5236  \uFF0C0x\u524D\u7F00\u53EF\u4EE5\u4E0D\u586B\u5199\uFF01");
		lbl_NmBaseAddress.setBounds(24, 399, 326, 33);
		panel_NetAttr.add(lbl_NmBaseAddress);
		lbl_NmBaseAddress.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));

		text_NmBaseAddress = new JTextField();
		text_NmBaseAddress.setBounds(346, 399, 143, 33);
		panel_NetAttr.add(text_NmBaseAddress);
		text_NmBaseAddress.setToolTipText("AUTOSAR/OSEK NM\u5C5E\u6027");
		text_NmBaseAddress.setText("0x400");
		text_NmBaseAddress.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_NmBaseAddress.setColumns(10);

		JLabel lbl_Baudrate = new JLabel("Baudrate(Bit/s):");
		lbl_Baudrate.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		lbl_Baudrate.setBounds(24, 21, 238, 33);
		panel_NetAttr.add(lbl_Baudrate);

		text_Baudrate = new JTextField();
		text_Baudrate.setToolTipText("");
		text_Baudrate.setText("500000");
		text_Baudrate.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_Baudrate.setColumns(10);
		text_Baudrate.setBounds(288, 21, 201, 33);
		panel_NetAttr.add(text_Baudrate);

		JLabel lbl_ILTxTimeout = new JLabel("ILTxTimeout:");
		lbl_ILTxTimeout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
		lbl_ILTxTimeout.setBounds(24, 291, 238, 33);
		panel_NetAttr.add(lbl_ILTxTimeout);

		text_ILTxTimeout = new JTextField();
		text_ILTxTimeout.setToolTipText("");
		text_ILTxTimeout.setText("0");
		text_ILTxTimeout.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
		text_ILTxTimeout.setColumns(10);
		text_ILTxTimeout.setBounds(288, 291, 201, 33);
		panel_NetAttr.add(text_ILTxTimeout);

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

	private void getJDialogInput() {
		// Message Attributes
		nmType = comboBox_NmType.getSelectedItem().toString();
		if (nmType != null && nmType.equals("OSEK")) {
			isAutosarNM = false;
		}
		comType = rdbtn_CANType.isSelected() ? rdbtn_CANType.getText() : rdbtn_CANFDType.getText();
		byteOrder = (String) comboBox_ByteOrder.getSelectedItem();
		try {
			messageID = hextoDec(text_MessageID.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Node Attributes
		try {
			nmAsrCanMsgCycleOffset = Integer.valueOf(text_NmAsrCanMsgCycleOffset.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmAsrCanMsgReducedTime = Integer.valueOf(text_NmAsrCanMsgReducedTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmNodeAddress = hextoDec(text_NmNodeAddress.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			genNodSleepTime = Integer.valueOf(text_GenNodSleepTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Network Attributes
		try {
			baudrate = Integer.valueOf(text_Baudrate.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmAsrRepeatMessageTime = Integer.valueOf(text_NmAsrRepeatMessageTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmAsrTimeoutTime = Integer.valueOf(text_NmAsrTimeoutTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmAsrWaitBusSleepTime = Integer.valueOf(text_NmAsrwaitBusSleepTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmAsrCanMsgCycleTime = Integer.valueOf(text_NmAsrCanMsgCycleTime.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			iLTxTimeout = Integer.valueOf(text_ILTxTimeout.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmMessageCount = Integer.valueOf(text_NmMessageCount.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			nmBaseAddress = hextoDec(text_NmBaseAddress.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static int hextoDec(String str) {
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
