// Functions related to using the Spotify Web Player 

 // Play a specified track on the Web Playback SDK's device ID
 function play(device_id) {
    $.ajax({
    url: "https://api.spotify.com/v1/me/player/play?device_id=" + device_id,
    type: "PUT",
    data: '{"uris": ["spotify:track:49mu0ewiqzCYPGY3IEtNad"]}',
    beforeSend: function(xhr){xhr.setRequestHeader('Authorization', 'Bearer ' + access_token );},
    success: function(data) { 
    console.log(data)
    }
});