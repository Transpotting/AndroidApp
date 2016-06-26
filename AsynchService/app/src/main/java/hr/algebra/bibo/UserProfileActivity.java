package hr.algebra.bibo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.map) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_achievments) {
            Intent intent = new Intent(getApplicationContext(), AchievmentActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_saldo) {
            Intent intent = new Intent(getApplicationContext(), SaldoActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_current) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_routing) {
            Intent intent = new Intent(getApplicationContext(), InvoiceActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
