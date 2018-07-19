package com.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springboot.app.dao.IClienteDao;
import com.springboot.app.models.Cliente;

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
	
	
	@RequestMapping(value="/form")
	public String crear( Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario Cliente");
		return "form";
	}	
	
	
	@RequestMapping(value="/form", method=RequestMethod.POST)
	public String crear(@Valid Cliente cliente, BindingResult result) {
		
		if(result.hasErrors()) {
			return "form";
		}
				
		clienteDao.save(cliente);
		return "redirect:listar";
	}		
	
}
