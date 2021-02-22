package com.example.android.mymusic;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * {@link SongTextAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link Song} objects.
 * */
public class SongTextAdapter extends ArrayAdapter<Song> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param songs   A List of Song objects to display in a list
     */
    public SongTextAdapter(Activity context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    the position in the list of data that should be displayed
     *                    in the listItemView.
     * @param convertView the recycled view to populate.
     * @param parent      the parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.text_list_item, parent, false);
        }

        // Get the {@link Song} object located at this position in the list
        Song currentSong = getItem(position);

        // Fined the TextView in the text_list_item.xml with the id song_artist_text_view
        TextView songArtistTextView = (TextView) listItemView.findViewById(R.id.song_artist_text_view);

        // Get the song artist from the current song object
        // Set this text on the TextView
        String artistName = "Artist: " + currentSong.getSongArtist();
        songArtistTextView.setText(artistName);

        // Find the TextView in the text_list_item.xml with the id song_title_text_view
        TextView songTitleTextView = (TextView) listItemView.findViewById(R.id.song_title_text_view);

        // Get the song title from the current Song object
        // Set this text on the TextView
        String songTitle = "Song title: " + currentSong.getSongTitle();
        songTitleTextView.setText(songTitle);

        // Find the ImageView in the text_list_item.xml with the id song_image_view
        ImageView songImageView = (ImageView) listItemView.findViewById(R.id.song_image_view);

        // Check if currentSong has a image
        if (currentSong.hasImage()) {

            // Set the ImageView to current songs image
            songImageView.setImageDrawable(currentSong.getSongImage());
        } else {

            // Set the Image view to default no image
            songImageView.setImageResource(R.drawable.no);
        }
        // Return the whole list item layout (containing 2 TextViews)
        // So that it can be shown in the ListView
        return listItemView;
    }
}
