package vamz.kst.fri.simplemessengerexample;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

public class RespondService extends Service {

    final public static int SPRAVA_A = 1;
    final public static int SPRAVA_B = 2;
    final public static int SPRAVA_OK = 1000;

    private Messenger _toActivityMessenger;
    private Messenger _respondServiceMessenger;


    public RespondService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        // create messenger with and insert service custom handler
        _respondServiceMessenger = new Messenger(new HandleServiceMsg());

        // this is reference for sending messages back to activity
        _toActivityMessenger = (Messenger) intent.getParcelableExtra("msg");

        return _respondServiceMessenger.getBinder();
    }


    /***
     * Handles messages send to service from activity
     */
    private class HandleServiceMsg extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RespondService.SPRAVA_A:
                    showMessage("Message type A received.");
                    break;
                case RespondService.SPRAVA_B:
                    showMessage("Message type B received.");
                    break;

                default:
                    super.handleMessage(msg);
            }
        }

        private void showMessage(String message){
            Log.i( "tmsg", message);
            try {
                _toActivityMessenger.send(Message.obtain(null, RespondService.SPRAVA_OK, 0,0));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
