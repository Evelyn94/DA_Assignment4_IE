/** An Interface to OPENNLP Part of Speech Tagging
 * 
 *  See main() for an example.
 *  
 *  @author Kimi Sun (yuesun@nicta.com.au)	
 *  @author Scott Sanner (ssanner@nicta.com.au)
 */

package opennlp;

import java.io.*;

import opennlp.tools.lang.english.Tokenizer;
import opennlp.tools.postag.TagDictionary;
import opennlp.tools.sentdetect.SentenceDetectorME;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.postag.POSTaggerME;

public class POSTagger {
	
	SentenceDetectorME _sdetector;
	Tokenizer          _tokenizer;
	POSTaggerME        _tagger;

	// Constructor
	public POSTagger() throws IOException {

		// Load models for Sentence Detector
		System.out.println("Loading models for Sentence Detector...");
		_sdetector = new SharedSentenceDetector(
				"./models/sentdetect/EnglishSD.bin.gz");

		// Load models for Tokenizer
		System.out.println("Loading models for Tokenizer...");
		_tokenizer = new Tokenizer(
				"./models/tokenize/EnglishTok.bin.gz");

		// Load models for POS tagging
		System.out.println("Loading models for POS Tagging...");
		_tagger = new SharedPOSTagger(
				"./models/postag/SpanishPOS.bin.gz", (Dictionary) null);

		//_tagger = new SharedPOSTagger(
				//"./models/postag/classifier.bin.gz", (Dictionary) null);
	}

	// Return value for POSTagging
	public static class POSTagging {
		public POSTagging(String[][] tokens, String[][][] taggings) {
			_tokens   = tokens;
			_taggings = taggings;
		}
		public String[/*sent*/][/*word*/]          _tokens;
		public String[/*sent*/][/*tag*/][/*word*/] _taggings;
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int si = 0; si < _taggings.length; si++) {
				//sb.append("Sentence #" + si + " [" + _tokens[si].length + "]: ");
				for (int ti = 0; ti < _taggings[si].length; ti++) {
					//sb.append("\n- Tagging #" + ti + ": ");
					for (int wi = 0; wi < _taggings[si][ti].length; wi++) {
						sb.append(_tokens[si][wi] + "\t");
						sb.append(_taggings[si][ti][wi] + "\n");
					}
				}
				//sb.append("\n\n");
			}
			return sb.toString();
		}
	}
	
	// Main method to process a String of text and return a POS
	// tagging for each word of each sentence in the text.
	public POSTagging process(String para, int num_tags) {

		// Extract sentences
		String[] sents = _sdetector.sentDetect(para.toString());

		// Extract tokens
		String[][] tokens = new String[sents.length][];
		for (int n = 0; n < sents.length; n++) 
			tokens[n] = _tokenizer.tokenize(sents[n]);
		
		// Perform POS tagging
		String[][][] taggings = new String[sents.length][][];
		for (int sent_index = 0; sent_index < tokens.length; sent_index++) {
			taggings[sent_index] = 
				_tagger.tag(num_tags, tokens[sent_index]);
		}                
		return new POSTagging(tokens, taggings);
	}	
	
	//////////////////////////////////////////////////////////////////
	//                              Tests
	//////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws IOException {    
            
		POSTagger tagger = new POSTagger();
		FileReader in = null;
		FileWriter out = null;
		String s = "";

		try{
			in = new FileReader("./testSet.txt");
			int c;
			while ((c = in.read()) != -1){
				s = s + (char) c;
			}

		}finally {
			if(in != null){
				in.close();
			}
		}

		//System.out.println(s);

		//String sentence = "El gato come pescado.  Hoy es jueves";
		System.out.println("----------------------\n");
		POSTagging process = tagger.process(s, 1); // 3 best taggings
		//System.out.print(process);
		try{
			out = new FileWriter("./testTag.txt");
			String op = process.toString();
			out.write(op);
		}finally {
			if(out != null){
				out.close();
			}
		}
		System.out.println("----------------------\n");

	}
}
