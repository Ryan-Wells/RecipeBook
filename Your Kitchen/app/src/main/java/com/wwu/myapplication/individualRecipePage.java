package com.wwu.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;


public class individualRecipePage extends AppCompatActivity {

    Button buttonToHomePage;
    Button buttonToEditRecipe;
    Button buttonToManageTimers;
    Button buttonToPublish;

    TextView recipeName;
    TextView servingSize;
    TextView recipeCalories;
    TextView recipeCost;
    TextView estimatedTime;

    ListView instructions;
    //private static ingredientContent sampleIngredients = new sampleIngredients

    //private static List<String> sampleIngredients = new ArrayList<>(Arrays.asList("2 slices of bread", "1 tbsp peanut butter", "1 tbsp jam"));
   // private static List<String> sampleTags = new ArrayList<>(Arrays.asList("lunch", "easy", "cheap"));
    //private static List<String> sampleInstructions = new ArrayList<>(Arrays.asList("spread jam on one slice of bread", "clean knife off", "spread peanut butter on other slice of bread", "put bread slices together and eat"));
    private static Recipe recipe = new Recipe();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        setContentView(R.layout.activity_individual_recipe_page);

        buttonToHomePage = findViewById(R.id.buttonToHomePage);
        buttonToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        buttonToEditRecipe = findViewById(R.id.buttonToEditRecipe);
        buttonToEditRecipe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openEditPage();
            }
        });

        buttonToManageTimers = findViewById(R.id.buttonToManageTimers);
        buttonToManageTimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageTimers();
            }
        });

        buttonToPublish = findViewById(R.id.buttonToPublish);
        buttonToPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishRecipe();
            }
        });

        //create ingredient list objects to feed to recycler view
        String[] ingredientsString = recipe.getIngredients().toArray(new String[0]);
        ingredientContent.buildIngredientList(ingredientsString);

        initializeScreen();

        View recyclerView = findViewById(R.id.ingredient_list);
        assert recyclerView != null;
        setupRecycler((RecyclerView) recyclerView);

    }

    private void setupRecycler(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ingredientContent.ITEMS));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final individualRecipePage mParentActivity;
        private final List<ingredientContent.IngredientItem> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };

        SimpleItemRecyclerViewAdapter(individualRecipePage parent,
                                      List<ingredientContent.IngredientItem> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_content, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mIdView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.ingredient_checkbox);
            }
        }
    }

    public void openHomePage(){
        Intent intent = new Intent(this, homePage.class);
        startActivity(intent);
    }

    public void openEditPage(){
        Intent intent = new Intent(this, editRecipePage.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }

    public void manageTimers(){
        Fragment timeFrag = new TimerFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().add(R.id.individualLayout, timeFrag).commit();
    }

    public void publishRecipe(){
        //This will add the recipe to the database and update it if it's already been posted
        dbHandler.getInstance().addNewRecipe(recipe);
    }

    public void initializeScreen(){

        //add recipe instructions to layout
        instructions = findViewById(R.id.individualRecipeList);
        final ArrayAdapter<String> recipeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipe.instructions);
        instructions.setAdapter(recipeAdapter);
        /*recipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()) {
            @Override
            public boolean onItemLongClick Object parent;
            (AdapterView<?> parent, final View view){

            }
        }*/

        //add recipe name to layout
        recipeName = findViewById(R.id.recipeName);
        recipeName.setText(recipe.name);

        //add serving size to layout
        servingSize = findViewById(R.id.servingSize);
        servingSize.setText(getString(R.string.serving_size, recipe.servingSize));

        //add calories to layout
        recipeCalories = findViewById(R.id.publicRecipeCalories);
        recipeCalories.setText(getString(R.string.calories, recipe.calories));

        //add cost to layout
        recipeCost = findViewById(R.id.publicRecipeCost);
        recipeCost.setText(getString(R.string.cost, recipe.costToMake));

        //add time to layout
        estimatedTime = findViewById(R.id.estimatedTime);
        estimatedTime.setText(getString(R.string.estimated_time, recipe.timeToMake));

        //add image to layout if it has an image added by the user


    }




}
