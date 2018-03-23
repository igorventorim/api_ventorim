package com.ventorim.api;

import java.io.IOException;
import java.util.Locale;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Document;
import org.cogroo.text.impl.DocumentImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/* TUTORIAL:
 * 	http://ccsl.ime.usp.br/redmine/projects/cogroo/wiki/API_CoGrOO_4x
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
		    
		    System.out.println(document);
		
	    return "ok";
	}
	
	@RequestMapping(value="grammarcheck",method=RequestMethod.GET)
	public void grammarChecker(@RequestParam(value="texto",required=true) String documentText)
	{
		try 
		{	
			GrammarChecker gc = new GrammarChecker(factory.createPipe());
			CheckDocument document = new CheckDocument(documentText);
			gc.analyze(document);
			
			System.out.println(document);
			
		} catch (IllegalArgumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
