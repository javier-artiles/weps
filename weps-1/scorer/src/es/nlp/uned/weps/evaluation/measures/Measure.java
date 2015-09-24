package es.nlp.uned.weps.evaluation.measures;

import java.util.HashSet;

import es.nlp.uned.weps.evaluation.core.Clustering;

public abstract class Measure {

	public String ID;
	
	public abstract double getScore(Clustering key, Clustering answer);

	/**
	 * Checks wheter a pair of elements a grouped in the same cluster acording to a clustering solution.<br>
	 * 
	 * @param clustering the clustering
	 * @param pair the pair of elements (string identifiers)
	 * 
	 * @return true, if in same cluster
	 */
	protected boolean inSameCluster(String[] pair, Clustering clustering){

		String element_1 = pair[0];
		String element_2 = pair[1];
		
		for(HashSet<String> partition : clustering.getPartition()){
			
			if(partition.contains(element_1) && partition.contains(element_2))
				return true;
		}
		
		return false;
	}
	
	public String getID(){
		return ID;
	}
	
}
