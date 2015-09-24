package es.nlp.uned.weps.evaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import es.nlp.uned.utilities.MathHelper;
import es.nlp.uned.weps.evaluation.core.Clustering;


/**
 * This class holds the results for different clustering evaluation measures using a key and an answer clusterings.
 * 
 * @author javart
 */
public class ClusteringEvaluation {

	private HashMap<String, Double> measure2result;

	private Clustering key, answer;
	
	/**
	 * The Constructor.
	 * 
	 * @param key the key
	 * @param answer the answer
	 */
	public ClusteringEvaluation(Clustering key, Clustering answer){
		this.key = key;
		this.answer = answer;
		measure2result = new HashMap<String, Double>();
	}
	
	public ClusteringEvaluation(){
		measure2result = new HashMap<String, Double>();
	}

	/**
	 * Gets the answer.
	 * 
	 * @return the answer
	 */
	public Clustering getAnswer() {
		return answer;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public Clustering getKey() {
		return key;
	}



	/**
	 * Gets the measure2result.
	 * 
	 * @return the measure2result
	 */
	public HashMap<String, Double> getMeasure2result() {
		return measure2result;
	}



	/**
	 * Gets the measures.
	 * 
	 * @return a list of metric IDs evaluated for this clustering problem
	 */
	public String[] getMeasures(){
		Set<String> scores_set = measure2result.keySet();
		
		String[] scores_list = new String[scores_set.size()];
		
		scores_set.toArray(scores_list);
		
		Arrays.sort(scores_list);
		
		return scores_list;
	}
	
	/**
	 * Sets the score.
	 * 
	 * @param measure_id the measure_id
	 * @param value the value
	 */
	public void setScore(String measure_id, double value){
		measure2result.put(measure_id, value);
	}
	
	/**
	 * Gets the score.
	 * 
	 * @param measure_id the measure_id
	 * 
	 * @return the score
	 */
	public double getScore(String measure_id){
		return measure2result.get(measure_id);
	}
	
	/**
	 * Gets the score.
	 * 
	 * @param measure_id the measure_id
	 * @param round_precision the round_precision
	 * 
	 * @return the score
	 */
	public double getScore(String measure_id, int round_precision){
		return MathHelper.round(measure2result.get(measure_id), round_precision);
	}
		
}
