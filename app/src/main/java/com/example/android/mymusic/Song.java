package com.example.android.mymusic;

import android.graphics.drawable.Drawable;

public class Song {

    private String mSongArtistInfo;

    private String mSongTitleInfo;

    private Drawable mSongImage;

    private int mFileAddress;

    public Song(int fileAddress, String title, String artist, Drawable image) {
        mFileAddress = fileAddress;

        if (title != null) {
            mSongTitleInfo = title;
        } else {
            mSongTitleInfo = "No title info!";
        }

        if (artist != null) {
            mSongArtistInfo = artist;
        } else {
            mSongArtistInfo = "No artist info!";
        }

        if (image != null) {
            mSongImage = image;
        }

    }

    public String getSongTitle() {
        return mSongTitleInfo;
    }

    public String getSongArtist() {
        return mSongArtistInfo;
    }

    public Drawable getSongImage() {
        return mSongImage;
    }

    public boolean hasImage() {
        return (mSongImage != null);
    }

    public int getFileAddress() {
        return mFileAddress;
    }

}
