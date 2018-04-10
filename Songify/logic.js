const User = require("./models/user");

module.exports = {
	updateScore: function(user, result){
		User.findById(user, function (err, u) {
			if(err){
				console.log(err);
			} else {
				if (result) {
					u.score += 1;
				}
				u.gamesPlayed += 1;
				u.save(function (err, updatedUser) {
					if (err) {
						console.log(err);
					}
					console.log("updated");
				});
			}
		});
	},

	isCorrect: function(guess, answer){
		return guess.toUpperCase() === answer.toUpperCase();
	},

	allCorrect: function(guesses, answers){
		console.log(guesses);
		console.log(answers);
		return guesses.blank1 == answers.blank1; 
	},

	// Reset score and gamesplayed of a user
	resetProgress: function(user){
		User.findById(user, function (err, u){
			if(err){
				console.log(err);
			} else {
				u.score = 0;
				u.gamesPlayed = 0;
				u.save(function (err, updatedUser) {
					if (err) {
						console.log(err);
					}
					console.log("reset");
				});
			}
		});
	},

	// Addd artists to the users list of favourited artists
	favourite: function(user, artist){
		User.findById(user, function(err, u){
			if(err){
				console.log(err);
			} else {
				if (u.favArtists.indexOf(artist) < 0){
					u.favArtists.push(artist);
					u.save(function (err, updatedUser) {
						if (err) {
							console.log(err);
						}
						console.log("Favourited Artist: " + artist);
					});
				} else {
					console.log("artist is already favourited");
				}
			}
		});
	},

	removeFavourite: function(user, artist){
		User.findById(user, function(err, u){
			if(err){
				console.log(err);
			} else {
				var index = u.favArtists.indexOf(artist);
				u.favArtists.splice(index, 1);
				u.save(function (err, updatedUser){
					if (err) {
						console.log(err);
					}
					console.log("Removed Artist: " + artist);
				});
			}
		});
	}
}
