package es.nlp.uned.weps.evaluation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import drasys.or.prob.BinomialDistributionBase;
import es.nlp.uned.utilities.MathHelper;

/**
 * The Class UnanimityEvaluation implements the Unanimity metric combination method.
 */
public class UnanimityEvaluation {
	
	
	/**
	 * 
	 * @param teamid2evaluation the teamid2evaluation
	 * @param measures_to_consider the measures_to_consider
	 */
	public static void printUnanimityMatrix(HashMap<String, TeamEvaluation> teamid2evaluation, HashSet<String> measures_to_consider){
		
		System.out.println("\nUnanimity evaluation: \n");
		
		//Matrix team_1 <> team_2, with values 1 (team 1 beats team 2), -1 viceversa and 0 (draw)
		Integer[][] beat_matrix = new Integer[teamid2evaluation.size()][teamid2evaluation.size()];
		Double[][] stat_rel_matrix = new Double[teamid2evaluation.size()][teamid2evaluation.size()]; 
		
		//Columns/rows for the previous matrices
		String[] teams_list = new String[teamid2evaluation.size()];
		
		teamid2evaluation.keySet().toArray(teams_list);
		
		Arrays.sort(teams_list);
		
		for(int t_1=0; t_1<teams_list.length; t_1++) {
			
			String team_1 = teams_list[t_1];
			
			TeamEvaluation team_1_eval = teamid2evaluation.get(team_1);
			
			for(int t_2=0; t_2<teams_list.length; t_2++) {
				
				//Times that team 1 beats 2
				int beats_1_2 = 0;
				
				//Viceversa
				int beats_2_1 = 0;
			
				String team_2 = teams_list[t_2];
				
				TeamEvaluation team_2_eval = teamid2evaluation.get(team_2);
				
				Set<String> topics_ids = team_1_eval.getTopicNames();
								
				for(String topic_id : topics_ids) {	
					
					boolean exist_measure_1sup2 = false;
					boolean exist_measure_2sup1 = false;
					
					ClusteringEvaluation team_1_topic_evaluation = team_1_eval.getTopic2evaluation().get(topic_id);
					ClusteringEvaluation team_2_topic_evaluation = team_2_eval.getTopic2evaluation().get(topic_id);
					
					for(String measure_id : measures_to_consider){
						
						double measure_val_1 = team_1_topic_evaluation.getScore(measure_id);
						double measure_val_2 = team_2_topic_evaluation.getScore(measure_id);

						/*
						System.out.println(measure_id+"\n"+
								" \t"+team_1+" : "+measure_val_1+"\n"+
								" \t"+team_2+" : "+measure_val_2+"\n");
						*/
						
						if(measure_val_1 > measure_val_2){
							exist_measure_1sup2 = true;
							
						} else if(measure_val_2 > measure_val_1){
							exist_measure_2sup1 = true;
							
						}
						
					}
					
					//System.out.println(team_1+"\t"+team_2+"\t"+topic_id+"\t"+exist_measure_1sup2+":"+exist_measure_2sup1);
					
					if(exist_measure_1sup2 && !exist_measure_2sup1) beats_1_2 ++;
					if(exist_measure_2sup1 && !exist_measure_1sup2) beats_2_1 ++;
					
				}
				
				System.out.println(""+team_1+" : "+beats_1_2+"\n"+
						""+team_2+" : "+beats_2_1+"\n\n");
				
				BinomialDistributionBase binomial = new BinomialDistributionBase(0.5, beats_1_2 + beats_2_1);
				
				int beat = 0;
				double stat_rel = 1.0;
				
				if(beats_1_2 > beats_2_1){
					stat_rel =binomial.pdf(beats_2_1);
					
					if (stat_rel < 0.02654425) {
						beat = 1;
					}
					
				} else if(beats_2_1 > beats_1_2) {
					stat_rel = binomial.pdf(beats_1_2);
					
					if (stat_rel < 0.02654425) {
						beat = -1; 
					}
				}
				
				beat_matrix[t_1][t_2] = beat;
				stat_rel_matrix[t_1][t_2] = stat_rel;
			}
		}
		
		//Print matrices
		
		System.out.println("\nStatistical relevance matrix\n");
		
		MathHelper.printMatrix(teams_list, teams_list, stat_rel_matrix);
		
		System.out.println("\nWho beats who matrix\n");
		
		MathHelper.printMatrix(teams_list, teams_list, beat_matrix);
		
		System.out.println("\nWho beats who table\n");
		
		System.out.println("team_id\tbeats...\tis_beaten...");
		
		for(int i=0; i<beat_matrix.length; i++){
			
			String team_id = teams_list[i];
			
			System.out.print(team_id);
			System.out.print("\t");
			
			//Beats
			for(int c=0; c<beat_matrix.length; c++){
				if(beat_matrix[i][c] == 1) System.out.print(teams_list[c]+", ");
				
			}
			
			System.out.print("\t");
			
			//Is beaten
			for(int c=0; c<beat_matrix.length; c++){
				if(beat_matrix[i][c] == -1) System.out.print(teams_list[c]+", ");
				
			}
			
			System.out.print("\n");
		}
		
	}
}
