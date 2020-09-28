package mx.gob.queretaro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

	@GetMapping
	public String home(ModelMap model) {
		model.addAttribute("message", "Bienvenid@ a Spring Boot 2");

		return "home";
	}
}
