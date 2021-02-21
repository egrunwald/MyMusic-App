package com.example.android.mymusic;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view that sets the recent nav button and Set text.
        TextView recentTextView = (TextView) findViewById(R.id.recent_nav_text_view);
        recentTextView.setText(R.string.header_recent);

        // Find the view that sets the songs list nav button and Set text.
        TextView songsTextView = (TextView) findViewById(R.id.songs_nav_text_view);
        songsTextView.setText(R.string.header_all);

        // Find the view that sets the audio player nav button and Set text.
        TextView playerTextView = (TextView) findViewById(R.id.player_nav_text_view);
        playerTextView.setText(R.string.header_player);

        // Get recent playedList from intent when mainActivity opened from
        // another Activity.
        Intent intent = getIntent();

        // Create an empty ArrayList of Integer to be filled from intent extra
        ArrayList<Integer> playedList = new ArrayList<>();

        // Create an empty ArrayList of Song to be filled later with song data
        ArrayList<Song> songDataList = new ArrayList<>();

        // Check if Intent contains extra "recentPlayed"
        if (intent.getIntegerArrayListExtra("recentPlayed") != null) {

            // If intent contains a list of recentPlayed songs add them to playedList.
            playedList.addAll(intent.getIntegerArrayListExtra("recentPlayed"));

            // for each song in listIfSong retrieve metadata and save as Song in songDataList
            for (int i = playedList.size() - 1; i >= 0; i--) {

                // Create fileAddress variable for current song in list
                int fileAddress = playedList.get(i);

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
        } else {

            // If intent does not contain a list of recentPlayed songs add blank song to playedList.
            Song thisSong = new Song(-1, getString(R.string.no_recent), getString(R.string.no_recent), null);

            // Add the blank song address to the playedList to pass to other activities
            playedList.add(thisSong.getFileAddress());

            // Add the blank song to the songDataList to display
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
        ListView listView = (ListView) findViewById(R.id.recent_list);

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

                // Check if selected song is a actual song
                if (songAddress != -1) {

                    // Create a new intent to open the {@link PlayerActivity}
                    Intent intent = new Intent(MainActivity.this, PlayerActivity.class);

                    // Add songAddress data as extra to the Intent
                    intent.putExtra("songAddress", songAddress);

                    // Add the playedList Data as extra to the intent
                    intent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(intent);
                } else {

                    // Create Toast for no recent songs
                    Toast toast = Toast.makeText(MainActivity.this, getText(R.string.no_recent_toast), Toast.LENGTH_LONG);

                    // Set Toast gravity to center of screen
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    // Show the Toast
                    toast.show();

                    // Create a new Intent to open the {@link SongsActivity}
                    Intent songIntent = new Intent(MainActivity.this, SongsActivity.class);

                    // Add the playedList Data as extra to the intent
                    songIntent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(songIntent);
                }
            }
        });

        // Set the on click listener for the view
        songsTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the songs_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SongsActivity}
                Intent songsIntent = new Intent(MainActivity.this, SongsActivity.class);

                // Add the playedList Data as extra to the intent
                songsIntent.putExtra("recentPlayed", playedList);

                // Start the new activity
                startActivity(songsIntent);
            }
        });

        // Set the on click listener for the view
        playerTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create variable lastSongPlayed set value to first song in songDataList
                Song lastSongPlayed = songDataList.get(0);

                // Create variable songAddress and set to lastSongPlayed file address
                int songAddress = lastSongPlayed.getFileAddress();

                // Check if lastSongPlayed is an actual song
                if (songAddress != -1) {

                    // Create a new intent to open the {@link PlayerActivity}
                    Intent playerIntent = new Intent(MainActivity.this, PlayerActivity.class);

                    // Add the songAddress data to the Intent as extra
                    playerIntent.putExtra("songAddress", songAddress);

                    // Add the playedList data to the Intent as extra
                    playerIntent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(playerIntent);
                } else {

                    // Create Toast for no recent songs
                    Toast toast = Toast.makeText(MainActivity.this, getText(R.string.no_recent_toast), Toast.LENGTH_LONG);

                    // Set Toast gravity to center of screen
                    toast.setGravity(Gravity.CENTER, 0, 0);

                    // Show the Toast
                    toast.show();

                    // Create a new Intent to open the {@link SongsActivity}
                    Intent songIntent = new Intent(MainActivity.this, SongsActivity.class);

                    // Add the playedList data to the Intent as extra
                    songIntent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(songIntent);
                }
            }
        });
    }
}