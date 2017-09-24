var numSquares = 6;
var colours=[];
var pickedColour;

//DOM 
var squares = document.querySelectorAll(".square");
var colourDisplay = document.getElementById("colourDisplay");
var messageDisplay = document.querySelector("#message");
var h1 = document.querySelector("h1");
var resetButton = document.querySelector("#reset");
var modeButtons = document.querySelectorAll(".mode");

//Run game
init();

//Initialize and setup game functions 
function init() {
	//mode buttons event listeners
	setupModeButtons();
	setupSquares();
	reset();
}

//Difficulty mode
function setupModeButtons(){
	for(var i = 0; i < modeButtons.length; i++){
		modeButtons[i].addEventListener("click", function(){
			//make sure that one mode has the class selected
			modeButtons[0].classList.remove("selected");
			modeButtons[1].classList.remove("selected");
			this.classList.add("selected");
			this.textContent === "Easy" ? numSquares = 3: numSquares = 6;
			reset();
		});
	}
}

//Determine game outcome
function setupSquares(){
	for(var i = 0; i < squares.length; i++){
		//add click listeners to squares
		squares[i].addEventListener("click", function(){
			var clickedColour = this.style.backgroundColor;

			//winner
			if(clickedColour == pickedColour){
				messageDisplay.textContent = "Correct!";
				changeColours(clickedColour);
				h1.style.backgroundColor = clickedColour;
				resetButton.textContent = "Play Again?";
			}
			//fail to win
			else {
				this.style.backgroundColor = "#232323";
				messageDisplay.textContent = "Try Again";
			}
		});
	}
}

//Reset Game function
function reset(){
	colours = generateRandomColours(numSquares); 
	//pick a new random colour from array
	pickedColour = pickColour();
	//change colourDisplay to macth picked Colour
	colourDisplay.textContent = pickedColour;
	//change colours of squares
	for(var i = 0; i < squares.length; i++){
		if(colours[i]){
			//renders all squares as a block element incase its has been set to none
			squares[i].style.display = "block";
			squares[i].style.backgroundColor = colours[i];
		}
		//hides colour
		else {
			squares[i].style.display = "none";
		}
	}
	h1.style.backgroundColor = "steelblue";

	messageDisplay.textContent = "";
	resetButton.textContent = "New Colours";
}

//Reset event listener
resetButton.addEventListener("click", function(){
	reset();
});


//Used for winning scenario
function changeColours(colour){
	//loop through all squares
	for(var i = 0; i < squares.length; i++) {
		//change each colour to match given colour
		squares[i].style.backgroundColor = colour;
	}
}

//Selects a random colour from the set of generated colours
function pickColour() {
	var random = Math.floor(Math.random() * colours.length);
	return colours[random];
}

//Generates random colours
function generateRandomColours(num){
	//make an array
	var arr = [];
	//repeat num times
	for(var i = 0; i< num; i++){
		//get random colour and push into arr
		arr.push(randomColour());
	}
	//return that array
	return arr;
}

//Random Colour Generator
function randomColour(){
	//pick a "red" from 0 - 255
	var r = Math.floor(Math.random() * 256);
	//pick a "green" from 0 - 255
	var g = Math.floor(Math.random() * 256);
	//pick a "blue" from 0 - 255
	var b = Math.floor(Math.random() * 256);
	return "rgb(" + r + ", " + g + ", " + b + ")";
}

