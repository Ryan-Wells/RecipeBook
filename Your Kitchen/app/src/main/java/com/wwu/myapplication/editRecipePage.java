package com.wwu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class editRecipePage extends AppCompatActivity {

    EditText editName;
    EditText editServingSize;
    EditText editCalories;
    EditText editCost;
    EditText editTime;

    ListView instructions;
    ListView ingredients;

    //private static List<String> sampleIngredients = new ArrayList<>(Arrays.asList("1 egg", "pot with water"));
    private static List<String> sampleTags = new ArrayList<>(Arrays.asList("easy", "cheap", "food"));
    //private static List<String> sampleInstructions = new ArrayList<>(Arrays.asList("Bring water to a boil", "put egg into water", "remove egg after 8 minutes", "remove shell from egg and serve"));
    private static Recipe recipe = new Recipe();
    int currentIngredientIndex = -1;
    int currentInstructionIndex = -1;


    /**
     * The recipe this activity is presenting.
     */
    //List<String> instructionsTest = new ArrayList<>(Arrays.asList("step 1", "step 2", "step 3"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        setContentView(R.layout.activity_edit_recipe_page);

        final ListView ingredientsList = findViewById(R.id.editIngredientsList);
        ingredients = ingredientsList;
        final ListView instructionsList = findViewById(R.id.editInstructionsList);
        instructions = instructionsList;
        //final ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipe.ingredients);
        final Button buttonToEditIngredients = findViewById(R.id.buttonToEditIngredients);
        final Button buttonToEditInstructions = findViewById(R.id.buttonToEditInstructions);
        final Button buttonToDeleteIngredients = findViewById(R.id.buttonToDeleteIngredients);
        final Button buttonToDeleteInstructions = findViewById(R.id.buttonToDeleteInstructions);
        final Button buttonToDeleteRecipe = findViewById(R.id.buttonToDeleteRecipe);

        //List<String> ingredients = new ArrayList<>(Arrays.asList("1 egg", "pot with water"));
        final EditText ingredientEdit= findViewById(R.id.ingredientEdit);
        final EditText instructionEdit= findViewById(R.id.instructionEdit);
        final List<String> ingredients = recipe.getIngredients();
        final List<String> instructions = recipe.getInstructions();

        //for(int i = 0; i < recipe.getIngredients().size()-1; i++){
            //ingredients.set(i, recipe.getIngredients().get(i));
        //}

        final ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientAdapter.setNotifyOnChange(true);
        ingredientsList.setAdapter(ingredientAdapter);

        final ArrayAdapter<String> instructionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, instructions);
        instructionAdapter.setNotifyOnChange(true);
        instructionsList.setAdapter(instructionAdapter);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initializeScreen();

        //String[] ingredientsString = recipe.getIngredients().toArray(new String[0]);
        //ingredientContent.buildIngredientList(ingredientsString);

        Button buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        Button buttonToRecipePage = findViewById(R.id.buttonToRecipePage);
        buttonToRecipePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecipePage();
            }
        });


        ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if user presses '+', create a new ingredient
                if(ingredients.get(i).equals("+") && i == ingredients.size() - 1){
                    ingredients.set(i, getResources().getString(R.string.new_ingredient));
                    ingredients.add("+");
                    currentIngredientIndex = i;
                    ingredientsList.invalidateViews();
                    ingredientAdapter.notifyDataSetChanged();
                    ingredientsList.setSelection(ingredientAdapter.getCount() - 1);
                    buttonToEditIngredients.setVisibility(View.VISIBLE);
                    ingredientEdit.setVisibility(View.VISIBLE);
                    buttonToDeleteIngredients.setVisibility(View.VISIBLE);
                    ingredientEdit.requestFocus();
                }

            }
        });

        ingredientsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(ingredients.get(i).equals("+") && i == ingredients.size() - 1){
                    return false;
                }
                currentIngredientIndex = i;
                buttonToEditIngredients.setVisibility(View.VISIBLE);
                ingredientEdit.setVisibility(View.VISIBLE);
                buttonToDeleteIngredients.setVisibility(View.VISIBLE);
                ingredientEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(ingredientEdit, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        //save any edits made to a ingredient
        buttonToEditIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ingredientEdit.getText().toString().equals("")){
                    ingredients.remove(currentIngredientIndex);
                    ingredientEdit.setText("");
                    ingredientEdit.setVisibility(View.INVISIBLE);
                    buttonToEditIngredients.setVisibility(View.INVISIBLE);
                    buttonToDeleteIngredients.setVisibility(View.INVISIBLE);
                    ingredientsList.invalidateViews();
                    ingredientAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                else if(ingredientEdit.getText().toString().equals("+")) {
                    //do nothing, as we don't want to add this specific character to the list of tags
                    ingredientEdit.setText(R.string.noPlus);
                }
                else {
                    ingredients.set(currentIngredientIndex, ingredientEdit.getText().toString());
                    //ingredientAdapter.notifyDataSetChanged();
                    ingredientEdit.setText("");
                    ingredientEdit.setVisibility(View.INVISIBLE);
                    buttonToEditIngredients.setVisibility(View.INVISIBLE);
                    buttonToDeleteIngredients.setVisibility(View.INVISIBLE);
                    ingredientsList.invalidateViews();
                    ingredientAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

            }
        });

        //delete an ingredient
        buttonToDeleteIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientEdit.setVisibility(View.INVISIBLE);
                buttonToEditIngredients.setVisibility(View.INVISIBLE);
                buttonToDeleteIngredients.setVisibility(View.INVISIBLE);
                ingredients.remove(currentIngredientIndex);
                ingredientsList.invalidateViews();
                ingredientAdapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });




        instructionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if user presses '+', create a new ingredient
                if(instructions.get(i).equals("+") && i == instructions.size() - 1){
                    instructions.set(i, getResources().getString(R.string.new_instruction));
                    //instructions.set(currentInstructionIndex, instructionEdit.getText().toString());
                    //instructions.add("+");
                    currentInstructionIndex = i;
                    instructionAdapter.add("+");
                    instructionsList.setSelection(instructionAdapter.getCount() - 1);
                    buttonToEditInstructions.setVisibility(View.VISIBLE);
                    instructionEdit.setVisibility(View.VISIBLE);
                    buttonToDeleteInstructions.setVisibility(View.VISIBLE);
                    instructionsList.invalidateViews();
                    instructionAdapter.notifyDataSetChanged();
                    instructionEdit.requestFocus();
                }

            }
        });

        instructionsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(instructions.get(i).equals("+") && i == instructions.size() - 1){
                    return false;
                }
                currentInstructionIndex = i;
                buttonToEditInstructions.setVisibility(View.VISIBLE);
                instructionEdit.setVisibility(View.VISIBLE);
                buttonToDeleteInstructions.setVisibility(View.VISIBLE);
                instructionEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(instructionEdit, InputMethodManager.SHOW_IMPLICIT);
                return true;
            }
        });

        //save any edits made to a ingredient
        buttonToEditInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(instructionEdit.getText().toString().equals("")){
                    instructions.remove(currentInstructionIndex);
                    instructionEdit.setText("");
                    instructionEdit.setVisibility(View.INVISIBLE);
                    buttonToEditInstructions.setVisibility(View.INVISIBLE);
                    buttonToDeleteInstructions.setVisibility(View.INVISIBLE);
                    instructionsList.invalidateViews();
                    instructionAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                else if(instructionEdit.getText().toString().equals("+")) {
                    //do nothing, as we don't want to add this specific character to the list of instructions
                    instructionEdit.setText(R.string.noPlus);
                }
                else {
                    instructions.set(currentInstructionIndex, instructionEdit.getText().toString());
                    instructionEdit.setText("");
                    instructionEdit.setVisibility(View.INVISIBLE);
                    buttonToEditInstructions.setVisibility(View.INVISIBLE);
                    buttonToDeleteInstructions.setVisibility(View.INVISIBLE);
                    instructionsList.invalidateViews();
                    instructionAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }

            }
        });

         //delete an ingredient
        buttonToDeleteInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructionEdit.setVisibility(View.INVISIBLE);
                buttonToEditInstructions.setVisibility(View.INVISIBLE);
                buttonToDeleteInstructions.setVisibility(View.INVISIBLE);
                //String currentInstruction = instructions.get(currentInstructionIndex); //TEST
                //instructionAdapter.remove(currentInstruction); //TEST
                instructions.remove(currentInstructionIndex);
                instructionsList.invalidateViews();
                instructionAdapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });


        //delete a recipe
        buttonToDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAndReturn();
            }
        });


       /*
            //Find recipe using database id
            mItem = dbHandler.getInstance().getDBRecipeID(savedInstanceState.getString(ARG_ITEM_ID));
        */

       //open keyboard when editing instructions
        instructionEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(instructionEdit, InputMethodManager.SHOW_IMPLICIT);
                }
                if (!hasFocus){
                    instructionEdit.setVisibility(View.INVISIBLE);
                    buttonToEditInstructions.setVisibility(View.INVISIBLE);
                    buttonToDeleteInstructions.setVisibility(View.INVISIBLE);
                }
            }
        });

        //open keyboard when editing ingredients
        ingredientEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(ingredientEdit, InputMethodManager.SHOW_IMPLICIT);
                }
                if (!hasFocus){
                    ingredientEdit.setVisibility(View.INVISIBLE);
                    buttonToEditIngredients.setVisibility(View.INVISIBLE);
                    buttonToDeleteIngredients.setVisibility(View.INVISIBLE);
                }
            }
        });

    }


    public void openRecipePage(){
        Intent intent = new Intent(this, individualRecipePage.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    public void deleteAndReturn(){
        localDBHandler.getInstance(this).deleteRecipe(recipe.id);
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }

    public void saveChanges(){
        //If new recipe add to database
        List<String> instructionsList = getList((ArrayAdapter<String>) instructions.getAdapter());
        instructionsList.remove(instructionsList.size()-1);

        List<String> ingredientsList = getList((ArrayAdapter<String>) ingredients.getAdapter());
        ingredientsList.remove(ingredientsList.size()-1);

        if(recipe.id.equals("")){
            recipe = new Recipe(editName.getText().toString(), dbHandler.getInstance().getNewDBID(),
                    instructionsList, sampleTags, ingredientsList,
                    Integer.parseInt(editServingSize.getText().toString()),
                    Integer.parseInt(editCalories.getText().toString()));
        }else{
            recipe.name = editName.getText().toString();
            recipe.instructions = instructionsList;
            recipe.ingredients = ingredientsList;
            recipe.servingSize = Integer.parseInt(editServingSize.getText().toString());
            recipe.calories = Integer.parseInt(editCalories.getText().toString());
            recipe.costToMake = Float.parseFloat(editCost.getText().toString());
            recipe.timeToMake = Integer.parseInt(editTime.getText().toString());
        }
        localDBHandler.getInstance(this).AddRecipe(recipe);

        Toast.makeText(getBaseContext(), getResources().getString(R.string.recipe_saved_changes) , Toast.LENGTH_SHORT ).show();
        openRecipePage();
    }

    public void initializeScreen(){
        ListView instructionsList = findViewById(R.id.editInstructionsList);
        ListView ingredientsList = findViewById(R.id.editIngredientsList);
        final ArrayAdapter<String> recipeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipe.instructions);
        final ArrayAdapter<String> ingredientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipe.ingredients);
        instructionsList.setAdapter(recipeAdapter);
        ingredientsList.setAdapter(ingredientAdapter);
        /*recipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()) {
            @Override
            public boolean onItemLongClick Object parent;
            (AdapterView<?> parent, final View view){

            }
        }*/

        //String[] ingredientsString = recipe.getIngredients().toArray(new String[0]);
        //ingredientAdapter.addAll(ingredientsString);
        //ingredientAdapter.notifyDataSetChanged();
        ingredientAdapter.add("+");
        recipeAdapter.add("+");

        //add recipe name to layout
        editName = findViewById(R.id.editRecipeName);
        editName.setText(recipe.name);

        //add serving size to layout
        editServingSize = findViewById(R.id.editServingSize);
        editServingSize.setText(Integer.toString(recipe.servingSize));

        //add calories to layout
        editCalories = findViewById(R.id.editRecipeCalories);
        editCalories.setText(Integer.toString(recipe.calories));

        //add cost to layout
        editCost = findViewById(R.id.editRecipeCost);
        editCost.setText(Double.toString(recipe.costToMake));

        //add time to layout
        editTime = findViewById(R.id.editTime);
        editTime.setText(Integer.toString(recipe.timeToMake));
    }

    private List<String> getList(ArrayAdapter<String> adapter){
        List<String> newList = new ArrayList<>();
        for(int i=0 ; i<adapter.getCount() ; i++){
             newList.add(adapter.getItem(i));
        }
        return newList;
    }
}
