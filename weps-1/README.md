# Web People Search Task - 2007]

## Introduction.

The Web People Search Task took place as part of the 4th Workshop on
Semantic Evaluations (Semeval 2007).
In the following sections we describe briefly the task, the training
and test data and the scoring program provided.

## Folder contents

~~~
training
  \-- web_pages          //raw web pages downloaded for each name
  \-- description_files  //xml files with the list of documents for each name.
  \-- truth_files        //human clustering of the documents in each name set.

test
  \-- web_pages          //raw web pages downloaded for each name
  \-- description_files  //xml files with the list of documents for each name.
  \-- truth_files        //human clustering of the documents in each name set.
       \-- annotation_1  //data produced by the annotation team 1
       \-- annotation_2  //data produced by the team 2
       \-- official_annotation //combination of files from team 1 and 2 used for the participants evaluation

scorer                   //Documentation, source and jar files of the evaluation package

weps_task_description.pdf//Task description paper
~~~

## WePS 2007 task definition.

This task focuses on the disambiguation of person names in a Web
searching scenario. Finding people, information about people, in the
World Wide Web is one of the most common activities of Internet
users. Person names, however, are highly ambiguous. In most cases,
therefore, the results for this type of search are a mixture of pages
about different people that share the same name.

The participant's systems will receive as input, web pages retrieved
from a web search engine using a given person name as a query. The aim
of the task is to determine how many referents (different people)
exist for that person name, and assign to each referent its
corresponding documents. The challenge is to correctly estimate the
number of referents and group documents referring to the same
individual.

## Training data.

The training data is composed of sets of up to 100 web pages
corresponding to the results from a web search engine (Yahoo, via its
search API) for a person name query.

The names were sampled from a list of biographies in the Wikipedia and
from the list of participants in the European Conference in Digital
Libraries.  Additionally we have included the Web03 corpus, which
is composed of names sampled from the US Census (for more details,
Gideon S. Mann, "Multidocument Statistical Fact Extraction and Fusion",
2006, Johns Hopkins University).

With this data we expect to cover at least two frequently
occurring ambiguity scenarios: very common names that have a high
ambiguity on the web, and names of famous or historical people, which
might monopolize most of the documents in the web search results. With
the ECDL names we also provide a subset of the corpus in which there
is at least one namesake from a specific domain (in this case,
researchers in digital libraries).

Each name set has a "person_name.xml" file with information about
ranking, URL, title and snippet for each retrieved web page. All the
pages have been downloaded and stored in directories named according
to their ranking in the search results.


- Wikipedia person names

  - "John Kennedy"
  - "George Clinton"
  - "Paul Collins"
  - "Michael Howard"
  - "Tony Abbott"
  - "David Lodge"
  - "Alexander Macomb"

- ECDL person names

  - "Allan Hanbury"
  - "Edward Fox"
  - "Andrew Powell"
  - "Donna Harman"
  - "Gregory Crane"
  - "Jane Hunter"
  - "Paul Clough"
  - "Anita Coleman"
  - "Thomas Baker"
  - "Christine Borgman"

- Gideon Mann's corpus (US Census names)

   - "Abby Watkins"
   - "Armando Valencia"
   - "Cynthia Voigt"
   - "Gregory Brennan"
   - "Helen Cawthorne"
   - "Maile Doyle"
   - "Pam Tetu"
   - "Sidney Shorter"
   - "Young Dawkins"
   - "Alexander Markham"
   - "Cathie Ely"
   - "Dan Rhone"
   - "Guy Crider"
   - "Ione Westover"
   - "Martin Nagel"
   - "Patrick Karlsson"
   - "Stacey Doughty"
   - "Alfred Schroeder"
   - "Celeste Paquette"
   - "Elmo Hardy"
   - "Guy Dunbar"
   - "Louis Sidoti"
   - "Mary Lemanski"
   - "Tim Whisler"
   - "Alice Gilbreath"
   - "Charlotte Bergeron"
   - "Gillian Symons"
   - "Hannah Bassham"
   - "Luke Choi"
   - "Miranda Bollinger"
   - "Roy Tamashiro"
   - "Todd Platts"


## Test data

The test data for the task is composed, as in the training data, of
sets of up to 100 webpages corresponding with the results in a web
search engine (Yahoo, via its search API) for a person name query.
Each set has a separate XML file with information about ranking, URL,
title and snippet for each retrieved webpage.  All the pages have been
downloaded and stored in directories named according to their ranking
in the search results. The names where randomly extracted from the
Wikipedia, ACL 2006 Conference and US Census data.

We provide a double annotation of the test data by different
annotation teams (five human annotators in total). Each name set was
annotated by one person.
During the official evaluation period we only had access to one
annotation of the whole test data. This annotation was composed
of the combination of name set annotations from both teams.
The corresponding truth files have been provided in a separate
folder for ease of use.

- Wikipedia person names

   - "Arthur Morgan"
   - "James Morehead"
   - "James Davidson"
   - "Patrick Killen"
   - "William Dickson"
   - "George Foster"
   - "James Hamilton"
   - "John Nelson"
   - "Thomas Fraser"
   - "Thomas Kirk"

- ACL06 person names

   - "Chris Brockett"
   - "Dekang Lin"
   - "James Curran"
   - "Mark Johnson"
   - "Jerry Hobbs"
   - "Frank Keller"
   - "Leon Barrett"
   - "Robert Moore"
   - "Sharon Goldwater"
   - "Stephan Johnson"

- US Census names

   - "Alvin Cooper"
   - "Harry Hughes"
   - "Jonathan Brooks"
   - "Jude Brown"
   - "Karen Peterson"
   - "Marcy Jackson"
   - "Martha Edwards"
   - "Neil Clark"
   - "Stephen Clark"
   - "Violet Howard"


## Gold standard

The gold standard for each person-name document set is named
"person_name.clust.xml".  It contains a root element "<clustering>"
followed by one "entity" element for each entity.  The entity element
has an identifier attribute ("id") with an integer value. Nested in
the "entity" element there are "doc" elements (pages that refer to
this particular entity), each of which has a "rank" attribute that
corresponds to the ranking information provided in the xml file
described above.  Note that a document might have been clustered in
more than one entity. This is the case when multiple person names
referring to different entities appear in a single document.  Also,
note that a person name may have a namesake that is not a person (for
instance an organization or a location). In those cases the non-person
entity will have its own cluster.  Finally, when the annotator could
not cluster a page it was included under a "discarded" element.  The
reasons for this might be the non-occurrence of the person name in the
page (probably because Yahoo index had outdated information when the
corpus was built) or simply that the human annotator could not decide
whether to cluster that page. Discarded pages are not taken into
account for the evaluation.

Here is an example of what the gold standard files looks like:

~~~
<clustering>
   <entity id="0">
       <doc rank="0"/>
       <doc rank="5"/>
   </entity>
   <entity id="1">
       <doc rank="1"/>
       <doc rank="3"/>
   &nbsp;   <doc rank="5"/>
       <doc rank="10"/>
   </entity>
   ...
   <discarded>
       <doc rank="8"/>
       <doc rank="9"/>
   </discarded>
</clustering>
~~~

Note that empty lines are permitted anywhere in the file. Space and
tab does not have special meaning anything in the file.

Some files that appear in the list of downloaded documents migth
contain no text, probably because it was not possible to download
them. Those pages where not clustered by the human annotators and
should not appear in the "clust.xml" files.

## WePS 2007 Scoring Package 1.1.

* version 1.1 solves an bug in the results output that was giving same value for PURITY_F05 and BCUBED_05

We include the jar file with the source code and a basic documentation.

This program scores the performance of one or more systems
according to several optional evaluation measures.

### Usage
~~~
java SystemScorer [keysDir] [systemsDir] [outputDir] [MEASURES] [BASELINES] [OPTIONS]

[keysDir]    	 Directory containing all the gold standard for the clustering problems.
 	 Files must be well formed XML, follow the WePS 2007 clustering format and filenames end in 'clust.xml'.

[systemsDir] 	 Directory containig all the systems solutions to evaluate using the following structure
 	 systemsDir/TEAM_A/problem1.clust.xml
 	 systemsDir/TEAM_A/problem2.clust.xml
 	 systemsDir/TEAM_A/...
 	 systemsDir/TEAM_B/problem1.clust.xml
 	 systemsDir/TEAM_B/problem2.clust.xml
 	 systemsDir/TEAM_B/...
 	 systemsDir/...


[outputDir] 	 Directory where all the results will be written

MEASURES:
 -ALLMEASURES 	 Evaluates all the available measures -P   	 Purity
 -IP  	 Inverse purity
 -FMeasure_0.5_P-IP  	 F-measure for Purity and Inverse Purity (alpha=0.5)
 -BER 	 BCubed Recall (extended for multiclass problems)
 -BEP 	 BCubed Precision (extended for multiclass problems)
 -FMeasure_0.5_BER-BEP 	 F-measure for BCubed Precision and Recall (alpha=0.5)
 -PR  	 Pairs measure using Rand Statistic
 -PJ  	 Pairs measure using Jaccard Coefficient
 -PF  	 Pairs measure using Folkes and Mallows

BASELINES:
 -AllInOne 	 
 -OneInOne 	 
 -Combined 	 

OPTIONS:
 -overwrite 	 overwrites previous evaluation files (.eval) if necessary.
 -average   	 prints the averaged scores for all the teams
~~~

Example using the official annotation set as key and also as a team, evaluating baseline answers:

~~~
$ /usr/lib/jvm/java-6-sun-1.6.0.03/bin/java -cp distributions/1.1/wepsEvaluation.jar es.nlp.uned.weps.evaluation.SystemScorer weps07test/truth_official/ weps07test/test_system/  tmp -ALLMEASURES -AllInOne -OneInOne -Combined -average 
WePS 2007 Evaluation Package (http://nlp.uned.es/weps)

Key clustering files path:    weps07test/truth_official
Answer clustering files path: weps07test/test_system
Output evaluation files path: tmp
Measures:                     [P, IP, FMeasure_0.5_P-IP, BEP, BER, FMeasure_0.5_BEP-BER, PM, PJ, PR, ]
Baselines:                    [COMBINED_BASELINE, ONE_IN_ONE_BASELINE, ALL_IN_ONE_BASELINE]
Overwrite:                    false

Evaluating clustering answers (team truth_official) from weps07test/test_system/truth_official
Saving team evaluation to: tmp/truth_official.eval

Evaluating clustering answers (baseline COMBINED_BASELINE)
Saving team evaluation to: tmp/COMBINED_BASELINE.eval

Evaluating clustering answers (baseline ONE_IN_ONE_BASELINE)
Saving team evaluation to: tmp/ONE_IN_ONE_BASELINE.eval

Evaluating clustering answers (baseline ALL_IN_ONE_BASELINE)
Saving team evaluation to: tmp/ALL_IN_ONE_BASELINE.eval


topic                   BEP     BER     FMeasure_0.5_BEP-BER    FMeasure_0.5_P-IP       IP      P       PJ      PM      PR

truth_official 		1,0     1,0     1,0     1,0     1,0     1,0     1,0     1,0     1,0
ONE_IN_ONE_BASELINE     1,0     0,43    0,57    0,61    0,47    1,0     0,0     1,0     0,83
COMBINED_BASELINE       0,17    0,99    0,24    0,78    1,0     0,64    0,17    0,34    0,17
ALL_IN_ONE_BASELINE     0,18    0,98    0,25    0,4     1,0     0,29    0,17    0,34    0,17
~~~

## System's output

Participants are expected to provide an output clustering for each
person name set in the test set.  The format of this output should be
the same as the Gold Standard format described above.  The data output
for each person name set should be created in one separate file. The
file name should be the person name (blanks replaced by "_") with the
".clust.xml" extension.

