const express = require("express");
const bodyParser = require("body-parser");
const mongoose = require("mongoose");
const passport = require("passport");
const LocalStrategy = require("passport-local");
const logic = require('./logic');

const app = express();
const PORT = process.env.PORT || 3000;

// Schemas
const User = require("./models/user");

const mongoDB = "mongodb://admin:admin@ds029715.mlab.com:29715/songazzz";
mongoose.connect(mongoDB);
app.use(bodyParser.urlencoded({extended:false}));

app.set("view engine", "ejs");
app.use(express.static('./'));


// PASSPORT CONFIGURATION
app.use(require("express-session")({
    secret:"it's a secret",
    resave: false,
    saveUninitialized: false
}));
app.use(passport.initialize());
app.use(passport.session());
passport.use(new LocalStrategy(User.authenticate()));
passport.serializeUser(User.serializeUser());
passport.deserializeUser(User.deserializeUser());


// middleware for all code
app.use(function(req, res, next){
    // user would either be undefined or the signed in user
    res.locals.currentUser = req.user;
    next();
});

///////////////////////////////
// ROUTES
///////////////////////////////
app.get('/', function(req, res){
	res.render('landing');
});

app.get('/index', function(req, res){
    res.render('index');
});

app.put('/index', function(req, res){
    logic.favourite(res.locals.currentUser, req.body.search);
    res.send("");;
});

app.post('/songOption', function(req, res){
    res.render('chooseSong', {tracks: JSON.parse(req.body.tracks), search: req.body.search});
});

app.post('/indexSong', function(req, res){
	res.redirect('guessSong');
});

app.get('/chooseSong', function(req, res){
    res.render('chooseSong');
});

app.post('/guessSong', function(req,res){
    res.render('guessSong', {search: req.body.search, song: req.body.song, lyrics: req.body.data});
});

app.get('/guessSong', function(req, res){
	res.render('guessSong');
});

app.post('/guessLyrics', function(req, res){
	res.render('guessLyrics', {search: req.body.search, song: req.body.song, lyrics: req.body.lyrics, answers: req.body.answers});
});

app.get('/howTo', function(req, res){
	res.render('howTo');
});

app.get('/leaderboards', function(req, res){
    User.find({}, function(err, allUsers){
        if(err){
            console.log(err);
        } else {
            res.render('leaderboards', {users: allUsers.sort({score: -1})});
        }
    })
})

app.get('/leaderboards/:id', function(req, res){
    User.findById(req.params.id, function(err, foundUser){
        if(err){
            console.log(err);
        } else {
            res.render("show", {user: foundUser});
        }
    })
})

app.get('/myAccount', isLoggedIn, function(req,res){
    res.render('myAccount');
});

app.delete('/myAccount', isLoggedIn, function(req, res){
    logic.removeFavourite(res.locals.currentUser, req.body.artist);
    res.send("ok");
});

app.delete('/myAccountDelete', isLoggedIn, function(req, res){
    User.remove(res.locals.currentUser, function(err){
        if (err){
            console.log("err");
        } else {
            //console.log("removed user: " + res.locals.currentUser.username);
            res.send(res.locals.currentUser.username)
        }
    });
});

app.post('/updateScoreGuessSong', function(req, res){
    var result = logic.isCorrect(req.body.guess, req.body.answer);
    if (res.locals.currentUser){
         logic.updateScore(res.locals.currentUser, result);
    }
    if (result){
        res.redirect("/index");
    }
});

app.post('/updateScoreGuessLyrics', function(req, res){
    var result = logic.allCorrect(JSON.parse(req.body.guesses), JSON.parse(req.body.answers));
    console.log(result);
    if (res.locals.currentUser){
         logic.updateScore(res.locals.currentUser, result);
    }
    if (result){
        res.redirect("/index");
    }
});

///////////////////////////
// USER LOGIN
///////////////////////////
app.get('/signUp', function(req, res){
	res.render('signUp');
});

app.post('/signUp', function(req, res){
    var newUser = new User({username: req.body.username});
    User.register(newUser, req.body.password, function(err, user){
        if(err){
            console.log(err);
            return res.render("signUp");
        }
        passport.authenticate("local")(req,res, function(){
            res.redirect("/index");
        });
    });
});

app.get('/login', function(req, res){
	res.render('login');
});

app.post('/login', passport.authenticate("local", 
    {   
        //middleware
        successRedirect: "/index",
        failureRedirect: "/login"
    }), function(req, res){
});

//logout route
app.get("/logout", isLoggedIn, function(req, res){
    req.logout();
    res.redirect("/index");
})

// Checks if user is loggedIn
function isLoggedIn(req, res, next){
    if(req.isAuthenticated()){
        return next();
    }
    res.redirect("/login");
}

///////////////////////////
// REST
///////////////////////////

// rest functions...what would we use it for in our assignment

// get a user's score
app.get('/user/:username', function(req, res){
	// find the username
	User.find({username: req.params.username}, function (err, target) {
		var accuracy;
		var message;
		if(err){
			accuracy = "0";
			message = "404";
			console.log(err);
		} else {
			// if the user exist, calculate the accuracy of the user
			console.log(target);
			if (target.length > 0){
				if (target[0].gamesPlayed == 0){
					accuracy = "1";
				} else {
					accuracy = String(target[0].score/target[0].gamesPlayed);
				}
				message = "200";
			} else {
				// if the user does not exist, accuracy is set to 0
				message = "404";
				accuracy = "0";
				console.log("user does not exist");
			}
		}
		
		res.json({
			username: req.params.username,
			accuracy: accuracy,
			message: message
		});
	})
});

// put a favorite artist for a user
app.put('/user/:username/artist/:fav',  function(req, res){
	// find the user
	User.find({username: req.params.username}, function (err, target) {
		var msg
		
		if(err){
			msg = "404";
			console.log(err);
		} else {
			// if the user exists
			if (target.length > 0){
				var artist = req.params.fav.toUpperCase();
				var msg;
				// if the favorite artist is not already added
				if (target[0].favArtists.indexOf(artist) < 0){
					target[0].favArtists.push(artist);
					// add the artist
					target[0].save(function (err, updatedUser) {
						if (err) {
							console.log(err);
						} else {
							console.log("Favourited Artist: " + artist);
						}
					});
					msg = "200";
				} else {
					console.log("artist is already favourited");
					msg = "400";
				}
			} else {
				// the user does not exist
				msg = "404";
				console.log("user does not exist");
			}
		}
		res.json({
			username: req.params.username,
			message: msg
		});
	})
});

// create a new user account
app.post('/user/:username/pass/:password', function(req, res){
	User.find({username: req.params.username}, function (err, target) {
		var msg;
		if(err){
			msg = "404";
			console.log(err);
		} else {
			
			if (target.length > 0){
				// user already exists so return error message
				msg = "404";
				console.log("user already exist");
			} else {
				// user does not exist, so create the user
				msg = "200";
				
				var newUser = new User({username: req.params.username});
				User.register(newUser, req.params.password, function(err, user){
					if(err){
						console.log(err);
					}
				});
				
			}
			
		}
		res.json({
			username: req.params.username,
			message: msg
		});
	})
});

// delete a favorite artist from the db
app.delete('/user/:username/artist/:fav', function(req, res){
	// find the user
	User.find({username: req.params.username}, function (err, target) {
		if(err){
			res.json({
				username: req.params.username,
				message: "404"
			});
			console.log(err);
		} else {
			// if the user exist
			if (target.length > 0){
				// check if the artist exists
				var artist = req.params.fav.toUpperCase();
				var msg;
				if (target[0].favArtists.indexOf(artist) >= 0){
					msg = "200";
				} else {
					console.log("artist does not exist");
					msg = "400";
				}

				// filter out the artist to be deleted
				target[0].favArtists = target[0].favArtists.filter(e => e !== artist);
				target[0].save();
				res.json({
					username: req.params.username,
					message: msg
				});
				
			} else {
				res.json({
					username: req.params.username,
					message: "404"
				});
				console.log("user does not exist");
			}
		}
	})
});



app.listen(PORT, () => {
  console.log("Server started");
});
