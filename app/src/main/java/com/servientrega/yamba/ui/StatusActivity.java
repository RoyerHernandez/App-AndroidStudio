package com.servientrega.yamba.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.servientrega.yamba.R;
import com.servientrega.yamba.fakes.FakesYambaClient;
import com.servientrega.yamba.util.NetworkHelper;
import com.servientrega.yamba.webclient.IYambaClient;
import com.servientrega.yamba.webclient.YambaClient;
import com.servientrega.yamba.webclient.YambaClientException;

import org.w3c.dom.Text;

public class StatusActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StatusActivity";
    private EditText editStatus;
    FloatingActionButton fab;
    private boolean networkOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editStatus = findViewById(R.id.editText);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        networkOk = NetworkHelper.hasNetworkAccess(this);

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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

    @Override
    public void onClick(View v) {
        String status =  editStatus.getText().toString();
        Log.i(TAG,"onClicked with status: " + status);

        if(editStatus.getText().length()>0){
            fab.setEnabled(false);
            new PostTask().execute(status);
        }
       // sendTweet(status);
    }

    private String sendTweet(String status) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String username = prefs.getString("username","");
        String password = prefs.getString("password","");


        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            startActivity(new Intent(this,SettingsActivity.class));
            return getString(R.string.msg_update_credentials);
        }
        if (networkOk) {


            /*new FakesYambaClient(username, password);*/
            IYambaClient yambaClient = new YambaClient(username, password);

            try {
                yambaClient.postStatus(status);
                return getString(R.string.msg_sendedtweet_ok);
            } catch (YambaClientException e) {
                e.printStackTrace();
                return getString(R.string.msg_sendtweet_fail);
            }
        } else {
            return getString(R.string.msg_no_connection);
        }
    }
    private void ShowResult(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public final class PostTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {

            return sendTweet(params[0]);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            ShowResult(result);

            editStatus.setText("");
            fab.setEnabled(true);
        }

    }

}
