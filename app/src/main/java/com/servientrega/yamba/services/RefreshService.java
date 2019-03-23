package com.servientrega.yamba.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.servientrega.yamba.R;
import com.servientrega.yamba.fakes.FakesYambaClient;
import com.servientrega.yamba.model.DbHelper;
import com.servientrega.yamba.model.Status;
import com.servientrega.yamba.model.StatusContact;
import com.servientrega.yamba.webclient.IYambaClient;
import com.servientrega.yamba.webclient.YambaClient;
import com.servientrega.yamba.webclient.YambaClientException;

import java.util.List;

public class RefreshService extends IntentService {

    static  String TAG = "RefreshService";

    public RefreshService(){
        super(TAG);
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RefreshService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreated");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroyed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"onHandleIntent");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final String username = prefs.getString("username","");
        final String password = prefs.getString("password","");

        if(TextUtils.isEmpty(username)||TextUtils.isEmpty((password))){
            //Toast.makeText(this, R.string.msg_update_credentials,Toast.LENGTH_LONG)
                  //  .show();
            return;
        }

        IYambaClient yambaClient =  new FakesYambaClient(username,password);// new YambaClient(username,password);

        //DbHelper dbHelper = new DbHelper(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        try{
            List<Status> timeline = yambaClient.getTimeline(5);
            for(Status status: timeline){

                values.clear();
                values.put(StatusContact.Column.ID,status.getId());
                values.put(StatusContact.Column.USER,status.getUser());
                values.put(StatusContact.Column.MESSAGE,status.getMessage());
                values.put(StatusContact.Column.CREATED_AT,status.getCreatedAt().getTime());

                //db.insertWithOnConflict(StatusContact.TABLE,null,values,SQLiteDatabase.CONFLICT_IGNORE);

                Uri uri = getContentResolver().insert(StatusContact.CONTENT_URI,values);
                if(uri !=null){
                    Log.d(TAG,String.format("%s: %s:",status.getUser(),status.getMessage()));
                }
            }
        }catch (YambaClientException e){
            Log.d(TAG,e.getMessage());
        }

    }
}
