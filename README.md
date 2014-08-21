NLP-Sentiment-Analysis-Turney-Algorithm-and-Sentinet
====================================================

This implementation of sentiment analysis using Turney Algortihm and Sentinet is done via Java(80%) and Python(20%).
It uses Stanford CoreNLP system for parsing. You have to download the system from http://nlp.stanford.edu/software/corenlp.shtml
We need all the Stanford coreNLP jar files to run the Java RPC server.

All the following steps are tested in Ubuntu 12.04. You are advised to run the .java files from a Java IDE (e.g. Eclipse/Netbeans). 


SYSTEM REQUIREMENTS:
...................

1. Ubuntu 12.04 or above, Debian, ...
2. Java 6 or above
3. Python 2.7
4. Stanford CoreNLP jar files
5. pyvirtualdisplay, xvfb, selenium
6. Very high speed internet
7. Firefox
8. jar files from the "jar-libraries.zip"


STEP 1: CREATION OF DATABASE
----------------------------

Get into the database folder and follow the steps mentioned in the "readme.dat" file inside the database folder. This will
create the necessary database and table.

STEP 2: START THE Python-Selenium RPC server
--------------------------------------------

First, make sure you have the following packages to run selenium in headless mode.

a) Selenium
b) Pyvirtualdisplay
c) Xvfb

Or, run the following commands in terminal

sudo apt-get install xvfb
sudo pip install pyvirtualdisplay, selenium

And now run the "python-selenium-rpc.py" file. It will start the server.

STEP 3: START THE Stanford-NLP-RPC server
-----------------------------------------

First of all, extract the "jar-libraries.zip" in your system. Then open the "corenlp_rpc.java" file (can be in Eclipse/Netbeans),
and attach (refer) all the stanford corenlp jar files, as well as all the jar files from the extracted folder. Then run the "corenlp_rpc.java" file.


FINAL STEP: RUN THE SENTIMENT ANALYZER
--------------------------------------

If you have completed all the above steps successfully, you can now get into this step. Run the "Call_java_rpc.java" file. Attach (Refer) all the
.jar files from the extracted folder (not Stanford NLP jars) to the above Java file.



---------------------NLP SYSTEM BRIEFING------------------------

The Turney algorithm is based upon the following,

Polarity(phrase) = Log[hits(phrase NEAR "EXCELLENT")*hits("POOR")/hits(phrase NEAR "POOR")*hits("EXCELLENT")]

The above log is in base 2. You are very strongly advised to experiment with the "Excellent" and "Poor" phrase to judge the polarity. You can change to "good description"/"favourable description"... instead of "excellent" and so forth. This experimentation has to be incorporated in the "python-selenium-rpc.py" file as this file talks to Google and get the corresponding hits.

We pick all phrases which match to the following:

1) JJ, NN/NNS
2) RB/RBR/RBS, JJ, NOT NN/NNS
3) JJ, JJ, NOT NN/NNS
4) RB/RBR/RBS, VB/VBD/VBN/VBG, NOT NN/NNS

Regular expressions are employed to pick phrases from the parsed string. You are advised to tweak the phrase picking or add more in the "Call_java_rpc.java". The phrase picking starts from line #94 in the above java file.

Now the database sentiment is done via following. In the picked phrases picked via above rules,

1) stronger words get higher sentiment values
2) if the sentiment of the 2nd word is of other sign than the preceding word, then the overall sentiment value is zero


-------------------------------------------------------------------

This program is under GPL license. For help -->  parai.a@gmail.com

My website: https://www.invlab.in

-------------------------------------------------------------------




















