package de.uni_koeln.dh.lyra.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.uni_koeln.dh.lyra.services.CorpusService;

@Controller
@RequestMapping(value = "/analytics")
public class AnalysisController {

	@Autowired
	private CorpusService corpusService;

	@RequestMapping(value = "/places")
	public String places(Model model) {
		model.addAttribute("artistsList", corpusService.getArtistList());
		return "places";
	}

	// TODO frequencies
	@RequestMapping(value = "/frequencies")
	public String frequencies(/* Model model */) {
		return "frequencies";
	}

}
