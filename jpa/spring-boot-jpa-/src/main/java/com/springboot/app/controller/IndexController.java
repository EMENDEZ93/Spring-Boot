package com.springboot.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.app.models.Cliente;
import com.springboot.app.models.service.IClienteService;
import com.springboot.app.models.service.IUploadFileService;
//import com.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		return "index";
	}

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 3);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		// PageRender<Cliente> pageRender = new PageRender("/listar", clientes);

		model.addAttribute("titulo", "listar clientes");
		model.addAttribute("clientes", clientes);
		// model.addAttribute("pages", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario Cliente");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, @RequestParam("file") MultipartFile foto,
			RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			return "form";
		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != 0L && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadFileService.delete(cliente.getFoto());
			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Foto almacenada correctamente '" + uniqueFilename + "'");
			cliente.setFoto(uniqueFilename);

		}

		String mensajeFlash = (cliente.getId() != 0L) ? "editado con exito" : "creado con exito";

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);

			if (cliente == null) {
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

	@RequestMapping(value = "/eliminar/{id}", method = RequestMethod.GET)
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			clienteService.delete(id);
			flash.addFlashAttribute("success", "cliente eliminado con exito");

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con Exito.");
			}

		}
		return "redirect:/listar";
	}

	@RequestMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "Cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "detalle cliente");
		return "ver";

	}

	@RequestMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

}
