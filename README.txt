Name: Kiran Bhat Gopalakrishna
NetId: kxb140230

*************************************************************************************************
To Compile:

javac Information_Retrieval_Hw1.java

*************************************************************************************************

To Run:

java Information_Retrieval_Hw1.java <path to cranfield director>

*************************************************************************************************

Programm Description:

Program Description :

This Programm gathers information about tokens in the cranfield database.SGML tags are  not considered words.
Information inside the tags are considered as words.


1. The program gets all the text characteristics in about  4 seconds

2. Program handling:
Programm replaces all the NON alpha numeric characters with " "(space) and seperates words based on the " "(space). However if it encounters . it replaces the word with ""(nothing).
A. The program handles upper and lower case letters to be the same. Ex: People, PEOPLE, People, PEoplE are all same. 
B.  Words with "-" are seperated into two words based on hyphen. Ex:middle-class is treated as 2 words 'middle' and 'class'.
C. Possessives divide the word into two halves. Ex: university's is treated as two words 'university' and 's'
D. The acronyms are treated as 1 word. Ex: U.S is treated as 'us'

3. Datastructures  & Algorithms used: 

A) Hash Map is used to store all the tokens and stemmed tokens.
B) An ArraList is used for computing distinct words in each file of cranfield directory. 
C) Stemmer algorithm to stem all the words.

****************************************************************************************************