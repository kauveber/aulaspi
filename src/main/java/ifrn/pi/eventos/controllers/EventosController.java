package ifrn.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.ConvidadoRepository;
import ifrn.pi.eventos.repositories.EventoRepository;

@Controller
@RequestMapping("/eventos")
public class EventosController {

	@Autowired
	private EventoRepository er;
	@Autowired
	private ConvidadoRepository cr;

	@GetMapping("/form")
	public String form(Evento evento) {
		return "eventos/formEvento";
	}

	@PostMapping
	public String adicionar(Evento evento) {

		System.out.println(evento);
		er.save(evento);

		return "redirect:/eventos";
	}

	@GetMapping
	public ModelAndView listar() {
		List<Evento> eventos = er.findAll();
		ModelAndView mv = new ModelAndView("eventos/lista");
		mv.addObject("eventos", eventos);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detalhar(@PathVariable Long id, Convidado convidado) {
		ModelAndView md = new ModelAndView();
		Optional<Evento> opt = er.findById(id);

		if (opt.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}

		md.setViewName("eventos/detalhes");
		Evento evento = opt.get();
		md.addObject("evento", evento);

		List<Convidado> convidados = cr.findByEvento(evento);
		md.addObject("convidados", convidados);
		
		return md;
	}

	@PostMapping("/{idEvento}")
	public String salvarConvidado(@PathVariable Long idEvento, Convidado convidado) {
		
		Optional<Evento> opt = er.findById(idEvento);
		if(opt.isEmpty()) {
			return "redirect:/eventos";
		}
		
		Evento evento = opt.get();
		convidado.setEvento(evento);
		
		cr.save(convidado);
		
		return "redirect:/eventos/{idEvento}";
	}
	
	@GetMapping("/{id}/remover")
	public String apagarEvento(@PathVariable Long id) {
	
		Optional<Evento> opt = er.findById(id);
		
		if(!opt.isEmpty()) {
			Evento evento = opt.get();
			List<Convidado> convidados = cr.findByEvento(evento);
			
			cr.deleteAll(convidados);
			er.delete(evento);
		}
		
		return "redirect:/eventos";
	}
	
	@GetMapping("/{idEvento}/convidado/{idConvidado}/remover")
	public String apagarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
		
		Optional<Evento> opt = er.findById(idEvento);
		Optional<Convidado> optc = cr.findById(idConvidado);
		
		if(!opt.isEmpty() || !optc.isEmpty()) {
			Convidado convidado = optc.get();
			cr.delete(convidado);
		}
		
		return "redirect:/eventos/{idEvento}";
	}
	
	@GetMapping("/{id}/selecionar")
	public ModelAndView selecionarEvento(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView();
		Optional<Evento> opt = er.findById(id);
		
		if(opt.isEmpty()) {
			mv.setViewName("redirect:/eventos");
			return mv;
		}
		
		Evento evento = opt.get();
		mv.setViewName("eventos/formEvento");
		mv.addObject("evento", evento);
		
		return mv;
	}
	
	@GetMapping("/{idEvento}/convidado/{idConvidado}/selecionar")
	public ModelAndView selecionarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
		ModelAndView mv = new ModelAndView();
		
		Optional<Evento> optEvento = er.findById(idEvento);
		Optional<Convidado> optConvidado = cr.findById(idConvidado);
		
		if(optEvento.isEmpty() || optConvidado.isEmpty()) {
			mv.setViewName("redirect:/eventos");
			return mv;
		}
		
		Evento evento = optEvento.get();
		Convidado convidado = optConvidado.get();
		
		if(evento.getId() != convidado.getEvento().getId()) {
			mv.setViewName("redirect:/eventos");
			return mv;
		}
		
		mv.setViewName("eventos/detalhes");
		mv.addObject("convidado", convidado);
		mv.addObject("evento", evento);
		mv.addObject("convidados", cr.findByEvento(evento));
		
		return mv;
	}
	
}
