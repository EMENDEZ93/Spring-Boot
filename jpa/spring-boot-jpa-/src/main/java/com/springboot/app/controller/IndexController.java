package com.springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springboot.app.dao.IClienteDao;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@Autowired
	private IClienteDao clienteDao;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "listar clientes");
		model.addAttribute("clientes", clienteDao.findAll());
		return "listar";
	}
	
	
	
}
