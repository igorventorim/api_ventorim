package com.ventorim.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@RequestMapping(value="/")
	public String home()
	{
		return "<h1>Welcome to API Ventorim, this API provide NLP services.</h1>";
	}
	
}
