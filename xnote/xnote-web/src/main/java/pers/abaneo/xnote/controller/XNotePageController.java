package pers.abaneo.xnote.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.service.IXNoteServie;

@Controller
@RequestMapping("xnote")
public class XNotePageController {
	@Autowired
	IXNoteServie servie;
	
	@RequestMapping("")
	public String index(Map<String, Object> map){
		return "xnote/index";
	}
	
	@RequestMapping("mkedit")
	public String mkedit(Map<String, Object> map){
		return "xnote/mkedit";
	} 
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id,Map<String, Object> map) {
		XNote xnote = servie.getXnote(id);
		map.put("xnote", xnote);
		return "xnote/view";
	}
}
