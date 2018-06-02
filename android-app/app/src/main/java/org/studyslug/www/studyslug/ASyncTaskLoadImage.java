package org.studyslug.www.studyslug;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ASyncTaskLoadImage extends AsyncTask<String, String, Bitmap> {
  private static final String TAG = "ASyncTaskLoadImage";
  private ImageView imageView;

  public ASyncTaskLoadImage(ImageView imageView) {
    this.imageView = imageView;

  }
  @Override
  protected Bitmap doInBackground(String... params) {
    Bitmap bitmap = null;
    try {
      URL url = new URL(params[0]);
      bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
    return bitmap;
  }
  @Override
  protected void onPostExecute(Bitmap bitmap) {
    imageView.setImageBitmap(bitmap);
  }
}
