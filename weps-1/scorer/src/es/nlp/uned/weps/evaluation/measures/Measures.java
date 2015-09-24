package es.nlp.uned.weps.evaluation.measures;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Class Measures contains a set of static methods that implement different clustering measures and combined measures.
 * 
 * Reference:
 * E. Amigó, J. Gonzalo and J. Artiles.
 * Evaluation metrics for clustering tasks: a comparison based on formal constraints.
 * Technical report to be published in http://nlp.uned.es
 * 
 */
public class Measures {

	private ArrayList<Measure> measuresList;
	private HashMap<String, Measure> id2measure;
	
	private Measure[] mArray = {
			new Purity(),
			new InversePurity(),
			new FMeasure(new Purity(), new InversePurity(), 0.5),
			
			new BCubedExtPrecision(),
			new BCubedExtRecall(),
			new FMeasure(new BCubedExtPrecision(), new BCubedExtRecall(), 0.5),
			
			new Pairs(Pairs.FOLKES_AND_MALLOWS),
			new Pairs(Pairs.JACCARD_COEFFICIENT),
			new Pairs(Pairs.RAND_STATISTIC)
	};
	
	public Measures(){
		measuresList = new ArrayList<Measure>();
		id2measure = new HashMap<String, Measure>();
		
		for(Measure m : mArray){
			measuresList.add(m);
			id2measure.put(m.getID(), m);
		}
	}
	
	public Measure getMeasureByID(String ID){
		return id2measure.get(ID);
	}
	
	public ArrayList<Measure> getAllMeasures(){
		return measuresList;
	}
	
	
	
	
	
	/**
	 * Calculates the multiplicity measure.<br>
	 * <br>
	 * Intuitively, multiplicity measures how different two clustering solutions are
	 * in terms of the number clusters assigned to each element.<br>
	 * This measure is useful in clustering solutions where one element can belong to more than one category.
	 * In the case of the <a  href="http://nlp.uned.es">Web People Search task</a>, one document can contain references to different people using
	 * the same name (e.g. in genealogies).<br>
	 * <br>
	 * This method builds a vector for the key and another for the answer.
	 * In the vector each component is an the number of clusters in which an element appears.
	 * Finally the euclidian distance between the two vectors is calculated.<br>
	 * <br>
	 * 
	 * Reference:
	 * E. Amigó, J. Gonzalo and J. Artiles.
	 * Evaluation metrics for clustering tasks: a comparison based on formal constraints.
	 * Technical report to be published in http://nlp.uned.es
	 * 
	 * @param key the key clustering
	 * @param answer the answer clustering
	 * 
	 * @return the result
	 */
	/*
	public static double multiplicity(Clustering answer, Clustering key){
		
		//First build vectors in which each component is an the number of clusters where an element appears
		
		int[] element_mult_1 = new int[key.getAssigned().size()];
		int[] element_mult_2 = new int[key.getAssigned().size()];
		
		int i = 0;
		for(String element : key.getAssigned()){
			element_mult_1[i] = key.getClusters(element).size();
			element_mult_2[i] = answer.getClusters(element).size();
			
			i++;
		}
		
		//Calculate euclidian distance
		
		double result = MathHelper.euclideanDistance(element_mult_1, element_mult_2, true);
		
		return result;
	}*/

	
	
	
	/**
	 * Calculates a BCubed precision measure, extended for multicategory clustering problems.<br>
	 * Swapping key and answer we obtain the equivalent recall measure.
	 * 
	 * <pre>
	 * 
	 * For each element in clustering e {
	 * 
	 * precision_samples = 0
	 * precision = 0
	 * recall_samples = 0
	 * recall = 0
	 * 
	 * For each element in clustering e' {
	 * boolean b_1 = e & e' share a cluster
	 * boolean b_2 = e & e' share a category
	 * 
	 * IF b_1 {
	 * precision_samples ++
	 * IF b_2 {
	 * precision ++
	 * }
	 * }
	 * IF b_2 {
	 * recall_samples ++
	 * IF b_1 {
	 * recall ++
	 * }
	 * }
	 * }
	 * 
	 * precision_total += precision/precision_samples
	 * recall_total += recall/recall_samples
	 * samples_total ++;
	 * 
	 * }
	 * 
	 * precision_total /= samples_total
	 * recall_total /= samples_total
	 * </pre>
	 * 
	 * Reference:
	 * E. Amigó, J. Gonzalo and J. Artiles.
	 * Evaluation metrics for clustering tasks: a comparison based on formal constraints.
	 * Technical report to be published in http://nlp.uned.es
	 * 
	 * @param key the key clustering
	 * @param answer the answer clustering
	 * 
	 * @return precision
	 */
	/*
	public static double BCubedExtendedPrecision(Clustering key, Clustering answer){
		
		double precision_total = 0.0;
		double recall_total    = 0.0;
		int samples_total = 0;
		
		for(String elem : key.getAssigned()){
			
			int precision_samples = 0;
			int precision = 0;
			int recall_samples = 0;
			int recall = 0;
			
			samples_total ++;
			
			if(answer.getAssigned().contains(elem)){
				
				for(String elem_prime : key.getAssigned()){
					
					String[] pair = {elem, elem_prime};
					
					boolean b_1 = false; //e & e' share some cluster
					if(inSameCluster(pair, answer)) b_1 = true;
					
					boolean b_2 = false; //e & e' share some category
					if(inSameCluster(pair, key)) b_2 = true;
					
					
					if(b_1) {
				     	precision_samples ++;
				       if(b_2) {
				       	precision ++;
				       }
				    }    
				    
					if(b_2){
				     	recall_samples ++;
				       if(b_1) {
				       	recall ++;
				       }
				    }	
				}
				//System.out.println(elem+" ; "+precision+" ; "+precision_samples);
			
				precision_total += precision/(double)precision_samples;
				recall_total += recall/(double)recall_samples;
			
			}
		}
		
		
		double precision = precision_total/(double)samples_total;

		return precision;
	}
	*/
	
	/**
	 * B cubed extended recall.
	 * 
	 * Reference:
	 * E. Amigó, J. Gonzalo and J. Artiles.
	 * Evaluation metrics for clustering tasks: a comparison based on formal constraints.
	 * Technical report to be published in http://nlp.uned.es
	 * 
	 * @param key the key
	 * @param answer the answer
	 * 
	 * @return the double
	 */
	/*
	public static double BCubedExtendedRecall(Clustering key, Clustering answer) {
		return BCubedExtendedPrecision(answer, key);
	}*/
	
}
