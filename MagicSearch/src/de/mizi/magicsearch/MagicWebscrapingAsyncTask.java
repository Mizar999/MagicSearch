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
			
			Element nextCard = null;
			
			int debugCounter = 0;
			do {
				publishProgress("getting data");
				Element name = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) a").first();
				Element typeLine = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) p:eq(1)").first();
				Element rules = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) p:eq(2)").first();
				Element flavor = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) p:eq(3)").first();
				Element artist = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) p:eq(4)").first();
				Element cardrules = null;
				if(doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) ul").size() > 1) {
					cardrules = doc.select("body > table").get(2).select("tr:eq(0) td:eq(1) ul:first-of-type").first();
				}
				Element collectorNumber = doc.select("body > table").get(2).select("tr:eq(0) td:eq(2) b:matchesOwn(#)").first();
				Element edition = doc.select("body > table").get(2).select("tr:eq(0) td:eq(2) img + b").first();

				MagicCardData data = new MagicCardData();
				data.expansion = "Dragon's Maze";
				data.name = name.text();
				data.types = typeLine.text();
				data.rulesText = rules.text();
				data.flavorText = flavor.text();
				data.artist = artist.text();
				data.cardRules = (cardrules == null ? "-" : cardrules.text());
				data.number = collectorNumber.text();
				data.expansion = edition.text();
				publishProgress("saving data");
				dao.create(data);
				
				nextCard = doc.select("body > table").get(1).select("tr:eq(0) td:eq(2) a").first();
				if(nextCard != null) {
					Thread.sleep(1000);
					publishProgress("connecting to next card");
					doc = Jsoup.connect(nextCard.absUrl("href")).get();
				}
				
				++debugCounter;
			}while(nextCard != null && debugCounter < 5);
			
//			for(Element e: elements) {
//				System.out.println(e.select("a").first().attr("href") + " " + e.text());
//			}
			
			
//			Log.i(MagicWebscrapingAsyncTask.class.getName(), data.toString());
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
