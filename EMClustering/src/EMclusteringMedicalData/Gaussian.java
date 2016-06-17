package EMclusteringMedicalData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

public class Gaussian implements Serializable {

	/**
	 * private attributes
	 */
	private Matrix _mean; //= Matrix.Factory.emptyMatrix();	
	private CovarianceMatrix _covMatrix;
	private double _weight;

	/**
	 * private transient attributes (not to be serialized)
	 */
	transient private int _dimension; //dimension of each gaussian, assumed constant
	transient private double _minimumValue = 0; //minimum value to set random mean
	transient public double _maximumValue = 1; //maximum value to set random mean

	//transient private double _piValue; 
	transient private CovarianceMatrix _newCovMatrix ;
	transient private double _newWeight = 0.0;
	transient private Matrix _newMean;

	transient private ArrayList<Double> _piValues = new ArrayList<Double>(); 
	//probability that a sample came from this gaussian


	//Public Constructor
	public Gaussian(int dimension){
		_dimension = dimension;
		_covMatrix = new CovarianceMatrix(_dimension);
		_weight = Math.random(); 

		_newCovMatrix = new CovarianceMatrix(_dimension);
		_newCovMatrix.setSigma((DenseMatrix.Factory.ones(_dimension, _dimension)).times(2));
		_newMean = DenseMatrix.Factory.zeros(_dimension, 1);

		_newMean = DenseMatrix.Factory.zeros(_dimension, 1);
	/*	ArrayList<Double> mean = new ArrayList<Double>();
		for(int i = 0; i < _dimension ; i++){
			double max = _maximumValue - _minimumValue;
			mean.add(_minimumValue + Math.random()*max );
		}
		_mean = Matrix.Factory.linkToCollection(mean);*/
	}

	/**
	 * Public getters and setters
	 */
	public void setMean(Matrix mean){ _mean = mean; }
	public Matrix getMean(){ return _mean; }

	public void setCovMatrix(CovarianceMatrix covMatrix){ _covMatrix = covMatrix; }
	public CovarianceMatrix getCovMatrix(){ return _covMatrix; }

	public double getWeight(){ return _weight; }
	public void setWeight(double weight){ _weight = weight; }

	public int getDimension(){ return _dimension; }
	public void setDimension(int dimension){ _dimension = dimension; }

	public double getMaximumValue(){ return _maximumValue; }
	public void setMaximumValue( double maximumValue ){ _maximumValue = maximumValue; }

	public double getMinimumValue(){ return _minimumValue; }
	public void setMinimumValue( double maximumValue ){ _minimumValue = maximumValue; }

	public void setNewMean(Matrix newMean){ _newMean = newMean; }
	public Matrix getNewMean(){ return _newMean; }

	public void setNewCovMatrix(CovarianceMatrix newCovMatrix){ _newCovMatrix = newCovMatrix; }
	public CovarianceMatrix getNewCovMatrix(){ return _newCovMatrix; }

	public double getNewWeight(){ return _newWeight; }
	public void setNewWeight(double newWeight){ _newWeight = newWeight; }

	public ArrayList<Double> getPiValues(){return _piValues; }
	public void setPiValues(ArrayList<Double> piValues){_piValues = piValues;}

	public ArrayList<Double> rndMean(double minVal, double maxVal){
		ArrayList<Double> mean = new ArrayList<Double>();

		for(int i = 0; i < _dimension ; i++){
			double max = _maximumValue - _minimumValue;
			mean.add(_minimumValue + Math.random()*max );
		}
		return mean;
	}

	/**
	 * @param patient
	 * @return probablity of this gaussian 
	 */
	public double gaussianVectorProbability(PatientData patient) {

		if( this.getCovMatrix().isSingular() ){
			getCovMatrix().fixSingularity();
		}
		CovarianceMatrix sigma = getCovMatrix();
		double det = sigma.getDeterminant();
		Matrix sigmaI = sigma.getCovInverse();
		double d = getDimension();
		
		double prob = 0.0;
		double exponent = 0.0;
		double factorM = 0.0;
		
		factorM = 1.0 / ( Math.sqrt( (Math.pow( 2.0*Math.PI, d) ) * det ) );
		//System.out.println(factorM);
		Matrix x = Matrix.Factory.linkToCollection(patient.getData());	//patient vector data x
		Matrix minus = x.minus(this.getMean()); //X - Mean
		Matrix minusT = minus.transpose();
		
		Matrix minusXsigma = minusT.mtimes( sigmaI ); //(X-Mean)^t * (Sigma^-1)
		Matrix minusXsigmaXminus = minusXsigma.mtimes(minus); //(X-Mean)^t * (Sigma^-1) * (X-Mean)
		Matrix exp = minusXsigmaXminus.times(-0.5); // -1/2 *[ (X-Mean)^t * (Sigma^-1) * (X-Mean) } 
		exponent = exp.getAsDouble(0,0);
		prob =  getWeight() * factorM * Math.exp( exponent ) ;

		if(((Double) prob).isNaN() || ((Double) prob).isInfinite())
			return 0.0;

		return prob;

	}



	public double stop(){
		double max;

		max = Math.abs( this.getWeight() - this.getNewWeight());
		//System.out.println(max);
		Matrix deltaMean = this.getMean().minus(this.getNewMean());
		double maxMean = deltaMean.getMaxValue();
		//System.out.println("delta: "+ deltaMean);
		
		double maxSimMean = (deltaMean.times(-1.0)).getMaxValue();
		//System.out.println(maxSimMean);
		if(maxMean > max && maxMean > maxSimMean)
			max = maxMean;
		else if(maxSimMean > max && maxMean < maxSimMean)
			max = maxSimMean;

		Matrix deltaSigma = this.getCovMatrix().getSigma().minus(this.getNewCovMatrix().getSigma());
		double maxSigma = deltaSigma.getMaxValue();
		//System.out.println(maxSigma);
		double maxSimSigma = (deltaSigma.times(-1.0)).getMaxValue();
		//System.out.println(maxSimSigma);
		if(maxSigma > max && maxSigma > maxSimSigma)
			max = maxSigma;
		else if(maxSimSigma > max && maxSigma < maxSimSigma)
			max = maxSimSigma;


		return max;
	}


	/*public double piValueNumerator(PatientData patient){
		return this.getWeight() * gaussianJVectorProbability(patient);
	}*/


	public double piValueJK( PatientData patient, double piValueDenominator ){
		return gaussianVectorProbability(patient) / piValueDenominator;
	}
	
	
	public double updateWeight(Sample sample){	
		double d = sample.length();
		double weight = 0.0;
		//Iterator<PatientData> it = sample.iterator();
		
		//while(it.hasNext()){
		//	PatientData data = it.next();
		for(double pi: getPiValues())	
			weight += pi;
		//}
		
		return weight/d;
	}
	
	
	
	public Matrix updateMean(Sample sample){
		int d = sample.dimension();
		Matrix newMean = DenseMatrix.Factory.zeros(d, 1);
		Matrix x = Matrix.Factory.emptyMatrix(); //PatientData vector
		double denominator = 0.0;
		double pi = 0.0;
		int i = 0;
		
		Iterator<PatientData> it = sample.iterator();
		while(it.hasNext()){
			PatientData data = it.next();
			x = Matrix.Factory.linkToCollection(data.getData());
			pi = getPiValues().get(i);
			Matrix aux = x.times(pi);
			
			denominator += pi;
			newMean = aux.plus(newMean);
			
			i++;
		}
		
		return newMean.times(1.0 / denominator);
	}
		

	public Matrix updateSigma(Sample sample){
		int d = sample.dimension();
		Matrix newSigma = DenseMatrix.Factory.zeros(d, d);
		Matrix x = Matrix.Factory.emptyMatrix(); //PatientData vector
		double denominator = 0.0;
		double pi = 0.0;
		int i = 0;
		
		Iterator<PatientData> it = sample.iterator();
		while(it.hasNext()){
			PatientData data = it.next();
			x = Matrix.Factory.linkToCollection(data.getData());
			pi = getPiValues().get(i);
			Matrix diference = x.minus( getNewMean());
			Matrix diferenceT = x.transpose();
			Matrix aux = diference.mtimes( diferenceT );
			Matrix auxPi = aux.times(pi);
			
			denominator += pi;
			newSigma = newSigma.plus(auxPi);
			
			i++;
		}
		
		
		return newSigma.times( 1.0 / denominator );
	}
	
	
		
	@Override
	public String toString() {
		return "Weight: " + getWeight() + "\nMean:\n" + getMean() + getCovMatrix();
	}

	

}