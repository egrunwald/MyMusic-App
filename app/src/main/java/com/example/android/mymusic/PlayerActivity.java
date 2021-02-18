package com.example.android.mymusic;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent intent = getIntent();
        int songIndex = (int) intent.getIntExtra("songAddress", -1);
        ArrayList<Integer> playedList = new ArrayList<Integer>();

        // If intent contains a list of recentPlayed songs add them to playedList.
        if (intent.getIntegerArrayListExtra("recentPlayed") != null) {
            playedList.addAll(intent.getIntegerArrayListExtra("recentPlayed"));
        }

        if (songIndex > -1) {
            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + songIndex);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, mediaPath);
            String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String songArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String songAlbum = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            Drawable albumArt;
            byte[] songImage = mmr.getEmbeddedPicture();
            if (songImage != null) {
                ByteArrayInputStream is = new ByteArrayInputStream(songImage);
                albumArt = Drawable.createFromStream(is, "AlbumArt");
            } else {
                albumArt = null;
            }

            ImageView currentSongImage = findViewById(R.id.current_song_image);
            if (albumArt != null) {
                currentSongImage.setImageDrawable(albumArt);
            } else {
                currentSongImage.setImageResource(R.drawable.no);
            }

            TextView currentSongTitle = findViewById(R.id.song_playing_title_text);
            currentSongTitle.setText("Song Title: " + songTitle);

            TextView currentSongAlbum = findViewById(R.id.song_playing_album_text);
            currentSongAlbum.setText("Album: " + songAlbum);

            TextView currentSongArtist = findViewById(R.id.song_playing_artist_text);
            currentSongArtist.setText("Song Artist: " + songArtist);

            if (playedList.size() > 9) {
                playedList.remove(0);
            }
            playedList.add(songIndex);
        }

        // Find the view that sets the songs list
        TextView recentSongs = (TextView) findViewById(R.id.recent_nav_text_view);

        // Set the on click listener for the view
        recentSongs.setOnClickListener(new View.OnClickListener() {

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

        // Find the view that sets the audio player
        TextView songs = (TextView) findViewById(R.id.songs_nav_text_view);

        // Set the on click listener for the view
        songs.setOnClickListener(new View.OnClickListener() {

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

    }
}
