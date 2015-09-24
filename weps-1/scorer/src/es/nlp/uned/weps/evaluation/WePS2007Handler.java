package es.nlp.uned.weps.evaluation;

import java.util.HashMap;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The Class WePS2007Handler is a XML handler for the WePS 2007 clustering format.
 */
public class WePS2007Handler  extends DefaultHandler {
	
	/** The buffer. */
	private StringBuffer buffer = null;
	
	/** The discarded. */
	private boolean discarded = false;
	
	/** The doc_count. */
	private int doc_count;
	
	/** The entity2docs. */
	private HashMap<String, HashSet<String>> entity2docs = null;
	
	/** The cluster_docs. */
	private HashSet<String> cluster_docs = null;
	
	/** The cluster_id. */
	private String          cluster_id = null;
	
	/** The discarded_docs. */
	private HashSet<String> discarded_docs = null;
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument(){
		entity2docs = new HashMap<String, HashSet<String>>();
		discarded_docs = new HashSet<String>();
		doc_count = 0;
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	public void endDocument(){ }	
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qname, Attributes attr) {
		buffer = new StringBuffer();
		
		if(localName.equals("entity")){
			
			cluster_id = attr.getValue("", "id"); 
			cluster_docs = new HashSet<String>();
		
		} else if(localName.equals("discarded")){
			discarded = true;
			
		} else if(localName.equals("doc") && !discarded && !cluster_docs.contains(attr.getValue("", "rank"))){
			doc_count++;
			String rank = attr.getValue("", "rank").trim();
			
			if(rank.length() == 0){
				System.err.println("! A doc rank (id) is empty.");
				System.exit(0);
			}
			
			//Remove trailing zeroes
			rank = rank.replaceAll("^0+", "");
			if(rank.length() == 0) rank = "0";
			cluster_docs.add(rank);
			
		} else if(localName.equals("doc") && discarded && !discarded_docs.contains(attr.getValue("", "rank"))){
			doc_count++;
			String rank = attr.getValue("", "rank").trim();
			
			if(rank.length() == 0){
				System.err.println("! A doc rank (id) is empty.");
				System.exit(0);
			}
			
			//Remove trailing zeroes
			rank = rank.replaceAll("^0+", "");
			if(rank.length() == 0) rank = "0";
			discarded_docs.add(rank);
			
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qname){
		
		if(localName.equals("entity")){
			
			if(cluster_docs.size() == 0){
				System.err.println("! The entity "+cluster_id+" has no documents on it.\n" +
						           "  All <entity> elements must have at least one <doc> element.");
				System.exit(0);
				
			} else if(entity2docs.containsKey(cluster_id)) {
				System.err.println("! The entity ID "+cluster_id+" must be unique.");
				System.exit(0);

			}
			
			entity2docs.put(cluster_id, cluster_docs);
			
			cluster_id = null;
			cluster_docs = null;
			
		} else if(localName.equals("discarded")){
			discarded = false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length){	
		buffer.append(ch, start, length); 
	}
	
	/**
	 * Gets the dictionary {cluster identifier: set of documents}
	 * 
	 * @return the entity dic
	 */
	public HashMap<String, HashSet<String>> getEntityDic(){
		
		return entity2docs;
		
	}
	
	/**
	 * Gets the set of discarded documents (documents specified as not assigned to any cluster).
	 * 
	 * @return the discarded
	 */
	public HashSet<String> getDiscarded(){
		
		return discarded_docs;
		
	}
	
	/**
	 * Gets the set of assigned documents.
	 * 
	 * @return the assigned docs
	 */
	public int getAssignedDocs(){
		
		return doc_count;
		
	}
}