package EMclusteringMedicalData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

public class EMsample extends Sample{
	

	private final static double EPSILON = 0.0000001; 
	
	/**
	 * private attributes
	 */
	private GaussianMixture _mixture;
	private boolean _running = true;
	public EMsample(){}
	
	public EMsample(LinkedList<PatientData> sample) {
		super(sample);
		//_piValues = piValues();
	}
	
	public EMsample(LinkedList<PatientData> sample, GaussianMixture mixture) {
		super(sample);
		_mixture = mixture;
		//_piValues = piValues();
	}
	
	//public Sample getSample(){ return _sample; }
	
	//public void setSample(Sample sample){ _sample = sample;}
	
	public GaussianMixture getMixture(){ return _mixture; }
	
	public void setMixture( GaussianMixture mixture){ _mixture = mixture; }
	
	public boolean isRunning(){return _running; }
	public void setRunning(boolean running) { _running = running; }
	
	//public void setPiValues(ArrayList<Double> piValues){ _piValues = piValues; }
	
	

	
	
	/**
	 * uptades weights in the GM acording to the EM algorithm
	 */
	public void updateWeights(){
		for(Gaussian g: getMixture()){
			
			double newWeight = g.updateWeight( this );
			g.setNewWeight(newWeight);
			
		}
		getMixture().normalizeNewWeights();
	}
	
	
	/**
	 * uptades means in the GM acording to the EM algorithm
	 */
	public void updateMeans(){
		for(Gaussian g: getMixture()){
			
			Matrix newMean = g.updateMean( this );
			g.setNewMean(newMean);
			
		}
	}
	
	/**
	 * uptades covariance matrices in the GM acording to the EM algorithm
	 */
	public void updateSigmas(){
		for(Gaussian g: getMixture()){
		
			Matrix newSigma = g.updateSigma( this );
			g.getNewCovMatrix().setSigma(newSigma);
			
		}
	}
	


	
	/**
	 * Performs the M-Step of the algorithm, that is, it updates the weights, means and covariance matrices of the GM.
	 */
	public double mStep(){
		updateWeights();
		updateMeans();
		updateSigmas();
		
		double max = this.stop();
		Iterator<Gaussian> it = getMixture().iterator();
		while(it.hasNext()){
		//for(Gaussian g: getMixture()){
			Gaussian g;
			g = it.next();
			g.setWeight(g.getNewWeight());
			if( g.getWeight() > 0.00000001 ){
				g.setMean(g.getNewMean());
				g.setCovMatrix(g.getNewCovMatrix());
				getMixture().setSize( getMixture().getSize() -1);
			}
			else
			it.remove();
			
		}
		getMixture().normalizeWeights();
		getMixture().assignPiValues(this);
		
		return max;
	}
	
	
	public double eStep(){
		double logLikelihood = 0.0;
		for(PatientData data: getSample()){
			logLikelihood += Math.log( getMixture().mixtureVectorProbability(data));
		}
		//System.out.println(logLikelihood);
		return logLikelihood;
	}
	
	

	public double stop(){
		double max = 0;
		double max2 = 0;
		for(Gaussian g: getMixture()){
			if(max > max2)
				max2 = max;
			
			max = Math.abs( g.getWeight() - g.getNewWeight());
			//System.out.println(max);
			Matrix deltaMean = g.getMean().minus(g.getNewMean());
			double maxMean = deltaMean.getMaxValue();

			double maxSimMean = (deltaMean.times(-1.0)).getMaxValue();
			//System.out.println(maxSimMean);
			if(maxMean > max && maxMean > maxSimMean)
				max = maxMean;
			else if(maxSimMean > max && maxMean < maxSimMean)
				max = maxSimMean;

			Matrix deltaSigma = g.getCovMatrix().getSigma().minus(g.getNewCovMatrix().getSigma());
			double maxSigma = deltaSigma.getMaxValue();
			//System.out.println(maxSigma);
			double maxSimSigma = (deltaSigma.times(-1.0)).getMaxValue();
			//System.out.println(maxSimSigma);
			if(maxSigma > max && maxSigma > maxSimSigma)
				max = maxSigma;
			else if(maxSimSigma > max && maxSigma < maxSimSigma)
				max = maxSimSigma;
		}
		
		return max2;
	}
	
	
	
	
	
	public int computeIterations(){
		System.out.println(getMixture());
		double log1 = eStep();
		double max = 0;
		System.out.println(log1);
		max = mStep();
		//System.out.println(getMixture());
		double log2 = eStep();
		//System.out.println(log2);
		max = mStep();
		//System.out.println(getMixture());
		double log3 = eStep();
		//System.out.println(log3);
		int i = 2;
	//	System.out.println("Stop: " + getMixture().stop());
		while( isRunning() && !(Math.abs(log2 - log1) < EPSILON )  &&  !(Math.abs(log3 - log2 ) < EPSILON ) && !((Double) log3).isInfinite()){ 
			// { 
			//&& ! (max < EPSILON )
			//System.out.println("Number of Iterations: " +i);
			//System.out.println("Stop: "+ max);
			log1 = log2;
			log2 = log3;
			max = mStep();
			log3 = eStep();
			//System.out.println("Likelihood:" + ( ( log2 ) ));
			//System.out.println(getMixture());
			i++;
		}
		System.out.println("Number of Iterations: " +i);
		//System.out.println(this);
		return i;
		
	}
}

