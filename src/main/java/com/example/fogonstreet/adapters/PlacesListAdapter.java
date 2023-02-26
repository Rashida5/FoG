package com.example.fogonstreet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fogonstreet.R;
import com.example.fogonstreet.google.model.Result;

import java.io.InputStream;
import java.util.List;

public class PlacesListAdapter  extends ArrayAdapter<Result> {
    private Context context;
    private List<Result> results;
    public PlacesListAdapter(Context context, List<Result> results){
        super(context, R.layout.place_row_layout,results);
        this.context=context;
        this.results=results;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent){
      try{
   ViewHolder viewHolder;
   if(view==null){
       viewHolder = new ViewHolder();
       view = LayoutInflater.from(context).inflate(R.layout.place_row_layout,null);
       viewHolder.textViewName= view.findViewById(R.id.textViewName);
       viewHolder.textViewAddress= view.findViewById(R.id.textViewAddress);
       viewHolder.imageViewPhoto= view.findViewById(R.id.imageViewPhoto);
       view.setTag(viewHolder);
   }
   else{
       viewHolder =(ViewHolder) view.getTag();
   }
   Result result = results.get(position);
   viewHolder.textViewName.setText(result.getName());
   viewHolder.textViewAddress.setText(result.getVicinity());
   Bitmap bitmap = new ImageAsymc().doInBackground(result.getIcon());
   viewHolder.imageViewPhoto.setImageBitmap(bitmap);
   return view;
      }catch (Exception e){
          return null;
      }
        //  return super.getView(position,convertView,parent);
    }
    public static class ViewHolder{
        public  TextView textViewName;
        public  TextView textViewAddress;
        public  ImageView imageViewPhoto;
    }
    private class ImageAsymc extends AsyncTask<String,Void , Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{
                InputStream inputStream = new java.net.URL(strings[0]).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e){
return null;
            }
        }
    }
}
