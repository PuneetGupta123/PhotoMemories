package dell.kinvey;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;
    String My_App_Key="kid_-ygWEFrCwe";
    String  My_App_Secret="71ba431811634b70b8c2839d291f8787";
    Client mKinveyClient;

    TextView AppName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppName=(TextView)findViewById(R.id.app_name);
        Typeface type = Typeface.createFromAsset(getAssets(),"capture_it.ttf");
        AppName.setTypeface(type);
        mKinveyClient = new Client.Builder(My_App_Key,My_App_Secret,this.getApplicationContext()).build();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String username = preferences.getString("UserName", "");
                String password=preferences.getString("Password","");
                if(!mKinveyClient.user().isUserLoggedIn())
                {
                    if(!username.equals(""))
                    {
                        mKinveyClient.user().login(username, password , new KinveyUserCallback() {
                            @Override
                            public void onFailure(Throwable t) {
                                String text = "Could not log in. "+ t.toString() ;
                               // tv.setText(t.toString());
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onSuccess(User u) {
                                CharSequence text = u.getUsername() + "Welcome Back :)";
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SplashScreen.this, Custom_camera.class);
                                startActivity(i);
                            }
                        });
                    }
                    else if(username.equals(""))
                    {
                        Intent i = new Intent(SplashScreen.this, UserLogin.class);
                        startActivity(i);
                    }
                }
                else if(mKinveyClient.user().isUserLoggedIn())
                {
                    Intent i = new Intent(SplashScreen.this, Custom_camera.class);
                    startActivity(i);
                }



                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
