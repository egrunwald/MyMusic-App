package com.example.android.mymusic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class SongsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        //Find the view that sets the recent nav button and Set text.
        TextView recentTextView = (TextView) findViewById(R.id.recent_nav_text_view);
        recentTextView.setText(R.string.header_recent);

        // Find the view that sets the songs list nav button and Set text.
        TextView songsTextView = (TextView) findViewById(R.id.songs_nav_text_view);
        songsTextView.setText(R.string.header_all);

        // Find the view that sets the audio player nav button and Set text.
        TextView playerTextView = (TextView) findViewById(R.id.player_nav_text_view);
        playerTextView.setText(R.string.header_player);

        // Create empty ArrayList to fill with played songs list.
        ArrayList<Integer> playedList = new ArrayList<>();

        // Get recent playedList from intent when SongActivity opened from
        // another Activity.
        Intent intent = getIntent();

        // If intent contains a list of recentPlayed songs add them to playedList.
        if (intent.getIntegerArrayListExtra("recentPlayed") != null) {
            playedList.addAll(intent.getIntegerArrayListExtra("recentPlayed"));
        }

        // create an empty ArrayList of Song to be filled later
        ArrayList<Song> songDataList = new ArrayList<Song>();

        // Create object songList of type SongList
        SongList songList = new SongList();

        // create an ArrayList of song integer ids for songs in raw resource file from object songList
        ArrayList<Integer> listOfSongs = songList.getSongList();

        // for each song in listIfSong retrieve metadata and save as Song in songDataList
        for (int i = 0; i < listOfSongs.size(); i++) {

            // Create fileAddress variable for current song in list
            int fileAddress = listOfSongs.get(i);

            // Create Uri path for current song in list
            Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + fileAddress);

            // Create new MediaMetadataRetriever and set current song as data source
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, mediaPath);

            // Extract current song Title meta data
            String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            // Extract current song Artist meta data
            String songArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            // Crate Drawable variable for current song album art
            Drawable albumArt;

            // Extract current song Image meta data as byte[]
            byte[] songImage = mmr.getEmbeddedPicture();

            // Check if a image was extracted
            if (songImage != null) {

                // Convert byte[] to ByteArrayInputStream
                ByteArrayInputStream is = new ByteArrayInputStream(songImage);

                // Convert ByteArrayInputStream to Drawable and save in albumArt variable
                albumArt = Drawable.createFromStream(is, "AlbumArt");
            } else {

                // Set albumArt to null
                albumArt = null;
            }

            // Create Song object with values from the current song in the list
            Song thisSong = new Song(fileAddress, songTitle, songArtist, albumArt);

            // Add the thisSong Song object to the songDataList ArrayList
            songDataList.add(thisSong);
        }

        // Create an {@link SongTextAdapter}, whose data source is a list of Strings and images. The
        // adapter knows how to create layouts for each item in the list, using the
        // list_item.xml layout resource defined in the Android framework.
        // This list item layout contains two {@link TextView} and one {@link ImageView},
        // which the adapter will set to display song title, song artist and a image
        SongTextAdapter adapter = new SongTextAdapter(this, songDataList);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called songs_list,
        // which is declared in the activity_songs.xml layout file.
        ListView listView = (ListView) findViewById(R.id.songs_list);

        // Make the {@link ListView} use the {@link SongTextAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link SongTextAdapter} with the variable name adapter.
        listView.setAdapter(adapter);

        // Set click listener for the adapter view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // The code in this method will execute when a list item in the songs_list view is click on.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the selected Song based on click position
                Song currentSong = adapter.getItem(position);

                // Get selected song file address
                int songAddress = currentSong.getFileAddress();

                // Create a new intent to open the {@link PlayerActivity}
                Intent intent = new Intent(SongsActivity.this, PlayerActivity.class);

                // Add songAddress data as extra to the Intent
                intent.putExtra("songAddress", songAddress);

                // Check if the recent playedList contains actual songs
                if (playedList.get(0) != -1) {

                    // Add the playedList Data as extra to the intent
                    intent.putExtra("recentPlayed", playedList);
                }

                // Start the new activity
                startActivity(intent);
            }
        });

        // Set the on click listener for the recentTextView
        recentTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the songs_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SongsActivity}
                Intent recentIntent = new Intent(SongsActivity.this, MainActivity.class);
                if (playedList.get(0) != -1) {
                    recentIntent.putExtra("recentPlayed", playedList);
                }

                // Start the new activity
                startActivity(recentIntent);
            }
        });

        // Set the on click listener for the playerTextView
        playerTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create variable lastSong set value to last song in playedList
                int lastSong = playedList.get(playedList.size() - 1);

                // Check if lastSong is a actual song
                if (lastSong != -1) {

                    // Create a new intent to open the {@link PlayerActivity}
                    Intent playerIntent = new Intent(SongsActivity.this, PlayerActivity.class);

                    // Add the lastSong data to the Intent as extra
                    playerIntent.putExtra("songAddress", lastSong);

                    // Add the playedList data to the Intent as extra
                    playerIntent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(playerIntent);
                } else {

                    // Create Toast for no recent songs
                    Toast toast = Toast.makeText(SongsActivity.this, getText(R.string.no_recent_toast), Toast.LENGTH_LONG);

                    // Set Toast gravity to center of screen
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    // Show the Toast
                    toast.show();
                }
            }
        });
    }
}
