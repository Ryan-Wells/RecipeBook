package com.wwu.myapplication;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static android.content.ContentValues.TAG;


public class Recipe implements Serializable {
    public String id;
    public String name;
   // public Bitmap image;
    List<String> instructions;
    List<String> tags;
    List<String> ingredients;
    int servingSize;
    int calories;
    int timeToMake;
    double costToMake;
    private boolean estimatedTime;

    public Recipe() {
        this.name = "New Recipe";
        this.id = dbHandler.getInstance().getNewDBID();
        this.instructions = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }

    public Recipe(String name, String id, List<String> instructions, List<String> tags, List<String> ingredients, int servingSize, int calories){
        this.name = name;
        this.id = id;
        this.instructions = instructions;
        this.tags = tags;
        this.ingredients = ingredients;
        this.servingSize = servingSize;
        this.calories = calories;
        this.timeToMake = 0;
        this.costToMake = 0.00;
        this.estimatedTime = true;
    }

    List<String> getIngredients(){
        return this.ingredients;
    }
    List<String> getInstructions(){
        return this.instructions;
    }

    String toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("id", this.id);
            jsonObject.put("name", this.name);

            JSONArray instructions = new JSONArray();
            for(int i = 0; i < this.instructions.size(); i++){
                instructions.put(this.instructions.get(i));
            }
            jsonObject.put("instructions", instructions);

            JSONArray tags = new JSONArray();
            for(int i = 0; i < this.tags.size(); i++){
                tags.put(this.tags.get(i));
            }
            jsonObject.put("tags", tags);

            JSONArray ingredients = new JSONArray();
            for(int i = 0; i < this.ingredients.size(); i++){
                ingredients.put(this.ingredients.get(i));
            }
            jsonObject.put("ingredients", ingredients);

            jsonObject.put("servingSize", this.servingSize);
            jsonObject.put("calories", this.calories);
            jsonObject.put("timeToMake", this.timeToMake);
            jsonObject.put("costToMake", this.costToMake);
            jsonObject.put("estimatedTime", this.estimatedTime);

            return jsonObject.toString();
        }catch (JSONException e) {
            Log.d(TAG, "Could turn object into JSON String");
            return "";
        }
    }
    Recipe fromJSON(String str){
        Recipe newRecipe = new Recipe();
        try{
            JSONObject jsonObject = new JSONObject(str);
            newRecipe.id =  jsonObject.getString("id");
            newRecipe.name = jsonObject.getString("name");

            JSONArray jsonArray = jsonObject.getJSONArray("instructions");
            for(int i=0; i < jsonArray.length(); i++) {
                newRecipe.instructions.add(jsonArray.get(i).toString());
            }

            jsonArray = jsonObject.getJSONArray("tags");
            for(int i=0; i < jsonArray.length(); i++){
                newRecipe.tags.add(jsonArray.get(i).toString());
            }

            jsonArray = jsonObject.getJSONArray("ingredients");
            for(int i=0; i < jsonArray.length(); i++){
                newRecipe.ingredients.add(jsonArray.get(i).toString());
            }

            newRecipe.servingSize = jsonObject.getInt("servingSize");
            newRecipe.calories = jsonObject.getInt("calories");
            newRecipe.timeToMake = jsonObject.getInt("timeToMake");
            newRecipe.costToMake = jsonObject.getDouble("costToMake");
            newRecipe.estimatedTime = jsonObject.getBoolean("estimatedTime");

            return newRecipe;
        }catch (JSONException e) {
            Log.d(TAG, "Could not parse string into new Object");
            return new Recipe();
        }
    }
}

