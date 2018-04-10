const mongoose = require("mongoose");

const mongoDB = "mongodb://admin:admin@ds029715.mlab.com:29715/songazzz";

// connect to db
mongoose.connect(mongoDB, function (err, res){
	if(err) console.log(err);
});

var schema = mongoose.Schema;

// schema for users
// should username be _id for unique identification?
var userSchema = new schema({
	_id: String,
	password: String, 
	num_correct: Number,
	num_wrong: Number,
	accuracy: Number
});

// queries
// create the user model
var users = mongoose.model('users', userSchema);


// adding users
function addUser(u, p) {
	users.create({_id: u, password: p, num_correct: 0, num_wrong: 0, accuracy: 0}, function (err, res) {
		if(err) console.log(err);
	});
}



function getPW (u) {
	users.findById(u, function (err, res) {
		if(err) console.log(err);
		return res.password;
	});
	/*
	var user = users.findOne({ _id: u }, function (err, res) {
		if(err) console.log(err);
	});

	return user.password;*/
};

function getCorrect (u) {
	users.findById(u, function (err, res) {
		if(err) console.log(err);
		return res.num_correct;
	});
};

function getWrong (u) {
	users.findById(u, function (err, res) {
		if(err) console.log(err);
		return res.num_wrong;
	});
};

function getAccuracy (u) {
	users.findById(u, function (err, res) {
		if(err) console.log(err);
		return res.accuracy;
	});
};


// updating user scores
// num is the new number of correct to be added to previous value
function updateCorrect(u, num) {
	users.findById(u, function (err, user) {
		if(err) console.log(err);
		user.num_correct += num;
		user.save(function (err, updatedUser) {
			if (err) console.log(err);
			res.send(updatedUser);
			
		});
	});
};


// num is the new number of wrong to be added to previous value
function updateCorrect(u, num) {
	users.findById(u, function (err, user) {
		if(err) console.log(err);
		user.num_wrong += num;
		user.save(function (err, updatedUser) {
			if (err) console.log(err);
			res.send(updatedUser);
			
		});
	});
};





