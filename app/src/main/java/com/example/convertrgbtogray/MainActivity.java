package com.example.convertrgbtogray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button colorButtonRed, colorButtonRed2, colorButtonRed3;
    ImageView iv;
    String ColorRed1, ColorRed2;
    //take bitmap and bitmap drawable to get image form image view
    BitmapDrawable drawable;
    Bitmap bitmap;

    String imageString="";

    ///////////////iv
    ImageView imageView;
    ImageView originalImageView;

    ///////////////about gallery
    public static final int REQUEST_GALLERY = 1;
    //Bitmap bitmap;
    ///////////////

    ///////////////about camera
    public static final int REQUEST_CAMERA = 2;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView)findViewById(R.id.imageView);
        originalImageView = new ImageView(this);
        originalImageView.setImageDrawable(iv.getDrawable());


        ///////////////////////////////////////////open gallery
        imageView = (ImageView)findViewById(R.id.imageView);
        ImageButton gallery_button = findViewById(R.id.imageButton);
        gallery_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select Picture"), REQUEST_GALLERY);
            }
        });



        ///////////////////////////////////////////open camera
        ImageButton buttonIntent = findViewById(R.id.imageButton2);
        buttonIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String timeStamp =
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "IMG_" + timeStamp + ".jpg";
                File f = new File(Environment.getExternalStorageDirectory()
                        , "DCIM/Camera/" + imageFileName);
                uri = Uri.fromFile(f);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(Intent.createChooser(intent
                        , "Take a picture with"), REQUEST_CAMERA);
            }
        });


        ///////////////////////////////////////////////////////////////////////////////////
        //submit
        colorButtonRed = (Button)findViewById(R.id.button5);
        colorButtonRed2 = (Button)findViewById(R.id.button59);
        colorButtonRed3 = (Button)findViewById(R.id.button60);


        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        /////////////////////////////////////////////red1
        final Python py = Python.getInstance();
        colorButtonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get image from image view
                iv.setImageDrawable(originalImageView.getDrawable());
                drawable = (BitmapDrawable)iv.getDrawable();
                bitmap = drawable.getBitmap();
                imageString = getStringImage(bitmap);

                //imageString we get encoded iamge string
                //pass this string in python script

                //call .py file
                PyObject pyo = py.getModule("lips");
                //call module in .py file
                PyObject obj = pyo.callAttr("main",imageString,String.valueOf(colorButtonRed.getText()));
                //return value
                String str = obj.toString();


                //convert bytearray
                byte[]data = android.util.Base64.decode(str, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

                //set this bitmap to imageView2
                iv.setImageBitmap(bmp);
            }
        });
        ///////////////////////////////////////////////
        ///////////////////////////////////////////////red2
        colorButtonRed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get image from image view
                iv.setImageDrawable(originalImageView.getDrawable());
                drawable = (BitmapDrawable)iv.getDrawable();
                bitmap = drawable.getBitmap();
                imageString = getStringImage(bitmap);

                //imageString we get encoded iamge string
                //pass this string in python script

                //call .py file
                PyObject pyo = py.getModule("lips");
                //call module in .py file
                PyObject obj = pyo.callAttr("main", imageString, String.valueOf(colorButtonRed2.getText()));
                //return value
                String str = obj.toString();


                //convert bytearray
                byte[]data = android.util.Base64.decode(str, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

                //set this bitmap to imageView2
                iv.setImageBitmap(bmp);
            }
        });


        /////////////////////////////////////red3
        colorButtonRed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get image from image view
                iv.setImageDrawable(originalImageView.getDrawable());
                drawable = (BitmapDrawable)iv.getDrawable();
                bitmap = drawable.getBitmap();
                imageString = getStringImage(bitmap);

                //imageString we get encoded iamge string
                //pass this string in python script

                //call .py file
                PyObject pyo = py.getModule("lips");
                //call module in .py file
                PyObject obj = pyo.callAttr("main", imageString, String.valueOf(colorButtonRed3.getText()));
                //return value
                String str = obj.toString();


                //convert bytearray
                byte[]data = android.util.Base64.decode(str, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(data,0,data.length);

                //set this bitmap to imageView2
                iv.setImageBitmap(bmp);
            }
        });
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //store in bytearray
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            getContentResolver().notifyChange(uri, null);
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext()
                        , uri.getPath(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            getContentResolver().notifyChange(uri, null);
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext()
                        , uri.getPath(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}