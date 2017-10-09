package pers.abaneo.xnote.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class DefaultController extends BaseController{
	
	@RequestMapping("")
	public String go(){
		return "redirect:/blog/index.html";
	}

}
