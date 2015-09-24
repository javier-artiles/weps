package es.nlp.uned.weps.evaluation;

import java.util.ArrayList;
import java.util.HashSet;

import es.nlp.uned.weps.evaluation.core.Clustering;
import es.nlp.uned.weps.evaluation.measures.Measure;

/**
 * The Class SimpleScorer evaluates a single clustering solution.
 */
public class SimpleScorer {

	/** The answer. */
	private Clustering key, answer;
	
	/** The Constant ALL_IN_ONE_BASELINE. */
	public static final String ALL_IN_ONE_BASELINE = "ALL_IN_ONE_BASELINE";
	
	/** The Constant ONE_IN_ONE_BASELINE. */
	public static final String ONE_IN_ONE_BASELINE = "ONE_IN_ONE_BASELINE";
	
	/** The Constant COMBINED_BASELINE. */
	public static final String COMBINED_BASELINE = "COMBINED_BASELINE";
	
	/** The answer_is_baseline. */
	private boolean answer_is_baseline;
	
	
	/**
	 * This Constructor creates one of the baseline systems for scoring.
	 * 
	 * @param key_path the key clustering file (WePS 2007 format)
	 * @param baselineType  type of baseline (SystemScorer.ALL_IN_ONE_BASELINE, SystemScorer.ONE_IN_ONE_BASELINE or SystemScorer.COMBINED_BASELINE).
	 */
	public SimpleScorer(String baselineType, String key_path) {

		this.answer_is_baseline = true;
		
		//Parse clustering files
		key = new Clustering(key_path);
		
		HashSet<String> description_docs = key.getAssigned();
		
		if(baselineType.equals(ALL_IN_ONE_BASELINE)) {
			answer = getAllInOneClustering(description_docs);
			
		} else if(baselineType.equals(ONE_IN_ONE_BASELINE)) {
				answer = getOneInOneClustering(description_docs);
				
		} else if(baselineType.equals(COMBINED_BASELINE)){
			answer = getCombinedClustering(description_docs);
			
		} 
		
		//Error check
		
		Logging.getInstance().log("\nChecking errors in key...");
		errorCheck(key, description_docs);
		
		Logging.getInstance().log("\nChecking errors in answer...");
		errorCheck(answer, description_docs);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param key_path the key clustering file (WePS 2007 format)
	 * @param team_id the team_id
	 * @param answer_path the answer clustering file (WePS 2007 format)
	 */
	public SimpleScorer(String team_id, String key_path, String answer_path) {
		
		this.answer_is_baseline = false;
				
		//Parse clustering files
		key = new Clustering(key_path);
		answer = new Clustering(answer_path);
		
		HashSet<String> description_docs = key.getAssigned();
		
		//Error check
		
		Logging.getInstance().log("\nChecking errors in key...");
		errorCheck(key, description_docs);
		
		Logging.getInstance().log("Checking errors in answer...");
		errorCheck(answer, description_docs);
		
		removeDiscardedDocs();
	}

	/**
	 * Checks if is answer baseline.
	 * 
	 * @return true, if is answer baseline
	 */
	public boolean isAnswerBaseline(){
		 return answer_is_baseline;
	}
		
	/**
	 * Generates an all in one clustering solution.
	 * 
	 * @param description_docs the description_docs
	 * 
	 * @return the all in one clustering
	 */
	private Clustering getAllInOneClustering(HashSet<String> description_docs){
		ArrayList<HashSet<String>> partition = new ArrayList<HashSet<String>>();		
		
		partition.add(description_docs);
		
		Clustering clustering = new Clustering(partition);
		
		return clustering;
	}
	
	/**
	 * Generates an one in one clustering solution.
	 * 
	 * @param description_docs the description_docs
	 * 
	 * @return the one in one clustering
	 */
	private Clustering getOneInOneClustering(HashSet<String> description_docs){
		ArrayList<HashSet<String>> partition = new ArrayList<HashSet<String>>();
		
		for(String doc : description_docs){
			HashSet<String> singleton_cluster = new HashSet<String>(); 
			
			singleton_cluster.add(doc);
			
			partition.add(singleton_cluster);
		}
		
		Clustering clustering = new Clustering(partition);
		
		return clustering;
	}

	/**
	 * Generates an combined clustering solution.
	 * 
	 * @param description_docs the description_docs
	 * 
	 * @return the combined clustering
	 */
	private Clustering getCombinedClustering(HashSet<String> description_docs){
		ArrayList<HashSet<String>> partition = new ArrayList<HashSet<String>>();
		
		partition.add(description_docs);
		
		for(String doc : description_docs){
			HashSet<String> singleton_cluster = new HashSet<String>(); 
			
			singleton_cluster.add(doc);
			
			partition.add(singleton_cluster);
		}
		
		Clustering clustering = new Clustering(partition);
		
		return clustering;
	}

	/**
	 * Checks and tries to fix errors in a clustering.
	 * - Discards unknown (not in the key) elements in the clustering.
	 * - Elements in the key but not in the clustering are considered as discarded.
	 * - If an element appears in the clustering as both assigned and discarded it will be considered as assigned.
	 * 
	 * @param clustering the clustering
	 * @param description_docs the description_docs
	 */
	private void errorCheck(Clustering clustering, HashSet<String> description_docs){
		
		HashSet<String> assigned = clustering.getAssigned();
		HashSet<String> discarded = clustering.getDiscarded();
		
		//All and only description_docs are should appear in the discarded and assigned lists
				
		for(String doc : description_docs){
			if(!(assigned.contains(doc) || discarded.contains(doc))){
				Logging.getInstance().log("\tDoc ("+doc+") is not in the clustering file, will be considered as discarded.");
				discarded.add(doc);
			}
		}
		
		HashSet<String> remove_from_assigned = new HashSet<String>();
		for(String doc : assigned){
			if(!description_docs.contains(doc)){
				Logging.getInstance().log("\tDoc ("+doc+") is not in the key file, will be removed.");
				remove_from_assigned.add(doc);
			}
		}
		for(String doc : remove_from_assigned){
			assigned.remove(doc);
		}
		
		HashSet<String> remove_from_discarded = new HashSet<String>();
		for(String doc : discarded){
			if(!description_docs.contains(doc)){
				Logging.getInstance().log("\tDoc ("+doc+") is not in the key file, will be removed.");
				remove_from_discarded.add(doc);
			}
		}
				
		//A document can't appear both in assigned and discarded lists.
		for(String doc : discarded){
			if(assigned.contains(doc)){
				Logging.getInstance().log("\tDiscarded doc ("+doc+") is also assigned to an entity, we will remove it from the discarded list");
				remove_from_discarded.add(doc);
			}
		}
		
		for(String doc : remove_from_discarded){
			discarded.remove(doc);
		}
	}

	
	/**
	 * Removes from the answer documents that have been discarded in the human annotation
	 * (usually because the human was not able to annotate that particular document).
	 */
	public void removeDiscardedDocs(){
		for(String doc_id : key.getDiscarded()){
			for(HashSet<String> cluster : answer.getPartition()){
				if(cluster.contains(doc_id)) cluster.remove(doc_id);
			}
		}
		//Remove null clusters
		ArrayList<HashSet<String>> null_clusters = new ArrayList<HashSet<String>>();
		for(HashSet<String> cluster : answer.getPartition()){
			if(cluster.size() == 0) null_clusters.add(cluster);
		}
		for(HashSet<String> null_cluster : null_clusters){
			answer.getPartition().remove(null_cluster);
		}
	}
	
	/**
	 * Gets the evaluation for a clutering solution
	 */
	public ClusteringEvaluation getEvaluation(ArrayList<Measure> measures){
		
		ClusteringEvaluation evaluation = new ClusteringEvaluation(key, answer);

		for(Measure measure : measures) {
			evaluation.setScore(measure.getID(), measure.getScore(key, answer));
		}
		
		/*
		if(measures.contains(Measures.PURITY)) evaluation.setScore(Measures.PURITY, Measures.purity(key, answer));
		if(measures.contains(Measures.INVERSE_PURITY)) evaluation.setScore(Measures.INVERSE_PURITY, Measures.inversePurity(key, answer));
		
		if(measures.contains(Measures.PURITY_F05)){
			double purity     = evaluation.getScore(Measures.PURITY);
			double inv_purity = evaluation.getScore(Measures.INVERSE_PURITY);
			double alpha      = 0.5;
			evaluation.setScore(Measures.PURITY_F05, Measures.FMeasure(purity, inv_purity, alpha));
		}
		
		if(measures.contains(Measures.BCUBED_EXT_PRECISION)) evaluation.setScore(Measures.BCUBED_EXT_PRECISION , Measures.BCubedExtendedPrecision(key, answer));
		if(measures.contains(Measures.BCUBED_EXT_RECALL)) evaluation.setScore(Measures.BCUBED_EXT_RECALL , Measures.BCubedExtendedPrecision(answer, key));
		
		if(measures.contains(Measures.BCUBED_EXT2_PRECISION)) evaluation.setScore(Measures.BCUBED_EXT2_PRECISION , Measures.BCubedExtendedPrecision2(key, answer));
		if(measures.contains(Measures.BCUBED_EXT2_RECALL)) evaluation.setScore(Measures.BCUBED_EXT2_RECALL , Measures.BCubedExtendedPrecision2(answer, key));
		
		
		if(measures.contains(Measures.BCUBED_F05)){
			double precision  = evaluation.getScore(Measures.BCUBED_EXT_PRECISION);
			double recall     = evaluation.getScore(Measures.BCUBED_EXT_RECALL);
			double alpha      = 0.5;
			evaluation.setScore(Measures.BCUBED_F05, Measures.FMeasure(precision, recall, alpha));
		}
		
		if(measures.contains(Measures.MULTIPLICITY)) evaluation.setScore(Measures.MULTIPLICITY , Measures.multiplicity(key, answer));
		
		if(measures.contains(Measures.PAIRS_RAND_STATISTIC)) evaluation.setScore(Measures.PAIRS_RAND_STATISTIC , Measures.pairsMeasure(answer, key, Measures.RAND_STATISTIC));
		if(measures.contains(Measures.PAIRS_JACCARD_COEFFICIENT)) evaluation.setScore(Measures.PAIRS_JACCARD_COEFFICIENT , Measures.pairsMeasure(answer, key, Measures.JACCARD_COEFFICIENT));
		if(measures.contains(Measures.PAIRS_FOLKES_AND_MALLOWS)) evaluation.setScore(Measures.PAIRS_FOLKES_AND_MALLOWS , Measures.pairsMeasure(answer, key, Measures.FOLKES_AND_MALLOWS));
		*/
		
		return evaluation;
	}
	
	
	public static ClusteringEvaluation getZeroEvaluation(ArrayList<Measure> measures) {
		
		ClusteringEvaluation evaluation = new ClusteringEvaluation();
		
		for(Measure measure : measures){
			evaluation.setScore(measure.getID(), 0.0);
		}
		
		return evaluation;
	}
	
}
