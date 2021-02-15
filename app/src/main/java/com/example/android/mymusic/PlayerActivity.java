package com.example.android.mymusic;

import android.app.Activity;
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

        // Find the view that sets the songs list
        TextView recentSongs = (TextView) findViewById(R.id.recent_nav_text_view);

        // Set the on click listener for the view
        recentSongs.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the songs_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SongsActivity}
                Intent recentIntent = new Intent(PlayerActivity.this, MainActivity.class);

                // Start the new activity
                startActivity(recentIntent);
            }
        });

        // Find the view that sets the audio player
        TextView player = (TextView) findViewById(R.id.player_nav_text_view);

        // Set the on click listener for the view
        player.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link PlayerActivity}
                Intent songsIntent = new Intent(PlayerActivity.this, SongsActivity.class);

                // Start the new activity
                startActivity(songsIntent);
            }
        });

        // create an ArrayList of song integer ids for songs in raw resource file
        ArrayList<Integer> listOfSongs = new ArrayList<Integer>();
        listOfSongs.add(R.raw.computer_music_all_stars_clueless);
        listOfSongs.add(R.raw.computer_music_all_stars_may_the_chords_be_with_you);
        listOfSongs.add(R.raw.dancefloor_is_lava_drown_in_noise);
        listOfSongs.add(R.raw.dancefloor_is_lava_control);
        listOfSongs.add(R.raw.dancefloor_is_lava_why_oh_you_are_love);
        listOfSongs.add(R.raw.jens_east_galaxies_ft_diandra_faye);
        listOfSongs.add(R.raw.jens_east_nightrise);
        listOfSongs.add(R.raw.camilla_north_x_jens_east_invisible);
        listOfSongs.add(R.raw.juanitos_del_carnaval);
        listOfSongs.add(R.raw.tintamare_propane);

        Intent intent = getIntent();
        int songIndex = (int)intent.getIntExtra("songAddress", -1);
        if(songIndex>-1) {
            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + listOfSongs.get(songIndex));
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

        }

    }
}
