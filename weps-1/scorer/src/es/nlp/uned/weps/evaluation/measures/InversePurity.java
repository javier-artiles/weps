package es.nlp.uned.weps.evaluation.measures;

import es.nlp.uned.weps.evaluation.core.Clustering;

public class InversePurity extends Measure {

	/** The INVERS e_ PURITY. */
	public static final String ID      = "IP";
	
	private Purity purity;
	
	public InversePurity(){
		this.purity = new Purity();
	}
	
	/**
	 * Inverse purity.
	 * 
	 * @param key the key
	 * @param answer the answer
	 * 
	 * @return the double
	 */
	public double getScore(Clustering key, Clustering answer){
		return purity.getScore(answer, key);
	}
	
	public String getID(){
		return ID;
	}

}
