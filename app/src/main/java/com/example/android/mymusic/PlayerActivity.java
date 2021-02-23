package com.example.android.mymusic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    // Create new empty MediaPlayer called mPlayer
    MediaPlayer mPlayer = new MediaPlayer();

    // Create new seekBar called currentSongSeekBar
    SeekBar currentSongSeekBar;

    // Create Handler called seekBarHandler
    Handler seekBarHandler = new Handler();

    /**
     * This is a runnable method for the media handler to check and update
     * the seek bar position every 0.1 seconds when a song is playing.
     */
    private final Runnable moveSeekBar = new Runnable() {
        public void run() {

            // check if a song is playing
            if (mPlayer.isPlaying()) {

                // Get media player current position
                int mediaPos_new = mPlayer.getCurrentPosition();

                // Get song length
                int mediaMax_new = mPlayer.getDuration();

                // Set seek bar length to song length
                currentSongSeekBar.setMax(mediaMax_new);

                // Set progress to the media player current position
                currentSongSeekBar.setProgress(mediaPos_new);

                //Looping the thread after 0.1 second
                seekBarHandler.postDelayed(this, 100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //Find the view that sets the recent nav button and Set text.
        TextView recentTextView = (TextView) findViewById(R.id.recent_nav_text_view);
        recentTextView.setText(R.string.header_recent);

        // Find the view that sets the songs list nav button and Set text.
        TextView songsTextView = (TextView) findViewById(R.id.songs_nav_text_view);
        songsTextView.setText(R.string.header_all);

        // Find the view that sets the audio player nav button and Set text.
        TextView playerTextView = (TextView) findViewById(R.id.player_nav_text_view);
        playerTextView.setText(R.string.header_player);

        // Get the intent that opened this activity
        Intent intent = getIntent();

        // Create variable songIndex set value to the song address from the intent extra data
        int songIndex = (int) intent.getIntExtra("songAddress", -1);

        // Create an empty ArrayList of Integer to be filled from intent extra
        ArrayList<Integer> playedList = new ArrayList<>();

        // Check if Intent contains extra "recentPlayed"
        if (intent.getIntegerArrayListExtra("recentPlayed") != null) {

            // If intent contains a list of recentPlayed songs add them to playedList.
            playedList.addAll(intent.getIntegerArrayListExtra("recentPlayed"));
        }

        // Update variable mPlayer using the songIndex passed by the intent
        mPlayer = MediaPlayer.create(this, songIndex);

        // Create a Uri path for the current song
        Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + songIndex);

        // Create a MediaMetadataRetriever to retrieve meta data of current song
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, mediaPath);

        // Extract song Title
        String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

        // Extract song Artist
        String songArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        // Extract song Album
        String songAlbum = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        // create a Drawable variable
        Drawable albumArt;

        // Extract album art as byte[]
        byte[] songImage = mmr.getEmbeddedPicture();

        // check if a Image was extracted
        if (songImage != null) {

            // If Image exists convert byte[] to  ByteArrayInputStream
            ByteArrayInputStream is = new ByteArrayInputStream(songImage);

            // Create Drawable from ByteArrayInputStream and save as albumArt
            albumArt = Drawable.createFromStream(is, "AlbumArt");
        } else {

            // If no Image available set album art to null
            albumArt = null;
        }

        // Find the view that sets the current song album art
        ImageView currentSongImage = findViewById(R.id.current_song_image);

        // check if a image is present in albumArt to display
        if (albumArt != null) {

            // If there is an image in albumArt display it
            currentSongImage.setImageDrawable(albumArt);
        } else {

            //If no image in albumArt display default no image
            currentSongImage.setImageResource(R.drawable.no);
        }

        // Find view that sets the song title text
        TextView currentSongTitle = findViewById(R.id.song_playing_title_text);
        String songTitleDisplay = "Song Title: " + songTitle;
        currentSongTitle.setText(songTitleDisplay);

        // Find view that sets the song album text
        TextView currentSongAlbum = findViewById(R.id.song_playing_album_text);
        String songAlbumDisplay = "Album: " + songAlbum;
        currentSongAlbum.setText(songAlbumDisplay);

        // Find view that sets the song artist text
        TextView currentSongArtist = findViewById(R.id.song_playing_artist_text);
        String songArtistDisplay = "Song Artist: " + songArtist;
        currentSongArtist.setText(songArtistDisplay);

        // Check size of played list
        if (playedList.size() > 9) {

            // keep played list at a max of 10
            // by removing the oldest song on list
            playedList.remove(0);
        }

        // Add the current song to the playedList
        playedList.add(songIndex);

        // Set the on click listener for the view
        recentTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the songs_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SongsActivity}
                Intent recentIntent = new Intent(PlayerActivity.this, MainActivity.class);
                recentIntent.putExtra("recentPlayed", playedList);

                // Start the new activity
                startActivity(recentIntent);
            }
        });

        // Set the on click listener for the view
        songsTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link PlayerActivity}
                Intent songsIntent = new Intent(PlayerActivity.this, SongsActivity.class);
                songsIntent.putExtra("recentPlayed", playedList);

                // Start the new activity
                startActivity(songsIntent);
            }
        });

        // find ImageViews for player control buttons.
        ImageView rewindImageView = (ImageView) findViewById(R.id.rewind_img_view);
        ImageView stopImageView = (ImageView) findViewById(R.id.stop_img_view);
        ImageView playPauseImageView = (ImageView) findViewById(R.id.play_pause_img_view);
        ImageView fastForwardImageView = (ImageView) findViewById(R.id.fast_forward_img_view);

        // Set player control buttons to there default images
        rewindImageView.setImageResource(R.drawable.rewind_icon_black);
        stopImageView.setImageResource(R.drawable.stop_icon_black);
        playPauseImageView.setImageResource(R.drawable.play_icon_black);
        fastForwardImageView.setImageResource(R.drawable.ff_icon_black);

        // Set start time value of current song
        int startPosition = mPlayer.getCurrentPosition();

        // Set end time value of current song
        int endPosition = mPlayer.getDuration();

        // Find the view that sets the seekBar
        currentSongSeekBar = (SeekBar) findViewById(R.id.playing_seek_bar);

        // Set the length of the seekBar to the current song length
        currentSongSeekBar.setMax(endPosition);

        // Set the seekBar progress to the start position
        currentSongSeekBar.setProgress(startPosition);

        // Set listener for end of song
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                // Pause the media player
                mp.pause();

                // Change the Play/Pause Image to a play Button
                playPauseImageView.setImageResource(R.drawable.play_icon_black);

                // Set media player to start of song
                mp.seekTo(startPosition);

                // Set seekBar to start position
                currentSongSeekBar.setProgress(startPosition);
            }
        });

        // Set onClick listener for the Play/pause imageView
        playPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if the song is playing
                if (!mPlayer.isPlaying()) {

                    // Change the image from a play image to a pause image
                    playPauseImageView.setImageResource(R.drawable.pause_icon_black);

                    // Start the song
                    mPlayer.start();

                    // Start the handler to update the seekBar with the song progress
                    seekBarHandler.postDelayed(moveSeekBar, 100);
                } else {

                    // Change the pause image to the play image
                    playPauseImageView.setImageResource(R.drawable.play_icon_black);

                    // Pause the song
                    mPlayer.pause();
                }
            }
        });

        // Set onClick listener for the stop imageView
        stopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pause the song
                mPlayer.pause();

                // Change the play/pause image to the play image
                playPauseImageView.setImageResource(R.drawable.play_icon_black);

                // Set media player to the start of the song
                mPlayer.seekTo(startPosition);

                // Set seekBar progress to the start position
                currentSongSeekBar.setProgress(startPosition);

            }
        });

        // Set onClick listener for rewind imageView
        rewindImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the current position of the song
                int currentPosition = mPlayer.getCurrentPosition();

                // Get new song position
                int newPosition = currentPosition - 5000;

                // Set song limit for rewind
                int rewindLimit = 5000;

                // Check if rewind is within the song limits
                if (currentPosition >= rewindLimit) {

                    // Set media player position back 5000ms
                    mPlayer.seekTo(currentPosition - 5000);

                    // Check if song is playing
                    if (!mPlayer.isPlaying()) {

                        // Set seekBar progress back 5000ms
                        currentSongSeekBar.setProgress(newPosition);
                    }
                } else {

                    // Set the media player back to start of song
                    mPlayer.seekTo(startPosition);

                    // Check if song is playing
                    if (!mPlayer.isPlaying()) {

                        // Set seekBar progress back to start of song
                        currentSongSeekBar.setProgress(startPosition);
                    }
                }
            }
        });

        // Set onClick Listener for fast forward imageView
        fastForwardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the current position of the song
                int currentPosition = mPlayer.getCurrentPosition();

                // Get new song position
                int newPosition = currentPosition + 5000;

                // Get song limit for fast forward
                int fastforwardLimit = endPosition - 5000;

                // Check if fast forward is within the song limits
                if (currentPosition <= fastforwardLimit) {

                    // Set the media player ahead 5000ms
                    mPlayer.seekTo(newPosition);

                    // Check if song is playing
                    if (!mPlayer.isPlaying()) {

                        // Set seekBar progress ahead 5000ms
                        currentSongSeekBar.setProgress(newPosition);
                    }
                } else {

                    // Set media player to the end of song
                    mPlayer.seekTo(endPosition);

                    // Check if song is playing
                    if (!mPlayer.isPlaying()) {

                        // Set seekBar progress to end of song
                        currentSongSeekBar.setProgress(endPosition);
                    }
                }
            }
        });

        // Set seekBar onChange listener
        currentSongSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // check if change from user input
                if (fromUser) {

                    // Set media player position to the new user input progress
                    mPlayer.seekTo(progress);

                    // Set seekBar progress to match the new user input progress
                    currentSongSeekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // to be added in future updates
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // to be added in future updates
            }
        });
    }
}
