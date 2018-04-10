const mongoose = require('mongoose');
const passportLocalMongoose = require("passport-local-mongoose");

var UserSchema = new mongoose.Schema({
	username: String,
	passwrod: String,
	score: { type: Number, default: 0 },
	gamesPlayed: { type: Number, default: 0},
	favArtists: [{type: String, default: []}]
});

UserSchema.plugin(passportLocalMongoose);

module.exports = mongoose.model("User", UserSchema);