package edu.depaul.csc472.restaurant;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.InputStream;

import edu.depaul.csc472.restaurant.Model.Restaurant;
import edu.depaul.csc472.restaurant.Model.RestaurantList;

/**
 * A fragment representing a single Restaurant detail screen.
 * This fragment is either contained in a {@link RestaurantListActivity}
 * in two-pane mode (on tablets) or a {@link RestaurantDetailActivity}
 * on handsets.
 */
public class RestaurantDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Restaurant restaurant;

    public interface DetailCallbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemChanged();
    }

    private DetailCallbacks mCallbacks;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RestaurantDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            restaurant = RestaurantList.Restaurant_Map.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (restaurant != null) {
            TextView name = (TextView) rootView.findViewById(R.id.text1);
            TextView location = (TextView) rootView.findViewById(R.id.location);
            ImageView icon = (ImageView) rootView.findViewById(R.id.image);
            RatingBar rating = (RatingBar) rootView.findViewById(R.id.rating);
            TextView reviews = (TextView) rootView.findViewById(R.id.reviews);

            name.setText(restaurant.getName());
            location.setText(restaurant.getLocation());
            icon.setImageResource(Restaurant.getIconResource(restaurant.getType()));
            rating.setRating(restaurant.getRating());
            reviews.setText(restaurant.getLongReviewText());
            rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    restaurant.setRating(v);
                    if (mCallbacks != null) {
                        mCallbacks.onItemChanged();
                    }
                }
            });

            // show The Image
            if (restaurant.getImage1().length() != 0) {
                new DownloadImageTask((ImageView) rootView.findViewById(R.id.image1), (TextView) rootView.findViewById(R.id.download1))
                        .execute(restaurant.getImage1());
            }
            // show The Image
            if (restaurant.getImage2().length() != 0) {
                new DownloadImageTask((ImageView) rootView.findViewById(R.id.image2), (TextView) rootView.findViewById(R.id.download2))
                        .execute(restaurant.getImage2());
            }
            // show The Image
            if (restaurant.getImage3().length() != 0) {
                new DownloadImageTask((ImageView) rootView.findViewById(R.id.image3), (TextView) rootView.findViewById(R.id.download3))
                        .execute(restaurant.getImage3());
            }
        }

        return rootView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        TextView txtDownload;

        public DownloadImageTask(ImageView bmImage, TextView txtDown) {
            this.bmImage = bmImage;
            this.txtDownload = txtDown;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            txtDownload.setVisibility(View.GONE);
            bmImage.setVisibility(View.VISIBLE);
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment restaurantListFragment	= fragmentManager.findFragmentById(R.id.restaurant_list);
        if (restaurantListFragment instanceof DetailCallbacks) {
            mCallbacks = (DetailCallbacks) restaurantListFragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mCallbacks = null;
    }
}
