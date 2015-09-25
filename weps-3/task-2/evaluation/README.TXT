*** WePS-3 Task-2 evaluation script ***
*** http://nlp.uned.es/weps         ***

Usage: java Task2Evaluator [goldStandardFile] [runsDirectory] [outputFile]

Mandatory arguments:

  [goldStandardFile]     Text file containing reference classification of each tweet.
                         In this file each line represents one tweet and data fields are separated by tabs.
                         Data in each column:
                         entity_name    tweet_num       tweet_id        tweet_content   label   #TRUE   #FALSE  #UNKNOWN        reconciled
                         Each data field contains:
                          'entity_name'     identifies the company or organization.
                          'tweet_num'           the number assigned to that tweet in the set of tweets retrieved for a compay or organization.
                          'tweet_id'            tweet identification number returned by Tweeter.
                          'tweet_content'   text content of the tweet.
                          'label'           assigned label: 'TRUE' if the tweet mentions the entity, 'FALSE' if it does not, 'UNKNOWN' if the anotators where unable to make a decision.
                          '#TRUE'           number of annotators that chose the TRUE label.
                          '#FALSE'          number of annotators that chose the FALSE label.
                          '#UNKNOWN'        number of annotators that chose the UNKNOWN label.
                          'reconciled'      indicates whether the label assigned to this tweet was chosen by a meta-annotator.
                         Example of one line:
                         borders        1       8357648607      Cursing myself for getting that double espresso at 4pm...       FALSE
  [runsDirectory]        Directory containing the system output file. 
                         Each run of a system should be contained in a single text file with the extension ".orm" (e.g. UNED_1.orm, UNED_2.orm, etc).
                         The file name will be used to identify that particular run in the evaluation results.
                         System output files must follow the same format described previously for the gold standard.

  [outputFile]           Evaluation results will be written in this file. 
                         This file will be overwriten by default, so be careful when choosing the file name.

Example:
  java -jar weps3-task2-evaluator.jar ../training/weps-3_task-2_training.tsv sample/runs/sample/evaluation.txt