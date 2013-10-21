package de.mizi.magicsearch;

import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicDatabaseHelper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class MagicWebscrapingAsyncTask extends AsyncTask<String, String, Boolean>
{
	private MagicDatabaseHelper helper;
	private Dao<MagicCardData, Integer> dao;
	private ProgressDialog progress;
	// http://magiccards.info/dgm/en/1.html
	
	public MagicWebscrapingAsyncTask(MagicDatabaseHelper helper, ProgressDialog progress) throws SQLException
	{
		this.helper = helper;
		this.dao = helper.getMagicDao();
		this.progress = progress;
	}
	
	@Override
	protected Boolean doInBackground(String... params)
	{
		try
		{
			publishProgress("clear table");
			TableUtils.clearTable(helper.getConnectionSource(), MagicCardData.class);
			publishProgress("connecting to website");
			Document doc = Jsoup.connect("http://magiccards.info/dgm/en/1.html").get();
			Element name = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) a").first();
//			for(Element e: elements) {
//				System.out.println(e.select("a").first().attr("href") + " " + e.text());
//			}
			
			MagicCardData data = new MagicCardData();
			data.expansion = "Dragon's Maze";
			data.name = name.text();
			publishProgress("saving data");
			dao.create(data);
			
			Log.i(MagicWebscrapingAsyncTask.class.getName(), data.toString());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		progress.dismiss();
		return true;
	}
	
	@Override
	protected void onProgressUpdate(String... values)
	{
		progress.setTitle("Status: " + values[0]);
	}
}
