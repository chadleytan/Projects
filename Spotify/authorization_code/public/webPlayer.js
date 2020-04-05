"use strict"

function StartWebPlayer(access_token){
    console.log("Starting Web API");
 
    window.onSpotifyWebPlaybackSDKReady = () => {
        const token = access_token;
        const player = new Spotify.Player({
            name: 'Web Playback SDK Quick Start Player',
            getOAuthToken: cb => { cb(token); }
        });

        // Error handling
        player.addListener('initialization_error', ({ message }) => { console.error(message); });
        player.addListener('authentication_error', ({ message }) => { console.error(message); });
        player.addListener('account_error', ({ message }) => { console.error(message); });
        player.addListener('playback_error', ({ message }) => { console.error(message); });

        // Playback status updates
        player.addListener('player_state_changed', state => { console.log(state); });

        // Ready
        player.addListener('ready', data => {
            console.log('Ready with Device ID', data.device_id);
            console.log(data);

            $('#submit-search').click(function() {
                var q_search = document.getElementById("search-track").value;
                SearchTrack(q_search, access_token);
            });

            // Play a track using our new device ID
            $("#play-track").click(function() {
                //var trackid = 
                Play(data.device_id, access_token);
                console.log("trying to play track");
            })
            
            // On click skips to position in track given by time
            $("#skip-pos").click(function(){
                var time = document.getElementById("time-skip").value;
                console.log(time);
                SkipToPosition(player, time);
            });
        });

        // Not Ready
        player.addListener('not_ready', ({ device_id }) => {
            console.log('Device ID has gone offline', device_id);
        });

        // Connect to the player!
        player.connect();
    };
}

// Functions related to using the Spotify Web Player 
// Play a specified track on the Web Playback SDK's device ID
function Play(device_id, access_token) {
    $.ajax({
        url: "https://api.spotify.com/v1/me/player/play?device_id=" + device_id,
        type: "PUT",
        data: '{"uris": ["spotify:track:1RMJOxR6GRPsBHL8qeC2ux"]}',
        beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'Bearer ' + access_token );},
        success: function(data) { 
            console.log(data)
        }
    });
}

function SearchTrack(q_search, access_token){
    $.ajax({
        url: "https://api.spotify.com/v1/search?q=" + q_search + "&type=track&market=US&limit=10",
        type: "GET",
        headers: {'Content-Type': 'application/json', 'Authorization': 'Bearer ' + access_token},
        success: function(data) {
            console.log(data);
        }
    });
}

// Skip to a certain position in the song
function SkipToPosition(player, time) {
    player.seek(time * 1000).then(() => {
        console.log('Changed position!');
    });
}

// Return the length of a track in mins the number of ms
function CalculateLength(number) {
    return "" + Math.floor(number/1000.0/60) + ":" + Math.round((number / 1000) % 60)
}