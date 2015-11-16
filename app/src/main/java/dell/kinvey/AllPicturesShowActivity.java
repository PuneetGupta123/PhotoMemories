package dell.kinvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyPingCallback;

import java.util.ArrayList;


public class AllPicturesShowActivity extends ActionBarActivity {
    RecyclerView recyclerView;
    ArrayList<GridViewItemData> arrayList;
    GridViewItemAdapter adapter;

    DatabaseHelper databaseHelper;
    String My_App_Key="kid_-ygWEFrCwe";
    String  My_App_Secret="71ba431811634b70b8c2839d291f8787";
    Client mKinveyClient;
    Activity activity;
    Integer Flag=0;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_pictures_show);
        mKinveyClient = new Client.Builder(My_App_Key,My_App_Secret,this.getApplicationContext()).build();
        c=this;
        activity=this;
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        arrayList=new ArrayList<GridViewItemData>();

        mKinveyClient.ping(new KinveyPingCallback() {
            public void onFailure(Throwable t) {
                Toast.makeText(c, "No Internet Connection", Toast.LENGTH_LONG).show();
                Flag=1;
            }
            public void onSuccess(Boolean b) {
                Toast.makeText(c,"Kinvey Ping Succcess" ,Toast.LENGTH_SHORT).show();
                Flag=0;

            }
        });

        databaseHelper=new DatabaseHelper(c);
        getAllImages();
        adapter=new GridViewItemAdapter(this,arrayList,mKinveyClient,activity);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }
//    public void initializeData()
//    {
//
//       arrayList=databaseHelper.getAllImagePaths();
//
//
//    }
    void getAllImages()
    {
        Toast.makeText(c,"Getting All Images",Toast.LENGTH_LONG).show();
        AsyncAppData<UserClass> myevents = mKinveyClient.appData("UserClass",UserClass.class);
        myevents.get(new KinveyListCallback<UserClass>()     {
            @Override
            public void onSuccess(UserClass[] results) {
                for(int i=0;i<results.length;i++)
                {
                    Toast.makeText(c,"Got them"+results.length,Toast.LENGTH_LONG).show();
                    arrayList.add(new GridViewItemData(results[i].getImagePath(),results[i].getImagePath(),results[i].getUserName()));
                    adapter=new GridViewItemAdapter(c,arrayList,mKinveyClient,activity);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(c,2);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);
                }
                Log.v("TAG", "received " + results.length + " events");
            }
            @Override
            public void onFailure(Throwable error)  {
                Toast.makeText(c,"Uh oh sorry"+error.toString(),Toast.LENGTH_LONG).show();
                adapter=new GridViewItemAdapter(c,arrayList,mKinveyClient,activity);
                GridLayoutManager gridLayoutManager=new GridLayoutManager(c,2);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(adapter);
                Log.e("TAG", "failed to fetchByFilterCriteria", error);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_pictures_show, menu);
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