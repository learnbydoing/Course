package edu.depaul.csc472.rzhuangandroidtvcontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_dvr extends Activity
        implements CompoundButton.OnCheckedChangeListener{
    enum DvrState{
        Stopped,
        Playing,
        Paused,
        FastForwarding,
        FastRewinding,
        Recording
    }

    private DvrState currentState = DvrState.Stopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dvr);

        // Power
        TextView txtDvrPower = (TextView) findViewById(R.id.dvrpower);
        txtDvrPower.setText(R.string.tv_on);
        // Power
        Switch switchDvrPower = (Switch) findViewById(R.id.switchdvrpower);
        switchDvrPower.setChecked(true);
        switchDvrPower.setOnCheckedChangeListener(this);

        // State
        TextView txtDvrState = (TextView) findViewById(R.id.dvrstate);
        txtDvrState.setText(R.string.dvr_state_stopped);

        // Add listener
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnPause = (Button) findViewById(R.id.btnPause);
        Button btnFastForward = (Button) findViewById(R.id.btnFastForward);
        Button btnFastRewind = (Button) findViewById(R.id.btnFastRewind);
        Button btnRecord = (Button) findViewById(R.id.btnRecord);


        // Listener for buttons
        View.OnClickListener listenerPlay = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.Playing);
            }
        };
        btnPlay.setOnClickListener(listenerPlay);
        View.OnClickListener listenerStop = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.Stopped);
            }
        };
        btnStop.setOnClickListener(listenerStop);
        View.OnClickListener listenerPause = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.Paused);
            }
        };
        btnPause.setOnClickListener(listenerPause);
        View.OnClickListener listenerFastForward = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.FastForwarding);
            }
        };
        btnFastForward.setOnClickListener(listenerFastForward);
        View.OnClickListener listenerFastRewind = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.FastRewinding);
            }
        };
        btnFastRewind.setOnClickListener(listenerFastRewind);
        View.OnClickListener listenerRecord = new View.OnClickListener () {
            public void onClick (View v) {
                setState(DvrState.Recording);
            }
        };
        btnRecord.setOnClickListener(listenerRecord);

        //Intent
        Button btnToTv = (Button) findViewById(R.id.totv);
        btnToTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            setResult(RESULT_CANCELED);
            finish();
            }
        });
    }

    private void setState(DvrState state) {
        // State
        TextView txtDvrState = (TextView) findViewById(R.id.dvrstate);

        switch (state) {
            case Stopped:
                currentState = DvrState.Stopped;
                txtDvrState.setText(R.string.dvr_state_stopped);
                break;
            case Playing:
                if (currentState == DvrState.Stopped || currentState == DvrState.Paused ||
                        currentState == DvrState.FastForwarding ||
                        currentState == DvrState.FastRewinding) {
                    currentState = DvrState.Playing;
                    txtDvrState.setText(R.string.dvr_state_playing);
                }
                else {
                    if (currentState == DvrState.Recording) {
                        Toast.makeText(this, "The DVR is recording, can't play!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case Paused:
                if (currentState == DvrState.Playing) {
                    currentState = DvrState.Paused;
                    txtDvrState.setText(R.string.dvr_state_paused);
                }
                else {
                    if (currentState == DvrState.Recording) {
                        Toast.makeText(this, "The DVR is recording, can't pause!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "The DVR is not playing, can't pause!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case FastForwarding:
                if (currentState == DvrState.Playing) {
                    currentState = DvrState.FastForwarding;
                    txtDvrState.setText(R.string.dvr_state_fastforwarding);
                }
                else {
                    if (currentState == DvrState.Recording) {
                        Toast.makeText(this, "The DVR is recording, can't fast forward!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "The DVR is not playing, can't fast forward!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case FastRewinding:
                if (currentState == DvrState.Playing) {
                    currentState = DvrState.FastRewinding;
                    txtDvrState.setText(R.string.dvr_state_fastrewinding);
                }
                else {
                    if (currentState == DvrState.Recording) {
                        Toast.makeText(this, "The DVR is recording, can't fast rewind!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "The DVR is not playing, can't fast rewind!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case Recording:
                if (currentState == DvrState.Stopped) {
                    currentState = DvrState.Recording;
                    txtDvrState.setText(R.string.dvr_state_recording);
                }
                else {
                    Toast.makeText(this, "The DVR is not stopped, can't start record!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        changeEnabledControl(isChecked);
    }

    private void changeEnabledControl(boolean isChecked) {
        final TextView dvrPower = (TextView) findViewById(R.id.dvrpower);
        dvrPower.setText((isChecked ? R.string.tv_on : R.string.tv_off));

        if (isChecked) {
            TextView txtDvrState = (TextView) findViewById(R.id.dvrstate);
            currentState = DvrState.Stopped;
            txtDvrState.setText(R.string.dvr_state_stopped);
        }

        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnPause = (Button) findViewById(R.id.btnPause);
        Button btnFastForward = (Button) findViewById(R.id.btnFastForward);
        Button btnFastRewind = (Button) findViewById(R.id.btnFastRewind);
        Button btnRecord = (Button) findViewById(R.id.btnRecord);
        btnPlay.setEnabled(isChecked);
        btnStop.setEnabled(isChecked);
        btnPause.setEnabled(isChecked);
        btnFastForward.setEnabled(isChecked);
        btnFastRewind.setEnabled(isChecked);
        btnRecord.setEnabled(isChecked);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_dvr, menu);
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
