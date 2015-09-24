package es.nlp.uned.weps.evaluation.measures;

import es.nlp.uned.weps.evaluation.core.Clustering;

public class FMeasure extends Measure {
	
	private Measure precMeasure, recMeasure;
	
	private double alpha;
	
	private String ID;
	
	public FMeasure(Measure precMeasure, Measure recMeasure, double alpha){
		this.precMeasure = precMeasure;
		this.recMeasure = recMeasure;
		this.alpha = alpha;
		
		ID = "FMeasure_"+alpha+"_"+precMeasure.getID()+"-"+recMeasure.getID();
		
	}

	
	public String getID(){
		return ID;
	}
	
	/**
	 * Calculates the F measure as follows:<br>
	 * <pre>F-Measure = 1 / (alpha*1/purity + (1-alpha)*1/inv_purity)</pre>
	 * where alpha range is in the range of [0.0, 1.0]<br>
	 * 
	 * @return the F-measure
	 */
	public double getScore(Clustering key, Clustering answer){
		
		double precisionScore = precMeasure.getScore(key, answer);
		double recallScore    = recMeasure.getScore(key, answer);
		
		return getScore(precisionScore, recallScore, alpha);
	}
	
	
	/**
	 * Calculates the F measure as follows:<br>
	 * <pre>F-Measure = 1 / (alpha*1/purity + (1-alpha)*1/inv_purity)</pre>
	 * where alpha range is in the range of [0.0, 1.0]<br>
	 * 
	 * @param R the recall measure
	 * @param P the precision measure
	 * @param alpha the alpha
	 * 
	 * @return the F-measure
	 */
	public static double getScore(double P, double R, double alpha){
		/*
		System.out.println(alpha);
		System.out.println(P);
		System.out.println(R);
		*/
		double fmeasure = 1 / (alpha * 1/P + (1-alpha) * 1/R);
		
		//System.out.println(fmeasure+"\n");
		
		return fmeasure;
	}
	
}
