package vamz.kst.fri.simplemessengerexample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CustomServiceConnection _customServiceConnection;
    private ActivityMessageHandler _activityMessageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create instances of our handler and service connection
        _customServiceConnection = new CustomServiceConnection();
        _activityMessageHandler = new ActivityMessageHandler();

        // put out handler to new Messenger object
        Messenger msgr = new Messenger(_activityMessageHandler);

        // create intent,  put in messenger wit oir handler, set our service and send it
        Intent intent = new Intent(this, RespondService.class);
        intent.putExtra( "msg", msgr);
        bindService(intent, _customServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void bt_a_onclick(View view){
        _customServiceConnection.sendMessage(RespondService.SPRAVA_A);
    }

    public void bt_b_onclick(View view){
        _customServiceConnection.sendMessage(RespondService.SPRAVA_B);
    }

    /***
     * Class which handle messages from our activity
     */
    private class ActivityMessageHandler extends Handler {

        @Override
        public void handleMessage(Message message){
            // do action based on received message
            switch (message.what){
                case RespondService.SPRAVA_OK:
                    showMessage("Message was delivered to service.");
                    break;

                default:
                    super.handleMessage(message);
            }
        }
        private void showMessage(String message){
            Log.i("tmsg", message);
        }
    }

    /***
     * Class used for sending messages to service
     */
    private class CustomServiceConnection implements ServiceConnection {

        private Messenger _messenger;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            _messenger = new Messenger(service);
        }
        public void sendMessage(int messageType){
            // Create new message with set message type
            Message msg = Message.obtain(null, messageType, 0, 0);
            try {
                _messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

    }
}
