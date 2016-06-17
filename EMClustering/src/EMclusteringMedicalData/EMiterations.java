package EMclusteringMedicalData;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class EMiterations {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JLabel label_6;
	private JLabel label_7;
	

	EMsample sample;// = new EMsample();
	GaussianMixture mixture;// = new GaussianMixture();
	String filename = "Results.txt";
	int size = 0;
	private JMenuBar menuBar;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EMiterations window = new EMiterations();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EMiterations() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setForeground(new Color(233, 150, 122));
		frame.setBounds(500, 30, 900, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("EM Clustering");
		
		JPanel panel = new JPanel();
		panel.setForeground(new Color(240, 248, 255));
		panel.setBackground(new Color(248, 248, 255));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		/////////////////////////////////////////////////
		
		/**
		 * Set DB link
		 */
		JLabel label = new JLabel("Data Base");
		label.setForeground(new Color(0, 102, 153));
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBackground(new Color(0, 128, 128));
		label.setBounds(145, 28, 139, 16);
		panel.add(label);
		
		/**
		 * Link request
		 */
		JLabel lblBdLink = new JLabel("Link:");
		lblBdLink.setForeground(new Color(0, 0, 0));
		lblBdLink.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBdLink.setBounds(40, 71, 56, 16);
		panel.add(lblBdLink);
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBconnector.dbUrl = textField.getText();
				Image check = new ImageIcon(this.getClass().getResource("/checkmark.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
				label_3.setIcon(new ImageIcon(check));
			}
		});
		
		
		/**
		 * Table Name request
		 */
		JLabel lblBdTabelName = new JLabel("Table Name:");
		lblBdTabelName.setForeground(new Color(0, 0, 0));
		lblBdTabelName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBdTabelName.setBounds(40, 139, 125, 16);
		panel.add(lblBdTabelName);

		

		
		
		JLabel lblMixture = new JLabel("Mixture");
		lblMixture.setForeground(new Color(0, 102, 153));
		lblMixture.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblMixture.setBackground(new Color(0, 128, 128));
		lblMixture.setBounds(155, 211, 139, 16);
		panel.add(lblMixture);
		

		
		JLabel lblInsertMixtureSize = new JLabel("Insert mixture size:");
		lblInsertMixtureSize.setForeground(new Color(0, 0, 0));
		lblInsertMixtureSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInsertMixtureSize.setBounds(38, 252, 139, 19);
		panel.add(lblInsertMixtureSize);
		

		
		textField.setBounds(83, 69, 241, 22);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DBconnector.tableName = textField_1.getText();
				Image check = new ImageIcon(this.getClass().getResource("/checkmark.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
				label_2.setIcon(new ImageIcon(check));
			}
		});
		textField_1.setBounds(135, 137, 189, 22);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();//Mixture size
		textField_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					size= Integer.parseInt(textField_2.getText());
					if(size > 0){
					Image check = new ImageIcon(this.getClass().getResource("/checkmark.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
					label_1.setIcon(new ImageIcon(check));
					}
					else{
						EventQueue.invokeLater(new Runnable() {
							public void run() {
								try {
									Alert a = new Alert("Please enter a valid number");
									a.getFrame().setVisible(true);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});	
					}
						
				}catch(NumberFormatException e1){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								Alert a = new Alert("Please enter a valid number");
								a.getFrame().setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});
		textField_2.setBounds(180, 251, 144, 22);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		label_1 = new JLabel("");
		Image minus = new ImageIcon(this.getClass().getResource("/minus.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
		label_1.setIcon(new ImageIcon(minus));
		label_1.setBounds(336, 231, 56, 59);
		panel.add(label_1);
		
		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(minus));
		label_2.setBounds(335, 121, 56, 59);
		panel.add(label_2);
		
		label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon(minus));
		label_3.setBounds(336, 49, 56, 59);
		panel.add(label_3);
		
		label_4 = new JLabel("");
		label_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Image run2 = new ImageIcon(this.getClass().getResource("/run2.png")).getImage();
				label_4.setIcon(new ImageIcon(run2));
			}
			//@Override
			/*public void mouseReleased(MouseEvent e) {
				Image run = new ImageIcon(this.getClass().getResource("/run.png")).getImage();
				label_4.setIcon(new ImageIcon(run));
			}*/
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(sample != null && mixture != null){

					sample.computeIterations();
					System.out.println("Mix: "+ mixture.toString());
					
					Image run = new ImageIcon(this.getClass().getResource("/run.png")).getImage();
					label_4.setIcon(new ImageIcon(run));
				}
				else{
					try {
						throw new NullSampleException();
					} catch (NullSampleException e1) {
						e1.error();
					}
				}

			}
			
			
		});
		Image run = new ImageIcon(this.getClass().getResource("/run.png")).getImage();
		label_4.setIcon(new ImageIcon(run));
		
		label_4.setBounds(499, 139, 132, 132);
		panel.add(label_4);
		
		JButton button = new JButton("Create Sample and Mixture");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				button.setBackground(new Color(0, 102, 153));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				button.setBackground(new Color(70, 130, 180));
			}
		});
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(size != 0 && DBconnector.dbUrl != "" && DBconnector.tableName != ""){
					sample = new EMsample(DBconnector.getData());

					double min = sample.minimumValue();
					System.out.println("min: " +min);
					double max = sample.maximumValue();
					System.out.println("max: "+max);
					int dimension =  sample.dimension();
					mixture = new GaussianMixture( size , dimension);
					mixture.mix(min, max); //min, max
					mixture.assignPiValues(sample);
					sample.setMixture(mixture);
					Image unit2 = new ImageIcon(this.getClass().getResource("/unit-completed.png")).getImage();
					label_7.setIcon(new ImageIcon(unit2));
				}
				else{
					try {
						throw new NoInitializationSource();
					} catch (NoInitializationSource e1) {
						e1.error();
					}
				}
			}
		});
		button.setForeground(new Color(255, 255, 255));
		button.setBackground(new Color(70, 130, 180));
		button.setBounds(499, 55, 189, 40);
		panel.add(button);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(0, 128, 128));
		separator.setBackground(new Color(0, 128, 128));
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(426, 28, 5, 262);
		panel.add(separator);
		
		label_5 = new JLabel("");
		label_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Image stop2 = new ImageIcon(this.getClass().getResource("/stop2.png")).getImage();
				label_5.setIcon(new ImageIcon(stop2));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Image stop = new ImageIcon(this.getClass().getResource("/stop.png")).getImage();
				label_5.setIcon(new ImageIcon(stop));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if(sample != null )
					sample.setRunning(false);
				else
					try {
						throw new NullSampleException();
					} catch (NullSampleException e1) {
						e1.error();
					}
			}
		});
		Image stop = new ImageIcon(this.getClass().getResource("/stop.png")).getImage();
		label_5.setIcon(new ImageIcon(stop));
		label_5.setBounds(705, 199, 80, 72);
		panel.add(label_5);
		
		label_6 = new JLabel("");
		label_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Image reset2 = new ImageIcon(this.getClass().getResource("/reset2.png")).getImage();
				label_6.setIcon(new ImageIcon(reset2));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				Image reset = new ImageIcon(this.getClass().getResource("/reset.png")).getImage();
				label_6.setIcon(new ImageIcon(reset));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				sample = null;
				mixture = null;
				DBconnector.dbUrl = "";
				DBconnector.tableName = "";
				Image unit2 = new ImageIcon(this.getClass().getResource("/unit.png")).getImage();
				label_7.setIcon(new ImageIcon(unit2));
				Image minus = new ImageIcon(this.getClass().getResource("/minus.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
				label_1.setIcon(new ImageIcon(minus));
				label_2.setIcon(new ImageIcon(minus));
				label_3.setIcon(new ImageIcon(minus));
				Image run = new ImageIcon(this.getClass().getResource("/run.png")).getImage();
				label_4.setIcon(new ImageIcon(run));
			}
		});
		Image reset = new ImageIcon(this.getClass().getResource("/reset.png")).getImage();
		label_6.setIcon(new ImageIcon(reset));
		label_6.setBounds(797, 199, 73, 72);
		panel.add(label_6);
		
		label_7 = new JLabel("");
		Image unit = new ImageIcon(this.getClass().getResource("/unit.png")).getImage();
		label_7.setIcon(new ImageIcon(unit));
		label_7.setBounds(693, 49, 66, 59);
		panel.add(label_7);

		Image save = new ImageIcon(this.getClass().getResource("/save.png")).getImage();
		
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(255, 255, 255));
		frame.setJMenuBar(menuBar);
		
		JLabel label_10 = new JLabel("");
		label_10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(! (mixture == null)){
					try {
	
						write();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			}
		});
		menuBar.add(label_10);
		label_10.setIcon(new ImageIcon(save));
	}
	
	
	
	public void write() throws IOException {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filename), "utf-8"))) {
			writer.write(mixture.toString());	
		} catch (IOException ex) {
			ex.printStackTrace();


		}
	}
		
		
		
		
}
