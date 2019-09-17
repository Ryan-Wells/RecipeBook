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


public class publicRecipesPage extends AppCompatActivity {


    List<String> publicTags = new ArrayList<>(Arrays.asList("popular", "new", "cheap", "breakfast", "lunch", "dinner", "+"));
    List<String> publicRecipes = new ArrayList<>();
    int currentIndexPublic = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_recipes_page);

        Button buttonToHomePage = findViewById(R.id.buttonToHomePage);
        Button buttonToManageTimers = findViewById(R.id.buttonToManageTimers);
        final Button buttonToEditTagsPublic= findViewById(R.id.buttonToEditTagsPublic);

        final ListView publicTagsList = findViewById(R.id.publicTagsList);
        final ListView publicRecipesList = findViewById(R.id.publicRecipeList);

        final EditText editTextForTagsPublic = findViewById(R.id.editTextForTagsPublic);

        final ArrayAdapter<String> publicTagsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, publicTags);
        final ArrayAdapter<String> publicRecipesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, publicRecipes);
        publicTagsList.setAdapter(publicTagsAdapter);
        publicRecipesList.setAdapter(publicRecipesAdapter);
        dbHandler.getInstance().getDBRecipeNames(publicRecipesAdapter);


        //button back to the home screen
        buttonToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        buttonToManageTimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageTimers();
            }
        });

        //when a tag is pressed...
        publicTagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if user presses '+', create a new tag
                if(publicTags.get(i).equals("+")){
                    publicTags.set(i, "new tag");
                    publicTags.add("+");
                    publicTagsAdapter.notifyDataSetChanged();
                    publicTagsList.setSelection(publicTagsAdapter.getCount() - 1);
                }else {
                    List<Recipe> recipes = dbHandler.getInstance().getDBRecipesTag(publicTags.get(i));
                    publicRecipesAdapter.clear();
                    for(int j = 0; j < recipes.size(); j++){
                        publicRecipes.add(recipes.get(j).name);
                    }
                    publicRecipesAdapter.add("+");
                    publicRecipesAdapter.notifyDataSetChanged();
                }

            }
        } );

        //when a tag is long-pressed, edit it.
        publicTagsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(publicTags.get(i).equals("+")){
                    return false;
                }
                currentIndexPublic = i;
                buttonToEditTagsPublic.setVisibility(View.VISIBLE);
                editTextForTagsPublic.setVisibility(View.VISIBLE);
                editTextForTagsPublic.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(editTextForTagsPublic, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });


        //when a public recipe is pressed, go to that recipe's page
        publicRecipesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                openPublicRecipe(i);
            }
        });

        //save any edits made to a tag
        buttonToEditTagsPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextForTagsPublic.getText().toString().equals("")){
                    publicTags.remove(currentIndexPublic);
                    publicTagsAdapter.notifyDataSetChanged();
                    editTextForTagsPublic.setText("");
                    editTextForTagsPublic.setVisibility(View.INVISIBLE);
                    buttonToEditTagsPublic.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                else if(editTextForTagsPublic.getText().toString().equals("+")){
                    //do nothing, as we don't want this specific character to be a tag
                    editTextForTagsPublic.setText(R.string.noPlus);
                }
                else{
                    publicTags.set(currentIndexPublic, editTextForTagsPublic.getText().toString());
                    publicTagsAdapter.notifyDataSetChanged();
                    editTextForTagsPublic.setText("");
                    editTextForTagsPublic.setVisibility(View.INVISIBLE);
                    buttonToEditTagsPublic.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

    }

    public void openHomePage(){
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }

    public void openPublicRecipe(int i){
        Intent intent = new Intent(this, individualPublicRecipePage.class);
        intent.putExtra("Recipe", dbHandler.getInstance().getDBRecipeName(publicRecipes.get(i)));
        startActivity(intent);
    }

    public void manageTimers(){
        Fragment timeFrag = new TimerFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.publicLayout, timeFrag).commit();
    }
}
