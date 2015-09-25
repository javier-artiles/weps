*** WePS-3 Task-2 test data     ***
*** http://nlp.uned.es/weps     ***

This directory contains:

"weps-3_task-2_test.tsv" --> The test data for Task-2 of the WePS-3 evaluation.

In this file each line represents one tweet and data fields are separated by tabs.
Data in each column:
entity_name    tweet_num       tweet_id        tweet_content
Each data field contains:
'entity_name'     identifies the company or organization.
'tweet_num'       the number assigned to that tweet in the set of tweets retrieved for a compay or organization.
'tweet_id'        tweet identification number returned by Tweeter.
'tweet_content'   text content of the tweet.

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


Expected output:

Given this set of Twitter entries containing an (ambiguous) company name, and given the home page of the company, systems should discriminate entries that do not refer the company. Systems must classify each tweet as TRUE (it refers to the company) or FALSE (it refers to something else).
The system output should be contained in a single tab-separated file. If the team is submitting output for multiple runs each one should be contained in a separate file. The file should be named with the team ID and a numeric suffix in the case of multiple runs (e.g. UNED_1.tsv, UNED_2.tsv, etc). Each line represents a classified tweet and has the following columns: entity name (the name used in the file "weps-3_task-2_test.tsv"), tweet identifier and the assigned label (either TRUE or FALSE).

For example:

yamaha	12465638093	TRUE
yamaha	12448811836	FALSE
lufthansa	12465757672	TRUE

Submission:

Each can submit their results until June 21st to the address weps-organizers@lsi.uned.es
Please include in the subject of your email your team ID and the words "WePS-3 Task-2 submission".



** For more information regarding Task-2, please refer to the guidelines: 
   http://nlp.uned.es/weps/weps-3/guidelines/40-guidelines-for-the-weps-3-on-line-reputation-management-task
