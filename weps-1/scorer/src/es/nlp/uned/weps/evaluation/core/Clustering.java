package es.nlp.uned.weps.evaluation.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import es.nlp.uned.weps.evaluation.Logging;
import es.nlp.uned.weps.evaluation.WePS2007Handler;


/**
 * The Class Clustering: holds the description of a clustering solution (partition of elements 
 * in different clusters). Note that we allow elements to appear in more than one cluster and
 * that some element might not be assigned to any cluster.
 */
public class Clustering {

	/** The partition of documents. */
	private ArrayList<HashSet<String>> partition;
		
	/** The assigned documents. */
	private HashSet<String> assigned;
	
	/** The discarded documents. */
	private HashSet<String> discarded;
	
	/** Dictionary from cluster ID to cluster */
	private HashMap<String, HashSet<String>> clust_id2cluster;
	
	/** Dictionary from document ID to cluster/s */
	private HashMap<String, HashSet<String>> doc_id2clusters;
	
	/**
	 * Constructor using a partition of documents.
	 * 
	 * @param partition the partition of documents.
	 */
	public Clustering(ArrayList<HashSet<String>> partition){
		this.partition = partition;
		
		clust_id2cluster = new HashMap<String, HashSet<String>>();
		assigned = new HashSet<String>();
		discarded = new HashSet<String>();
		
		int i = 0;
		for(HashSet<String> cluster : partition){
			clust_id2cluster.put(String.valueOf(i), cluster);
			
			assigned.addAll(cluster);
			
			i++;
		}
		
	}
	
	/**
	 * Constructor using a WePS 2007 clustering file as input.
	 * 
	 * @param clustering_path the path to a clustering xml in the WePS 2007 format .
	 */
	public Clustering(String clustering_path){
		
		WePS2007Handler handler = new WePS2007Handler();
		
		parse(clustering_path, handler);
		
		clust_id2cluster = handler.getEntityDic();
		
		partition = new ArrayList<HashSet<String>>();
		for(HashSet<String> part : clust_id2cluster.values()){
			partition.add(part);
		}
		discarded = handler.getDiscarded();		
		
		assigned = new HashSet<String>();
		for(HashSet<String> docs : clust_id2cluster.values()){
			assigned.addAll(docs);
		}
		
	}

	/**
	 * Returns a set of clusters containing the document identifier.
	 * 
	 * @param docID the document identifier
	 * 
	 * @return the clusters
	 */
	public ArrayList<HashSet<String>> getClusters(String docID){
		
		ArrayList<HashSet<String>> result = new ArrayList<HashSet<String>>();
		
		for(HashSet<String> cluster : partition){
			if(cluster.contains(docID)) result.add(cluster);
		}
		
		return result;
	}
	
	/**
	 * Gets the dictionary "document identifier to clusters" 
	 * 
	 * @return a dictionary of {element_id => [cluster_id_1,  cluster_id_3,], ...}
	 */
	public HashMap<String, HashSet<String>> getDocumentToClustDict(){
		
		if(doc_id2clusters == null) buildDoc2ClustDict();
		
		return doc_id2clusters;
	}

	/**
	 * Builds the inverse dict (doc_id TO its cluster_ids)
	 */
	private void buildDoc2ClustDict(){
		doc_id2clusters = new HashMap<String, HashSet<String>>();
		
		for(String doc_id : assigned){
			
			HashSet<String> containing_clust_ids = new HashSet<String>();
			
			for(String clust_id : clust_id2cluster.keySet()){
				HashSet<String> cluster = clust_id2cluster.get(clust_id);
				
				if(cluster.contains(doc_id)){
					containing_clust_ids.add(clust_id);
				}
			}
			doc_id2clusters.put(doc_id, containing_clust_ids);	
		}
	}
	
	
	
	/**
	 * Gets the cluster ident. to documents dictionary.
	 * 
	 * @return a dictionary of {cluster_id => set of documents in that cluster, ...}
	 */
	public HashMap<String, HashSet<String>> getIdToClustDict(){
		return clust_id2cluster;
	}
	
	/**
	 * Gets the documents partition.
	 * 
	 * @return the partition
	 */
	public ArrayList<HashSet<String>> getPartition(){
		return partition;
	}
	
	/**
	 * Gets the set of discarded documents.
	 * 
	 * @return the discarded
	 */
	public HashSet<String> getDiscarded(){
		return discarded;
	}
	
	/**
	 * Gets the set of documents that have been assigned to at least one cluster.
	 * 
	 * @return the assigned
	 */
	public HashSet<String> getAssigned(){
		return assigned;
	}

	/**
	 * Parses a WePS 2007 clustering file.
	 * 
	 * @param handler the handler
	 * @param xml_path the xml_path
	 */
	private void parse(String xml_path, WePS2007Handler handler){
		Logging.getInstance().log("Parsing clustering file: "+xml_path);
		
		SAXParserFactory spf = SAXParserFactory.newInstance();

		SAXParser parser = null;
		spf.setNamespaceAware(true);
		spf.setValidating(true);

		try{
			parser = spf.newSAXParser();
			
		} catch(SAXException e){
			e.printStackTrace(System.err);
			System.exit(1);
			
		} catch(ParserConfigurationException e){
			e.printStackTrace(System.err);
			System.exit(1);
		}
		
		try{
			parser.parse(xml_path, handler);
			
		} catch(IOException e){
			e.printStackTrace(System.err);
			
		} catch(SAXException e){
			e.printStackTrace(System.err);
			
		}
	}

}
