=======================================
=== Web People Search Task - 2010   ===
===                                 ===
=== http://nlp.uned.es/weps         ===
===                                 ===
=== Task-2 trial data               ===
=======================================


This folder contains the trial data for Task 2 (On-line Reputation Management) in WePS-3. 

1. Meta-data

Each document in the sub-folder "meta_data" contains information about 300 Twitter entries. This information includes: 

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


2. Annotation data

The file data_Weps3_Task2_Trial.txt contains the data annotated for trial purposes. It includes 100 annotated entries for each organisation name. Each line in the file contains the following tab separated fields:

- organisation name
- entry number [1..100]
- Original identifier in Twitter.
- Text content.
- Annotation [True, False or Unknown]

Any tweet in which the query term is related with the organisation is annotated as True. Entries are annotated as false when it is not possible to disambiguate the query term.


3. Organisation selection

This corpus contains entries for 17 (English) and 6 (Spanish) organisations. The organisation for English entries have been selected from the Twitter list "public companies" (http://listorious.com/irwebreport/public-companies). The Spanish organisations has been selected arbitrarily.

The file organization_features.txt contains the information about all organisations separated by tabulators, including:

- The organisation name
- The query for retrieving tweets (which corresponds with the organisation identifier in the corpus).
- Organisation category.
- Organisation URL.
- Meta data file name.
- Language


4. Task-2 guidelines:

http://nlp.uned.es/weps/weps-3/guidelines/40-guidelines-for-the-weps-3-on-line-reputation-management-task

