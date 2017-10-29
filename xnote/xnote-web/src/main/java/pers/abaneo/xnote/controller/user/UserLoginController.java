package pers.abaneo.xnote.controller.user;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pers.abaneo.xnote.controller.BaseController;

@Controller
public class UserLoginController extends BaseController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "user/login";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public String signin() {
		return "user/register";
	}
	
	@RequestMapping("logout")
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "redirect:/login";
	}
}
