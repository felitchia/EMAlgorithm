
package EMclusteringMedicalData;



import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class Sample implements Iterable<PatientData>{
	
	/**
	 * private attribute sample
	 */
	private LinkedList<PatientData> _sample;
	
	/**
	 * Constructor
	 * @param sample
	 */
	public Sample(){}	
	
	public Sample(LinkedList<PatientData> sample){ 
		_sample = sample;	
	}
	
	/**
	 * public getters and setters
	 */
	public LinkedList<PatientData> getSample(){ return _sample; }
	
	public void setSample(LinkedList<PatientData> sample){ _sample = sample;}
	
	
	/**
	 * Add a patient data data to the sample
	 * @param vector
	 */
	public void add(PatientData vector){
		getSample().add(vector);
	}
	
	/**
	 * @return the length of the sample
	 */
	public int length(){ 
		return getSample().size();
	}
	
	/**
	 * @param index
	 * @return patient data at the position index
	 */
	public PatientData element(int index){
		return getSample().get(index);
	}
	
	/**
	 * 
	 * @param sample
	 * @return this sample concatenated to the @param
	 */
	public LinkedList<PatientData> join( LinkedList<PatientData> sample){
		getSample().addAll(sample);
		return getSample();
	}

	/**
	 * @return minimum value among all the data points for all the PatientData
	 */
	public double maximumValue(){
		ListIterator<PatientData> it = getSample().listIterator();
		double maximum = 0;
		while(it.hasNext()){
			PatientData patient = it.next();
			maximum =  patient.getData().get(0);
			for(Double value: patient){
				if (value > maximum)
					maximum = value;
			}
		}
		return maximum;
	}
	
	
	/**
	 * @return maximum value among all the data points for all the PatientData
	 */
	public double minimumValue(){
		ListIterator<PatientData> it = getSample().listIterator();
		double minimum = 0;
		while(it.hasNext()){
			PatientData patient = it.next();
			minimum =  patient.getData().get(0);
			for(Double value: patient){
				if (value < minimum )
					minimum = value;
			}
		}
		return minimum;
	}
	
	
	
	/**
	 * 
	 * @return the dimension of each PatientData vector, assuming all have the same dimension.
	 */
	public int dimension(){
		if( this.length() != 0)
			return getSample().get(0).getData().size();
		
		return 0;
	}
	
	
	public LinkedList<PatientData> getData(){return null;}

	@Override
	public Iterator<PatientData> iterator() {
		return getSample().listIterator();
	}
	
	@Override
	public String toString(){
		String text = "";
		for(PatientData data: getSample())
			text += data.toString()+"\n";
		return text;
	}
}