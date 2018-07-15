package com.springboot.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@Value("${application.controller.mensaje}")
	private String mensaje;
	
	@RequestMapping("/index")
	public String index(Model model) {
		model.addAttribute("mensaje", this.mensaje);
		return "index";
	}
	
}
