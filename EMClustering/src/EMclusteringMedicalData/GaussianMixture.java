package EMclusteringMedicalData;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.ujmp.core.Matrix;


public class GaussianMixture implements Iterable<Gaussian>, Serializable{
	
	/**
	 * private attributes
	 */
	private int _size; //number of gaussians
	private int _dimension;
	private ArrayList<Gaussian> _thetaParameters = new ArrayList<Gaussian>();
	private ArrayList<Double> _piValues = new ArrayList<Double>();
	private Double _piValDenominator;
	
	/**
	 * Constructors
	 */
	public GaussianMixture(){}
	
	
	public GaussianMixture(int size, ArrayList<Gaussian> thetaParameters){
		_size = size;
		_thetaParameters = thetaParameters;
		this.normalizeWeights();
		_piValDenominator = null;
	}
	
	public GaussianMixture(int size, int dimension){	
		_size = size;
		_dimension = dimension;
		
		for(int i = 0; i < _size ; i++){
			_thetaParameters.add(new Gaussian(_dimension));
		}
		
		this.normalizeWeights();
		_piValDenominator = null;
		
	}
	
	
	/**
	 * public getters and setters
	 */
	public ArrayList<Gaussian>  getTheta() { return _thetaParameters;	}
	
	public void setTheta(ArrayList<Gaussian> thetaParameters) { _thetaParameters = thetaParameters;	}

	public int getSize() {	return _size;	}
	
	public void setSize(int size) {	_size = size;	}
	
	public int getDimention(){ return _dimension; }
	
	public void setDimention(int dimension){ _dimension = dimension; }
	
	public double getPiValDenominator(){ return _piValDenominator; }
	
	public void setPiValueDenominator(double piValDenominator){ _piValDenominator= piValDenominator;	}
	
	public ArrayList<Double> getPiValues(){return _piValues; }
	
	public void setPiValues(ArrayList<Double> piValues){ _piValues = piValues; }
	

	
	
	
	
	/**
	 * public methods
	 */
	public void mix(double minimumValue, double maximumValue){
		/*for(int i = 0; i < _size ; i++){
			getTheta().add(new Gaussian(getDimention(), minimumValue, maximumValue));
		}*/
	for(Gaussian g: getTheta()){
		ArrayList<Double> mean = new ArrayList<Double>();
		for(int i = 0; i < getDimention() ; i++){
			double max = maximumValue - minimumValue;
			mean.add(minimumValue + Math.random()*max );
		}
		g.setMean( Matrix.Factory.linkToCollection(mean) );
	}
	}
	
	
	
	/**
	 * 
	 * @param patient
	 * @return probability of the mixture for the given PatientData
	 */
	public double mixtureVectorProbability(PatientData data){
		double prob = 0.0;
		int i = 0;
		for(Gaussian g: getTheta()){
			i++;
			prob += g.gaussianVectorProbability(data);
			//System.out.println("Gaussiana " + i +": " + g.gaussianVectorProbability(data));
			
		}
		
		return prob;
	}
	

	
	/**
	 * returns the denominator of the pi values, wich is equal for all the gaussians for a given PatientData
	 * @param patient
	 * @return
	 */
	public double piValueDenominator(PatientData patient) {
		double denominator = 0.0;
		for( Gaussian g: getTheta())
			denominator += g.gaussianVectorProbability(patient);
		//System.out.println(denominator);
		
		return denominator;	
	}
	
	/**
	 * Returns parobability of the gaussian j for the given PatientData
	 * @param j
	 * @param patient
	 * @return
	 */
	public double gaussianJVectorProbability(int j, PatientData patient){
		
		return getTheta().get(j).gaussianVectorProbability(patient);
	}

	/**
	 * Normalizes the weights of the mixture ( sum(weights) = 1)
	 */
	public void normalizeWeights(){
		double weightSum = 0.0;
		for(Gaussian g: getTheta()){
			weightSum += g.getWeight();	
		}
		
		for(Gaussian g: getTheta()){
			double weight = g.getWeight()/weightSum ;
			g.setWeight( weight );	
		}
	}
	
	public void normalizeNewWeights(){
		double weightSum = 0.0;
		for(Gaussian g: getTheta()){
			weightSum += g.getNewWeight();	
		}
		
		for(Gaussian g: getTheta()){
			double weight = g.getNewWeight()/weightSum ;
			g.setNewWeight( weight );	
		}
	}


	/**
	 * Assignes pi values for each gaussian in the GM
	 * piValue is the probability that a data set came from a given gaussian
	 */
	public void assignPiValues(Sample sample){
		for(Gaussian g: getTheta()){
	
			ArrayList<Double> piValues = new ArrayList<Double>();
			
			Iterator<PatientData> it = sample.iterator();
			while(it.hasNext()){
				PatientData data = it.next();
				double pi = g.gaussianVectorProbability(data) / mixtureVectorProbability(data);
				piValues.add(pi);	
			}
			
			g.setPiValues(piValues);
		}	
	}
	
	public double stop(){
		double max ; //= 0;
		max = getTheta().get(0).stop();
		for(Gaussian g: getTheta()){
			if( Math.abs( g.stop() ) > max)
				max = g.stop();
		}

		return max;
	}
	
/*	public void setValues(PatientData patient){
		ArrayList<Double> piValues = new ArrayList<Double>();
		for( Gaussian g: getTheta()){
			piValues.add( g.gaussianVectorProbability(patient)/this.piValueDenominator(patient) );	
			
			g.setPiValues(piValues);
		}
	}*/
	
	
	@Override
	public String toString() {
		String text = "\r\n";
		int i = 0;
		for(Gaussian g: getTheta()){
			i++;
			text+="Gaussian " +i+ ": \r\n"+ g.toString()+"\r\n";
		}
		return text;
	}

	@Override
	public Iterator<Gaussian> iterator() {
		return getTheta().listIterator();
	}
	
}

