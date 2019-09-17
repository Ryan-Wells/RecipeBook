package com.wwu.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class ingredientContent {

    /**
     * An array of sample (restaurant) items.
     */
    static final List<IngredientItem> ITEMS = new ArrayList<>();
    private static final Map<String, IngredientItem> ITEM_MAP = new HashMap<>();

    static void buildIngredientList(String[] ingredients){


        //clear current hash map
        ITEM_MAP.clear();
        //clear current ITEMS list
        ITEMS.clear();

        //add new items
        int i = 0;
        while (i <= ingredients.length - 1) {
            addItem(new IngredientItem(String.valueOf(i + 1), ingredients[i], false));
            i++;


        }
    }
        //private static String ingredients[] = {"ingredient1", "ingredient2", "ingredient3", "ingredient4", "ingredient5", "ingredient6",
         //       "ingredient7", "ingredient8", "ingredient9"};
    /* private static String ingredients[] = {"ingredient1"};

        public static final List<IngredientItem> ITEMS = new ArrayList<IngredientItem>();


        public static final Map<String, IngredientItem> ITEM_MAP = new HashMap<String, IngredientItem>();

        static {
        //add new items
        int i = 0;
        while (i <= ingredients.length - 1) {
            addItem(new IngredientItem(String.valueOf(i + 1), ingredients[i], false));
            i++;
        }

    }*/

    private static void addItem(IngredientItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    /* static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/

    /**
     * A restaurant item representing a piece of content.
     */
    public static class IngredientItem {
        public final String id;
        public final String content;
        final Boolean checked;

        IngredientItem(String id, String content, Boolean checked) {
            this.id = id;
            this.content = content;
            this.checked = checked;
        }

        @Override
        public String toString() {
            return content;
        }
    }

//}
}
