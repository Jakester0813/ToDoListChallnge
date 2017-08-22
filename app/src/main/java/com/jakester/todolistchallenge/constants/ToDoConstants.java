package com.jakester.todolistchallenge.constants;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoConstants {

    /*CLASS VARIABLES ________________________________ */

    //DATABASE CONSTANTS
    public static final String DATABASE_NAME = "ToDoListDatabase";

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

    //Strings
    public static final String SHOW_COMPLETED = "Show Completed To Dos";
    public static final String HIDE_COMPLETED = "Hide Completed To Dos";
}
