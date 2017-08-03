package com.jakester.todolistchallenge.database;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
/**
 * Created by Jake on 8/2/2017.
 */

public class ToDoListContract {

    public static final String CONTENT_AUTHORITY = "com.jakester.todolistchallenge";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private ToDoListContract() {}

    // PATH VARIABLES:
    public static final String PATH_LISTS = "lists";

    public static final class ListEntry implements BaseColumns {

        // Content URI represents the base location for the table.
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LISTS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_LISTS;

        // Defines the table schema.
        public static final String TABLE_NAME = "TO_DO_LISTS_TABLE";
        public static final String KEY_ROW_ID = "KEY_ROW_ID";
        public static final String KEY_LIST_NAME = "KEY_LIST_NAME";
        public static final String KEY_ITEMS = "KEY_ITEMS";
        public static final String KEY_PRIORITY = "KEY_PRIORITY";
        public static final String KEY_ADDED_DATE = "KEY_ADDED_DATE";
        public static final String KEY_MODIFIED = "KEY_MODIFIED_DATE";

        // Defines a function to build a URI to find a specific recipe by it's identifier.
        public static Uri buildAlchenomiconRecipeUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
