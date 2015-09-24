package es.nlp.uned.weps.evaluation.measures;

import java.util.HashSet;

import es.nlp.uned.weps.evaluation.core.Clustering;


public class Pairs extends Measure {

	/** The Constant RAND_STATISTIC. */
	public static final int RAND_STATISTIC = 0;
	
	/** The Constant JACCARD_COEFFICIENT. */
	public static final int JACCARD_COEFFICIENT = 1;
	
	/** The Constant FOLKES_AND_MALLOWS. */
	public static final int FOLKES_AND_MALLOWS = 2;
	
	public String ID; 
	
	
	private int metricType;
	
	public Pairs(int metricType){

		assert(metricType >= 0 && metricType <= 2);
		
		this.metricType = metricType;
		
		switch (metricType) {
			case RAND_STATISTIC:
				ID = "PR";
				break;
				
			case JACCARD_COEFFICIENT:
				ID = "PJ";
				break;
				
			case FOLKES_AND_MALLOWS:
				ID = "PM";
				break;
		}
		
	}
	
	

	/**
	 * Calculates a pairs based measure.
	 * 
	 * @param key the key clustering
	 * @param metricType the type of pair based metric (RAND_STATISTIC, JACCARD_COEFFICIENT or FOLKES_AND_MALLOWS).
	 * @param answer the answer clustering
	 * 
	 * @return the result
	 */
	public double getScore(Clustering answer, Clustering key){
		
		double result = 0.0;
		
		int total_elements = answer.getAssigned().size();
		
		double total_pairs = (Math.pow(total_elements, 2) - total_elements);

		int total_ss_pairs = 0; //Pairs that belong to the "same class" and are in the "same cluster"
		int total_dd_pairs = 0; //Pairs that belong to the "different class" and are in the "different cluster"
		int total_sd_pairs = 0;
		int total_ds_pairs = 0;
		
		//Extract all pairs
		HashSet<String[]> pairs = getPairs(answer);
		
		/*
		System.out.println("Obtained pairs:  "+pairs.size());
		System.out.println("Predicted pairs: "+total_pairs);
		printPairs(pairs);
		*/
		
		assert(pairs.size() == total_pairs);
		
		for(String[] pair : pairs){
			
			if(inSameCluster(pair, key)){
				
				if(inSameCluster(pair, answer)) {
					total_ss_pairs ++;
					
				} else {
					total_sd_pairs ++;
				}
				
			} else {
				
				if(inSameCluster(pair, answer)) {
					total_ds_pairs ++;
					
				} else {
					total_dd_pairs ++;
				}
			}
		}
		
		assert(total_pairs == total_ss_pairs + total_dd_pairs + total_sd_pairs + total_ds_pairs);
		
		switch (metricType) {
		case RAND_STATISTIC:
			result = (double)(total_ss_pairs + total_dd_pairs) / (double)total_pairs;
			break;

		case JACCARD_COEFFICIENT:
			if(total_ss_pairs + total_sd_pairs + total_ds_pairs == 0) {
				result = 1.0;
			} else {
				result = (double)total_ss_pairs / (double)(total_ss_pairs + total_sd_pairs + total_ds_pairs);
			}
			break;

		case FOLKES_AND_MALLOWS:
			
			if(total_ss_pairs + total_sd_pairs == 0  || total_ss_pairs + total_ds_pairs ==0) {
				result = 1.0;
				
			} else {
				double res1 = (double)total_ss_pairs / (double)(total_ss_pairs + total_sd_pairs);
				double res2 = (double)total_ss_pairs / (double)(total_ss_pairs + total_ds_pairs);
				result = Math.sqrt(res1 * res2);
			}
			
			break;
		}
		
		return result;
	}

	
	

	/**
	 * Returns a set with the pairs of elements in a partition (does not include the pair of an element with itself).
	 * 
	 * @param clustering the clustering
	 * 
	 * @return the set of pairs in vectors <element_a, partition_a, element_b, parition_b>
	 */
	private static HashSet<String[]> getPairs(Clustering clustering){
		HashSet<String[]> result = new HashSet<String[]>();
		
		for(String elem_a : clustering.getAssigned()){
			for(String elem_b : clustering.getAssigned()){
				
				String[] pair = new String[2];
				pair[0] = elem_a;
				pair[1] = elem_b;
				
				String[] inv_pair = new String[2];
				pair[0] = elem_b;
				pair[1] = elem_a;
				
				if(!(elem_a.equals(elem_b) || result.contains(pair) || result.contains(inv_pair))) {
					result.add(pair);
				}
			}
		}
		
		return result;
	}
	
	
	public String getID(){
		return ID;
	}
	
	/**
	 * Prints the pairs of elements.
	 * 
	 * @param pairs the pairs
	 */
	/*
	private static void printPairs(HashSet<String[]> pairs){
		for(String[] pair : pairs){
			if(pair[0] == pair[1]) System.out.println(pair[0]+" : "+pair[1]);
		}
	}*/
}
