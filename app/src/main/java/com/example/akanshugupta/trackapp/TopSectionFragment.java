package com.example.akanshugupta.trackapp;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TopSectionFragment extends Fragment{

    ImageView myImageView;
    Context thiscontext;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_section_fragment,container,false);
       // thiscontext = container.getContext();
        myImageView = (ImageView) view.findViewById(R.id.SitImage);
                return view;
    }

    public void plot(float x,float y){
        float r = (float) 20;



        /*DrawDot mMap= new DrawDot(this,2,2);
        mMap=(DrawDot)myImageView;
        Canvas tempCanvas = new Canvas();
        mMap.onDraw(tempCanvas);*/
        /*int width = (int) getResources().getDimension("380dp")
        float w = myImageView.getWidth();
        Log.d(TAG, "onCreate width : "+w);
        int width = (int) getResources().getDimension()
        float h = myImageView.getHeight();
        Log.d(TAG, "onCreate height: "+ h);*/
        Bitmap myBitmap = Bitmap.createBitmap(750, 800, Bitmap.Config.ARGB_8888);
        Paint myPaint = new Paint();
        //int color = ContextCompat.getColor(thiscontext, R.color.black);
       // myPaint.setColor(color);
        myPaint.setStyle(Paint.Style.FILL);

        //Create a new image bitmap and attach a brand new canvas to it
        //Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(myBitmap);

        //Draw the image bitmap into the cavas

        tempCanvas.drawBitmap(myBitmap, 0, 0, null);

        //Draw everything else you want into the canvas, in this example a rectangle with rounded edges
        tempCanvas.drawCircle(x, y, r, myPaint);

        //Attach the canvas to the ImageView
        myImageView.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));
        myImageView.setBackground(getResources().getDrawable(R.drawable.sit_image));
    }
}
