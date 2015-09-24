package es.nlp.uned.weps.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import es.nlp.uned.weps.evaluation.measures.Measure;
import es.nlp.uned.weps.evaluation.measures.Measures;


/**
 * The Class MultiSystemScorer evaluates a set of systems and outputs the final ranking.
 */
public class SystemScorer {

	private ArrayList<File> key_files;
	
	private ArrayList<Measure> measures = new ArrayList<Measure>();
	
	private HashMap<String, TeamEvaluation> teamid2evaluation;
	
	/**
	 * The Constructor for scoring multiple clustering problems and their corresponding solutions.
	 *  
	 * @param keys_path the directory that contains all the key clustering files (following the WePS 2007 format)
	 */
	public SystemScorer(File keysDir, File systemsAnswersDir, File outputDir, ArrayList<Measure> measures, HashSet<String> baselines, boolean overwriteEvaluations) throws IOException{
		
		System.out.println("Key clustering files path:    "+keysDir);
		System.out.println("Answer clustering files path: "+systemsAnswersDir);
		System.out.println("Output evaluation files path: "+outputDir);
		System.out.print("Measures:                     [");
		for(Measure m : measures){
			System.out.print(m.getID()+", ");
		}
		System.out.print("]\n");
		System.out.println("Baselines:                    "+baselines.toString());
		System.out.println("Overwrite:                    "+overwriteEvaluations);
		System.out.println("");
		
		this.measures = measures;
		
		//Loading key and answer filenames
		key_files = new ArrayList<File>();
		
		if(!keysDir.exists()) {
			System.err.println("Couldn't find the keys directory: "+keysDir);
			System.exit(0);
		} else if(!systemsAnswersDir.exists()){
			System.err.println("Couldn't find the answers directory: "+systemsAnswersDir);
			System.exit(0);
		} else if(!outputDir.exists()){
			System.err.println("Couldn't find the output directory: "+outputDir);
			System.exit(0);
		}
		
		for(String filename : keysDir.list()){
			if(!filename.endsWith(".clust.xml")) continue;
			
			key_files.add(new File(keysDir, filename));
		}
		
		//Calculate each team evaluation
		teamid2evaluation = getEvaluations(systemsAnswersDir, outputDir, overwriteEvaluations);
		
		//Calculate baselines
		teamid2evaluation.putAll(getBaselineEvaluations(baselines, outputDir, overwriteEvaluations));
		
	}
	
	/**
	 * Generates a baseline answer and evaluates it with the key.
	 * 
	 * @param baseline_id the baseline_id
	 * 
	 * @return the team evaluation
	 */
	public TeamEvaluation evaluateBaseline(String baselineType){
		
		System.out.println(Logging.getInstance().log("Evaluating clustering answers (baseline "+baselineType+")"));

		HashMap<String, ClusteringEvaluation> name2evaluation = new HashMap<String, ClusteringEvaluation>();
		
		for(File keyFile : key_files){
			
			SimpleScorer scorer = new SimpleScorer(baselineType, keyFile.getAbsolutePath());
						
			ClusteringEvaluation evaluation = scorer.getEvaluation(measures);		
			
			String topicName = keyFile.getName().replace(".xml", "").replace(".clust", "");
			
			name2evaluation.put(topicName, evaluation);
		}
		
		TeamEvaluation team_evaluation = new TeamEvaluation(baselineType, true, name2evaluation);
		
		return team_evaluation;
	}
	
	/**
	 * Evaluate team using all the clustering problems.
	 * 
	 * @param answers_path the answers_path
	 * @param team_id the team_id
	 * 
	 * @return the team evaluation
	 */
	public TeamEvaluation evaluateTeam(String team_id, File teamAnswersDir){

		System.out.println(Logging.getInstance().log("Evaluating clustering answers (team "+team_id+") from "+teamAnswersDir));

		HashMap<String, ClusteringEvaluation> name2evaluation = new HashMap<String, ClusteringEvaluation>();
		
		for(File keyFile : key_files){
			
			File answer_file = new File(teamAnswersDir, keyFile.getName());
			
			ClusteringEvaluation evaluation;
			
			if(!answer_file.exists()){
				System.err.println(Logging.getInstance().log(team_id+" has no answer for the clustering problem "+keyFile+"\n" +
						"setting all the scores to 0.0 for this topic"));				
				
				evaluation = SimpleScorer.getZeroEvaluation(measures);
				
			} else {
				
				SimpleScorer scorer = new SimpleScorer(team_id, keyFile.getAbsolutePath(), answer_file.getAbsolutePath());
				evaluation = scorer.getEvaluation(measures);
			}
			
			String topicName = keyFile.getName().replace(".xml", "").replace(".clust", "");
						
			name2evaluation.put(topicName, evaluation);
		}
		
		TeamEvaluation team_evaluation = new TeamEvaluation(team_id, false, name2evaluation);
		
		return team_evaluation;
	}
	
	public HashMap<String, TeamEvaluation> getEvaluations(File systemsAnswersDir, File outputDir, boolean overwriteEvaluations) throws IOException{
		
		HashMap<String, TeamEvaluation> teamid2evaluation = new HashMap<String, TeamEvaluation>();
		
		for(String team_id : systemsAnswersDir.list()){
			
			File teamAnswersDir = new File(systemsAnswersDir, team_id);
			
			File outputFile      = new File(outputDir, team_id+".eval");
			
			TeamEvaluation team_evaluation;
			
			//If the evaluation already exists read it, else create it and save
			
			if(outputFile.exists() && !overwriteEvaluations){
				
				team_evaluation = new TeamEvaluation(outputFile);
				
			} else {
				
				team_evaluation = evaluateTeam(team_id, teamAnswersDir);
				team_evaluation.save(outputFile);
			}
			
			teamid2evaluation.put(team_id, team_evaluation);
		}
		
		return teamid2evaluation;
	}
	
	
	public HashMap<String, TeamEvaluation> getBaselineEvaluations(HashSet<String> baselines, File outputDir, boolean overwriteEvaluations) throws IOException{
		
		HashMap<String, TeamEvaluation> teamid2evaluation = new HashMap<String, TeamEvaluation>();
		
		for(String baseline_id : baselines){
			
			File outputFile = new File(outputDir, baseline_id+".eval");
			
			TeamEvaluation team_evaluation;
			
			if(outputFile.exists() && !overwriteEvaluations) {
				
				team_evaluation = new TeamEvaluation(outputFile);				
				
			} else {
			
				team_evaluation = evaluateBaseline(baseline_id);
				
				team_evaluation.save(outputFile);	
			}
			
			teamid2evaluation.put(baseline_id, team_evaluation);
		}
		
		return teamid2evaluation;
	}
	
	
	/**
	 * Gets the averaged evaluation for a particular team.
	 * 
	 * @param eval_path the eval_path
	 * 
	 * @return the averaged evaluation
	 * 
	 * @throws IOException the IO exception
	 */
	public double[] getAveragedScores(File evalFile) throws IOException{
		double[] averages = null;
		BufferedReader bfr = new BufferedReader(new FileReader(evalFile));
		String line;
		while((line = bfr.readLine()) != null){
			if(line.startsWith("Average\t")){
				
				String[] line_spl = line.trim().split("\t");
				
				averages = new double[line_spl.length-1]; 
				
				for(int i=1; i<line_spl.length; i++){
					averages[i-1] = Double.parseDouble(line_spl[i].replace(',', '.'));
				}
				
				break;
			}
		}
		
		if(averages == null) System.err.println("Could not find \"averages\" line in file "+evalFile);
		
		bfr.close();
		
		return averages;
	}
	
	public String[] getColumnLabels(File evalFile) throws IOException{
		
		String[] labels = null;
		
		BufferedReader bfr = new BufferedReader(new FileReader(evalFile));
		String line;
		while((line = bfr.readLine()) != null){
			if(line.startsWith("topic\t")){
				
				labels = line.trim().split("\t");
								
			}
		}
		
		if(labels == null) System.err.println("Could not find \"topic\" line in file "+evalFile);
		
		bfr.close();
		
		return labels;
	}
	
	/**
	 * Averaged evaluation: for each team lists the scores averaged for all the clustering problems.
	 */
	private void printAveragedEvaluation(File evaluationsDir) throws IOException {
		
		boolean printedLabels = false;
		
		for(String evalFile : evaluationsDir.list()){
			
			if(evalFile.endsWith(".eval")) {
				
				if(!printedLabels) {
					String[] labels = getColumnLabels(new File(evaluationsDir, evalFile));
					
					System.out.println("\n");
					for(String label : labels){
						System.out.print(label+"\t");
					}
					System.out.println("\n");
					
					printedLabels = true;
				}
					
			
				String team_name = evalFile.replace(".eval", "");
				
				double[] averages = getAveragedScores(new File(evaluationsDir, evalFile));
				
				System.out.print(team_name+"\t");
				
				for(double average : averages){
					System.out.print(String.valueOf(average).replace('.', ',')+"\t");
				}
				System.out.println();
				
			}
		}
	}
	
	/*
	private void printUnanimityEvaluation(HashSet<String> measures_to_consider){
		UnanimityEvaluation.printUnanimityMatrix(teamid2evaluation, measures_to_consider);
	}*/
	
	/**
	 * The main method.
	 * 
	 * @param args the args
	 * 
	 * @throws Exception the exception
	 */
	public static void main(String args[]) throws Exception{
		
		System.out.println("WePS 2007 Evaluation Package (http://nlp.uned.es/weps)\n");
		
		if(args.length < 1 || args[0].equals("-help")) {
			
			System.out.println("This program scores the performace of one or more systems \n" +
					"according to several optional evaluation measures. \n");
			
			System.out.println("USAGE: \n"+
					"java SystemScorer [keysDir] [systemsDir] [outputDir] [MEASURES] [BASELINES] [OPTIONS]");
			
			System.out.println("\n[keysDir]    \t Directory containing all the gold standard for the clustering problems.\n" +
					           " \t Files must be well formed XML, follow the WePS 2007 clustering format and filenames end in 'clust.xml'.");
			
			System.out.println("\n[systemsDir] \t Directory containig all the systems solutions to evaluate using the following structure\n" +
					" \t systemsDir/TEAM_A/problem1.clust.xml\n" +
					" \t systemsDir/TEAM_A/problem2.clust.xml\n" +
					" \t systemsDir/TEAM_A/...\n" +
					" \t systemsDir/TEAM_B/problem1.clust.xml\n" +
					" \t systemsDir/TEAM_B/problem2.clust.xml\n" +
					" \t systemsDir/TEAM_B/...\n" +
					" \t systemsDir/...\n");
			
			System.out.println("\n[outputDir] \t Directory where all the results will be written");
			
			System.out.println("\nMEASURES:\n" +
			" -ALLMEASURES \t Evaluates all the available measures" +
			" -P   \t Purity\n" +
			" -IP  \t Inverse purity\n" +
			" -FMeasure_0.5_P-IP  \t F-measure for Purity and Inverse Purity (alpha=0.5)\n" +
			
			" -BER \t BCubed Recall (extended for multiclass problems)\n" +
			" -BEP \t BCubed Precision (extended for multiclass problems)\n" +
			" -FMeasure_0.5_BER-BEP \t F-measure for BCubed Precision and Recall (alpha=0.5)\n" +

			/*" -M   \t Multiplicity\n" +*/
			" -PR  \t Pairs measure using Rand Statistic\n" +
			" -PJ  \t Pairs measure using Jaccard Coefficient\n" +
			" -PF  \t Pairs measure using Folkes and Mallows");
			
			System.out.println("\nBASELINES:\n" +
					" -AllInOne \t \n" +
					" -OneInOne \t \n" +
					" -Combined \t \n");
			
			System.out.println("\nOPTIONS:\n" +
					" -overwrite \t overwrites previous evaluation files (.eval) if necessary.\n" +
					" -average   \t prints the averaged scores for all the teams\n");
			
			System.exit(0);
		}
		
		File keysDir           = new File(args[0]);
		File systemsAnswersDir = new File(args[1]);
		File outputDir         = new File(args[2]);
		
		ArrayList<String> otherParameters = new ArrayList<String>();
		for(int i=3; i<args.length; i++){
			otherParameters.add(args[i]);
		}
			
		ArrayList<Measure> measuresToEval = new ArrayList<Measure>();

		boolean overwriteEvaluations = false;
		if(otherParameters.contains("-overwrite")) overwriteEvaluations = true;
		
		Measures measures = new Measures();
		
		if(otherParameters.contains("-ALLMEASURES")){
			
			measuresToEval.addAll(measures.getAllMeasures());
			
		} else {

			Iterator iterParams = otherParameters.iterator();
			
			while(iterParams.hasNext()){
				String param = ((String)iterParams.next()).substring(1);
				
				Measure measure = null;
				if((measure = measures.getMeasureByID(param)) != null) {
					measuresToEval.add(measure);
				}
			}
			
		}
		
		
		if(measuresToEval.size() == 0) {
			System.err.println("You must specify at least one evaluation measure (for more information try 'SystemScorer -help').");
			System.exit(0);
		}
		
		HashSet<String> baselines = new HashSet<String>();
		if(otherParameters.contains("-AllInOne")) baselines.add(SimpleScorer.ALL_IN_ONE_BASELINE);
		if(otherParameters.contains("-OneInOne")) baselines.add(SimpleScorer.ONE_IN_ONE_BASELINE);
		if(otherParameters.contains("-Combined")) baselines.add(SimpleScorer.COMBINED_BASELINE);
		
		
		SystemScorer systemScorer = new SystemScorer(keysDir,systemsAnswersDir, outputDir, measuresToEval, baselines, overwriteEvaluations);
		if(otherParameters.contains("-average")) systemScorer.printAveragedEvaluation(outputDir); 
		
		/*
		if(otherParameters.contains("-unanimity")) {
			systemScorer.printUnanimityEvaluation(measures);
			
		}
		*/
		
		Logging.getInstance().close();
		
	}
	
}