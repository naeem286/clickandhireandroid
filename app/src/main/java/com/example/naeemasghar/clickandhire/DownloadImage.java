package com.example.naeemasghar.clickandhire;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Naeem Asghar on 7/4/2016.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    //ProgressDialog loading;
    private ImageView imgView;
    public DownloadImage(ImageView imageView)
    {
        this.imgView = imageView;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //loading = ProgressDialog.show(ViewImage.this, "Uploading...", null, true, true);
    }

    @Override
    protected void onPostExecute(Bitmap b) {
        super.onPostExecute(b);
        this.imgView.setImageBitmap(b);
        //loading.dismiss();
        //saveImageToInternalStorage(b);
        //imageView.setImageBitmap(b);

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String id = params[0];
        //String add = "http://45.55.134.100/clickandhire/getImage.php?id="+id;
        String add = AppConfig.URL_For_Image + id + ".jpg";
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(add);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}

/*public class DownloadImage {

    private Context context;
    private String Id;
    private Bitmap bitmap;
    private RequestHandler requestHandler;
    public  Bitmap downloadingImage(String id)
    {
        requestHandler = new RequestHandler();
        //this.context=context;
        this.Id=id;
        getImage(id);
        return bitmap;
    }



    private void getImage(String userId) {

        class GetImage extends AsyncTask<String, Void, Bitmap> {
            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(context, "Downloading...", null, true, true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                //loading.dismiss();
                bitmap= b;

            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String id = params[0];
                String add = AppConfig.URL_For_Image + id + ".jpg";
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(userId);
    }


}
*/