package edu.depaul.csc472.rzhuangandroidtvcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity_config extends Activity
        implements CompoundButton.OnCheckedChangeListener {
    private int favorite = 0;
    private int currentChannel = 1;
    List<Integer> numbers = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        RadioButton rdbLeft = (RadioButton) findViewById(R.id.rdbLeft);
        RadioButton rdbMiddle = (RadioButton) findViewById(R.id.rdbMiddle);
        RadioButton rdbRight = (RadioButton) findViewById(R.id.rdbRight);
        rdbLeft.setOnCheckedChangeListener(this);
        rdbMiddle.setOnCheckedChangeListener(this);
        rdbRight.setOnCheckedChangeListener(this);

        TextView txvFavorite = (TextView) findViewById(R.id.favorite_channel);
        txvFavorite.setText(String.valueOf(currentChannel));

        //Channel buttons
        Button btnChannel0 = (Button) findViewById(R.id.config_channel0);
        Button btnChannel1 = (Button) findViewById(R.id.config_channel1);
        Button btnChannel2 = (Button) findViewById(R.id.config_channel2);
        Button btnChannel3 = (Button) findViewById(R.id.config_channel3);
        Button btnChannel4 = (Button) findViewById(R.id.config_channel4);
        Button btnChannel5 = (Button) findViewById(R.id.config_channel5);
        Button btnChannel6 = (Button) findViewById(R.id.config_channel6);
        Button btnChannel7 = (Button) findViewById(R.id.config_channel7);
        Button btnChannel8 = (Button) findViewById(R.id.config_channel8);
        Button btnChannel9 = (Button) findViewById(R.id.config_channel9);
        Button btnChannelAdd = (Button) findViewById(R.id.config_channel_add);
        Button btnChannelMinus = (Button) findViewById(R.id.config_channel_minus);

        View.OnClickListener listenerNumber = new View.OnClickListener () {
            public void onClick (View v) {
                int num = Integer.parseInt(((Button) v).getText().toString());
                numbers.add(num);
                if (numbers.size() == 3) {
                    String strChannel = "";
                    for(int item: numbers) {
                        strChannel += String.valueOf(item);
                    }
                    currentChannel = Integer.parseInt(strChannel);
                    setCurrentChannel(currentChannel);
                    numbers.clear();
                }
            }
        };

        // Listener for Add button
        View.OnClickListener listenerAdd = new View.OnClickListener () {
            public void onClick (View v) {
                setCurrentChannel(currentChannel + 1);
                numbers.clear();
            }
        };
        // Listener for Minus button
        View.OnClickListener listenerMinus = new View.OnClickListener () {
            public void onClick (View v) {
                setCurrentChannel(currentChannel - 1);
                numbers.clear();
            }
        };

        //Bind listener to buttons
        btnChannel0.setOnClickListener(listenerNumber);
        btnChannel1.setOnClickListener(listenerNumber);
        btnChannel2.setOnClickListener(listenerNumber);
        btnChannel3.setOnClickListener(listenerNumber);
        btnChannel4.setOnClickListener(listenerNumber);
        btnChannel5.setOnClickListener(listenerNumber);
        btnChannel6.setOnClickListener(listenerNumber);
        btnChannel7.setOnClickListener(listenerNumber);
        btnChannel8.setOnClickListener(listenerNumber);
        btnChannel9.setOnClickListener(listenerNumber);
        btnChannelAdd.setOnClickListener(listenerAdd);
        btnChannelMinus.setOnClickListener(listenerMinus);

        //Intent
        Button btnConfigCancel = (Button) findViewById(R.id.config_cancel);
        btnConfigCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        Button btnConfigSave = (Button) findViewById(R.id.config_save);
        btnConfigSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validation - channel button
                if (favorite != 1 && favorite != 2 && favorite != 3) {
                    Toast.makeText(Activity_config.this, "Please choose one favorite channel button!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // validation - label
                EditText txtLabel = (EditText) findViewById(R.id.config_label);
                String lblText  = String.valueOf(txtLabel.getText());
                if (lblText == null || lblText.isEmpty()) {
                    Toast.makeText(Activity_config.this, "Please input label text!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lblText.length() < 2 || lblText.length() > 4) {
                    Toast.makeText(Activity_config.this, "The label must be between 2-4 letters in length!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent();
                //TvSettings settings = new TvSettings(favorite, lblText, currentChannel);
                data.putExtra("favorite", favorite);
                data.putExtra("label", lblText);
                data.putExtra("channel", currentChannel);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            String rdbText = String.valueOf(button.getText());
            if (rdbText.equals(getResources().getString(R.string.config_channel_button_left))) {
                favorite = 1;
            } else if (rdbText.equals(getResources().getString(R.string.config_channel_button_middle))) {
                favorite = 2;
            } else if (rdbText.equals(getResources().getString(R.string.config_channel_button_right))) {
                favorite = 3;
            }
        }
    }

    private void setCurrentChannel(int channel) {
        if (channel < 1 ) {
            channel = 1;
        }
        if (channel > 999) {
            channel = 999;
        }

        currentChannel = channel;
        TextView txvChannel = (TextView) findViewById(R.id.favorite_channel);
        txvChannel.setText(String.valueOf(currentChannel));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_config, menu);
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
