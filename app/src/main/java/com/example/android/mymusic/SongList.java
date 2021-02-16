package com.example.android.mymusic;

import java.util.ArrayList;

public class SongList extends ArrayList<Integer> {

    private ArrayList<Integer> listOfSongs = new ArrayList<Integer>();

    private ArrayList<Integer> listOfRecentSongs = new ArrayList<Integer>();

    public SongList() {

        // create an ArrayList of song integer ids for songs in raw resource file
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

    }

    public ArrayList<Integer> getSongList() {
        return listOfSongs;
    }

    public ArrayList<Integer> getRecentSongList() {
        return listOfRecentSongs;
    }

    public void addToRecentList(int songAddress) {
        listOfRecentSongs.add(listOfSongs.get(songAddress));
    }

    public int getSongIndex(int songAddress) {
        return listOfSongs.indexOf(songAddress);
    }
}
