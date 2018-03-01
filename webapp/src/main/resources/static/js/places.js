function buildMap(artistPlacesMap) {
	// TODO height specific?
	$(window).on('resize', function() {
		$("#map").css("height", 
				getInnerHeight());	// see script.js
	});
	
	$(window).trigger('resize');
	
	/* input map keys (i.e. names of the artists) */
	var keys = Object.keys(artistPlacesMap);
	var places;
	var currentLyricsLayers;
	var currentMetaLayers;
	var color;
	
	/* leaflet components start */
	var map = L.map('map', {center: [ 36.754187, 3.058785 ],
		zoom: /*2*/3
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
	for (var j = 0; j < keys.length; j++) {
		
		/*
		 * Every artist gets one specific color and two kinds of layers (meta
		 * and lyrics)
		 */
		currentLyricsLayers = [];
		currentMetaLayers = [];
		color = getRandomColor();
		
		/* all meta and lyrics places by artist */
		places = artistPlacesMap[keys[j]];
		
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
    
		var metaIcon = new L.divIcon({
			  className: "metaIcon",
			  iconAnchor: [0, 24],
			  labelAnchor: [-6, 0],
			  popupAnchor: [0, -36],
			  html: `<span style="${bioMarkerHtmlStyles}"></span>`
			});
	
		var lyricsIcon = L.divIcon({
			  className: "LyricsIcon",
			  iconAnchor: [0, 24],
			  labelAnchor: [-6, 0],
			  popupAnchor: [0, -36],
			  html: `<span style="${lyricsMarkerHtmlStyles}"></span>`
			})
		
		/* all places get their attributes */
		for (var k = 0; k < places.length; k++) {
			var currentPlace = places[k]; 
			var currIcon = currentPlace.isMeta ? metaIcon : lyricsIcon;
			var marker = L.marker([currentPlace.longitude, currentPlace.latitude], {icon: currIcon});
			var tabs = buildTabs(currentPlace);
			var content = buildPopUpContent(currentPlace);
			marker.bindPopup(tabs + content);
			currentPlace.isMeta ? currentMetaLayers.push(marker) : currentLyricsLayers.push(marker);
			}
		
		console.log("currentMetaLayers.length:" ,currentMetaLayers.length);
		console.log("currentLyricsLayers.length:" ,currentLyricsLayers.length);
		
		/* adds the Layers to map and control (checkboxes for selection) */
		map.addLayer(L.layerGroup(currentLyricsLayers));
		layerControl.addOverlay(L.layerGroup(currentLyricsLayers).addTo(map), `<div style='display: inline-block;'>` + keys[j] + `<span style="${smallLyricsMarkerHtmlStyles}"</span></div>`);
		map.addLayer(L.layerGroup(currentMetaLayers));
		layerControl.addOverlay(L.layerGroup(currentMetaLayers).addTo(map), `<div style='display: inline-block;'>` + keys[j] + ` (Bio)<div style="${smallBioMarkerHtmlStyles}"></div></div>`);
	}
	
	layerControl.addTo(map);
}

/* Building the tabs inside the popup */
function buildTabs(place){
	if(place.popUps.length == 1){
		return "";
	}
	var tabs = "<div class='tab'>";
	for(var j = 0; j < place.popUps.length; j++){
		if (j == 0) {
			active = " active";
		}else{
			active = "";
		}
		tabs += "<button class=\"tablinks " + active + "\" onclick=\"openPopUpContent(event, '" + place.popUps[0].placeName + j
		+ "')\">" + (j + 1) + "</button>"
	}
	tabs += "</div>";
	return tabs;
}

/* Building the content inside the popup */
function buildPopUpContent(place) {
	var content = "";
	var active = "";
	var currentPlaceName = place.popUps[0].placeName;
	for (var m = 0; m < place.popUps.length; m++){
		if (m == 0) {
			active = " active\" style=\"display: block;";
		}else{
			active = "";
		}
		if(place.popUps[m].placeName){
			currentPlaceName = place.popUps[m].placeName;
		}
		
		content += "<div class=\"tabcontent" + active + "\" id=\"" + place.popUps[0].placeName + m + "\">"
		+ "<h3>"
		+ currentPlaceName
		+ "</h3>"
		+ "<p>" + place.popUps[m].content + "</p>"
		+ "</div>"
	}
	return content;
}

/* popUpController */
function openPopUpContent(evt, index) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(index).style.display = "block";
    evt.currentTarget.className += " active";
}

function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}