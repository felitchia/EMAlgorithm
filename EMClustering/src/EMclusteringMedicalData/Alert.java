package EMclusteringMedicalData;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;

public class Alert {

	private JFrame frame;
	private final JLabel lblError = new JLabel("ERROR");
	private String _warning;

	
	public void setWarning(String warning){ _warning = warning; }
	public String getWarning(){ return _warning; }
	


	/**
	 * Create the application.
	 */
	public Alert() {
		initialize();
	}
	
	public JFrame getFrame(){ return frame;} 
	
	public Alert(String warning) {
		_warning = warning;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(240, 248, 255));
		frame.setBounds(700, 150, 350, 200);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSomethingIsWrong = new JLabel("Something is wrong!");
		lblSomethingIsWrong.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblSomethingIsWrong.setBounds(128, 42, 168, 50);
		
		frame.getContentPane().add(lblSomethingIsWrong);
		lblError.setBounds(48, -26, 126, 36);
		frame.getContentPane().add(lblError);
		
		JLabel lblMakeSureYou = new JLabel(_warning);
		lblMakeSureYou.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMakeSureYou.setBounds(28, 124, 268, 16);
		frame.getContentPane().add(lblMakeSureYou);
		
		JLabel label = new JLabel("");
		Image warning = new ImageIcon(this.getClass().getResource("/warning.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
		label.setIcon(new ImageIcon(warning));
		label.setBounds(12, 13, 116, 98);
		frame.getContentPane().add(label);
		frame.setTitle("Error!");
	}
}
