NLP-Project
===========

Phase 1 - Semantic Similarity
- the semantic relationship of two words will be extracted from a semantic 
database (yago or wordnet). This tree will be used to compute the similarity 
of words based on the length and height in the tree. Two vectors will be created
from looking at the similarity of each word to other words in the tree. These results
will be shown in two tables.


Phase 2 - Vector Reversal
- the word vectors will be reversed if they contain a keyword that denotes the logic
of the sentence: "the man is old" and "the man is not old"

Phase 3 - Mapping Output
- In addition to the two tables, a diagram will be created to show the semantic 
similarity from phase 1. This will use the results from yago to map a word to the node
in the tree structure and show the similarity based on relational words.

This will look like this:

The man ran to the university. 	vs	The boy walked to school.

 
 The 		- 	SAME							- The
 man 		-   {male, boy, child}  			- boy
 ran 		-   {ran, dashed, walked, skipped}  - walked
 to  		-	SAME							- to
 the 		-   NA
 university -	{university, college, school}	- school
 
 
 **************************** Comments from after presentation ************************************
 - change Phase 2 to be first and Phase 1 to be second
 
 - compare our top phrases from each sentence with the score for the whole sentence similarity
 
 - create a group of sentences (maybe around 10) and list whether they are similar or not in our own 
 opinion (human evaluation) and compare to see if the sentence similarity is closer or our phrase comparison
 is closer. We will want to say why we think sentences are similar too: sentence structure, similar meaning, etc.