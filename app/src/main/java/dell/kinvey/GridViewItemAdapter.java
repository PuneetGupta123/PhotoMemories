package dell.kinvey;

/**
 * Created by dell on 10/21/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.squareup.picasso.Picasso;

import com.kinvey.android.Client;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.model.FileMetaData;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class GridViewItemAdapter extends RecyclerView.Adapter<GridViewItemAdapter.GridViewHolder>
{
    Context context;
    ArrayList<GridViewItemData> arrayList;
    Client mKinveyClient;
    Activity activity;

    public GridViewItemAdapter(Context context,ArrayList<GridViewItemData> arrayList,Client mKinveyClient,Activity activity)
    {
        this.arrayList=arrayList;
        this.context=context;
        this.mKinveyClient=mKinveyClient;
        this.activity=activity;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_view_item_layout,viewGroup,false);
        GridViewHolder gridViewHolder=new GridViewHolder(v);
        return gridViewHolder;
    }

    @Override
    public void onBindViewHolder(GridViewHolder gridViewHolder, int i)
    {
        gridViewHolder.imagePath.setText(arrayList.get(i).imagePath);
        gridViewHolder.itemId.setText(arrayList.get(i).id);
        File file=new File(arrayList.get(i).imagePath);
        if(!file.exists())
        {
            DownloadImage(arrayList.get(i).imagePath,arrayList.get(i).id,gridViewHolder);
        }
        else
        {
//            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            gridViewHolder.itemImage.setImageBitmap(myBitmap);
            Picasso.with(context).load("file://"+arrayList.get(i).imagePath).error(R.drawable.ic_launcher).placeholder(R.drawable.progress_animation).into(gridViewHolder.itemImage);
        }
    }
    public void DownloadImage(final String imagePath,String id,final GridViewHolder gridViewHolder)
    {
        File file = new File(imagePath);
        FileOutputStream fos=null;
        try
        {
            fos= new FileOutputStream(file);
        }
        catch (Throwable g)
        {

        }

        FileMetaData metadata1 =new FileMetaData(id);
        metadata1.set("UserName",mKinveyClient.user().getUsername());

        mKinveyClient.file().download(metadata1, fos, new DownloaderProgressListener() {
            @Override
            public void onSuccess(Void result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Successfully downloaded", Toast.LENGTH_LONG).show();
                        File imgFile = new  File(imagePath);

                        if(imgFile.exists()){

                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                            gridViewHolder.itemImage.setImageBitmap(myBitmap);


                        }


                    }
                });

            }
            @Override
            public void onFailure(final Throwable error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Not downloaded" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void progressChanged(MediaHttpDownloader downloader) throws IOException {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                             Toast.makeText(context, "Downloading" , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }



    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        TextView imagePath;

        TextView itemId;
        ImageView itemImage;

        public GridViewHolder(View itemView)
        {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.cv);
            imagePath=(TextView)itemView.findViewById(R.id.image_path);

            itemImage=(ImageView)itemView.findViewById(R.id.item_image);
            itemId=(TextView)itemView.findViewById(R.id.itemId);
            cardView.setUseCompatPadding(true);
            //cardView.setElevation(7);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent(context,ItemDetailActivity.class);
//                    intent.putExtra("itemId",itemId.getText());
//                    context.startActivity(intent);
                }
            });
        }
    }
}
