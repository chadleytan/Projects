"use strict"

function startWebPlayer(access_token){
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
        player.on('player_state_changed', state => {
            console.log(state)
            $('#current-track').attr('src', state.track_window.current_track.album.images[0].url);
            $('#current-track-name').text(state.track_window.current_track.name);
        });

        // Ready
        player.addListener('ready', data => {
        console.log('Ready with Device ID', data.device_id);
        
            // Play a track using our new device ID
            play(data.device_id, access_token);
        });

        // Not Ready
        player.addListener('not_ready', ({ device_id }) => {
            console.log('Device ID has gone offline', device_id);
        });

        // Connect to the player!
        player.connect().then(success => {
            if (success) {
            }
        });
    };
}

// Functions related to using the Spotify Web Player 
// Play a specified track on the Web Playback SDK's device ID
function play(device_id, access_token) {
    $.ajax({
    url: "https://api.spotify.com/v1/me/player/play?device_id=" + device_id,
    type: "PUT",
    data: '{"uris": ["spotify:track:49mu0ewiqzCYPGY3IEtNad"]}',
    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'Bearer ' + access_token );},
    success: function(data) { 
        console.log(data)
    }
    });
}