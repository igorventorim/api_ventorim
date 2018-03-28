package com.ventorim.api;

import java.io.IOException;
import java.util.Locale;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Document;
import org.cogroo.text.impl.DocumentImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private CoGrOOService cogrooService;
	
	private ComponentFactory factory = ComponentFactory.create(new Locale("pt","BR"));
	Analyzer cogroo = factory.createPipe();
	
	@RequestMapping(value="/analyze", method=RequestMethod.GET)
	public String analyze(@RequestParam(value="texto",required=true) String documentText)
	{
			Document document = new DocumentImpl();
			document.setText(documentText);
			
			// lets measure the time...
		    long start = System.nanoTime();
		    
		    this.cogroo.analyze(document);
		    
		    System.out.println("Document processed in " 
		            + ((System.nanoTime() - start) / 1000000) + "ms");
		    
	    return cogrooService.analyzeText(document); 
	}
	
	@RequestMapping(value="/extractSKru", method=RequestMethod.POST, produces = "application/json")
	public String extractSearchKeyword(@RequestParam(value="texto",required=true) String documentText)
	{
			Document document = new DocumentImpl();
			document.setText(documentText);		    
			this.cogroo.analyze(document);
	    return cogrooService.getSearchKeywordRU(document); 
	}
	
	
	@RequestMapping(value="grammarcheck",method=RequestMethod.GET)
	public String grammarChecker(@RequestParam(value="texto",required=true) String documentText)
	{
		try 
		{	
			GrammarChecker gc = new GrammarChecker(this.cogroo);
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
	
	
}
