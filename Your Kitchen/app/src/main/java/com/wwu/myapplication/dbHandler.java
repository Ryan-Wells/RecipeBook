package com.wwu.myapplication;

import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import static android.content.ContentValues.TAG;

public class dbHandler {
    private static dbHandler instance = null;
    private static DatabaseReference db;
    private List<Recipe> recipes = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();

    static dbHandler getInstance(){
        if (instance == null){
            instance = new dbHandler();
            db = FirebaseDatabase.getInstance().getReference("recipes");
        }
        return instance;
    }

    void setup(){
        getDBRecipesAux();
    }
    //Method returns an ArrayList<Recipe> that contains all Recipe objects in the database
    public List<Recipe> getDBRecipes(){
        return recipes;
    }
    private void getDBRecipesAux(){
        // Attach a listener to read the data at our posts reference
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    recipes.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = snapshot.getValue(Recipe.class);
                        recipes.add(recipe);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read");
            }
        });
    }

    //Method returns an ArrayList<Recipe> that contains all Recipe objects in the database
    void getDBRecipeNames(final ArrayAdapter<String> adapter){
        // Attach a listener to read the data at our posts reference
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    adapter.clear();
                    names.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Recipe recipe = snapshot.getValue(Recipe.class);
                        assert recipe != null;
                        names.add(recipe.name);
                    }
                    adapter.addAll(names);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read");
            }
        });
    }

    //Method returns an ArrayList<Recipe> that contains all Recipe objects in the database
    //That has a tag passed in via parameter.
    public List<Recipe> getDBRecipesTag(String tag){
        //Initialize return variable and make query for recipe based on tag
        final List<Recipe> foundRecipes = new ArrayList<>();

        for(int i = 0; i< recipes.size(); i++){
            if(recipes.get(i).tags.contains(tag))
                foundRecipes.add(recipes.get(i));
        }

        return foundRecipes;
    }


    //Method returns an Recipe object from the database
    //Based off of id passed in via parameter.
    public Recipe getDBRecipeID(String id){
        Recipe ret = new Recipe();
        for(int i = 0; i < recipes.size();i++){
            if(recipes.get(i).id.equals(id))
                ret = recipes.get(i);
        }
        return ret;
    }

    //Based off of name passed in via parameter.
    Recipe getDBRecipeName(String name){
        Recipe ret = new Recipe();
        for(int i = 0; i < recipes.size();i++){
            if(recipes.get(i).name.equals(name))
                ret = recipes.get(i);
        }
        return ret;
    }

    void addNewRecipe(Recipe newRecipe){
        db.child(newRecipe.id).setValue(newRecipe);
    }

    //Method returns an int that should be a unique id
    String getNewDBID(){
        DatabaseReference temp = db.push();
        return temp.getKey();
    }

}


