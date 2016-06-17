package EMclusteringMedicalData;

import java.io.Serializable;

import org.ujmp.core.*;

public class CovarianceMatrix implements Serializable{
	
	/**
	 * private attributes
	 * _sigma: covariance matrix
	 * _dimension: dimension of the square covariance matrix (_dimension c _dimension )
	 */
	private Matrix _sigma;
	private int _dimension;
	
	/**
	 * Constructor 1: Initializes the covariance matrix as an Identity Matrix dimension x dmension
	 * @param dimension
	 * 
	 */
	public CovarianceMatrix(int dimension){
		_dimension = dimension;
		_sigma  =  (DenseMatrix.Factory.eye(_dimension, _dimension));
	}

 
	/**
	 * Constructor2: Initializes covariance matrix with a dimension and a matrix
	 * @param dimension
	 * @param sigma
	 */
	public CovarianceMatrix(int dimension, Matrix sigma){
		_dimension = dimension;
		_sigma = sigma;
	}
	
	/**
	 * Public getters and setters
	 */
	public Matrix getSigma(){return _sigma; }
	public void setSigma(Matrix sigma){ _sigma = sigma; }
	public int getDimention(){return _dimension;}
	public void setDimention(int dimention){ _dimension = dimention; }
	//public boolean getSingular(){ return _isSingular; }
	//public void setSingular(boolean isSingular){_isSingular = isSingular; }
	
	/**
	 * @return sigma as an identity matrix
	 */
	public Matrix setCovToIdentity(){
		setSigma(DenseMatrix.Factory.eye(_dimension, _dimension));
		return getSigma();
	}

	/**
	 * 
	 * @return inverse
	 */
	public Matrix getCovInverse(){
		if( ! this.isSingular() )
			return getSigma().inv();
		return null;
	}
	
	/**
	 * 
	 * @return determinant
	 */
	public double getDeterminant(){
		return getSigma().det();
	}
	
	/**
	 * 
	 * @return true if the covariance matrix is singular
	 */
	public boolean isSingular(){
		return this.getSigma().isSingular();
	}

	
	
	/**
	 * fixes covariance matrix's singularity by adding a diagonal 
	 * matrix with a small constant value avoiding zeroes on the 
	 * covariance matrix's diagonal
	 * 
	 */
	public void fixSingularity(){
		Matrix nonSing = this.getSigma().plus( DenseMatrix.Factory.eye(_dimension, _dimension).times(0.001*Math.random()) );
		this.setSigma( nonSing );
	}
	

	@Override
	public String toString() {
		return "Sigma:\n" + getSigma();
	}
	
}