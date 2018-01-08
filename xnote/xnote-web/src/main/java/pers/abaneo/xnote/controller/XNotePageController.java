package pers.abaneo.xnote.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pers.abaneo.xnote.api.model.user.User;
import pers.abaneo.xnote.api.model.xnote.XNote;
import pers.abaneo.xnote.api.service.IUserService;
import pers.abaneo.xnote.api.service.IXNoteServie;
import pers.abaneo.xnote.service.xnote.UserService;

@Controller
@RequestMapping("xnote")
public class XNotePageController {
	@Autowired
	IXNoteServie servie;
	
	@Autowired
	IUserService uservice;
	
	@RequestMapping("")
	public String index(Map<String, Object> map){
		return "xnote/index";
	}
	
	@RequestMapping({"mkedit/{id}"})
	public String mkedit(@PathVariable Long id,Long groupId,Map<String, Object> map){
		if(id!=null){
			XNote xnote = servie.getXnote(id);
			map.put("xnote",xnote);
		}
		return "xnote/mkedit";
	}

	@RequestMapping({"mkedit"})
	public String mkedit(Map<String, Object> map){
		return "xnote/mkedit";
	}
	
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Long id,Map<String, Object> map) {
		XNote xnote = servie.getXnote(id);
		if(xnote!=null){
			User user=uservice.getUser(xnote.getCreaterId());
			map.put("author", user);
		}
		map.put("xnote", xnote);
		return "xnote/view";
	}
}
