package es.nlp.uned.weps.evaluation.measures;

import java.util.HashMap;
import java.util.HashSet;

import es.nlp.uned.weps.evaluation.core.Clustering;

public class BCubedExtPrecision extends Measure {
	

	/** The BCUBE d_ EX t_ PRECISION. */
	public static final String ID = "BEP";


	/**
	 * New formula for the extended BCubed precision and recall scores
	 * 
	 * Este método asume que todas los elementos tiene categoría
	 * 
	 * Cuando el sistema no anota un elemento no este tiene precision = 1 y recall = 0
	 * Cuando el anotador no anota, no se evalua el elemento ya que pertenece a un problema 
	 * de filtrado.
	 * 
	 * @param answer
	 * @param key
	 * @return 
	 */
	public double getScore(Clustering key, Clustering answer){
		return getScores(key, answer)[0];
	}
	
	/**
	 * Returns an array of two doubles {precision, recall}
	 * @param key
	 * @param answer
	 * @return
	 */
	public double[] getScores(Clustering key, Clustering answer){
		
		HashMap<String, HashSet<String>> keyId2clust = key.getIdToClustDict();
		HashMap<String, HashSet<String>> ansId2clust = answer.getIdToClustDict();
		
		double precision = 0.0;
		double recall    = 0.0;
		
		for(String docID : key.getAssigned()) {
			
			//If a document is not assigned in the answer,
			//evaluate it in a singleton cluster.
			//TODO: decide wheter this decision should be generalized to all measures
			if(!answer.getAssigned().contains(docID)) {
				
				precision ++;
				
			} else {
				
				double precisionID =      0.0;
				double samplesIDPrecision=0.0;
				
				double recallID   =      0.0;
				double samplesIDRecall=0.0;
				
				for(String docID2 : key.getAssigned()) {
					
					//Intersection of categories
					double numCatIntersection = 0.0;
					for(HashSet<String> category : keyId2clust.values()){
						if(category.contains(docID) &&
								category.contains(docID2)) 
							numCatIntersection++;
					}
	
					//Intersection of clusters				
					double numClustIntersection = 0.0;
					for(HashSet<String> cluster : ansId2clust.values()){
						if(cluster.contains(docID) &&
								cluster.contains(docID2)) 
							numClustIntersection++;
					}
					
					if(numClustIntersection > 0){
						samplesIDPrecision++;
						if(numCatIntersection >= numClustIntersection){
							precisionID ++;
						}
						if(numCatIntersection < numClustIntersection){
							
							precisionID += numCatIntersection/numClustIntersection;
						}
					}
					
					if(numCatIntersection > 0){
						samplesIDRecall++;
						if(numClustIntersection >= numCatIntersection){
							recallID ++;
						}
						if(numClustIntersection < numCatIntersection){
							
							recallID += numClustIntersection/numCatIntersection;
						}
					}
				}

				precision += precisionID/samplesIDPrecision;
				recall += recallID/samplesIDRecall;
				
			}
		}
		
		double averagePrecision = precision/(double)key.getAssigned().size();
		
		double averageRecall = recall/(double)key.getAssigned().size();
		//System.out.println(">>> "+averagePrecision);
		
		double[] precisionRecall = {averagePrecision, averageRecall};
		
		return precisionRecall;
	}
	
	
	public String getID(){
		return ID;
	}
	
}
