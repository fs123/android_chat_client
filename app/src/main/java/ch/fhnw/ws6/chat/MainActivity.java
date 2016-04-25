package ch.fhnw.ws6.chat;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ch.fhnw.ws6.chat.actorsystem.Consumer;

public class MainActivity extends AppCompatActivity {

    ChatClient client = new ChatClient();
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        public void accept(String s) {
                            listItems.add("> " + s);
                            adapter.notifyDataSetChanged();
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
