package com.example.android.mymusic;
/*Attributions for All Songs Used In This App
 *
 * Artist: Jens East
 * Song: Nightrise
 * Artist: Jens East (featuring Diandra Faye)
 * Song: Galaxies
 * Artist: Jens East (featuring Camilla North)
 * Song: Invisible
 * CC BY
 * http://www.jenseast.com/
 * www.soundcloud.com/jenseast
 * Licence: Creative Commons Attribution V4.0
 * https://creativecommons.org/licenses/by/4.0
 *
 * Artist: Computer Music Allstars
 * Song: Clueless
 * Song: May the Chords be With You
 * CC BY
 * http://www.cmallstars.com
 * Licence: Creative Commons Attribution V4.0
 * https://creativecommons.org/licenses/by/4.0
 *
 * Artist: Dancefloor is Lava
 * Song: Drown in Noise
 * Song: Control
 * Song: Why oh You Are Love
 * Public Domain
 * http://loyaltyfreakmusic.com
 * License: CC0 1.0 Universal/Public Domain Dedication
 * https://creativecommons.org/publicdomain/zero/1.0/
 *
 * Artist: Jaunitos
 * Song: Del Carnaval
 * CC BY
 * http://www.juanitos.net
 * License: Attribution 2.0 France
 * https://creativecommons.org/licenses/by/2.0/fr/
 *
 * Artist: Tintamare
 * Song: Propane
 * CC BY-SA
 * https://tintamare.bandcamp.com
 * License: Attribution-ShareAlike 4.0 International
 * https://creativecommons.org/licenses/by-sa/4.0/
 */

import java.util.ArrayList;

public class SongList extends ArrayList<Integer> {

    private ArrayList<Integer> listOfSongs = new ArrayList<Integer>();

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

}
