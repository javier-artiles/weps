# Web People Search 2 Attribution Extraction (WePS2AE)

This distribution contains test data and evaluation program of Web People Search 2 Attribution Extraction (WePS2AE)

## Directories

~~~~
AE/    attribute extraction data
NO/    file IDs which 
eval/  evaluation program
check/ script to check the correctness of the test data
~~~~

The corpus data is WePS2 data (the same as the clustering task).
You can download it at http://nlp.uned.es/weps


## How to run the evaluation program

At the top directory, run the following
I assume the system output data located at "System", and the evaluation
output will be created at "Eval_out".

~~~~
eval/eval_weps2ae.pl . System Eval_out
~~~~

Then open Eval_out/result.html using a browser.


## Versions
version 1.0: January 5, 2008  Initial version


## Author
Satoshi Sekine (New York University) 
sekine@cs.nyu.edu
