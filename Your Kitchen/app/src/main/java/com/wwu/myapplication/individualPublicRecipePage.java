package com.wwu.myapplication;

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
import android.widget.Toast;
import java.util.List;


public class individualPublicRecipePage extends AppCompatActivity {

    Button buttonToPublicPage;
    Button downloadButton;
    TextView recipeName;
    TextView servingSize;
    TextView recipeCalories;
    TextView recipeCost;
    TextView estimatedTime;
    ListView instructions;

    //private static List<String> sampleIngredients = new ArrayList<>(Arrays.asList("1 1/2 cups of milk", "2 cups of cereal"));
    //private static List<String> sampleTags = new ArrayList<>(Arrays.asList("breakfast", "easy", "cheap"));
    //private static List<String> sampleInstructions = new ArrayList<>(Arrays.asList("pour cereal into a bowl", "pour milk over cereal", "wait 2 minutes before eating for best results"));
    private static Recipe recipe = new Recipe();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = (Recipe) getIntent().getSerializableExtra("Recipe");

        setContentView(R.layout.activity_individual_public_recipe_page);

        buttonToPublicPage = findViewById(R.id.buttonToPublicPage);
        buttonToPublicPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPublicPage();
            }
        });

        initializeScreen();

        downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                downloadRecipe();
            }
        });

        //create ingredient list objects to feed to recycler view
        String[] ingredientsString = recipe.getIngredients().toArray(new String[0]);
        ingredientContent.buildIngredientList(ingredientsString);

        View recyclerView = findViewById(R.id.ingredient_list);
        assert recyclerView != null;
        setupRecycler((RecyclerView) recyclerView);

    }

    //@Override
    //public void onItemLongClick(AdapterView<?> parent, final View view,)

    private void setupRecycler(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ingredientContent.ITEMS));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final individualPublicRecipePage mParentActivity;
        private final List<ingredientContent.IngredientItem> mValues;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                }
        };

        SimpleItemRecyclerViewAdapter(individualPublicRecipePage parent, List<ingredientContent.IngredientItem> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_content, parent, false);

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

    public void openPublicPage(){
        Intent intent = new Intent(this, publicRecipesPage.class);
        startActivity(intent);
    }

    public void downloadRecipe(){
        Toast.makeText(getBaseContext(), getResources().getString(R.string.downloading_recipe) , Toast.LENGTH_SHORT ).show();
        localDBHandler.getInstance(this).AddRecipe(recipe);
    }


    public void initializeScreen(){

        //add recipe instructions to layout
        instructions = findViewById(R.id.public_recipe_list);
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

    }


}
