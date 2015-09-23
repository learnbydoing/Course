package edu.depaul.csc472.rzhuangandroidcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    enum CalculatorStatus {
        NumberRequired, // Require number
        AddClicked, // Add button is clicked
        EqualsClicked, // Equals button is clicked
        AddEquals // Add button clicked, but actioned as equal button
    };

    // Two numbers for sum calculation, only valid when they are both larger or equal to zero.
    private int number1 = -1, number2 = -1;
    // Initial the status
    private CalculatorStatus status = CalculatorStatus.NumberRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = (TextView) findViewById(R.id.txtResult);
        // Clear text
        tv.setText("");
        // Define all of the buttons
        Button b1 = (Button) findViewById(R.id.button1);
        Button b2 = (Button) findViewById(R.id.button2);
        Button b3 = (Button) findViewById(R.id.button3);
        Button b4 = (Button) findViewById(R.id.button4);
        Button b5 = (Button) findViewById(R.id.button5);
        Button b6 = (Button) findViewById(R.id.button6);
        Button b7 = (Button) findViewById(R.id.button7);
        Button b8 = (Button) findViewById(R.id.button8);
        Button b9 = (Button) findViewById(R.id.button9);
        Button b0 = (Button) findViewById(R.id.button0);
        Button bAdd = (Button) findViewById(R.id.buttonAdd);
        Button bEquals = (Button) findViewById(R.id.buttonEquals);

        // All number buttons share the same listener
        View.OnClickListener listenerNumber = new View.OnClickListener () {
            public void onClick (View v) {
                switch (status){
                    case NumberRequired:
                        tv.setText(tv.getText().toString() + ((Button) v).getText().toString());
                        break;
                    case AddClicked:
                        number1 = Integer.parseInt(tv.getText().toString());
                        tv.setText(((Button) v).getText());
                        status = CalculatorStatus.NumberRequired;
                        break;
                    case EqualsClicked:
                        tv.setText(((Button) v).getText());
                        number1 = -1;
                        number2 = -1;
                        status = CalculatorStatus.NumberRequired;
                        break;
                    case AddEquals:
                        tv.setText(((Button) v).getText());
                        status = CalculatorStatus.NumberRequired;
                        break;
                    default:
                        break;
                }
            }
        };

        // Listener for Add button
        View.OnClickListener listenerAdd = new View.OnClickListener () {
            public void onClick (View v) {
                if (!tv.getText().equals("")) {
                    if (number1 >= 0 && status == CalculatorStatus.NumberRequired){
                        // Same with Equals but consequence status is different
                        number2 = Integer.parseInt(tv.getText().toString());
                        int sum = number1 + number2;
                        tv.setText(String.valueOf(sum));
                        status  = CalculatorStatus.AddEquals;
                        number1 = sum;
                        number2 = -1;
                    }
                    else{
                        status = CalculatorStatus.AddClicked;
                    }
                }
            }
        };

        // Listener for Equals button
        View.OnClickListener listenerEquals = new View.OnClickListener () {
            public void onClick (View v) {
                if (number1 >= 0 && status == CalculatorStatus.NumberRequired){
                    number2 = Integer.parseInt(tv.getText().toString());
                    int sum = number1 + number2;
                    tv.setText(String.valueOf(sum));
                    status  = CalculatorStatus.EqualsClicked;
                    number1 = sum;
                    number2 = -1;
                }
            }
        };

        //Bind listener to buttons
        b1.setOnClickListener(listenerNumber);
        b2.setOnClickListener(listenerNumber);
        b3.setOnClickListener(listenerNumber);
        b4.setOnClickListener(listenerNumber);
        b5.setOnClickListener(listenerNumber);
        b6.setOnClickListener(listenerNumber);
        b7.setOnClickListener(listenerNumber);
        b8.setOnClickListener(listenerNumber);
        b9.setOnClickListener(listenerNumber);
        b0.setOnClickListener(listenerNumber);
        bAdd.setOnClickListener(listenerAdd);
        bEquals.setOnClickListener(listenerEquals);
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
