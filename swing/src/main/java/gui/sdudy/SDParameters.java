/**
 * 
 */
package gui.sdudy;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Administrator
 *
 */
public class SDParameters extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String minorVersion_P = null;
	String minorVersion_C = null;
	String majorVersion_P = null;
	String majorVersion_C = null;

	double offerCyclicDelay_P = 0.0d;
//	double offerCyclicDelay_C = 0.0d;
	double initialDelayMin_P = 0.0d;
	double initialDelayMin_C = 0.0d;
	double initialDelayMax_P = 0.0d;
	double initialDelayMax_C = 0.0d;
	double initialRepetitionBaseDelay_P = 0.0d;
	double initialRepetitionBaseDelay_C = 0.0d;
	int initialRepetitionsMax_P = 0;
	int initialRepetitionsMax_C =0;

	double responseDelayMin_P = 0.0d;
//	double responseDelayMin_C = 0.01;
	double responseDelayMax_P = 0.0d;
//	double responseDelayMax_C = 0.0;
	int timeToLive_P = 0;
	int timeToLive_C = 0;
	
//	static SDParameters dialog = null;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField_MinorVersion_P;
	private JTextField textField_MinorVersion_C;
	private JTextField textField_MajorVersion_P;
	private JTextField textField_MajorVersion_C;
	
	private JTextField textField_OfferCyclicDelay_P;
	private JTextField textField_OfferCyclicDelay_C;
	private JTextField textField_InitialDelayMin_P;
	private JTextField textField_InitialDelayMin_C;
	private JTextField textField_InitialDelayMax_P;
	private JTextField textField_InitialDelayMax_C;
	private JTextField textField_InitialRepetitionBaseDelay_P;
	private JTextField textField_InitialRepetitionBaseDelay_C;
	private JTextField textField_InitialRepetitionsMax_P;
	private JTextField textField_InitialRepetitionsMax_C;
	
	private JTextField textField_ResponseDelayMin_P;
	private JTextField textField_ResponseDelayMin_C;
	private JTextField textField_ResponseDelayMax_P;
	private JTextField textField_ResponseDelayMax_C;
	private JTextField textField_TimeToLive_P;
	private JTextField textField_TimeToLive_C;

	/**
	 * Create the dialog.
	 */
	public SDParameters() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				getTextFieldValues();
			}
		});
		setAlwaysOnTop(true);
		setTitle("Service Discovery Parameters ");
		setBounds(100, 100, 663, 595);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panel_Title = new JPanel();
			panel_Title.setBounds(14, 6, 633, 34);
			contentPanel.add(panel_Title);
			panel_Title.setLayout(null);
			{
				JLabel lb_Parameters = new JLabel("Parameters");
				lb_Parameters.setBounds(0, 0, 198, 34);
				panel_Title.add(lb_Parameters);
				lb_Parameters.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_ProvidedServiceInstance = new JLabel("Provided Service Instance");
				lb_ProvidedServiceInstance.setBounds(212, 0, 194, 34);
				panel_Title.add(lb_ProvidedServiceInstance);
				lb_ProvidedServiceInstance.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_ConsumedServiceInstance = new JLabel("Consumed Service Instance");
				lb_ConsumedServiceInstance.setBounds(420, 0, 213, 34);
				panel_Title.add(lb_ConsumedServiceInstance);
				lb_ConsumedServiceInstance.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
		}
		{
			JPanel panel_Version = new JPanel();
			panel_Version
					.setBorder(new TitledBorder(null, "Version", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_Version.setBounds(14, 46, 617, 99);
			contentPanel.add(panel_Version);
			panel_Version.setLayout(null);
			{
				JLabel lb_MinorVersion = new JLabel("Minor Version:");
				lb_MinorVersion.setBounds(6, 20, 208, 34);
				panel_Version.add(lb_MinorVersion);
				lb_MinorVersion.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_MajorVersion = new JLabel("Major Version:");
				lb_MajorVersion.setBounds(6, 58, 208, 34);
				panel_Version.add(lb_MajorVersion);
				lb_MajorVersion.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				textField_MinorVersion_P = new JTextField();
				textField_MinorVersion_P.setBounds(228, 20, 148, 29);
				panel_Version.add(textField_MinorVersion_P);
				textField_MinorVersion_P.setText("0");
				textField_MinorVersion_P.setColumns(15);
			}
			{
				textField_MajorVersion_P = new JTextField();
				textField_MajorVersion_P.setBounds(228, 58, 148, 29);
				panel_Version.add(textField_MajorVersion_P);
				textField_MajorVersion_P.setText("1");
				textField_MajorVersion_P.setColumns(15);
			}
			{
				textField_MinorVersion_C = new JTextField();
				textField_MinorVersion_C.setBounds(436, 20, 148, 29);
				panel_Version.add(textField_MinorVersion_C);
				textField_MinorVersion_C.setText("0");
				textField_MinorVersion_C.setColumns(15);
			}
			{
				textField_MajorVersion_C = new JTextField();
				textField_MajorVersion_C.setBounds(436, 58, 148, 29);
				panel_Version.add(textField_MajorVersion_C);
				textField_MajorVersion_C.setText("1");
				textField_MajorVersion_C.setColumns(15);
			}
		}
		{
			JPanel panel_ServiceDiscoveryConfig = new JPanel();
			panel_ServiceDiscoveryConfig.setBorder(new TitledBorder(null, "Service Discovery Configuration",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_ServiceDiscoveryConfig.setBounds(14, 151, 619, 204);
			contentPanel.add(panel_ServiceDiscoveryConfig);
			panel_ServiceDiscoveryConfig.setLayout(null);
			{
				JLabel lb_OfferCyclicDelay = new JLabel("Offer Cyclic Delay:");
				lb_OfferCyclicDelay.setBounds(6, 20, 208, 34);
				panel_ServiceDiscoveryConfig.add(lb_OfferCyclicDelay);
				lb_OfferCyclicDelay.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_InitialDelayMin = new JLabel("Initial Delay Min:");
				lb_InitialDelayMin.setBounds(6, 56, 208, 34);
				panel_ServiceDiscoveryConfig.add(lb_InitialDelayMin);
				lb_InitialDelayMin.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_InitialDelayMax = new JLabel("Initial Delay Max:");
				lb_InitialDelayMax.setBounds(6, 89, 208, 34);
				panel_ServiceDiscoveryConfig.add(lb_InitialDelayMax);
				lb_InitialDelayMax.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_InitialRepetitionBaseDelay = new JLabel("Initial Repetition Base Delay:");
				lb_InitialRepetitionBaseDelay.setBounds(6, 123, 208, 34);
				panel_ServiceDiscoveryConfig.add(lb_InitialRepetitionBaseDelay);
				lb_InitialRepetitionBaseDelay.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_InitialRepetitionsMax = new JLabel("Initial Repetitions Max:");
				lb_InitialRepetitionsMax.setBounds(6, 156, 208, 34);
				panel_ServiceDiscoveryConfig.add(lb_InitialRepetitionsMax);
				lb_InitialRepetitionsMax.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				textField_InitialRepetitionsMax_P = new JTextField();
				textField_InitialRepetitionsMax_P.setBounds(228, 156, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialRepetitionsMax_P);
				textField_InitialRepetitionsMax_P.setText("5");
				textField_InitialRepetitionsMax_P.setColumns(15);
			}
			{
				textField_InitialRepetitionBaseDelay_P = new JTextField();
				textField_InitialRepetitionBaseDelay_P.setBounds(228, 123, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialRepetitionBaseDelay_P);
				textField_InitialRepetitionBaseDelay_P.setText("0.05");
				textField_InitialRepetitionBaseDelay_P.setColumns(15);
			}
			{
				textField_InitialDelayMax_P = new JTextField();
				textField_InitialDelayMax_P.setBounds(228, 89, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialDelayMax_P);
				textField_InitialDelayMax_P.setText("0.1");
				textField_InitialDelayMax_P.setColumns(15);
			}
			{
				textField_InitialDelayMin_P = new JTextField();
				textField_InitialDelayMin_P.setBounds(228, 56, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialDelayMin_P);
				textField_InitialDelayMin_P.setText("0.01");
				textField_InitialDelayMin_P.setColumns(15);
			}
			{
				textField_OfferCyclicDelay_P = new JTextField();
				textField_OfferCyclicDelay_P.setBounds(228, 20, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_OfferCyclicDelay_P);
				textField_OfferCyclicDelay_P.setText("2.0");
				textField_OfferCyclicDelay_P.setColumns(15);
			}
			{
				JLabel lblNewLabel1 = new JLabel("s");
				lblNewLabel1.setBounds(387, 31, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel1);
			}
			{
				JLabel lblNewLabel2 = new JLabel("s");
				lblNewLabel2.setBounds(387, 67, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel2);
			}
			{
				JLabel lblNewLabel3 = new JLabel("s");
				lblNewLabel3.setBounds(387, 100, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel3);
			}
			{
				JLabel lblNewLabel4 = new JLabel("s");
				lblNewLabel4.setBounds(387, 138, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel4);
			}
			{
				JLabel lblNewLabel4_1 = new JLabel("s");
				lblNewLabel4_1.setBounds(595, 138, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel4_1);
			}
			{
				textField_InitialRepetitionsMax_C = new JTextField();
				textField_InitialRepetitionsMax_C.setBounds(436, 156, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialRepetitionsMax_C);
				textField_InitialRepetitionsMax_C.setText("5");
				textField_InitialRepetitionsMax_C.setColumns(15);
			}
			{
				textField_InitialRepetitionBaseDelay_C = new JTextField();
				textField_InitialRepetitionBaseDelay_C.setBounds(436, 123, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialRepetitionBaseDelay_C);
				textField_InitialRepetitionBaseDelay_C.setText("0.05");
				textField_InitialRepetitionBaseDelay_C.setColumns(15);
			}
			{
				textField_InitialDelayMax_C = new JTextField();
				textField_InitialDelayMax_C.setBounds(436, 89, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialDelayMax_C);
				textField_InitialDelayMax_C.setText("0.1");
				textField_InitialDelayMax_C.setColumns(15);
			}
			{
				textField_InitialDelayMin_C = new JTextField();
				textField_InitialDelayMin_C.setBounds(436, 56, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_InitialDelayMin_C);
				textField_InitialDelayMin_C.setText("0.01");
				textField_InitialDelayMin_C.setColumns(15);
			}
			{
				textField_OfferCyclicDelay_C = new JTextField();
				textField_OfferCyclicDelay_C.setBounds(436, 20, 148, 29);
				panel_ServiceDiscoveryConfig.add(textField_OfferCyclicDelay_C);
				textField_OfferCyclicDelay_C.setText("NA");
				textField_OfferCyclicDelay_C.setColumns(15);
			}
			{
				JLabel lblNewLabel2_1 = new JLabel("s");
				lblNewLabel2_1.setBounds(595, 67, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel2_1);
			}
			{
				JLabel lblNewLabel1_1 = new JLabel("s");
				lblNewLabel1_1.setBounds(595, 31, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel1_1);
			}
			{
				JLabel lblNewLabel3_1 = new JLabel("s");
				lblNewLabel3_1.setBounds(595, 100, 18, 18);
				panel_ServiceDiscoveryConfig.add(lblNewLabel3_1);
			}
		}
		{
			JPanel panel_ResponseConfig = new JPanel();
			panel_ResponseConfig.setBorder(new TitledBorder(null, "Response Configuration", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel_ResponseConfig.setBounds(14, 361, 619, 140);
			contentPanel.add(panel_ResponseConfig);
			panel_ResponseConfig.setLayout(null);
			{
				JLabel lb_ResponseDelayMin = new JLabel("Response Delay Min:");
				lb_ResponseDelayMin.setBounds(6, 20, 208, 34);
				panel_ResponseConfig.add(lb_ResponseDelayMin);
				lb_ResponseDelayMin.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_ResponseDelayMax = new JLabel("Response Delay Max:");
				lb_ResponseDelayMax.setBounds(6, 53, 208, 34);
				panel_ResponseConfig.add(lb_ResponseDelayMax);
				lb_ResponseDelayMax.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				JLabel lb_TTL = new JLabel("TTL :");
				lb_TTL.setBounds(6, 87, 208, 34);
				panel_ResponseConfig.add(lb_TTL);
				lb_TTL.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
			}
			{
				textField_TimeToLive_P = new JTextField();
				textField_TimeToLive_P.setBounds(228, 87, 148, 29);
				panel_ResponseConfig.add(textField_TimeToLive_P);
				textField_TimeToLive_P.setText("10");
				textField_TimeToLive_P.setColumns(15);
			}
			{
				textField_ResponseDelayMax_P = new JTextField();
				textField_ResponseDelayMax_P.setBounds(228, 53, 148, 29);
				panel_ResponseConfig.add(textField_ResponseDelayMax_P);
				textField_ResponseDelayMax_P.setText("0.5");
				textField_ResponseDelayMax_P.setColumns(15);
			}
			{
				textField_ResponseDelayMin_P = new JTextField();
				textField_ResponseDelayMin_P.setBounds(228, 20, 148, 29);
				panel_ResponseConfig.add(textField_ResponseDelayMin_P);
				textField_ResponseDelayMin_P.setText("0.01");
				textField_ResponseDelayMin_P.setColumns(15);
			}
			{
				JLabel lblNewLabel5 = new JLabel("s");
				lblNewLabel5.setBounds(387, 31, 18, 18);
				panel_ResponseConfig.add(lblNewLabel5);
			}
			{
				JLabel lblNewLabel6 = new JLabel("s");
				lblNewLabel6.setBounds(387, 64, 18, 18);
				panel_ResponseConfig.add(lblNewLabel6);
			}
			{
				JLabel lblNewLabel7 = new JLabel("s");
				lblNewLabel7.setBounds(387, 98, 18, 18);
				panel_ResponseConfig.add(lblNewLabel7);
			}
			{
				textField_TimeToLive_C = new JTextField();
				textField_TimeToLive_C.setBounds(436, 87, 148, 29);
				panel_ResponseConfig.add(textField_TimeToLive_C);
				textField_TimeToLive_C.setText("10");
				textField_TimeToLive_C.setColumns(15);
			}
			{
				JLabel lblNewLabel7_1 = new JLabel("s");
				lblNewLabel7_1.setBounds(595, 98, 18, 18);
				panel_ResponseConfig.add(lblNewLabel7_1);
			}
			{
				textField_ResponseDelayMax_C = new JTextField();
				textField_ResponseDelayMax_C.setBounds(436, 53, 148, 29);
				panel_ResponseConfig.add(textField_ResponseDelayMax_C);
				textField_ResponseDelayMax_C.setText("NA");
				textField_ResponseDelayMax_C.setColumns(15);
			}
			{
				textField_ResponseDelayMin_C = new JTextField();
				textField_ResponseDelayMin_C.setBounds(436, 20, 148, 29);
				panel_ResponseConfig.add(textField_ResponseDelayMin_C);
				textField_ResponseDelayMin_C.setText("NA");
				textField_ResponseDelayMin_C.setColumns(15);
			}
			{
				JLabel lblNewLabel5_1 = new JLabel("s");
				lblNewLabel5_1.setBounds(595, 31, 18, 18);
				panel_ResponseConfig.add(lblNewLabel5_1);
			}
			{
				JLabel lblNewLabel6_1 = new JLabel("s");
				lblNewLabel6_1.setBounds(595, 64, 18, 18);
				panel_ResponseConfig.add(lblNewLabel6_1);
			}
		}
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
						getTextFieldValues();
					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Rest");
				cancelButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						resetTextFieldValues();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void getTextFieldValues() {
		 minorVersion_P = textField_MinorVersion_P.getText();
		 minorVersion_C = textField_MinorVersion_C.getText();
		 majorVersion_P = textField_MajorVersion_P.getText();
		 majorVersion_C = textField_MajorVersion_C.getText();

		 offerCyclicDelay_P = Double.valueOf(textField_OfferCyclicDelay_P.getText());
//		 offerCyclicDelay_C = Double.valueOf(textField_OfferCyclicDelay_C.getText());
		 initialDelayMin_P = Double.valueOf(textField_InitialDelayMin_P.getText());
		 initialDelayMin_C = Double.valueOf(textField_InitialDelayMin_C.getText());
		 initialDelayMax_P = Double.valueOf(textField_InitialDelayMax_P.getText());
		 initialDelayMax_C = Double.valueOf(textField_InitialDelayMax_C.getText());
		 initialRepetitionBaseDelay_P = Double.valueOf(textField_InitialRepetitionBaseDelay_P.getText());
		 initialRepetitionBaseDelay_C = Double.valueOf(textField_InitialRepetitionBaseDelay_C.getText());
		 initialRepetitionsMax_P = Integer.valueOf(textField_InitialRepetitionsMax_P.getText());
		 initialRepetitionsMax_C = Integer.valueOf(textField_InitialRepetitionsMax_C.getText());

		 responseDelayMin_P = Double.valueOf(textField_ResponseDelayMin_P.getText());
//		 responseDelayMin_C = Double.valueOf(textField_ResponseDelayMin_C.getText());
		 responseDelayMax_P = Double.valueOf(textField_ResponseDelayMax_P.getText());
//		 responseDelayMax_C = Double.valueOf(textField_ResponseDelayMax_C.getText());
		 timeToLive_P = Integer.valueOf(textField_TimeToLive_P.getText());
		 timeToLive_C = Integer.valueOf(textField_TimeToLive_C.getText());
	}
	
	private void resetTextFieldValues() {
		 textField_MinorVersion_P.setText("0");
		 textField_MinorVersion_C.setText("0");
		 textField_MajorVersion_P.setText("1");
		 textField_MajorVersion_C.setText("1");
			
		 textField_OfferCyclicDelay_P.setText("2.0");
		 textField_OfferCyclicDelay_C.setText("NA");
		 textField_InitialDelayMin_P.setText("0.01");
		 textField_InitialDelayMin_C.setText("0.01");
		 textField_InitialDelayMax_P.setText("0.1");
		 textField_InitialDelayMax_C.setText("0.1");
		 textField_InitialRepetitionBaseDelay_P.setText("0.05");
		 textField_InitialRepetitionBaseDelay_C.setText("0.05");
		 textField_InitialRepetitionsMax_P.setText("5");
		 textField_InitialRepetitionsMax_C.setText("5");
			
		 textField_ResponseDelayMin_P.setText("0.01");
		 textField_ResponseDelayMin_C.setText("NA");
		 textField_ResponseDelayMax_P.setText("0.5");
		 textField_ResponseDelayMax_C.setText("NA");
		 textField_TimeToLive_P.setText("10");
		 textField_TimeToLive_C.setText("10");
	}

}
