package EMclusteringMedicalData;

import java.awt.EventQueue;

public class NullSampleException extends Exception {
	Alert a = new Alert("Sample or GM not yet Initialized");
	
	
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
