package hr.algebra.bibo;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class AsynchService extends Service
{
    private static final String TAG ="TAG" ;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean serviceOn = false;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket;
    private String tmp;
    private OutputStream mmOutStream = null;
    private InputStream mmInStream = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!serviceOn) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(device.getAddress());
                    Toast.makeText(this, device.getName(),
                            Toast.LENGTH_LONG).show();
                }
            }

            Toast.makeText(this, R.string.servis_pokrenut, Toast.LENGTH_SHORT).show();
            BackgroundTask bt = new BackgroundTask(this,this);
            bt.setBluetoothDevice(mBluetoothDevice);
            bt.setBluetoothAdapter(mBluetoothAdapter);
            mBluetoothAdapter.cancelDiscovery();

            try {
                mBluetoothSocket= mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            } catch (IOException e) {
                //e.printStackTrace();
            }

            bt.setInStream(mmInStream);
            bt.setOutStream(mmOutStream);
            bt.setBluetoothSocket(mBluetoothSocket);
            bt.execute();
            serviceOn = true;
        } else {
            Toast.makeText(this, R.string.servis_vec_radi,
                    Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.servis_zaustavljen, Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
