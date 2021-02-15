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

public class SongTextAdapter extends ArrayAdapter<Song> {

    public SongTextAdapter(Activity context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }


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
        songArtistTextView.setText("Artist: " + currentSong.getSongArtist());

        // Find the TextView in the text_list_item.xml with the id song_title_text_view
        TextView songTitleTextView = (TextView) listItemView.findViewById(R.id.song_title_text_view);

        // Get the song title from the current Song object
        // Set this text on the TextView
        songTitleTextView.setText("Song title: " + currentSong.getSongTitle());

        ImageView songImageView = (ImageView) listItemView.findViewById(R.id.song_image_view);

        if (currentSong.hasImage()) {
            songImageView.setImageDrawable(currentSong.getSongImage());
        } else {
            songImageView.setImageResource(R.drawable.no);
        }
        // Return the whole list item layout (containing 2 TextViews)
        // So that it can be shown in the ListView
        return listItemView;
    }

}
