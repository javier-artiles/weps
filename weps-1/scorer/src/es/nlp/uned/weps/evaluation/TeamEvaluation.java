package es.nlp.uned.weps.evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


/**
 * The Class TeamEvaluation.
 */
public class TeamEvaluation {
	
	private String team_name;
	
	private boolean is_baseline;
	
	private HashMap<String, ClusteringEvaluation> topic2evaluation;
	
	private static String TOPIC_ROW_PREFIX = "topic";
	
	private static String AVERAGE_ROW_PREFIX = "Average";
	
	/**
	 * The Constructor.
	 * 
	 * @param topic2evaluation the topic2evaluation
	 * @param team_name the team_name
	 * @param is_baseline the is_baseline
	 */
	public TeamEvaluation(String team_name, boolean is_baseline, HashMap<String, ClusteringEvaluation> topic2evaluation) {
		this.team_name = team_name;
		this.is_baseline = is_baseline;
		this.topic2evaluation = topic2evaluation;
	}

	/**
	 * The Constructor.
	 * 
	 * @param input_path the input_path
	 */
	public TeamEvaluation(File evaluationFile){
		try{
			load(evaluationFile);
			
		} catch(IOException e){
			e.printStackTrace();
			
		}
	}
	

	/**
	 * Checks if this evaluation was made with a baseline system
	 * 
	 * @return true, if is is_baseline
	 */
	public boolean isIsBaseline() {
		return is_baseline;
	}


	/**
	 * Gets the team_name.
	 * 
	 * @return the team_name
	 */
	public String getTeamName() {
		return team_name;
	}


	/**
	 * Gets the map from clustering names to their evaluation
	 * 
	 * @return the topic2evaluation
	 */
	public HashMap<String, ClusteringEvaluation> getTopic2evaluation() {
		return topic2evaluation;
	}

	/**
	 * Gets the topic names (names of clustering problems).
	 * 
	 * @return the topics
	 */
	public Set<String> getTopicNames(){
		return topic2evaluation.keySet();
	}
	
	/**
	 * Gets the averaged score for a particular measure.
	 * 
	 * @param measure_id the measure_id as specified in the class Measures
	 * @param float_precision the float precision we want for the result
	 * 
	 * @return the averaged score
	 */
	public double getAveragedScore(String measure_id, int float_precision){
		
		double sum = 0.0;
		
		for(String name : topic2evaluation.keySet()){
			
			ClusteringEvaluation evaluation = topic2evaluation.get(name);
			
			sum += evaluation.getScore(measure_id);
			
		}
		
		double averaged_score = sum / (double) topic2evaluation.size();
		
		return averaged_score; 
	}
	
	/**
	 * Parses an existing evaluation file and loads its contents.
	 * 
	 * @param evaluationFile the evaluation File
	 * 
	 * @throws IOException the IO exception
	 */
	private void load(File evaluationFile) throws IOException{
		
		System.out.println(Logging.getInstance().log("* Loading team evaluation from: "+evaluationFile));
		
		team_name = evaluationFile.getName().replace(".eval", "");
		
		if(team_name.toUpperCase().contains("BASELINE")) is_baseline = true;
		else is_baseline = false;
		
		topic2evaluation = new HashMap<String, ClusteringEvaluation>();
		
		BufferedReader bfr = new BufferedReader(new FileReader(evaluationFile));
		
		String[] scores_ids = null;
		String line;
		while((line = bfr.readLine()) != null){
			
			String[] line_spl = line.split("\t");
			
			//System.out.println(line);
			
			if(line.startsWith(AVERAGE_ROW_PREFIX) || line.trim().length() == 0){
				continue;
				
			} else if(line.startsWith(TOPIC_ROW_PREFIX)){
				
				//System.out.println("TOPIC_ROW_PREFIX");
				
				scores_ids  = new String[line_spl.length-1];
				
				for(int i=1; i<line_spl.length; i++){
					scores_ids[i-1] = line_spl[i];
					//System.out.println("\t"+scores_ids[i-1]);
				}
				
			} else {
				
				//The score ids column is read before arriving here
				assert(scores_ids != null);
				
				String topic_id = line_spl[0];
				
				ClusteringEvaluation topic_evaluation = new ClusteringEvaluation();
				//Add the measure values...
				
				for(int i=1; i<line_spl.length; i++){
					double value = Double.parseDouble(line_spl[i].replace(',', '.'));
					
					/*
					System.out.println(i-1);
					System.out.println(scores_ids[i-1]);
					System.out.println(value);
					*/
					
					topic_evaluation.setScore(scores_ids[i-1], value);
				}
				
				topic2evaluation.put(topic_id, topic_evaluation);
			}
		}
		
		bfr.close();
	}

	
	/**
	 * Saves the evaluation in a comma separated file.
	 * 
	 * @param output_path the output_path
	 * 
	 * @throws IOException the IO exception
	 */
	public void save(File outputFile) throws IOException{
		
		System.out.println(Logging.getInstance().log("Saving team evaluation to: "+outputFile+"\n"));
		
		int float_precision = 2;
		
		BufferedWriter bfw = new BufferedWriter(new FileWriter(outputFile));
		
		String[] names_list = new String[topic2evaluation.size()];
		(topic2evaluation.keySet()).toArray(names_list);
		
		Arrays.sort(names_list);
		
		// Get columns info (make sure all topics have the same columns)
		String[] measure_columns = null;
		
		for(String name_set : names_list){
			
			ClusteringEvaluation evaluation = topic2evaluation.get(name_set);
			
			String[] scores_ids = evaluation.getMeasures();
			
			/*
			for(String scoreId : scores_ids) System.out.print(scoreId+" - ");
			System.out.print("\n");
			*/
			
			if(measure_columns == null) measure_columns = scores_ids;
			
			assert(measure_columns.length == scores_ids.length);
		}
		
		// Write columns info
		bfw.write(TOPIC_ROW_PREFIX+"\t");
		
		for(int i=0; i<measure_columns.length; i++){
			bfw.write(measure_columns[i]);
			if(i+1 < measure_columns.length) bfw.write("\t");
		}
		
		bfw.write("\n");
		
		// Write detailed results per topic
		
		for(String name_set_id : names_list){
			ClusteringEvaluation evaluation = topic2evaluation.get(name_set_id);
			
			bfw.write(name_set_id);
			bfw.write("\t");
			
			String[] scores_ids = evaluation.getMeasures();
			
			for(int i=0; i<scores_ids.length; i++){
				double value = evaluation.getScore(scores_ids[i], float_precision);
				String formatted_value = String.format("%1$.2f", value);
				
				bfw.write(formatted_value);
				if(i+1 < scores_ids.length) bfw.write("\t");
			}
			
			bfw.write("\n");
		}
		
		// Averaged results
		
		bfw.write(AVERAGE_ROW_PREFIX);
		bfw.write("\t");
		
		for(int i=0; i<measure_columns.length; i++){
			double averaged_value = getAveragedScore(measure_columns[i], float_precision);
			
			String formatted_value = String.format("%1$.2f", averaged_value);
			
			bfw.write(formatted_value);
			if(i+1 < measure_columns.length) bfw.write("\t");
		}
				
		bfw.close();
		
	}
}
