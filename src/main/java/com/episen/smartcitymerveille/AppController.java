package com.episen.smartcitymerveille;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class AppController {

	@Autowired
	private CapteurService service;

	@RequestMapping("/")
	public String viewHomePage(Model model) {
		List<Capteur> listCapteurs = service.listAll();
		model.addAttribute("listCapteurs", listCapteurs);

		return "index";
	}
	
	@RequestMapping("/new")
	public String showNewCapteurPage(Model model) {
	    Capteur capteur = new Capteur();
	    model.addAttribute("capteur", capteur);
	     
	    return "new_capteur";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveCapteur_Polluant(@ModelAttribute("capteur") Capteur capteur) {
	    service.save(capteur);
	     
	    return "redirect:/";
	}
	
	@RequestMapping("/configurate/{id}")
    public ModelAndView showConfigurateCapteurPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("configurate_capteur");
        Capteur capteur = service.get(id);
        mav.addObject("capteur", capteur);

        return mav;
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        service.delete(id);
        return "redirect:/";
    }

	}


