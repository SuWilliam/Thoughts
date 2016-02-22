package hackillinois.com.thoughts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.kairos.Kairos;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MainActivity extends Activity {
    public static RequestQueue queue;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    static Kairos myKairos;
    Map<Long, String> users;
    public static Bitmap bmpImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ImageButton button = (ImageButton)findViewById(R.id.btnMind);
        YoYo.with(Techniques.BounceInUp).delay(1400).playOn(button);
        KairosUtils.init(getApplicationContext());
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Make a volley request here
                // Instantiate the RequestQueue.
                dispatchTakePictureIntent();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("MainActivity", "GotHere");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        //Log.d("MainActivity", "GotHere");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("MainActivity", ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data ) {
        Log.d("MainActivity", mCurrentPhotoPath);

        Bitmap image = BitmapFactory.decodeFile(mCurrentPhotoPath);
        int w = image.getWidth();
        int h = image.getHeight();

        Matrix mtx = new Matrix();
        mtx.preRotate(90);
        Bitmap rotatedBmp = Bitmap.createBitmap(image, 0, 0, w, h, mtx, true);
        int sW = (int) (rotatedBmp.getWidth() * 0.15f);
        int sH = (int) (rotatedBmp.getHeight() * 0.15f);
        Bitmap rBmp = Bitmap.createScaledBitmap(rotatedBmp, sW, sH, true);
        bmpImg = rBmp;
        ImageView imgview = (ImageView) findViewById(R.id.imageView);
        imgview.setImageBitmap(rBmp);
        Log.d("Image size", image.getWidth() + "");
        Intent intent = new Intent(this, TwitterSearchActivity.class);
        startActivity(intent);

    }
}
