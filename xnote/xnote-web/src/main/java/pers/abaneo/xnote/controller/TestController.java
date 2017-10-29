package pers.abaneo.xnote.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	@RequestMapping("test")
	public String test(Map<String, Object> map){
		map.put("name", "hello");
		return "test";
	}
}
