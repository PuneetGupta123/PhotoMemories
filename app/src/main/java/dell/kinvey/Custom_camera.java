package dell.kinvey;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.model.KinveyMetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Custom_camera extends ActionBarActivity {
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    Context c=this;
    FrameLayout preview;
    String My_App_Key="kid_-ygWEFrCwe";
    String  My_App_Secret="71ba431811634b70b8c2839d291f8787";
    Client mKinveyClient;
    String ImagePath;
    Activity activity=this;
    DatabaseHelper databaseHelper;
    Button captureButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        databaseHelper=new DatabaseHelper(c);
        mKinveyClient = new Client.Builder(My_App_Key,My_App_Secret,this.getApplicationContext()).build();
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);

         captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(captureButton.getText().equals("Capture"))
                {
                    Toast.makeText(c,"in capture",Toast.LENGTH_LONG).show();
                    if(!haveNetworkConnection())
                    {
                        Toast.makeText(c,"No Internet Connection",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        captureButton.setText("Uploading, Please Wait");
                        mCamera.takePicture(null, null, mPicture);
                        captureButton.setEnabled(false);
                    }

                }
               else
                {
                        captureButton.setText("Capture");
                        mCamera.startPreview();

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



    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */

    public Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
                String imagepath = getOutputImagePath();
            File pictureFile=new File(imagepath);
               ImagePath=imagepath;
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }

            Toast.makeText(c,"Saved",Toast.LENGTH_LONG).show();
            uploadImage(activity,imagepath);
//            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES), "Kinvey");
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                    Uri.parse("file://" + mediaStorageDir)));
            //mCamera = getCameraInstance();
//            Camera camera1=mCamera;
//            mCameraPreview = new CameraPreview(c, mCamera);
//            preview.addView(mCameraPreview);

        }
    };

    private static String getOutputImagePath() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Kinvey");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        //File mediaFile;
        String imagePath=mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg";
        //mediaFile = new File(imagePath);

        return imagePath;
    }
    public void uploadImage(final Activity activity,final String Imagepath)
    {
        File file = new File(Imagepath);
        FileMetaData metadata = new FileMetaData();  //create the FileMetaData object
        metadata.setId(Imagepath);
        metadata.setPublic(true);
        metadata.setFileName(Imagepath);
        metadata.put("UserName",mKinveyClient.user().getUsername());
        KinveyMetaData.AccessControlList acl = new KinveyMetaData.AccessControlList();
        acl.setGloballyReadable(true);
        metadata.setAcl(acl);

        mKinveyClient.file().upload(metadata, file, new UploaderProgressListener() {

            @Override
            public void onSuccess(final Void arg0) {
                // TODO Auto-generated method stub
//                Toast.makeText(c,"IMAGE UPLOADED",Toast.LENGTH_LONG).show();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "uploaded to backend" + arg0.toString(), Toast.LENGTH_SHORT).show();
//                        File file1=new File(ImagePath);
//                        file1.delete();
                        GridViewItemData gridViewItemData=new GridViewItemData(Imagepath,Imagepath,mKinveyClient.user().getUsername());
                        databaseHelper.addGridViewItemData(gridViewItemData);
                        Toast.makeText(activity, "Deleted From Phone" + arg0.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onFailure(final Throwable arg0) {
                // TODO Auto-generated method stub
                //tv.setText(arg0.toString());
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                       // Toast.makeText(activity, "Hello" + arg0.toString(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void progressChanged(final MediaHttpUploader uploader) throws IOException {

                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "uploading" + uploader.getUploadState().toString(), Toast.LENGTH_SHORT).show();
                        GridViewItemData gridViewItemData=new GridViewItemData(Imagepath,Imagepath,mKinveyClient.user().getUsername());
                        databaseHelper.addGridViewItemData(gridViewItemData);
                        Integer i=databaseHelper.getTableSize("boutique");
                        if(uploader.getUploadState().toString().equals("UPLOAD_COMPLETE"))
                        {
                            File file1=new File(ImagePath);
                            file1.delete();
                            captureButton.setText("Take Another Photo");
                            captureButton.setEnabled(true);
                            saveInUserClass(Imagepath,mKinveyClient.user().getUsername());
                        }

                        Toast.makeText(activity, "uploading + table size " +i.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }
    public void saveInUserClass(String imagepath,String Username)
    {
        UserClass userClass = new UserClass();
        userClass.setUserName(Username);
        userClass.setImagePath(imagepath);
        AsyncAppData<UserClass> myevents = mKinveyClient.appData("UserClass",UserClass.class);
        myevents.save(userClass, new KinveyClientCallback<UserClass>() {
            @Override
            public void onFailure(Throwable e) {
                Log.e("TAG", "failed to save event data", e);
            }
            @Override
            public void onSuccess(UserClass r) {
                Log.d("TAG", "saved photo for entity and userName "+ r.getUserName());
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_custom_camera, menu);
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
            mKinveyClient.user().logout().execute();
            Toast.makeText(c,"logged Out",Toast.LENGTH_LONG).show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("UserName","");
            editor.putString("Password","");
            editor.commit();

            Intent i = new Intent(Custom_camera.this, UserLogin.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            if(mCamera!=null){
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);

                mCamera.release();
                mCamera = null;
            }
            return true;
        }
        else if(id==R.id.action_all_pictures)
        {
            Intent i = new Intent(Custom_camera.this, AllPicturesShowActivity.class);

            startActivity(i);
        }
        else if(id==R.id.action_my_pictures)
        {
            Intent i = new Intent(Custom_camera.this, MyPicturesShowActivity.class);

            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);

            mCamera.release();
            mCamera = null;
        }
        super.onBackPressed();
    }
}
