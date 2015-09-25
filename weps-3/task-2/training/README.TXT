*** WePS-3 Task-2 training data     ***
*** http://nlp.uned.es/weps         ***

This directory contains:

"weps-3_task-2_training.tsv" --> The training data for Task-2 of the WePS-3 evaluation.

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


"training-stats.txt" --> Table with the number of tweets for each entity and other useful information.

"short2long_url_table.tsv" --> Table that maps short URLs appearing in the tweets to the original URLs.

"metadata" --> This subdiretory contains information about the Twitter entries. This information includes: 

- The original identifier (returned by Twitter)
- The creation date.
- The tweet text.
- Language identifier.
- Information about the author: Twitter user id, Twitter user name, number of user followers and Image.
- Source: application used by the user for post the tweet.

This is an example of a Twitter entry:

"id" : "9133687244",
"createdAt" : "Mon Feb 15 10:05:18 CET 2010",
"text" : "ashley tisdale En El Hormiguero. http://bit.ly/auppjZ",
"isoLanguageCode" : "es",
"fromUserId" : "61563563",
"fromUser" : "ashtisdalfan",
"fromUserFollowers"0", 
"toUserId" : "-1",
"toUser" : "null",
"source" : "&lt;a href=&quot;http://twitterfeed.com&quot; rel=&quot;nofollow&quot;&gt;twitterfeed&lt;/a&gt;",
"profileImageUrl" : "http://s.twimg.com/a/1265999168/images/default_profile_0_normal.png"

The fields "toUserId" and "toUser" are not relevant in our context. They are relevant only when the Tweet is addressed to a given user.


** For more information regarding Task-2, please refer to the guidelines: http://nlp.uned.es/weps/weps-3/guidelines/40-guidelines-for-the-weps-3-on-line-reputation-management-task