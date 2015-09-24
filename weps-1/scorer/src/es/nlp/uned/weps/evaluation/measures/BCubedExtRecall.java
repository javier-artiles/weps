package es.nlp.uned.weps.evaluation.measures;

import es.nlp.uned.weps.evaluation.core.Clustering;

public class BCubedExtRecall extends Measure {

	/** The BCUBE d_ EX t_ RECALL. */
	public static final String ID = "BER";
	
	private BCubedExtPrecision precision;
	
	public BCubedExtRecall(){
		precision = new BCubedExtPrecision(); 
	}
	
	public double getScore(Clustering key, Clustering answer) {
		return precision.getScores(key, answer)[1];
	}
	
	
	public String getID(){
		return ID;
	}

}
