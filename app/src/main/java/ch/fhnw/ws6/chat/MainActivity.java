package ch.fhnw.ws6.chat;

import android.app.ListActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.fhnw.ws6.chat.actorsystem.Consumer;

public class MainActivity extends AppCompatActivity {

    ChatClient client;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new ChatClient(configs());
        setContentView(R.layout.activity_main);
        final ListView listView = (ListView) findViewById(R.id.messageList);
        assert listView != null;
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);

        listView.setVisibility(View.INVISIBLE);
        final LinearLayout listFooter = (LinearLayout) findViewById(R.id.listFooter);
        assert listFooter != null;
        listFooter.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText loginName = (EditText)findViewById(R.id.loginName);

        ImageButton loginButton = (ImageButton) findViewById(R.id.loginButton);
        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginName.getText().length() < 1) {
                    return;
                }

               if (client.login(loginName.getText().toString(), new Consumer<String>() {
                        @Override
                        public void accept(final String s) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    listItems.add("> " + s);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    })) {
                    ((LinearLayout) findViewById(R.id.loginForm)).setVisibility(View.INVISIBLE);
                    listFooter.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
                ;
            }
        });

        ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
        assert sendButton != null;
        final EditText messageInput = (EditText)findViewById(R.id.messageInput);
        assert messageInput != null;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageInput.getText().length() < 1) {
                    return;
                }
                client.sendMessage(messageInput.getText().toString());
                messageInput.setText("");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private List<String> configs() {
        return Arrays.asList(
                assetAsString("common.conf"),
                assetAsString("reference1.conf"),
                assetAsString("reference2.conf")
        );
    }

    private String assetAsString(String path) {
        InputStream json = null;
        try {
            json = getApplicationContext().getAssets().open(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            StringBuilder buf = new StringBuilder();
            String str;
            while ((str=in.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }
            String config = buf.toString();
            Log.d("CHAT APP", "CONFIG: " + path + "--> " + config);
            ConfigFactory.parseString(config).resolve();
            return config;
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            try {
                if (json != null)
                    json.close();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
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
