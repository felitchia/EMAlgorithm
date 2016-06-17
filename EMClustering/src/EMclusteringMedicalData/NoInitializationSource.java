package EMclusteringMedicalData;

import java.awt.EventQueue;

public class NoInitializationSource extends Exception {
	Alert a = new Alert("Please enter all initialization parameters.");
	
	
	public void error(){
		EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				a.getFrame().setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	}

}
