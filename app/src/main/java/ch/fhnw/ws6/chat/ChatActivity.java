package ch.fhnw.ws6.chat;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ch.fhnw.ws6.chat.actorsystem.Consumer;

public class ChatActivity extends AppCompatActivity {

    ChatClient client;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        String nickname = intent.getStringExtra("nickname");

        LoginTask loginTask = new LoginTask();
        loginTask.execute(ip, port, nickname);

        final ListView listView = (ListView) findViewById(R.id.messageList);
        assert listView != null;
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        client.stop();
    }

    class LoginTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String ip = params[0];
            String port = params[1];
            String nickname = params[2];

            client = new ChatClient(ip, port, configs());
            boolean loginRes = client.login(nickname, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    Log.d("login-tag", s);
                    publishProgress(s);
                }
            });

            return loginRes;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            listItems.add("> " + values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                final EditText messageInput = (EditText) findViewById(R.id.messageInput);
                assert messageInput != null;
                ImageButton sendButton = (ImageButton) findViewById(R.id.sendButton);
                assert sendButton != null;
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
            } else {
                client.stop();
                Intent intentRet = new Intent();
                intentRet.putExtra("errorMsg", "Login failed");
                setResult(-1, intentRet);
                finish();
            }
        }
    }

    private List<String> configs() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        String ipString = String.format(
                Locale.ENGLISH,
                "%d.%d.%d.%d",
                (ip & 0xff),
                (ip >> 8 & 0xff),
                (ip >> 16 & 0xff),
                (ip >> 24 & 0xff));

        return Arrays.asList(
                "akka.remote.netty.tcp.hostname = " + ipString,
//                "akka.remote.netty.tcp.hostname = localhost",
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
            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }
            String config = buf.toString();
            Log.d("CHAT APP", "CONFIG: " + path + "--> " + config);
            ConfigFactory.parseString(config).resolve();
            return config;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (json != null)
                    json.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
