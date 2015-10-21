package edu.depaul.csc472.restaurantlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by RZHUANG on 10/21/2015.
 */
public class DetailActivity extends Activity {
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_details);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
        Intent intent = getIntent();
        if (intent != null) {
            TextView name = (TextView) findViewById(R.id.text1);
            TextView location = (TextView) findViewById(R.id.text2);
            ImageView icon = (ImageView) findViewById(R.id.image);
            RatingBar rating = (RatingBar) findViewById(R.id.rating);
            TextView reviews = (TextView) findViewById(R.id.reviews);

            Restaurant restaurant = intent.getParcelableExtra("Restaurant");
            name.setText(restaurant.getName());
            location.setText(restaurant.getLocation());
            icon.setImageResource(Restaurant.getIconResource(restaurant.getType()));
            rating.setRating(restaurant.getRating());
            reviews.setText(restaurant.getReviewText());

            // show The Image
            if (restaurant.getImage1().length() != 0) {
                new DownloadImageTask((ImageView) findViewById(R.id.image1))
                        .execute(restaurant.getImage1());
            }
            // show The Image
            if (restaurant.getImage2().length() != 0) {
                new DownloadImageTask((ImageView) findViewById(R.id.image2))
                        .execute(restaurant.getImage2());
            }
            // show The Image
            if (restaurant.getImage3().length() != 0) {
                new DownloadImageTask((ImageView) findViewById(R.id.image3))
                        .execute(restaurant.getImage3());
            }
        }
    }

    @Override
    public void finish() {
        Intent ratingResult = new Intent();
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        ratingResult.putExtra("Rating", ratingBar.getRating());
        setResult(RESULT_OK, ratingResult);
        super.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                //URL url = new URL(urldisplay);
                //mIcon11 = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

