package com.example.android.mymusic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            Intent intent = getIntent();
            ArrayList<Integer> playedList = (ArrayList<Integer>) intent.getIntegerArrayListExtra("recentPlayed");


        // create an empty ArrayList of Song to be filled later
        ArrayList<Song> songDataList = new ArrayList<Song>();

        SongList songList = new SongList();
        ArrayList<Integer> recentSongList = songList.getRecentSongList();
        if (playedList != null) {
            recentSongList.addAll(playedList);
            playedList.clear();
        }

        if (recentSongList.isEmpty()) {
            Song thisSong = new Song(-1, "NO recent songs played", "NO recent songs played", null);
            songDataList.add(thisSong);

        } else {

            // for each song in listIfSong retrieve metadata and save as Song in songDataList
            for (int i = recentSongList.size() - 1; i >= 0; i--) {
                int fileAddress = recentSongList.get(i);
                Uri mediaPath = Uri.parse("android.resource://" + getPackageName() + "/" + fileAddress);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(this, mediaPath);
                String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String songArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                Drawable albumArt;
                byte[] songImage = mmr.getEmbeddedPicture();
                if (songImage != null) {
                    ByteArrayInputStream is = new ByteArrayInputStream(songImage);
                    albumArt = Drawable.createFromStream(is, "AlbumArt");
                } else {
                    albumArt = null;
                }
                Song thisSong = new Song(fileAddress, songTitle, songArtist, albumArt);
                songDataList.add(thisSong);
            }

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song currentSong = adapter.getItem(position);
                int songAddress = currentSong.getFileAddress();

                if (songAddress != -1) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("songAddress", songAddress);
                intent.putExtra("recentPlayed", recentSongList);
                startActivity(intent);
                }
            }
        });

        // Find the view that sets the songs list
        TextView songsList = (TextView) findViewById(R.id.songs_nav_text_view);

        // Set the on click listener for the view
        songsList.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the songs_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {

                // Create a new intent to open the {@link SongsActivity}
                Intent songsIntent = new Intent(MainActivity.this, SongsActivity.class);
                songsIntent.putExtra("recentPlayed", playedList);

                // Start the new activity
                startActivity(songsIntent);
            }
        });

        // Find the view that sets the audio player
        TextView player = (TextView) findViewById(R.id.player_nav_text_view);

        // Set the on click listener for the view
        player.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {
                int songAddress = songDataList.get(0).getFileAddress();

                // Create a new intent to open the {@link PlayerActivity}
                Intent playerIntent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("songAddress", songAddress);
                playerIntent.putExtra("recentPlayed", playedList);

                // Start the new activity
                startActivity(playerIntent);
            }
        });

    }
}