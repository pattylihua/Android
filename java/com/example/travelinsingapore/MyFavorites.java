package com.example.travelinsingapore;

import android.provider.BaseColumns;

public class MyFavorites {
    public static final class Entry implements BaseColumns {

        public static final String TABLE_NAME = "Favorites";
        public static final String COL_NAME = "Name";
        public static final String COL_ADRESS = "Address";

    }
}
