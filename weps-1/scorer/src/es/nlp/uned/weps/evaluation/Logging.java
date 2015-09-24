package es.nlp.uned.weps.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logging {
	
	private static Logging me;
	
	public static final File logFile = new File("log.txt");
	
	private BufferedWriter bfw;
	
	private Logging()  {
		try {
			bfw = new BufferedWriter(new FileWriter(logFile));
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static Logging getInstance() {
		if(me == null){
			me = new Logging();
		}
		return me;
	}
	
	public String log(String message)  {
		
		try {
			bfw.write(message);
			bfw.write("\n");
			
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return message;
	}
	
	public void close() throws IOException{
		bfw.close();
	}

}
