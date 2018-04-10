"use strict";

//Give selected a default value
var selected = 1;
var chosen = false;
var search;
var tracks = null;
var selectedArtist = "";
var songChosen = "";

//////////////////////
// EVENT LISTENERS
//////////////////////


// Search event listener
$("#searchArtist").keypress(function(event){
	if(event.which === 13){
		//gets the input value
		search = $(this).val();
		$(this).val("");
		setArtist(search);
		document.getElementsByName("search")[0].placeholder = "Selected Artist is: " + search;
    	document.getElementById("selectedartistdisplay").innerHTML="SELECTED ARTIST: " + search;
    	chosen = true;
	}
});

// Event listener for guessing the correct song
$("#submitGuess").on("click", function(){
	var guess = $("#guessSongInput").val();
	var song = document.getElementById("hiddenSongName").innerHTML;

	if (guess.toUpperCase() == song.toUpperCase()) {
			alert("correct");
		} else {
			alert("incorrect");
  	}

	// Force submit a post request to send guess and answer to next route
	var form = document.createElement("form");
	form.setAttribute("method", "POST");
	form.setAttribute("action", "/updateScoreGuessSong");

	// Add an input to send the top 10 tracks
    var guessField = document.createElement("input");
    guessField.setAttribute("type", "hidden");
    guessField.setAttribute("name", "guess");
    guessField.setAttribute("value", guess);
    // Add an input to show the name of what user searched
    var answerField = document.createElement("input");
    answerField.setAttribute("type", "hidden");
    answerField.setAttribute("name", "answer");
    answerField.setAttribute("value", song);

    form.appendChild(guessField);
    form.appendChild(answerField);
    document.body.appendChild(form);
    form.submit();
});


// PUT request to add an artist to a users favourites
$("button#addFavs").on("click", function(){
	if(chosen){
		$.ajax({
			type: 'PUT',
			url: '/index',
			data: {
				search: search.toUpperCase()
			},
			dataType: 'JSON',
			success: function(res){
				console.log("Added artist: " + res);
				window.location = "/index";
			}
		});
	}
});

// remove a favourited artist
$("li.favouritedArtists").on("click", function(){
	$(this).css("text-decoration", "line-through");
	var art = $(this)[0].innerText;
	$.ajax({
		url: '/myAccount',
		type:'delete',
		data: {
			artist: art
		},
		dataType: 'JSON',
		success: function(res){
			console.log("ok");
			location.reload();
		}
	});
});

// Play button which goes to next views
$("button.play").on("click", function(){
	// Lyrics Option
	if(selected == 1 && chosen){
		//window.top.location = "/index";

		// Force submit a post request to send tracks to next route
		var form = document.createElement("form");
    	form.setAttribute("method", "POST");
    	form.setAttribute("action", "/songOption");

    	// Add an input to send the top 10 tracks
        var topTracksField = document.createElement("input");
        topTracksField.setAttribute("type", "hidden");
        topTracksField.setAttribute("name", "tracks");
        topTracksField.setAttribute("value", JSON.stringify(tracks));
        // Add an input to show the name of what user searched
        var searchField = document.createElement("input");
        searchField.setAttribute("type", "hidden");
        searchField.setAttribute("name", "search");
        searchField.setAttribute("value", search);

        form.appendChild(topTracksField);
        form.appendChild(searchField);
	    document.body.appendChild(form);
	    form.submit();
	}
	// Song Option
	else if (selected == 2){
		guessSong(search, tracks[getRandomInt(10)]["name"]);
	}
});


// User selects option for version of game
$(".lyricsOp").on("click", function(){
	$(this).addClass("selectedOption");
	$(".songOp").removeClass("selectedOption");
	selected = 1;
});

//guess song option
$(".songOp").on("click", function(){
	$(this).addClass("selectedOption");
	$(".lyricsOp").removeClass("selectedOption");
	selected = 2;
});

// check to see if current view has this element
if( $("#songList").length){
	//Event Listener for choosing the song to guess the lyrics to
	document.getElementById("songList").addEventListener("click",function(e) {
		// e.target is our targetted element.
		if(e.target && e.target.nodeName == "LI") {
			var selectedArtist = document.getElementById("selectedArtistName").innerHTML;
			console.log(e.target.id + " was clicked"); //event listener to find which song is clicked
			songChosen = e.target.id;
			getLyrics(selectedArtist, songChosen);
		}
	});
}


$("#lyricsGuess").on("click", function(){
	var guesses = {
		blank1: $(".blank1").val().toUpperCase(),
	};
	var answers = JSON.parse($("#hiddenAnswers")[0].innerText);

	// Force submit a post request to answers and
	var form = document.createElement("form");
	form.setAttribute("method", "POST");
	form.setAttribute("action", "/updateScoreGuessLyrics");

	// Add an input to send the top 10 tracks
    var guessesField = document.createElement("input");
    guessesField.setAttribute("type", "hidden");
    guessesField.setAttribute("name", "guesses");
    guessesField.setAttribute("value", JSON.stringify(guesses));
    // Add an input to show the name of what user searched
    var answersField = document.createElement("input");
    answersField.setAttribute("type", "hidden");
    answersField.setAttribute("name", "answers");
    answersField.setAttribute("value", JSON.stringify(answers));

    form.appendChild(guessesField);
    form.appendChild(answersField);
    document.body.appendChild(form);
    form.submit();
});

// Event Listener to remove the currently logged in user from the db
$("#deleteAccount").on("click", function(){
	$.ajax({
		url: "/myAccountDelete",
		type: "delete",
		success: function(data){
			console.log("success deleted: " + data);
			window.location = "/index";
		},
	});
});


//////////////////////
// FUNCTIONS
//////////////////////

// Random int generating function
function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}

// get lyrics and randomly chooses a small portion of the lyrics
function showSomeLyrics(lyrics) {
	var length = lyrics.length;

	if (length > 200) {
		var starting = Math.floor(Math.random() * Math.floor(length - 200));
		var index = lyrics.slice(starting).indexOf(" ");
		if( index >= 200){
			return lyrics.slice(starting, starting + 200);
		} else {
			 return lyrics.slice(starting + index, starting + 200);
		}
	// Lyrics is less than 50 characters
	} else {
		return lyrics;
	}
}

function makeBlanks(lyrics, search, song, callback) {
	var splitLyrics = lyrics.split(" ");
	var length = splitLyrics.length;
	var blanks = [];
	var indices = [];
	var i = 0;

	while (i < 1) {
	    var index = getRandomInt(length);
	    if(indices.indexOf(index) < 0 && splitLyrics[index].indexOf("\n") < 0){
	    	indices.push(index);
		    blanks.push(splitLyrics[index]);
		    splitLyrics[index] = "||" + (i+1) + ". " + "BLANK ||";
		    i++;
	    };
	}
	callback(search, song, blanks, splitLyrics);
}

// sets data to the top 15 tracks of the artist specified
function setArtist(artist){
	//Ajax request
	$.ajax({
		type: 'GET',
		url: 'http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=' + artist +'&api_key=281a3a12cfe9ef3f8b2e65da0035af3c&format=json&limit=15',
		dataType:'JSON',
		success: function(res){
			tracks = res["toptracks"]["track"];
		}
	});
}

// Finds the top 12 artists
function topArtists(){
	//get the top 12 current artists from last.fm
	$.ajax({
		type: 'GET',
		url: 'http://ws.audioscrobbler.com/2.0/?method=chart.gettopartists&api_key=281a3a12cfe9ef3f8b2e65da0035af3c&format=json&limit=12',
		dataType: 'JSON',
		success: function(res){
			//gets the top 12 artists from api
			var topTracks = res["artists"]["artist"];
			var topArtists = $("#topArtists");

			//add a button for each of the top artist
			jQuery.each(topTracks, function(){
				// create button element with classes
				var button = document.createElement("button");
				button.className += " btn btn-sm top12";
				button.innerHTML = this["name"].toUpperCase();

				// event listener to select artist
				button.addEventListener("click", function(){
					setArtist(button.innerHTML);
					search = button.innerHTML;
					document.getElementsByName("search")[0].placeholder = "OR Search for an Artist...";
          			document.getElementById("selectedartistdisplay").innerHTML= "SELECTED ARTIST: " + search;
          			chosen = true;
				});
				topArtists.append(button);
			});
		}
	});
}
topArtists();


// makes request to the api and then a post request to the guesslyrics page
// gets lyrics and sends it over
function getLyrics(search, song){
	$.ajax({
		type:'GET',
		url: 'https://api.lyrics.ovh/v1/' + search + '/' + song,
		success:function(data){ // still to be implemented correctly
			makeBlanks(showSomeLyrics(data["lyrics"]), search, song, function(search, song, blanks, lyricsSplit){
				var lyrics = lyricsSplit.join(" ");
				var answers = {
					blank1: blanks[0].toUpperCase(),
				};

				// Force submit a post request to send tracks to next route
				var form = document.createElement("form");
		    	form.setAttribute("method", "POST");
		    	form.setAttribute("action", "/guessLyrics");

		    	// Send variables so it can be displayed in ejs file
		        var songField = document.createElement("input");
		        songField.setAttribute("type", "hidden");
		        songField.setAttribute("name", "song");
		        songField.setAttribute("value", song);

		        var searchField = document.createElement("input");
		        searchField.setAttribute("type", "hidden");
		        searchField.setAttribute("name", "search");
		        searchField.setAttribute("value", search);

		        // data from the get request to the api would be sent
		       	var lyricsField = document.createElement("input");
		        lyricsField.setAttribute("type", "hidden");
		        lyricsField.setAttribute("name", "lyrics");
		        lyricsField.setAttribute("value", lyrics);

		        // data from the get request to the api would be sent
		       	var answersField = document.createElement("input");
		        answersField.setAttribute("type", "hidden");
		        answersField.setAttribute("name", "answers");
		        answersField.setAttribute("value", JSON.stringify(answers));

		        form.appendChild(songField);
		        form.appendChild(searchField);
		        form.appendChild(lyricsField);
		        form.appendChild(answersField);
			    document.body.appendChild(form);
			    form.submit();
			});
		}
	});
}

// goes to next view where user can guess lyrics from a song
function guessSong(search, song){
	$.ajax({
	    type:'GET',
	    url: 'https://api.lyrics.ovh/v1/' + search + '/' + song,
	    success:function(data){ // still to be implemented correctly

			var form = document.createElement("form");
	    	form.setAttribute("method", "POST");
	    	form.setAttribute("action", "/guessSong");

	    	// Send variables so it can be displayed in ejs file
	        var songField = document.createElement("input");
	        songField.setAttribute("type", "hidden");
	        songField.setAttribute("name", "song");
	        songField.setAttribute("value", song);
	        var searchField = document.createElement("input");
	        searchField.setAttribute("type", "hidden");
	        searchField.setAttribute("name", "search");
	        searchField.setAttribute("value", search);

	        // data from the get request to the api would be sent
	       	var dataField = document.createElement("input");
	        dataField.setAttribute("type", "hidden");
	        dataField.setAttribute("name", "data");
	        dataField.setAttribute("value", showSomeLyrics(data["lyrics"]));
	        console.log(showSomeLyrics(data["lyrics"]));

	        form.appendChild(songField);
	        form.appendChild(searchField);
	        form.appendChild(dataField);
		    document.body.appendChild(form);
		    form.submit();
	     }
	});
}
