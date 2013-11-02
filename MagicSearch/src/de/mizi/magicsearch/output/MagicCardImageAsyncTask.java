package de.mizi.magicsearch.output;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MagicCardImageAsyncTask extends AsyncTask<String, String, Boolean>
{
	private ImageView image;
	private ProgressBar progress;
	private TextView text;
	private Bitmap bitmap;
	
	public MagicCardImageAsyncTask(ImageView image, ProgressBar progress, TextView text)
	{
		this.image = image;
		this.progress = progress;
		this.text = text;
	}
	
	@Override
	protected Boolean doInBackground(String... params)
	{
		boolean success = false;
		
		if(params.length > 0 && !params[0].equals(""))
		{
			try
			{
				URL url = new URL(params[0]);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();   
	            conn.setDoInput(true);   
				conn.connect();
	            InputStream is = conn.getInputStream();
	            bitmap = BitmapFactory.decodeStream(is); 
	            success = true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}     
		}
		
		return success;
	}
	
	@Override
	protected void onPostExecute(Boolean success)
	{
		progress.setVisibility(View.GONE);
		
		if(success) {
			image.setImageBitmap(bitmap);
		} else {
			text.setVisibility(View.VISIBLE);
		}
	}
}
