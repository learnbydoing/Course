package edu.depaul.csc472.restaurantlist;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ListActivity {

    private static final String TAG = "MyActivity";
    private static final int RATING = 100; // request code
    private Restaurant selectedRestaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setListAdapter(new RestaurantAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        selectedRestaurant = Restaurants[position];
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);

        intent.putExtra("Restaurant", selectedRestaurant);
        startActivityForResult(intent, RATING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult"); // + data.getFloatExtra("WineRating", 4));
        if (requestCode == RATING) {
            if (resultCode == RESULT_OK && data != null) {
                selectedRestaurant.setRating(data.getFloatExtra("Rating", 4));
                ((RestaurantAdapter) getListAdapter()).notifyDataSetChanged();
            }
        }
    }

    // More efficient version of adapter
    static class RestaurantAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Map<Restaurant.Type, Bitmap> icons;
        private Map<Integer, Bitmap> ratings;

        RestaurantAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            icons = new HashMap<Restaurant.Type, Bitmap>();

            for (Restaurant.Type type : Restaurant.Type.values()) {
                icons.put(type, BitmapFactory.decodeResource(context.getResources(),
                        Restaurant.getIconResource(type)));
            }

            ratings = new HashMap<Integer, Bitmap>();
            ratings.put(1, BitmapFactory.decodeResource(context.getResources(), R.drawable.rating1));
            ratings.put(2, BitmapFactory.decodeResource(context.getResources(), R.drawable.rating2));
            ratings.put(3, BitmapFactory.decodeResource(context.getResources(), R.drawable.rating3));
            ratings.put(4, BitmapFactory.decodeResource(context.getResources(), R.drawable.rating4));
            ratings.put(5, BitmapFactory.decodeResource(context.getResources(), R.drawable.rating5));
        }

        @Override
        public int getCount() {
            return Restaurants.length;
        }

        @Override
        public Object getItem(int i) {
            return Restaurants[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View row = convertView;
            if (row == null) {
                row = inflater.inflate(R.layout.restaurant_list_item, parent, false);
                holder = new ViewHolder();
                holder.icon = (ImageView) row.findViewById(R.id.image);
                holder.name = (TextView) row.findViewById(R.id.text1);
                holder.location = (TextView) row.findViewById(R.id.location);
                holder.rating = (ImageView) row.findViewById(R.id.rating);
                holder.reviews = (TextView) row.findViewById(R.id.reviews);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            Restaurant restaurant = Restaurants[position];
            holder.name.setText(restaurant.getName());
            holder.location.setText(restaurant.getLocation());
            holder.icon.setImageBitmap(icons.get(restaurant.getType()));
            int ratingvalue = (int)(Math.round(restaurant.getRating()));
            holder.rating.setImageBitmap(ratings.get(ratingvalue));
            holder.reviews.setText(restaurant.getReviewText());
            return row;
        }

        static class ViewHolder {
            TextView name;
            TextView location;
            ImageView rating;
            TextView reviews;
            ImageView icon;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static final Restaurant[] Restaurants = {
            new Restaurant("Alinea",
                    Restaurant.Type.Restaurant,
                    "1723 North Halsted St., Chicago, IL 60614-5501",
                    4.0f,
                    88,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/02/86/93/20/filename-ainea-1-jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/3a/a2/3c/photo1jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/3a/a2/3a/photo0jpg.jpg"
            ),
            new Restaurant("Bavette's Bar and Boeuf",
                    Restaurant.Type.Restaurant,
                    "218 W Kinzie, Chicago, IL 60654 (Wells/Franklin)",
                    5.0f,
                    12,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/03/0b/9d/de/bavette-s-bar-and-boeuf.jpg",
                    "http://media-cdn.tripadvisor.com/media/daodao/photo-s/09/27/2b/71/caption.jpg",
                    "http://media-cdn.tripadvisor.com/media/daodao/photo-s/09/27/2b/70/caption.jpg"),
            new Restaurant("Polo Cafe & Catering Bridgeport",
                    Restaurant.Type.Restaurant,
                    "3322 S Morgan St, Chicago, IL 60608",
                    1.0f,
                    56,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/06/0a/cf/9f/polo-cafe-catering-bridgeport.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/41/5e/27/photo2jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/41/5e/24/photo0jpg.jpg"),
            new Restaurant("Garrett Popcorn Shops",
                    Restaurant.Type.Dessert,
                    "625 N Michigan Ave, Chicago, IL 60611",
                    3.0f,
                    132,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/02/65/c5/ae/filename-img-0444-jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/2e/ef/01/photo1jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/04/14/fe/11/garrett-popcorn-shops.jpg"),
            new Restaurant("Glazed and Infused",
                    Restaurant.Type.Dessert,
                    "30 E Hubbard, Chicago, IL 60611",
                    4.0f,
                    94,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/06/5f/29/0c/blueberry-lemon-donut.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/05/0d/eb/photo2jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/05/0d/ea/photo1jpg.jpg"),
            new Restaurant("Swirlz Cupcakes",
                    Restaurant.Type.Dessert,
                    "705 W Belden Ave, Chicago, IL 60614-3301",
                    5.0f,
                    76,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/02/f8/84/b1/swirlz-cupcakes.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/29/28/70/photo0jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/29/28/70/photo0jpg.jpg"),
            new Restaurant("Wildberry Pancakes and Cafe",
                    Restaurant.Type.CoffeeTea,
                    "130 E Randolph St, Chicago, IL 60601-6207",
                    1.0f,
                    33,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/02/76/f2/5a/filename-photo-35-jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/30/e6/98/super.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/03/01/2d/02/wildberry-pancakes-and.jpg"),
            new Restaurant("Intelligentsia Coffee",
                    Restaurant.Type.CoffeeTea,
                    "53 West Jackson Boulevard, Chicago, IL 60612-2512",
                    4.0f,
                    12,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/01/47/29/ee/great-coffee-intelligenista.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/08/a5/e6/6b/intelligentsia-coffee.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/01/2e/70/74/avatar056.jpg"),
            new Restaurant("West Egg Cafe",
                    Restaurant.Type.CoffeeTea,
                    "620 N Fairbanks Ct, Chicago, IL 60611-3011",
                    5.0f,
                    45,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/01/6b/b9/e7/hubby-s-meal.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/32/90/9c/west-egg-cafe.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/01/2a/fd/a3/avatar.jpg"),
            new Restaurant("Do - Rite Donuts",
                    Restaurant.Type.Bakeries,
                    "50 W Randolph St, Chicago, IL 60601",
                    1.0f,
                    72,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/05/a4/ea/c4/do-rite-donuts-coffee.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/11/77/f2/do-rite-donuts.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/07/37/69/2d/flyboy27.jpg"),
            new Restaurant("Doughnut Vault",
                    Restaurant.Type.Bakeries,
                    "401 1/2 N. Franklin St., Chicago, IL 60654",
                    4.0f,
                    16,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/05/ff/71/d6/doughnut-vault.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/1a/6c/c0/doughnut-vault.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/01/2e/70/6b/avatar049.jpg"),
            new Restaurant("Glazed and Infused",
                    Restaurant.Type.Bakeries,
                    "30 E Hubbard, Chicago, IL 60611",
                    3.0f,
                    19,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/06/5f/29/0c/blueberry-lemon-donut.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/05/0d/eb/photo2jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/05/0d/ea/photo1jpg.jpg"),
            new Restaurant("Lickity Split Frozen Custard & Sweets",
                    Restaurant.Type.IceCream,
                    "6056 N. Broadway, Chicago, IL 60660",
                    2.0f,
                    23,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/04/71/88/30/amazing.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/08/98/07/5f/photo0jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/08/12/43/ab/what-a-wonderful-place.jpg"),
            new Restaurant("Ghirardelli Ice Cream & Chocolate Shop",
                    Restaurant.Type.IceCream,
                    "830 N Michigan Ave, Chicago, IL 60611-2078",
                    5.0f,
                    21,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/03/73/c2/24/ghirardelli-ice-cream.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/09/3d/c6/b0/homemade-whipped-cream.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/09/3d/c6/af/homemade-whipped-cream.jpg"),
            new Restaurant("Jeni's Splendid Ice Creams",
                    Restaurant.Type.IceCream,
                    "3404 N. Southport Ave, Chicago, IL 60657",
                    4.0f,
                    69,
                    "",
                    "http://media-cdn.tripadvisor.com/media/photo-s/05/21/50/16/jeni-s-splendid-ice-creams.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-s/08/28/4c/83/photo0jpg.jpg",
                    "http://media-cdn.tripadvisor.com/media/photo-t/08/28/4c/83/photo0jpg.jpg"),

    };

}
