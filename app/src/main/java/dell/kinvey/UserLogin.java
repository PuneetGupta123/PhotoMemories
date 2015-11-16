package dell.kinvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;


public class UserLogin extends Activity {
    String My_App_Key="kid_-ygWEFrCwe";
    String  My_App_Secret="71ba431811634b70b8c2839d291f8787";
    Client mKinveyClient;
    //Activity activity=this;
    EditText name,password,email,confirmPassword;
    String uName,uPassword,uEmail,uConfirmPassword;
    Button SignUp,SignIn;
    Integer Flag=0;
    Context c=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mKinveyClient = new Client.Builder(My_App_Key,My_App_Secret,this.getApplicationContext()).build();
        name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email);
        SignUp=(Button)findViewById(R.id.SignUp);

        SignIn=(Button)findViewById(R.id.SignIn);
        confirmPassword=(EditText)findViewById(R.id.confirm_password);
        uName=name.getText().toString();
        uPassword=password.getText().toString();
        uConfirmPassword=confirmPassword.getText().toString();
        uEmail=email.getText().toString();
        mKinveyClient.ping(new KinveyPingCallback() {
            public void onFailure(Throwable t) {
                Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
                Flag=1;
            }
            public void onSuccess(Boolean b) {
                Toast.makeText(c,"Kinvey Ping Succcess" ,Toast.LENGTH_SHORT).show();
                Flag=0;
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uName=name.getText().toString();
                uPassword=password.getText().toString();
                if(uName.equals(""))
                {
                    Toast.makeText(c,"Please Enter User Name",Toast.LENGTH_LONG).show();
                }
                else if(uPassword.equals(""))
                {
                    Toast.makeText(c,"Please enter Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mKinveyClient.user().login(uName, uPassword, new KinveyUserCallback() {
                        @Override
                        public void onFailure(Throwable t) {
                            CharSequence text = "Wrong username or password.";
                            if(!haveNetworkConnection())
                            {
                                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onSuccess(User u) {
                            CharSequence text = "Welcome back," + u.getUsername() + " :)";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("UserName",u.getUsername());
                            editor.putString("Password",uPassword);
                            editor.commit();
                            Intent i = new Intent(UserLogin.this, Custom_camera.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });
                }

            }
        });





        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uName=name.getText().toString();
                uPassword=password.getText().toString();
                uConfirmPassword=confirmPassword.getText().toString();
                uEmail=email.getText().toString();

                if(uName.equals(""))
                {
                    Toast.makeText(c,"Please Enter User Name",Toast.LENGTH_LONG).show();
                }
                else if(uEmail.equals(""))
                {
                    Toast.makeText(c,"Please Enter your Email ID",Toast.LENGTH_LONG).show();
                }
                else if(uConfirmPassword.equals(""))
                {
                    Toast.makeText(c,"Please Confirm Password",Toast.LENGTH_LONG).show();
                }
                else if(uPassword.equals(""))
                {
                    Toast.makeText(c,"Please Enter Password",Toast.LENGTH_LONG).show();
                }
                else if(!uPassword.equals(uConfirmPassword))
                {
                    Toast.makeText(c,"Passwords don't match",Toast.LENGTH_LONG).show();
                }
                else if(Flag==0||Flag==1&&haveNetworkConnection())
                {
                    Flag=0;

                    mKinveyClient.user().create(uName, uPassword, new KinveyUserCallback() {
                        @Override
                        public void onFailure(Throwable t) {
                            if(!haveNetworkConnection())
                            {
                                Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                   String text = "Could not sign up. " + t.toString();

                                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSuccess(User u) {
                            CharSequence text = u.getUsername() + ", your account has been created.";
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("UserName",u.getUsername());
                            editor.putString("Password",uPassword);
                            editor.commit();
                            mKinveyClient.user().put("email", uEmail);
                            //mKinveyClient.user().put("password",uPassword);
                            mKinveyClient.user().update(new KinveyUserCallback() {
                                @Override
                                public void onFailure(Throwable e) {

                                }

                                @Override
                                public void onSuccess(User u) {
                                    Log.i("Finally Username",u.getUsername());
                                }
                            });
                            Intent i = new Intent(UserLogin.this, Custom_camera.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    });
                }
                else if(Flag==1&&!haveNetworkConnection())
                {
                      Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login, menu);
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
        else if(id==R.id.action_logout)
        {
            mKinveyClient.user().logout().execute();
            Toast.makeText(c,"logged Out",Toast.LENGTH_LONG).show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UserName","");
            editor.putString("Password","");
            editor.commit();

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
