package de.uni_koeln.dh.lyra.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.data.Place;
import de.uni_koeln.dh.lyra.data.PopUp;
import de.uni_koeln.dh.lyra.services.CorpusService;
import de.uni_koeln.dh.lyra.services.SearchService;

@Controller
public class IndexController {

	@Autowired
	public CorpusService corpusService;

	@Autowired
	public SearchService searchService;

	private Map<Place, Set<String>> map;

	@RequestMapping(value = { "", "/" })
	public String index(Model model) {
		initStock(model);

		if (map != null) {
			model.addAttribute("places", map);
		}

		return "index";
	}

	@PostMapping(value = "/evaluation")
	public String evaluation(@RequestParam(value = "placeName", required = false) List<String> placeNames, Model model)
			throws IOException {
		System.out.println("EVALUATION");

		// TODO placeNames == null

		Iterator<Map.Entry<Place, Set<String>>> iter = map.entrySet().iterator();

		while (iter.hasNext()) {
			Set<String> set = iter.next().getValue();

			set.removeAll(placeNames);
			if (set.isEmpty())
				iter.remove();
		}

		corpusService.completeCorpus(map);
		searchService.updateIndex();

		// TODO idle
		initStock(model);

		map = null;
		return "index";
	}

	@RequestMapping(value = "/about")
	public String about() {
		return "about";
	}

	private void initStock(Model model) {
		if (corpusService.getArtistList() != null && !corpusService.getArtistList().isEmpty()) {
			int totalCount = corpusService.getAllSongs().size();

			Map<Artist, Integer> stockMap = new TreeMap<Artist, Integer>(Collections.reverseOrder());

			List<Artist> artists = corpusService.getArtistList();

			for (int i = 0; i < artists.size(); i++) {
				int cnt = artists.get(i).getSongs().size();
				int percentage = (int) ((100 / (float) totalCount) * cnt);
				stockMap.put(artists.get(i), percentage);
			}
			model.addAttribute("quotes", corpusService.getRandomQuotes());
			model.addAttribute("songCount", totalCount);
			model.addAttribute("artistMap", stockMap);
		}
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> uploadFile(@RequestParam("uploadfile") MultipartFile uploadFile, Model model) {
		System.out.println("Upload new file:" + uploadFile.getOriginalFilename());

		try {

			String filename = uploadFile.getOriginalFilename();
			String directory = "data/tmp";
			File dir = new File(directory);
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			String filepath = Paths.get(directory, filename).toString();

			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filepath)));
			stream.write(uploadFile.getBytes());
			stream.close();

			File tmpFile = new File(filepath);
			List<Place> placesToEvaluate = corpusService.prepareEvaluation(tmpFile);

			if (placesToEvaluate != null) {

				map = new HashMap<Place, Set<String>>();

				for (Place place : placesToEvaluate) {
					Set<String> set = new TreeSet<String>();

					for (PopUp popup : place.getPopUps())
						set.add(popup.getPlaceName());

					map.put(place, set);
				}

				// model.addAttribute("places", map);
			}

			// delete files in tmp dir
			File[] tmps = dir.listFiles();
			for (int i = 0; i < tmps.length; i++) {
				tmps[i].delete();
			}

			// TODO clear map/list
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// return "index";// new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public StreamingResponseBody getSteamingFile(HttpServletResponse response) throws IOException {

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");// ("text/html;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"sample.xlsx\"");
		InputStream inputStream = new FileInputStream(new File("src/main/resources/sample/sample.xlsx"));

		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				outputStream.write(data, 0, nRead);
			}
			inputStream.close();
		};
	}

}
