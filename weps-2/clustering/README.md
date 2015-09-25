# Web People Search Task - 2008  

## Folder contents

~~~
test
  \-- metadata           //xml files with the metadata for each web page in the search results
  \-- web_pages          //web pages downloaded for each name
  \-- gold_standard      //human clustering of the test datasets
  \-- scorer_1.2         //Source code and jar file of the WePS evaluation package
~~~

## Metadata

Each xml file contains the top 150 search results metadata for each name. 
It includes the URL, title, rank number (starting at 1) and MIME type of each document. 
Not all documents in the metadata files are part of the WePS-2 corpus. The attribute "inWepsCorpus"
on each "doc" element indicates whether the refered document is included or not. Documents not
included won't be evaluated neither in the attribute nor the clustering tasks. 

## Web Pages

The web pages directory contains all the documents downloaded from the search results list of each person name.
Documents are named according to their position in the ranking (001.html, 002.html). In many cases the list
of files skips numbers of the original ranking. This is because not all documents have been downloaded and
included in the corpus. Only html and plain text documents have been downloaded and documents not containing
the query string (the person name) where ignored too. In some cases the document couln't be downloaded or the
server was unavailable.


## WePS 2008 Evaluation Package

This program scores the performace of one or more systems 
according to several optional evaluation measures. 

~~~
USAGE: 
java -jar wepsScorer.jar [corpusDescriptionDir] [keysDir] [systemsDir] [outputDir] [MEASURES] [BASELINES] [OPTIONS]

[corpusDescriptionDir]    	 Directory containing all the XML metadata files that describe each set of documents.
 	 Files must be well formed XML, follow the WePS metadata format and filenames end in '.xml'.

[keysDir]    	 Directory containing all the gold standard for the clustering problems.
 	 Files must be well formed XML, follow the WePS clustering format and filenames end in '.xml'.

[systemsDir] 	 Directory containig all the systems solutions to evaluate using the following structure
 	 systemsDir/TEAM_A/problem1.xml
 	 systemsDir/TEAM_A/problem2.xml
 	 systemsDir/TEAM_A/...
 	 systemsDir/TEAM_B/problem1.xml
 	 systemsDir/TEAM_B/problem2.xml
 	 systemsDir/TEAM_B/...
 	 systemsDir/...


[outputDir] 	 Directory where all the results will be written

MEASURES:
 -ALLMEASURES 	 Evaluates all the available measures -P   	 Purity
 -IP  	 Inverse purity
 -FMeasure_0.5_P-IP  	 F-measure for Purity and Inverse Purity (alpha=0.5)
 -FMeasure_0.2_P-IP  	 F-measure for Purity and Inverse Purity (alpha=0.2)
 -BER 	 BCubed Recall (extended for multiclass problems)
 -BEP 	 BCubed Precision (extended for multiclass problems)
 -FMeasure_0.5_BEP-BER 	 F-measure for BCubed Precision and Recall (alpha=0.5)
 -FMeasure_0.2_BEP-BER 	 F-measure for BCubed Precision and Recall (alpha=0.2)
 -PR  	 Pairs measure using Rand Statistic
 -PJ  	 Pairs measure using Jaccard Coefficient
 -PF  	 Pairs measure using Folkes and Mallows

BASELINES:
 -AllInOne 	 
 -OneInOne 	 
 -Combined 	 

~~~