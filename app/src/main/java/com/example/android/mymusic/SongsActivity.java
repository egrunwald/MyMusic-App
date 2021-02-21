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

        //Find the view that sets the recent nav button.
        TextView recentTextView = (TextView) findViewById(R.id.recent_nav_text_view);
        recentTextView.setText(R.string.header_recent);

        // Find the view that sets the songs list nav button
        TextView songsTextView = (TextView) findViewById(R.id.songs_nav_text_view);
        songsTextView.setText(R.string.header_all);

        // Find the view that sets the audio player nav button
        TextView playerTextView = (TextView) findViewById(R.id.player_nav_text_view);
        playerTextView.setText(R.string.header_player);

        // Create empty ArrayList to fill with played songs list.
        ArrayList<Integer> playedList  = new ArrayList<Integer>();

        // Get recent playedList from intent when SongActivity opened from
        // another Activity.
        Intent intent = getIntent();

        // If intent contains a list of recentPlayed songs add them to playedList.
        if (intent.getIntegerArrayListExtra("recentPlayed") != null) {
            playedList.addAll(intent.getIntegerArrayListExtra("recentPlayed"));
        }

        // create an empty ArrayList of Song to be filled later
        ArrayList<Song> songDataList = new ArrayList<Song>();

        // create an ArrayList of song integer ids for songs in raw resource file
        SongList songList = new SongList();
        ArrayList<Integer> listOfSongs = songList.getSongList();

        // for each song in listIfSong retrieve metadata and save as Song in songDataList
        for (int i = 0; i < listOfSongs.size(); i++) {
            int fileAddress = listOfSongs.get(i);
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
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song currentSong = adapter.getItem(position);
                int songAddress = currentSong.getFileAddress();

                Intent intent = new Intent(SongsActivity.this, PlayerActivity.class);
                intent.putExtra("songAddress", songAddress);
                if (playedList.get(0) != -1) {
                    intent.putExtra("recentPlayed", playedList);
                }
                startActivity(intent);
            }
        });


        // Set the on click listener for the view
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


        // Set the on click listener for the view
        playerTextView.setOnClickListener(new View.OnClickListener() {

            // The code in this method will be executed when the player_nav_text_view View is clicked on.
            @Override
            public void onClick(View view) {
                int lastSong = playedList.get(playedList.size() - 1);

                if (lastSong != -1) {
                    // Create a new intent to open the {@link PlayerActivity}
                    Intent playerIntent = new Intent(SongsActivity.this, PlayerActivity.class);
                    playerIntent.putExtra("songAddress", lastSong);
                    playerIntent.putExtra("recentPlayed", playedList);

                    // Start the new activity
                    startActivity(playerIntent);
                } else {
                    Toast toast = Toast.makeText(SongsActivity.this, getText(R.string.no_recent_toast), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
