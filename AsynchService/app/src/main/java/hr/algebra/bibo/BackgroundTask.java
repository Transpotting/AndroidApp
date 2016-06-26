package hr.algebra.bibo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BackgroundTask extends AsyncTask<Integer, Integer, Void> {
    private static final String TAG ="TAG" ;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Service service;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream=null;
    private OutputStream outputStream=null;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private Boolean inBus=false;
    private Boolean wasInBus=false;
    private Boolean shown=false;

    private Context context;
    private String readMessage="";
    private int i=1;
    private int startDealy=0;

    public BackgroundTask(Service service, Context context) {
        this.service = service;
        this.context=context;
    }

    @Override
    protected Void doInBackground(Integer... params) {
        //int taskCount = params[0];
        //for (int i = 0; i < taskCount; i++) {



        while(true)
        {
            i++;
            if(bluetoothSocket.isConnected()==false)
            {
                try
                {
                    bluetoothSocket= bluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
                    bluetoothSocket.connect();
                    Log.d(TAG, "BUS connected");
                    outputStream = bluetoothSocket.getOutputStream();
                    inputStream = bluetoothSocket.getInputStream();
                }
                catch (IOException eConnectException)
                {
                    Log.d(TAG, "Could Not Connect To Socket", eConnectException);
                    closeSocket(bluetoothSocket);
                }
                finally {


                    if(readMessage.compareTo("PONG\r\n")==0 && inBus==false ){

                        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                        Notification.Builder builder =
                                new Notification.Builder(context)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("HOP-IN via BiBo")
                                        .setSound(soundUri)
                                        .setContentText("Welcome to Route 66");
                        mNotificationManager.notify(333, builder.build());
                        inBus=true;
                    }

                    if(readMessage.compareTo("PONG\r\n")==0 && inBus==true){
                        startDealy=1;
                    }
                    readMessage="";
                }
            }
            else {
                sendPing("WTH.PING\n");
                performSleep();
                waitPong();
                publishProgress(666);
                // inBus = false
            }

        }

        //String result  = service.getResources().getString(R.string.gotovo);
        //return result;

    }
    private void closeSocket(BluetoothSocket nOpenSocket)
    {
        try
        {
            nOpenSocket.close();
            Log.d(TAG, "Socket Closed");
        }
        catch (IOException ex)
        {
            Log.d(TAG, "Could Not Close Socket");
        }
    }

    private void sendPing(String input) {
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            outputStream.write(msgBuffer);                //write bytes over BT connection via outstream
            Log.d("WTH Bluetooth SEND: ","PING");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void waitPong() {

        byte[] buffer = new byte[256];
        int bytes;

        try {
            bytes = inputStream.read(buffer);
            readMessage = new String(buffer, 0, bytes);
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("WTH Bluetooth RECEIVE: ",readMessage);
    }

    //@Override
    //protected void onPostExecute() {
    //   service.stopSelf();
    //}

    @Override
    protected void onProgressUpdate(Integer... percentage) {
        String text = readMessage+" "+i;
        Toast.makeText(service, text, Toast.LENGTH_SHORT).show();
        if(i>20 && i<30 && inBus==true)
        {
            startDealy++;

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification.Builder builder =
                    new Notification.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("HOP-OUT via BiBo")
                            .setSound(soundUri)
                            .setContentText("Ticket: 3,50 EUR. Thank you!");
            mNotificationManager.notify(666, builder.build());
            inBus=false;
        }
        //Toast.makeText(service, "in: "+ inBus.toString() + " was: " + wasInBus.toString(), Toast.LENGTH_SHORT).show();
        //i++;
        //readMessage="";
    }

    private void performSleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
    }


    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setInStream(InputStream inStream) {
        this.inputStream = inStream;
    }

    public void setOutStream(OutputStream outStream) {
        this.outputStream = outStream;
    }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;
    }
}
