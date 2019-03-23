package com.servientrega.yamba.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.servientrega.yamba.R;
import com.servientrega.yamba.model.StatusContact;
import com.servientrega.yamba.services.RefreshService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_settings:

                startActivity(new Intent(this,SettingsActivity.class));
                return true;

            case R.id.action_tweet:

                startActivity(new Intent(this,StatusActivity.class));
                return true;

            case R.id.action_refresh:

                startService(new Intent(this, RefreshService.class));
                return true;

            case R.id.action_purge:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.diag_title_delete));
                builder.setMessage(getString(R.string.diag_msg_delete));
                builder.setIcon(android.R.drawable.ic_dialog_alert);

                builder.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int rows = getContentResolver()
                                        .delete(StatusContact.CONTENT_URI,null,null);

                                Toast.makeText(MainActivity.this,String.format("%s rows deleted",rows),Toast.LENGTH_LONG).show();
                            }
                        });

                builder.setNegativeButton(android.R.string.no,null);
                builder.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
