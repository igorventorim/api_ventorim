package com.ventorim.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cogroo.text.Chunk;
import org.cogroo.text.Document;
import org.cogroo.text.Sentence;
import org.cogroo.text.SyntacticChunk;
import org.cogroo.text.Token;
import org.springframework.stereotype.Service;

@Service
public class CoGrOOService {

	/** A utility method that prints the analyzed document to the std output. */
	  public String analyzeText(Document document) {
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
//	    System.out.println(searchKeyword);
	    output.append("Possible search keywords: "+searchKeyword);
//	    output.append("Possible search keywords: "+possibleSearchKeywords.toString());
	    return output.toString();
	    
	  }
	  
	  
	  
	  public String getSearchKeywordRU(Document document) 
	  {
		    String searchKeyword = "";
		    String lastTag = "";
		    
		    for (Sentence sentence : document.getSentences()) 
		    {  
		      for (SyntacticChunk structure : sentence.getSyntacticChunks()) {
	  		  
			        for (Token innerToken : structure.getTokens())
			        {
			          if(structure.getTag().equals("ACC") || (structure.getTag().contains("P") && lastTag.equals("ACC")))
				      {
			        	  searchKeyword += innerToken.getLexeme()+" ";
				      }
			        }
			       lastTag = structure.getTag();
		      }
		    }
		    return searchKeyword;		    
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
