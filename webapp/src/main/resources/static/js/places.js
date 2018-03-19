function resize() {
	$("#map").css("height", 
			getInnerHeight());	// see script.js
}

function buildMap(artistsList) {
	$(window).on('resize', resize);
	$(window).trigger('resize');
	
	/* input map keys (i.e. names of the artists) */
	var currentLyricsLayers;
	var currentMetaLayers;
	var color;
	
	/* leaflet components start */
	var map = L.map('map', {center: [ 36.754187, 3.058785 ],
		zoom: /* 2 */3
	});
	
	L.tileLayer(
			'https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token={accessToken}',
			{
				attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
				maxZoom : 18,
				id : 'mapbox.streets',
				accessToken : ''
			}).addTo(map);

	var layerControl = new L.control.layers();
	/* leaflet components end */
	
	/* iterating over artists as keys retrieving the places */
	for (var j = 0; j < artistsList.length; j++) {
		
		/*
		 * Every artist gets one specific color and two kinds of layers (meta
		 * and lyrics)
		 */
		var currArtist = artistsList[j];
		currentLyricsLayers = [];
		currentMetaLayers = [];
		color = currArtist.color;
		
		/* prototype: markup for lyrics marker */
	    var lyricsMarkerHtmlStyles = `
			  background-color: ${color};
			  width: 3rem;
			  height: 3rem;
			  display: block;
			  left: -1.5rem;
			  top: -1.5rem;
			  position: relative;
			  border-radius: 3rem 3rem 0;
			  transform: rotate(45deg);
			  border: 1px solid #FFFFFF;`
		  
		/* prototype: markup for meta marker */
		var bioMarkerHtmlStyles = `
			  background-color: ${color};
			  width: 3rem;
			  height: 3rem;
			  display: block;
			  left: -1.5rem;
			  top: -1.5rem;
			  position: relative;
			  border-radius: 3rem 0 0;
			  transform: rotate(45deg);
			  border: 1px solid #FFFFFF;`
			  
		var smallLyricsMarkerHtmlStyles = `
			  background-color: ${color};
			  width: 1rem;
			  height: 1rem;
			  display: block;
			  left: -0.5rem;
			  top: -0.5rem;
			  border-radius: 1rem 1rem 0;
			  transform: rotate(45deg);
			  border: 1px solid #FFFFFF;
			  display: inline-block;`
			  
		var smallBioMarkerHtmlStyles = `
			  background-color: ${color};
			  width: 1rem;
			  height: 1rem;
			  display: block;
			  left: -10.5rem;
			  top: -0.5rem;
			  border-radius: 1rem 0 0;
			  transform: rotate(45deg);
			  border: 1px solid #FFFFFF;
			  display: inline-block;`
    
		var bioIcon = new L.divIcon({
			  className: "bioIcon",
			  iconAnchor: [0, 24],
			  labelAnchor: [-6, 0],
			  popupAnchor: [0, -36],
			  html: `<span style="${bioMarkerHtmlStyles}"></span>`
			});
	
		var lyricsIcon = L.divIcon({
			  className: "lyricsIcon",
			  iconAnchor: [0, 24],
			  labelAnchor: [-6, 0],
			  popupAnchor: [0, -36],
			  html: `<span style="${lyricsMarkerHtmlStyles}"></span>`
			})
		
		/* all places get their attributes */
		for (var k = 0; k < currArtist.lyricsPlaces.length; k++) {
			var places = currArtist.lyricsPlaces;
			var currentPlace = places[k]; 
			var marker = L.marker([currentPlace.longitude, currentPlace.latitude], {icon: lyricsIcon});

			var tabs = buildTabs(currentPlace);
			var content = buildPopUpContent(currentPlace);
			
			marker.bindPopup(tabs + content);
			currentLyricsLayers.push(marker);
			}
		
		for (var l = 0; l < currArtist.bioPlaces.length; l++) {
			var places = currArtist.bioPlaces;
			var currentPlace = places[l]; 
			var marker = L.marker([currentPlace.longitude, currentPlace.latitude], {icon: bioIcon});
			
			var tabs = buildTabs(currentPlace);
			var content = buildPopUpContent(currentPlace);
			
			marker.bindPopup(tabs + "<div class=\"tab-content\">" + content + "</div>");
			currentMetaLayers.push(marker);
			}
		
		/* adds the Layers to map and control (checkboxes for selection) */
		map.addLayer(L.layerGroup(currentLyricsLayers));
		layerControl.addOverlay(L.layerGroup(currentLyricsLayers).addTo(map), `<div style='display: inline-block;'>` + currArtist.name + `<span style="${smallLyricsMarkerHtmlStyles}"</span></div>`);
		map.addLayer(L.layerGroup(currentMetaLayers));
		layerControl.addOverlay(L.layerGroup(currentMetaLayers).addTo(map), `<div style='display: inline-block;'>` + currArtist.name + ` (Bio)<div style="${smallBioMarkerHtmlStyles}"></div></div>`);
	}
	
	layerControl.addTo(map);
}

/* Building the tabs inside the popup */
function buildTabs(place){
	var moreTabs = false; 
	if(place.popUps.length == 1){
		return "";
	}
	var tabs = "<div>" 
		+ "<ul class=\"nav nav-tabs\">";
	for(var j = 0; j < place.popUps.length; j++){
		if(j == 4){
			moreTabs = true;
			tabs += " <li class=\"dropdown\"><a class=\"dropdown-toggle\" data-toggle=\"dropdown\" href=\"#\">more<span class=\"caret\"></span></a>"
			+ "<ul class=\"dropdown-menu\">";
		}
		
		var hrefString = place.popUps[0].placeName + j;
		if (j == 0) {
			active = " class=\" active\"";
		}else{
			active = "";
		}
		tabs += "<li" + active + "><a data-toggle=\"tab\" href=\"#" + hrefString.replace(/\s/g,'')
		+ "\">" + (j + 1) + "</a></li>"
	}
	if(moreTabs){
		tabs += "</li></ul></li>";
	}
	tabs += "</ul></div>";
	return tabs;
}

/* Building the content inside the popup */
function buildPopUpContent(place) {
	var content = "";
	var active = "";
	var currentPlaceName = place.popUps[0].placeName;
	for (var m = 0; m < place.popUps.length; m++){
		var idString = place.popUps[0].placeName + m;
		if (m == 0) {
			active = " in active";
		}else{
			active = "";
		}
		if(place.popUps[m].placeName){
			currentPlaceName = place.popUps[m].placeName;
		}
		content += "<div id=\"" + idString.replace(/\s/g,'') + "\" class=\"tab-pane fade" + active + "\">"
		+ "<h3>"
		+ currentPlaceName
		+ "</h3>"
		+ "<p> \" " + place.popUps[m].content + " \" </p>";
		content += place.popUps[m].referredSong ? '<p> <a href="/browse/' + place.popUps[m].referredSong.uuid + '">' + place.popUps[m].referredSong.title + " </a>(" + place.popUps[m].referredSong.year + ")</p>" : "";
		content += "</div>";
	}
	return content;
}

