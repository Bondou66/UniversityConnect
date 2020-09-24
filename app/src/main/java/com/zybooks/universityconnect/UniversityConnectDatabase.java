package com.zybooks.universityconnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UniversityConnectDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "universityConnect.db";
    private static final int VERSION = 1;

    public UniversityConnectDatabase (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class User {
        private static final String TABLE = "User";
        private static final String COL_ID = "id";
        private static final String COL_EMAIL = "email";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
    }

    private static final class Interest {
        private static final String TABLE = "Interest";
        private static final String COL_ID = "id";
        private static final String COL_INTEREST = "interest";
        private static final String COL_USER = "user_id";
    }

    private static final class Chat {
        private static final String TABLE = "Chat";
        private static final String COL_ID = "id";
    }

    private static final class Message {
        private static final String TABLE = "Message";
        private static final String COL_ID = "id";
        private static final String COL_TEXT = "text";
        private static final String COL_USER = "user_id";
        private static final String COL_CHAT = "chat_id";
    }

    private static final class Friend {
        private static final String TABLE = "Friend";
        private static final String COL_USER_ONE = "userOne";
        private static final String COL_USER_TWO = "userTwo";
    }

    private static final class SavedChat {
        private static final String TABLE = "SavedChat";
        private static final String COL_USER = "user";
        private static final String COL_CHAT = "chat";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + User.TABLE + " (" +
                User.COL_ID + " integer primary key autoincrement, " +
                User.COL_EMAIL + " text, " +
                User.COL_USERNAME + " text, " +
                User.COL_PASSWORD + " text)");

        db.execSQL("create table " + Chat.TABLE + " (" +
                Chat.COL_ID + " integer primary key autoincrement)");

        db.execSQL("create table " + Interest.TABLE + " (" +
                Interest.COL_ID + " integer primary key autoincrement, " +
                Interest.COL_INTEREST + " text, " +
                Interest.COL_USER + " integer, " +
                "foreign key (" + Interest.COL_USER + ") references " +
                User.TABLE + "(" + User.COL_ID + "))");

        db.execSQL("create table " + Message.TABLE + "(" +
                Message.COL_ID + " integer primary key autoincrement, " +
                Message.COL_TEXT + " text, " +
                Message.COL_USER + " integer, " +
                Message.COL_CHAT + " integer, " +
                "foreign key (" + Message.COL_USER + ") references " +
                User.TABLE + "(" + User.COL_ID + "), " +
                "foreign key(" + Message.COL_CHAT + ") references " +
                Chat.TABLE + "(" + Chat.COL_ID + "))");

        db.execSQL("create table " + Friend.TABLE + "(" +
                Friend.COL_USER_ONE + " integer not null, " +
                Friend.COL_USER_TWO + " integer not null, " +
                "foreign key (" + Friend.COL_USER_ONE + ") references " +
                User.TABLE + "(" + User.COL_ID + "), " +
                "foreign key (" + Friend.COL_USER_TWO + ") references " +
                User.TABLE + "(" + User.COL_ID + "), " +
                "primary key(" + Friend.COL_USER_ONE + ", " + Friend.COL_USER_TWO + "))");

        db.execSQL("create table " + SavedChat.TABLE + "(" +
                SavedChat.COL_USER + " integer not null, " +
                SavedChat.COL_CHAT + " integer not null, " +
                "foreign key (" + SavedChat.COL_USER + ") references " +
                User.TABLE + "(" + User.COL_ID + "), " +
                "foreign key (" + SavedChat.COL_CHAT + ") references " +
                Chat.TABLE + "(" + Chat.COL_ID + "), " +
                "primary key(" + SavedChat.COL_USER + ", " + SavedChat.COL_CHAT + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + SavedChat.TABLE);
        db.execSQL("drop table if exists " + Friend.TABLE);
        db.execSQL("drop table if exists " + Message.TABLE);
        db.execSQL("drop table if exists " + Interest.TABLE);
        db.execSQL("drop table if exists " + Chat.TABLE);
        db.execSQL("drop table if exists " + User.TABLE);
        onCreate(db);
    }
}
