=======================================
=== Web People Search Task - 2010   ===
===                                 ===
=== http://nlp.uned.es/weps         ===
===                                 ===
=== Task-1 trial data               ===
=======================================


The trial data for Task-1 of WePS-3 is based on the WePS-2 clustering and attribute extraction test datasets.
The original web pages and the corresponding metadata (snippet, search ranking information, etc.) are 
available in http://nlp.uned.es/weps/weps2/WePS2_test_data.zip


1. Clustering

In the case of the Clustering task the output format remains as in WePS-2 (a "clustering" root element and
"entity" elements for each cluster of documents), except for a slight change in the XML format: now "doc" elements associated 
to a person are enclosed in a "documents" element, as in the following example:

<entity id="1">
  <documents>
    <doc rank="99" />
    <doc rank="104" />
  </documents>
</entity>

The directory "weps-3/data/task-1/trial/only_clustering/" contains all the gold-standard files in this XML format and
is intended for teams participating only in the Clustering task.


2. Attribute Extraction

The WePS-3 Attribute Extraction (AE) task is different from WePS-2 in that systems are requested to relate each
attribute to a person (cluster of documents) instead of just listing the attributes obtained from each document.
This is the reason why participants in the AE task are required to participate in the Clustering task too.
Systems are expected to output no more than one attribute of each type in each cluster of documents (i.e. only
one affiliation, only one occupation, etc. for each person).
To reflect these changes we have combined the WePS-2 AE and Clustering gold standards, as in the following example:

<entity id="2">
  <documents>
    <doc rank="22" />
    <doc rank="29" />
  </documents>
  <attributes>
    <attr type="location" source="22">West Bend</attr>
    <attr type="occupation" source="22">Full time students</attr>
    <attr type="affiliation" source="22">University of Wisconsin-Washington County</attr>
    <attr type="school" source="29">UW-Washington County</attr>
    <attr type="degree" source="29">Associate Degrees</attr>
  </attributes>
</entity>


The directory "weps-3/data/task-1/trial/full_ae_annotation/" contains the document clustering and all the attributes related
to the documents in each cluster, even if the same attribute type is repeated several times in the cluster. These
files will be useful for developing AE systems for the task, but do not represent an example of the expected output.
The reason for that is that only one attribute of each type is expected for each cluster. The files under the
directory "weps-3/data/task-1/trial/sample_ae_run/" do satisfy this constraint and are an example of the expect output.


3. Task-1 guidelines:

Person Name Disambiguation Task (main)
http://nlp.uned.es/weps/weps-3/guidelines/41-guidelines-for-the-weps-3-person-name-disambiguation-task

Attribute Extraction Subtask (optional)
http://nlp.uned.es/weps/weps-3/guidelines/42-guidelines-for-the-weps-3-attribute-extraction-subtask

