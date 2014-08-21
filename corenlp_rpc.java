package stanford_rpc;

import java.io.*;
import java.util.*;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;
import helma.xmlrpc.*;

import java.util.*;
import java.io.IOException;

import org.apache.xerces.parsers.SAXParser;

public class corenlp_rpc {

	public StanfordCoreNLP pipeline = new StanfordCoreNLP();

	public String parser_func(String senti_sent) {

		Annotation annotation;
		PrintWriter out = new PrintWriter(System.out);

		annotation = new Annotation(senti_sent);

		pipeline.annotate(annotation);

		String corefset = "";
		// Here calculate the co-reference set is calculated
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);
		Map<Integer, CorefChain> corefChains = annotation
				.get(CorefCoreAnnotations.CorefChainAnnotation.class);
		if (corefChains != null && sentences != null) {
			List<List<CoreLabel>> sents = new ArrayList<List<CoreLabel>>();
			for (CoreMap sentence : sentences) {
				List<CoreLabel> tokens = sentence
						.get(CoreAnnotations.TokensAnnotation.class);
				sents.add(tokens);
			}

			for (CorefChain chain : corefChains.values()) {
				CorefChain.CorefMention representative = chain
						.getRepresentativeMention();
				boolean outputHeading = false;
				for (CorefChain.CorefMention mention : chain
						.getMentionsInTextualOrder()) {
					if (mention == representative)
						continue;

					// all offsets start at 1!
					corefset = "\t(" + mention.sentNum + ","
							+ mention.headIndex + ",[" + mention.startIndex
							+ "," + mention.endIndex + "]) -> ("
							+ representative.sentNum + ","
							+ representative.headIndex + ",["
							+ representative.startIndex + ","
							+ representative.endIndex + "]), that is: \""
							+ mention.mentionSpan + "\" -> \""
							+ representative.mentionSpan + "\"";
				}
			}
		}
		String new_sent = "";
		if (corefset.length() > 5) {
			String[] s2 = corefset.split("that is:", 2);
			String[] s3 = s2[1].split("->", 2);
			String s4 = s3[0].replace('"', ' ').trim();
			String s5 = s3[1].replace('"', ' ').trim();
			System.out.println(Integer.toString(sentences.size()));
			System.out.println(sentences.size());
			// Making sure the pronoun is not a part of word(s)
			new_sent = senti_sent.replaceAll("([^\\w]("+s4+")[^\\w])", " " + s5 + " ");
		} 
		else {
			new_sent = senti_sent; // In case no coreference is found
		}
		annotation = new Annotation(new_sent);
	    pipeline.annotate(annotation);    
	    sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		
		// End of co-reference set calculation
		String my_string = "";

		if (sentences != null && sentences.size() > 0) {
			for (int i = 0; i < sentences.size(); ++i) {

				ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(i);

				Tree tree = sentence
						.get(TreeCoreAnnotations.TreeAnnotation.class);

				for (CoreMap token : sentence
						.get(CoreAnnotations.TokensAnnotation.class)) {
					ArrayCoreMap aToken = (ArrayCoreMap) token;
					// out.println(aToken.toShorterString());
				}
				my_string = my_string + tree.toString() + "##"; // ## is the
																// separator
																// between
																// individual
																// parsetrees
			}
		}
		return my_string;

	}

	public static void main(String[] args) {
		try {
			System.out.println("You are about to start the Stanford RPC server.\n"); 
			System.out.println("Type the port number on which you want to start, e.g. 8086\n"); 
			Scanner terminalInput = new Scanner(System.in);
			String s8 = terminalInput.nextLine();
			terminalInput.close();
			try{
				Integer.parseInt(s8);
			}
			catch(Exception e){
				System.out.println("You have entered invalid number. Exiting now.\n");
				System.exit(0);
			}
			WebServer server = new WebServer(Integer.parseInt(s8));
			server.addHandler("stan_nlp", new corenlp_rpc());
			server.start();
			System.out.println("Started XML-RPC Server...");
		} catch (IOException e) {
			// 
			System.out.println("The port number is already in use.\t Try again.\n");
			
		}
	}

}
