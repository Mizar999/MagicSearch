package de.mizi.magicsearch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicCardRarity;
import de.mizi.magicsearch.data.MagicDatabaseHelper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class MagicWebscrapingAsyncTask extends AsyncTask<String, String, Boolean>
{
	private static Pattern collectorNumberPattern = Pattern.compile("#(\\d+)");
	private static Pattern editionRarityPattern = Pattern.compile("([^\\(]+) ?\\(([^\\)]+)\\)");
	private static Pattern typePattern = Pattern.compile("([^\\d\\(,]+) ?(([\\d*]+)/([\\d*]+)|\\(Loyalty\\: ?(\\d+)\\))?,? ?([^\\( ]+)? ?\\(?(\\d+)?\\)?");
	
	private MagicDatabaseHelper helper;
	private Dao<MagicCardData, Integer> dao;
	private ProgressDialog progress;
	
	public MagicWebscrapingAsyncTask(MagicDatabaseHelper helper, ProgressDialog progress) throws SQLException
	{
		this.helper = helper;
		this.dao = helper.getMagicDao();
		this.progress = progress;
	}
	
	@Override
	protected Boolean doInBackground(String... expansions)
	{
		try
		{
			publishProgress("initialize");
			TableUtils.clearTable(helper.getConnectionSource(), MagicCardData.class);
			
			Document doc = null;
			Element nextCard = null;
			
			String text = null;
			Matcher matcher = null;
			
			for(String nextExpansion: expansions)
			{
				publishProgress(nextExpansion);
				String url = "http://magiccards.info/" + nextExpansion + "/en/1.html";
				doc = Jsoup.connect(url).get();
				
				int cardCounter = 0;
				do {
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
					data.name = name.text();
					data.rulesText = rules.text();
					data.flavorText = flavor.text();
					data.artist = artist.text().replaceFirst("Illus. ", "");
					data.cardRules = (cardrules == null ? "-" : cardrules.text());
					data.types = "-";
					data.power = "-";
					data.toughness = "-";
					data.loyalty = "-";
					data.cost = "-";
					data.convertedCost = "-";
					data.number = "-";
					data.expansion = "-";
					data.rarity = MagicCardRarity.NONE;
					
					text = typeLine.text();
					matcher = typePattern.matcher(text);
					if(matcher.find())
					{
						// should find 7 matching groups: 1 - type, 2 - sub P/T or Loyalty, 3 - power,
						// 4 - toughness, 5 - loyalty, 6 - manacost, 7 - converted manacost
						if(matcher.group(1) != null) data.types = matcher.group(1);
						if(matcher.group(3) != null) data.power = matcher.group(3);
						if(matcher.group(4) != null) data.toughness = matcher.group(4);
						if(matcher.group(5) != null) data.loyalty = matcher.group(5);
						if(matcher.group(6) != null) data.cost = matcher.group(6);
						if(matcher.group(7) != null) data.convertedCost = matcher.group(7);
					}
					
					text = collectorNumber.text();
					matcher = collectorNumberPattern.matcher(text);
					if(matcher.find() && matcher.group(1) != null)
					{
						data.number = matcher.group(1);
					}
					
					text = edition.text();
					matcher = editionRarityPattern.matcher(text);
					if(matcher.find() && matcher.group(1) != null)
					{
						data.expansion = matcher.group(1);
						data.rarity = MagicCardRarity.stringToRarity( matcher.group(2) );
					}
					
					dao.create(data);
					
					nextCard = doc.select("body > table").get(1).select("tr:eq(0) td:eq(2) a").first();
					if(nextCard != null) {
						Thread.sleep(1000);
						doc = Jsoup.connect(nextCard.absUrl("href")).get();
					}
					
					publishProgress(nextExpansion + " (" + ++cardCounter + ")");
				}while(nextCard != null && cardCounter < 5);
			}
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
