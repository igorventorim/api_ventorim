package com.ventorim.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Chunk;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.SyntacticChunk;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/* TUTORIAL:
 * 	http://ccsl.ime.usp.br/redmine/projects/cogroo/wiki/API_CoGrOO_4x
 *  https://github.com/cogroo/cogroo4/wiki/API-CoGrOO-4
 * */

@RestController
public class CoGrOOController {

	private ComponentFactory factory = ComponentFactory.create(new Locale("pt","BR"));
	
	@RequestMapping(value="/analyze", method=RequestMethod.GET)
	public String analyze(@RequestParam(value="texto",required=true) String documentText)
	{
		
			Analyzer cogroo = factory.createPipe();
			Document document = new DocumentImpl();
			document.setText(documentText);
			
			// lets measure the time...
		    long start = System.nanoTime();
		    
		    cogroo.analyze(document);
		    
		    System.out.println("Document processed in " 
		            + ((System.nanoTime() - start) / 1000000) + "ms");
		    
	    return this.print(document); 
	}
	
	@RequestMapping(value="grammarcheck",method=RequestMethod.GET)
	public String grammarChecker(@RequestParam(value="texto",required=true) String documentText)
	{
		try 
		{	
			GrammarChecker gc = new GrammarChecker(factory.createPipe());
			CheckDocument document = new CheckDocument(documentText);
			gc.analyze(document);
			System.out.println(document.getMistakes());
			return document.getMistakesAsString();
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	/** A utility method that prints the analyzed document to the std output. */
	  private String print(Document document) {
	    StringBuilder output = new StringBuilder();
	    String searchKeyword = "";
	    List possibleSearchKeywords = new ArrayList<>();
	    String lastTag = "";
	
	    // and now we navigate the document to print its data
	    for (Sentence sentence : document.getSentences()) {
	
	      // Print the sentence. You can also get the sentence span annotation.
	      output.append("Sentence: ").append(sentence.getText()).append("\n<br>\n");
	
	      output.append("  Tokens: \n");
	
	      // for each token found...
	      for (Token token : sentence.getTokens()) {
	        String lexeme = token.getLexeme();
	        String lemmas = Arrays.toString(token.getLemmas());
	        String pos = token.getPOSTag();
	        String feat = token.getFeatures();
	
	        output.append(String.format("    %-10s %-12s %-6s %-10s\n", lexeme,
	            lemmas, pos, feat));
	      }
	
	      // we can also print the chunks, but printing it is not that easy!
	      output.append("<br>\n Chunks: ");
	      for (Chunk chunk : sentence.getChunks()) {
	        output.append("[").append(chunk.getTag()).append(": ");
	        for (Token innerToken : chunk.getTokens()) {
	          output.append(innerToken.getLexeme()).append(" ");
	        }
	        output.append("] ");
	      }
	      output.append("\n<br>\n");
	
	      // we can also print the shallow parsing results!
	      output.append("  Shallow Structure: ");
	      for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
//	    	  searchKeyword = "";
	    		
    		  output.append("[").append(structure.getTag()).append(": ");
    		  
		        for (Token innerToken : structure.getTokens())
		        {
		          output.append(innerToken.getLexeme()).append(" ");
		          if(structure.getTag().equals("ACC") || (structure.getTag().contains("P") && lastTag.equals("ACC")))
			      {
		        	  searchKeyword += innerToken.getLexeme()+" ";
			      }
		        }
//		        if(!searchKeyword.trim().equals(""))
//		        {
//		        	possibleSearchKeywords.add(searchKeyword);
//		        }
		       lastTag = structure.getTag();
		        output.append("] ");
	      }
	      output.append("\n<br>\n");
	    }
	    System.out.println(searchKeyword);
	    
	    output.append("Possible search keywords: "+searchKeyword);
//	    output.append("Possible search keywords: "+possibleSearchKeywords.toString());
	    return output.toString();
	    
	  }
	
	
	
	
}

//for (org.cogroo.text.Sentence sentence : document.getSentences()) 
//{ // lista de sentenças
//	  System.out.print(sentence.getStart()+":"+sentence.getEnd());
//	  System.out.println(sentence.getText()); // texto da sentença

//	  // Tokens
//	  for (Token token : sentence.getTokens()) { // lista de tokens
//	    System.out.println(token.getStart()+":"+ token.getEnd()); // caracteres onde o token começa e termina
//	    System.out.println(token.getLexeme()); // o texto do token
//	    System.out.println(token.getLemmas()); // um array com os possíveis lemas para o par lexeme+postag
//	    System.out.println(token.getPOSTag()); // classe morfológica de acordo com o contexto
//	    System.out.println(token.getFeatures()); // gênero, número, tempo etc
//	  }
//
//	  // Chunks
//	  for (Chunk chunk : sentence.getChunks()) { // lista de chunks
//	    System.out.println(chunk.getStart()+":"+chunk.getEnd()); // índice do token onde o chunk começa e do token onde ele termina
//	    System.out.println(chunk.getTag()); // the chunk tag
//	    System.out.println(chunk.getTokens()); // a list with the covered tokens
//	  }

	  // Structure
//	  for (SyntacticChunk structure : sentence.getSyntacticChunks()) { // lista de SyntacticChunks
//	    System.out.println(structure.getStart()+":"+structure.getEnd()); // índice do token onde o structure começa e do token onde ele termina
//	    System.out.println(structure.getTag()); // the structure tag
//	    System.out.println(structure.getTokens()); // a list with the covered tokens
//	  }