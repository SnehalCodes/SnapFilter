package com.example.newproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.ArrayList;

public class ApplyFilterActivity extends AppCompatActivity {
    Bitmap bitmap,resultBitmap,resultBitmapWithWatermark;
    String filterType;
    ImageView imgShowFilter;
    ImageFilters imgFilter;
    Switch wmswitch;
    public int height=128, width = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_filter);
        initView();
    }

    private void initView() {
        imgShowFilter = findViewById(R.id.imgShowFilter);
        wmswitch = findViewById(R.id.wmswitch);
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        SharedPreferences sharedPreferences = getSharedPreferences("FilterApp", Context.MODE_PRIVATE);
        filterType = sharedPreferences.getString("filterType", "");
        imgFilter = new ImageFilters();
        setFilterName(filterType);
        wmswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWaterMark(bitmap);
            }
        });
    }
    private void setFilterName(String filterType) {
        switch (filterType)
        {
            case "BlackWhite":
                showBitmap(imgFilter.applyGreyscaleEffect(bitmap));
                break;
            case "Sketch":
                showBitmap(imgFilter.Changetosketch(bitmap,ApplyFilterActivity.this));
                break;
            case "Cartoon":
                getCartoon();
                break;
            case "Rainbow":
                showBitmap(imgFilter.applyShadingFilter(bitmap, Color.CYAN));
                break;
            case "Reflection":
                showBitmap(imgFilter.applyReflection(bitmap));
                break;
        }
    }

    private void BlackWhite() {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bmpGrayscale);
        Paint paint = new Paint();

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        imgShowFilter.setImageBitmap(bmpGrayscale);

    }

    private void showBitmap(Bitmap bmp){
        try {
            resultBitmap = bmp;
            imgShowFilter.setImageBitmap(bmp);

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private Bitmap addWaterMark(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        //  canvas.drawBitmap(waterMark, 0, 0, null);
        int startX= (canvas.getWidth()-waterMark.getWidth())/2;//for horisontal position
        int startY=(canvas.getHeight()-waterMark.getHeight())/2;//for vertical position
        canvas.drawBitmap(waterMark,startX,startY,null);

        return result;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCartoon(){
        ArrayList<ArrayList<Float>> im_array = new ArrayList<>();
        for (int x=0;x<width;x++){
            ArrayList<Float> row = new ArrayList<>();
            for (int y=0;y<height;y++){
                int pixel = bitmap.getPixel(x,y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                Float bw = ((float)(red+blue+green))/765;
                row.add(bw);
            }
            im_array.add(row);
        }

        ArrayList<ArrayList<Float>> blur = new ArrayList<>();
        ArrayList<Float> blankRow = new ArrayList<>();
        for (int x=0; x<width;x++) {
            blankRow.add((float) 0);
        }
        blur.add(blankRow);
        for (int y=1; y<height-1;y++) {
            ArrayList<Float> row = new ArrayList<>();
            row.add((float)0);
            for (int x = 1; x < width-1; x++) {
                float lu = im_array.get(y-1).get(x-1);
                float mu = im_array.get(y-1).get(x);
                float ru = im_array.get(y-1).get(x+1);
                float lm = im_array.get(y).get(x-1);
                float mm = im_array.get(y).get(x);
                float rm = im_array.get(y).get(x+1);
                float ld = im_array.get(y+1).get(x-1);
                float md = im_array.get(y+1).get(x);
                float rd = im_array.get(y+1).get(x+1);

                float newPix = (lu+mu+ru+lm+mm+rm+ld+md+rd)/9;
                row.add(newPix);
            }
            row.add((float)0);
            blur.add(row);
        }
        blur.add(blankRow);

        ArrayList<ArrayList<Float>> edge = new ArrayList<>();
        for (int y=0; y<height; y++){
            ArrayList<Float> row = new ArrayList<>();
            for (int x=0; x<width; x++){
                float pix = im_array.get(y).get(x) - blur.get(y).get(x);
                row.add(pix);
            }
            edge.add(row);
        }



        ArrayList<Integer>flat = new ArrayList<>();
        for (int x=0; x<width; x++){
            for (int y=0; y<height; y++) {
                int pix = (int) (edge.get(y).get(x) * 255);
                flat.add(pix);
            }
        }
        int[] flatFinal = flat.stream().mapToInt(i -> i).toArray();
        Bitmap bmp = Bitmap.createBitmap(flatFinal, 128, 128, Bitmap.Config.ALPHA_8);

        Bitmap newBitmap = getCombinedBitmap(bitmap, bmp);
        resultBitmap = newBitmap;
        imgShowFilter.setImageBitmap(newBitmap);
    }
    public Bitmap getCombinedBitmap(Bitmap b, Bitmap b2) {
        Bitmap drawnBitmap = null;

        try {
            drawnBitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(drawnBitmap);
            canvas.drawBitmap(b, 0, 0, null);
            canvas.drawBitmap(b2, 0, 0, null);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return drawnBitmap;
    }
}
