package com.wwu.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class homePage extends AppCompatActivity {

    int currentIndex = -1;

    List<String> tags = new ArrayList<>(Arrays.asList("cheap", "quick", "vegetarian", "breakfast", "lunch", "dinner", "+"));
    List<String> recipeNames = new ArrayList<>(Arrays.asList("+"));
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button buttonToPublicPage = findViewById(R.id.buttonToPublicPage);
        final Button buttonToEditTags = findViewById(R.id.buttonToEditTags);
        Button buttonToManageTimers = findViewById(R.id.buttonToManageTimers);
        final EditText editTagsTextField = findViewById(R.id.editTextForTags);
        final ListView tagsList = findViewById(R.id.tagsList);
        final ListView recipesList = findViewById(R.id.myRecipeList);
        final ArrayAdapter<String> tagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tags);
        final ArrayAdapter<String> recipesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipeNames);/*{
            @Override
            public View getView(int i, View convertView, ViewGroup parent){
                View view = super.getView(i, convertView, parent);

                view.setBackground(ContextCompat.getDrawable(homePage.this.getApplicationContext(), R.drawable.gradient_2));

                return view;
            }

        };*/



        tagsList.setAdapter(tagsAdapter);
        recipesList.setAdapter(recipesAdapter);
        dbHandler.getInstance().setup();
        getRecipeList(recipesAdapter);

        buttonToPublicPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openPublicPage();
            }
        });
        buttonToManageTimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageTimers();
            }
        });


        tagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if user presses '+', create a new tag
                if(tags.get(i).equals("+")){
                    tags.set(i, getResources().getString(R.string.new_tag));
                    tags.add("+");
                    tagsAdapter.notifyDataSetChanged();
                    tagsList.setSelection(tagsAdapter.getCount() - 1);
                }
                else {
                    getRecipeListTag(recipesAdapter, tags.get(i));
                }

            }
        });

        tagsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(tags.get(i).equals("+")){
                    return false;
                }
                currentIndex = i;
                buttonToEditTags.setVisibility(View.VISIBLE);
                editTagsTextField.setVisibility(View.VISIBLE);
                editTagsTextField.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(editTagsTextField, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        recipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if user presses '+', add a new blank recipe
                if(recipeNames.get(i).equals("+")){
                    recipeNames.set(i, getResources().getString(R.string.new_blank_recipe));
                    recipeNames.add("+");
                    recipesAdapter.notifyDataSetChanged();
                    recipesList.setSelection(recipesAdapter.getCount() - 1);

                }
                else{
                    openRecipePage(i);
                }
            }
        });

        recipesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(recipeNames.get(i).equals("+")){
                    return false;
                }
                openEditRecipePage(i);
                return true;
            }
        });


        //save any edits made to a tag
        buttonToEditTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTagsTextField.getText().toString().equals("")){
                    tags.remove(currentIndex);
                    tagsAdapter.notifyDataSetChanged();
                    editTagsTextField.setText("");
                    editTagsTextField.setVisibility(View.INVISIBLE);
                    buttonToEditTags.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                else if(editTagsTextField.getText().toString().equals("+")) {
                    //do nothing, as we don't want to add this specific character to the list of tags
                    editTagsTextField.setText(R.string.noPlus);
                }
                else {
                    tags.set(currentIndex, editTagsTextField.getText().toString());
                    tagsAdapter.notifyDataSetChanged();
                    editTagsTextField.setText("");
                    editTagsTextField.setVisibility(View.INVISIBLE);
                    buttonToEditTags.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

    }

    private void getRecipeList(ArrayAdapter<String> adapter){
        recipes = localDBHandler.getInstance(this).getAllRecipes();
        adapter.clear();
        for(int i = 0; i < recipes.size(); i++){
            recipeNames.add(recipes.get(i).name);
        }
        adapter.add("+");
        adapter.notifyDataSetChanged();
    }

    private void getRecipeListTag(ArrayAdapter<String> adapter, String tag){
        recipes = localDBHandler.getInstance(this).getRecipesTag(tag);
        adapter.clear();
        for(int i = 0; i < recipes.size(); i++){
            recipeNames.add(recipes.get(i).name);
        }
        adapter.add("+");
        adapter.notifyDataSetChanged();
    }

    public void openEditRecipePage(int i){
        Intent intent = new Intent(this, editRecipePage.class);
        if(recipeNames.get(i).equals("new blank recipe"))
            intent.putExtra("Recipe", new Recipe());
        else
            intent.putExtra("Recipe", localDBHandler.getInstance(this).getRecipe(recipes.get(i).id));

        startActivity(intent);
    }

    public void openRecipePage(int i){
        Intent intent = new Intent(this, individualRecipePage.class);
        if(recipeNames.get(i).equals("new blank recipe"))
            intent.putExtra("Recipe", new Recipe());
        else
            intent.putExtra("Recipe", localDBHandler.getInstance(this).getRecipe(recipes.get(i).id));

        startActivity(intent);
    }

    public void openPublicPage(){
        Intent intent = new Intent(this, publicRecipesPage.class);
        startActivity(intent);
    }

    public void manageTimers(){
        Fragment timeFrag = new TimerFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.homeLayout, timeFrag).commit();
    }
}
