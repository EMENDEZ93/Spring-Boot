package com.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.Cliente;
import com.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("cliente")
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@Autowired
	private IClienteService clienteService;
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	public String listar(Model model) {
		model.addAttribute("titulo", "listar clientes");
		model.addAttribute("clientes", clienteService.findAll());
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
	public String crear(@Valid Cliente cliente, BindingResult result, RedirectAttributes flash, SessionStatus status) {
		
		if(result.hasErrors()) {
			return "form";
		}
			
		String mensajeFlash = (cliente.getId() != 0L)? "editado con exito":"creado con exito";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}		

	
	@RequestMapping(value="/form/{id}", method=RequestMethod.GET)
	public String editar(@PathVariable(value="id")Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id > 0) {
			cliente = clienteService.findOne(id);
			
			if(cliente == null) {
				flash.addFlashAttribute("error", "el cliente no existe");
			}
			
			
		} else {
			flash.addFlashAttribute("error", "el id no puede se cero");
			return "redirect:listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}	

	
	@RequestMapping(value="/eliminar/{id}", method=RequestMethod.GET)
	public String eliminar(@PathVariable(value="id")Long id, RedirectAttributes flash) {
		if(id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "cliente eliminado con exito");
		}
		return "redirect:/listar";
	}	
	
}
