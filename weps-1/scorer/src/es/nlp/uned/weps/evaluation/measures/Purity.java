package es.nlp.uned.weps.evaluation.measures;

import java.util.HashSet;

import es.nlp.uned.weps.evaluation.core.Clustering;

public class Purity extends Measure {
	
	/** The PURITY. */
	public static final String ID               = "P";

	/**
	 * Calculates the standard purity measure.
	 *  
	 * @param key the key clustering
	 * @param answer the answer clustering
	 * 
	 * @return the result
	 */
	public double getScore(Clustering key, Clustering answer){
		double result = 0.0;
		
		int total_docs = 0;
		
		for(HashSet<String> answer_cluster : answer.getPartition()) {
			total_docs += answer_cluster.size();
		}
		
		for(HashSet<String> answer_cluster : answer.getPartition()) {
			
			double max_pre     = maxPrecision(answer_cluster, key);
			
			double part_result =  ((double)answer_cluster.size() / (double)total_docs) * max_pre;
			
			result += part_result;
		}
		
		assert( !( result > 1.01 || result < 0.0 ));
		
		return result;
	}
	
	

	/**
	 * Calculates the maximum precision of a cluster in a particular clustering solution.
	 * 
	 * @param clustering the clustering
	 * @param cluster the cluster
	 * 
	 * @return the double
	 */
	private static double maxPrecision(HashSet<String> cluster, Clustering clustering){
		
		double max_precision = 0.0;
		
		for(HashSet<String> ref_cluster : clustering.getPartition()){
			
			double pre = precision(cluster, ref_cluster);
			
			if(pre > max_precision){
				max_precision = pre;
			}
		}
		
		return max_precision;
	}
	
	/**
	 * Calculates the precision of cluster_A in cluster_B.
	 * 
	 * Measures the overlap of elements between the two clusters
	 * and divides by the cardinality of cluster_A.
	 * 
	 * @param cluster_A the cluster_A
	 * @param cluster_B the cluster_B
	 * 
	 * @return the precision
	 */
	private static double precision(HashSet<String> cluster_A, HashSet<String> cluster_B){
		int overlap = 0;	
		
		for(String A_doc_id : cluster_A){
			if(cluster_B.contains(A_doc_id)){
				overlap ++;
			}
		}
		
		return (double)overlap / (double)cluster_A.size();
	}
	
	
	public String getID(){
		return ID;
	}
	
}
