package ifrn.pi.eventos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ifrn.pi.eventos.models.Evento;

@Controller
public class EventosController {
	
	@RequestMapping("/eventos/form")
	public String form() {
		return "formEvento";
	}
	
	@RequestMapping("/resp/form")
	public String formResp(Evento evento) {
		System.out.println("Formulario enviado");
		System.out.println("Nome: "+evento.getNome());
		System.out.println("Local: "+evento.getLocal());
		System.out.println("Data: "+evento.getData());
		System.out.println("Horario: "+evento.getHorario());
		return "formEvento";
	}
	
}
