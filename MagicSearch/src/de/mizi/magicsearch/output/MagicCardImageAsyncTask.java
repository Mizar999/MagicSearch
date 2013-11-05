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

/**
 * This asynchrinus task downloads a cardimage of a magiccard.
 */
public class MagicCardImageAsyncTask extends AsyncTask<String, String, Boolean>
{
	/**
	 * This imageview shows the downloaded cardimage
	 */
	private ImageView image;
	/**
	 * The loading icon that is shown while the cardimage is downloaded.
	 */
	private ProgressBar progress;
	/**
	 * This textview shows an error message if something unexpected happened.
	 */
	private TextView text;
	/**
	 * The downloaded image data.
	 */
	private Bitmap bitmap;
	
	/**
	 * The constructor creates the task object. It also sets all the references this object should have access to.
	 * 
	 * @param image reference to the imageview
	 * @param progress reference to the loading icon
	 * @param text reference to the textview for an error message
	 */
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
				// Get access to the url from the given parameter and download the image data.
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
		// Hide the loading icon and show the image if everything went well or show an error message if something went wrong.
		progress.setVisibility(View.GONE);
		
		if(success) {
			image.setImageBitmap(bitmap);
		} else {
			text.setVisibility(View.VISIBLE);
		}
	}
}
