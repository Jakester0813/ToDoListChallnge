package com.jakester.todolistchallenge.constants;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoConstants {

    //SharedPreferences file name
    public static final String TO_DO_PREFS = "ToDoPrefs";

    //Request Codes
    public static final int LIST_REQUEST_CODE = 100;
    public static final int ITEM_REQUEST_CODE = 200;

    //Result Codes
    public static final int EDITED_LIST_RESULT = 100;
    public static final int DELETE_LIST_RESULT = 200;
    public static final int EDITED_ITEM_RESULT = 300;
    public static final int DELETE_ITEM_RESULT = 400;

    //Toolbar Titles
    public static final String YOUR_TO_DO_LISTS = "Your To Do Lists";
    public static final String SETTINGS = "Settings";

    //Intent Strings
    public static final String USERLIST_KEY = "userlist";
    public static final String POSITION_KEY = "position";
    public static final String UPDATED_LIST_KEY = "updatedlist";

    //Dialog Strings
    public static final String NEW_LIST_TITLE = "Add a new list";
    public static final String DONE = "Done";
    public static final String CANCEL = "Cancel";

    //Strings
    public static final String EMPTY_STRING = "";
    public static final String SHOW_COMPLETED = "Show Completed To Dos";
    public static final String HIDE_COMPLETED = "Hide Completed To Dos";
    public static final String NOTE_HINT = "Click here to add a note...";
}
