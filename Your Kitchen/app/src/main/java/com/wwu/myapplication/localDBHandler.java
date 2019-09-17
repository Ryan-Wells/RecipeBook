package com.wwu.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;


public class localDBHandler extends SQLiteOpenHelper {
    private static localDBHandler instance = null;

    private static final String TABLE_NAME = "my_recipes";
    private static final String COL1 = "ID";
    private static final String COL2 = "recipeID";
    private static final String COL3 = "object";

    private localDBHandler(Context context){
        super(context, TABLE_NAME, null, 4);
    }

    static localDBHandler getInstance(Context context){
        if (instance == null){
            instance = new localDBHandler(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( "+ COL1 +" INTEGER PRIMARY KEY, " +
                COL2 + " TEXT, " + COL3 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean AddRecipe(Recipe newRecipe){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String jsonObject = newRecipe.toJSON();
        contentValues.put(COL2, newRecipe.id);
        contentValues.put(COL3, jsonObject);

        if(getRecipeData(newRecipe.id).moveToNext()){
            if(db.update(TABLE_NAME, contentValues, COL2 + " = '" + newRecipe.id + "'", null) == -1){
                Log.d(TAG, "Updating recipe in database failed ");
                return false;
            }
        }else{
            if(db.insert(TABLE_NAME, null, contentValues) == -1){
                Log.d(TAG, "Adding recipe to database failed ");
                return false;
            }
        }

        return true;
    }

    ArrayList<Recipe> getAllRecipes(){
        Cursor data = getAllData();
        ArrayList<Recipe> recipes = new ArrayList<>();

        while(data.moveToNext()){
            String jsonRecipe = data.getString(2);
            Recipe newRecipe = new Recipe();
            recipes.add(newRecipe.fromJSON(jsonRecipe));
        }
        return recipes;
    }

    ArrayList<Recipe> getRecipesTag(String tag){
        Cursor data = getAllData();
        ArrayList<Recipe> recipes = new ArrayList<>();

        while(data.moveToNext()){
            String jsonRecipe = data.getString(2);
            Recipe newRecipe = new Recipe();
            if(newRecipe.fromJSON(jsonRecipe).tags.contains(tag))
                recipes.add(newRecipe.fromJSON(jsonRecipe));
        }
        return recipes;
    }

    Recipe getRecipe(String id){
        Cursor data = getRecipeData(id);
        Recipe newRecipe = new Recipe();

        while(data.moveToNext()){
            String jsonRecipe = data.getString(2);
            newRecipe = newRecipe.fromJSON(jsonRecipe);
        }
        return newRecipe;
    }

    void deleteRecipe(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + "'"+id+"'";
        db.execSQL(query);
    }

    private Cursor getRecipeData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = " + "'"+id+"'";

        return db.rawQuery(query,null);
    }

    private Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query,null);
    }
}