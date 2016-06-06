package ch.fhnw.ws6.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();

        final EditText ipText = (EditText)findViewById(R.id.ipText);
        final EditText portText = (EditText)findViewById(R.id.portText);
        final EditText nicknameText = (EditText)findViewById(R.id.nicknameText);

        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("ip", ipText.getText().toString());
        i.putExtra("port", portText.getText().toString());
        i.putExtra("nickname", nicknameText.getText().toString());

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == -1) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Connection Error");
            alert.setMessage(data.getStringExtra("errorMsg"));
            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // your action here
                }
            });
            alert.show();
        }
    }

}
