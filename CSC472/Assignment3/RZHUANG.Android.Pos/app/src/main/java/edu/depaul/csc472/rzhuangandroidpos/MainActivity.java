package edu.depaul.csc472.rzhuangandroidpos;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    private ArrayList<OrderItemModel> orderitems = new ArrayList<OrderItemModel>();
    private boolean addedtoorder = false;

    // Menu model
    class MenuItemModel {

        private String name;
        private double price;

        public MenuItemModel(String name, double price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Order model
    class OrderItemModel {

        private String itemname;
        private int quantity;
        private double unitprice;

        public OrderItemModel(String itemname, int quantity, double unitprice) {
            this.itemname = itemname;
            this.quantity = quantity;
            this.unitprice = unitprice;
        }

        @Override
        public String toString() {
            return itemname + " x " + quantity + " = " + quantity * unitprice;
        }
    }

    static final String[] MENUS = new String[] {
         "Cupcake", "Pizza", "Fried Chicken", "Cake", "Water", "Fish", "Pork", "Beef", "Chicken", "Fried Rice"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial UI
        createOrder();

        // Prepare data
        final HashMap<String, MenuItemModel> menus = new HashMap<String, MenuItemModel>();
        menus.put("Cupcake", new MenuItemModel("Cupcake", 3.79));
        menus.put("Pizza", new MenuItemModel("Pizza", 10.29));
        menus.put("Fried Chicken", new MenuItemModel("Fried Chicken", 14.89));
        menus.put("Cake", new MenuItemModel("Cake", 3.49));
        menus.put("Water", new MenuItemModel("Water", 1.09));
        menus.put("Fish", new MenuItemModel("Fish", 2.99));
        menus.put("Pork", new MenuItemModel("Pock", 4.49));
        menus.put("Beef", new MenuItemModel("Beef", 1.69));
        menus.put("Chicken", new MenuItemModel("Chicken", 3.49));
        menus.put("Fried Rice", new MenuItemModel("Fried Rice", 4.29));

        // Add AutoComplete to item name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MENUS);
        final AutoCompleteTextView tvItemName = (AutoCompleteTextView) findViewById(R.id.item_name);
        final EditText etUnitPrice = (EditText) findViewById(R.id.item_unit_price);
        tvItemName.setAdapter(adapter);

        // Add event to update unit price once item name is provided
        tvItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MenuItemModel selectedMenu = menus.get(tvItemName.getText().toString());
                if (selectedMenu != null)
                    etUnitPrice.setText(String.valueOf(selectedMenu.price));
            }
        });

        // Quick path to update summary after edit the unit price
        etUnitPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dismissKeyboard(etUnitPrice);
                    updateSummary();
                    handled = true;
                }
                return handled;
            }
        });

        etUnitPrice.setRawInputType(Configuration.KEYBOARD_12KEY);
        etUnitPrice.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("^(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                    String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                    StringBuilder sbPrice = new StringBuilder(userInput);

                    // Append zero in the front if two short
                    while (sbPrice.length() > 3 && sbPrice.charAt(0) == '0') {
                        sbPrice.deleteCharAt(0);
                    }
                    // Append zero at the end if two short
                    while (sbPrice.length() < 3) {
                        sbPrice.insert(0, '0');
                    }
                    // Insert dot at the last third position
                    sbPrice.insert(sbPrice.length() - 2, '.');

                    etUnitPrice.setText(sbPrice.toString());
                    // keeps the cursor always to the right
                    Selection.setSelection(etUnitPrice.getText(), sbPrice.toString().length());

                }

            }
        });

        // Add button click listener to NewItem
        Button btnNewOrder = (Button) findViewById(R.id.btnNewOrder);
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismissKeyboard(v);
                createOrder();
            }
        });

        // Add button click listener to NewItem
        Button btnNewItem = (Button) findViewById(R.id.btnNewItem);
        btnNewItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismissKeyboard(v);
                resetItem();
            }
        });

        // Add button click listener to Total
        Button btnTotal = (Button) findViewById(R.id.btnTotal);
        btnTotal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismissKeyboard(v);
                updateSummary();
            }
        });
    }

    // Create new order
    private void createOrder() {
        // Clear all order items
        orderitems.clear();

        // Reset all in the UI
        resetItem();

        TextView txtOrderTotal = (TextView) findViewById(R.id.order_total);
        TextView txtOrderSummary = (TextView) findViewById(R.id.order_summary);
        txtOrderTotal.setText("$0.00");
        txtOrderSummary.setText("");
    }

    // Reset current item
    private void resetItem() {
        // Clear the item inputs
        AutoCompleteTextView txtItemName = (AutoCompleteTextView) findViewById(R.id.item_name);
        EditText txtItemQuantity = (EditText) findViewById(R.id.item_quantity);
        EditText txtItemUnitPrice = (EditText) findViewById(R.id.item_unit_price);

        txtItemName.setText("");
        txtItemQuantity.setText("1");
        txtItemUnitPrice.setText("0.00");

        // Reset flag to notify this is a new item
        addedtoorder = false;
    }

    // Update summary
    private void updateSummary() {
        if (addedtoorder) {
            showMessage("This item has already been added to the order, please create a new item!");
            return;
        }

        AutoCompleteTextView txtItemName = (AutoCompleteTextView) findViewById(R.id.item_name);
        EditText txtItemQuantity = (EditText) findViewById(R.id.item_quantity);
        EditText txtItemUnitPrice = (EditText) findViewById(R.id.item_unit_price);


        // Check whether item name is empty
        String itemname = txtItemName.getText().toString();
        if (TextUtils.isEmpty(itemname)){
            showMessage("The item name cannot be empty!");
            return;
        }

        // Check quantity value
        if (TextUtils.isEmpty(txtItemQuantity.getText().toString())){
            showMessage("The quantity cannot be empty!");
            return;
        }
        int quantity = 0;
        try {
            quantity = Integer.parseInt(txtItemQuantity.getText().toString());
            if (quantity <= 0) {
                showMessage("Quantity must be larger than 0!");
                return;
            }
        } catch(NumberFormatException nfe) {
            showMessage("Wrong input for quantity, must be numbers!");
            return;
        }

        // Check unit price
        if (TextUtils.isEmpty(txtItemUnitPrice.getText().toString())){
            showMessage("The unit price cannot be empty!");
            return;
        }
        double unitprice = 0;
        try {
            unitprice = Double.parseDouble(txtItemUnitPrice.getText().toString());
            if (unitprice <= 0) {
                showMessage("Unit price must be larger than 0!");
                return;
            }
        } catch(NumberFormatException nfe) {
            showMessage("Wrong input for unit price, must be numbers!");
            return;
        }

        orderitems.add(0, new OrderItemModel(itemname, quantity, unitprice));

        // update total price and summary
        updateOrderInfo();

        addedtoorder = true; //update the flag prevent from duplicated submission

    }

    // Update total price and summary
    private void updateOrderInfo(){
        double totalprice = 0;
        String totalsummary = "";
        for (OrderItemModel item : orderitems) {
            totalprice += (item.quantity * item.unitprice);
            if (!TextUtils.isEmpty(totalsummary))
                totalsummary += "\n";
            totalsummary += item.toString();
        }

        TextView txtOrderTotal = (TextView) findViewById(R.id.order_total);
        TextView txtOrderSummary = (TextView) findViewById(R.id.order_summary);
        DecimalFormat df = new DecimalFormat("#.00");
        txtOrderTotal.setText("$"+String.valueOf(df.format(totalprice)));
        txtOrderSummary.setText(totalsummary);
    }

    // Dismiss keyboard
    private void dismissKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    // Show error message
    private void showMessage(String messageText){
        int duration = Toast.LENGTH_SHORT;
        //int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this, messageText, duration);
        toast.show();
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
}

