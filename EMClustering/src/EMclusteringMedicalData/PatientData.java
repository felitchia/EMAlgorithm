package EMclusteringMedicalData;

import java.util.Iterator;
import java.util.LinkedList;

public class PatientData implements Iterable<Double>{
	/**
	 * private attribute representing the patient's sample values
	 */
	private LinkedList<Double> _data = new LinkedList<Double>();

	/**
	 * Constructors
	 */
	public PatientData(){}
	
	public PatientData(LinkedList<Double> data){_data = data; }
	
	/**
	 * _data Setter
	 * @param data
	 */
	public void setData(LinkedList<Double> data){_data = data;}
	/**
	 * _data Getter
	 * @return _data
	 */
	public LinkedList<Double> getData(){return _data;}

	
	
	/**
	 * @return iterator through the patient's values
	 */
	@Override
	public Iterator<Double> iterator() {
		return getData().listIterator();
	}

	/**
	 * 
	 * @return dimension of the data
	 */
	public int getDim(){ return getData().size(); }
	
	/**
	 * @return String of the vector
	 */
	@Override
	public String toString() {
		return "" + getData();
	}
	
}