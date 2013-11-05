package de.mizi.magicsearch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicCardRarity;
import de.mizi.magicsearch.data.MagicDatabaseHelper;
import android.app.IntentService;
import android.content.Intent;

/**
 * This service downloads all the carddata.
 */
public class MagicWebscrapingService extends IntentService
{
	/**
	 * Indicates that download was successful.
	 */
	public static final int RESULT_OK = 2;
	/**
	 * Indicates that there was an error while downloading.
	 */
	public static final int RESULT_FAILURE = 4;
	/**
	 * Indicates that there is a new status update while downloading.
	 */
	public static final int RESULT_UPDATE = 8;
	
	/**
	 * The key for the bundle to lookup the expansions to download.
	 */
	public static final String KEY_EXPANSIONS = "expansions";
	/**
	 * The key for the bundle to lookup the result of the download.
	 */
	public static final String KEY_RESULT = "result";
	/**
	 * The key for the bundle to lookup the status update message.
	 */
	public static final String KEY_UPDATE_MESSAGE = "updateMessage";
	/**
	 * The key for the bundle to lookup the intent filter string.
	 */
	public static final String KEY_NOTIFICATION = "de.mizi.magicsearch.MagicWebscrapingService";
	
	/**
	 * How many cards shall be downloaded before there will be a pause of some seconds.
	 */
	private static final int CARDS_TO_DOWNLOAD_BEFORE_SLEEP = 4;
	
	/**
	 * The regex pattern for the collectorsnumber.
	 */
	private static Pattern collectorNumberPattern = Pattern.compile("#(\\d+)");
	/**
	 * The regex pattern for the expansion and rarity.
	 */
	private static Pattern editionRarityPattern = Pattern.compile("([^\\(]+) ?\\(([^\\)]+)\\)");
	/**
	 * The regex pattern for the typeline.
	 */
	private static Pattern typePattern = Pattern.compile("([^\\d\\(,]+) ?(([\\d*]+)/([\\d*]+)|\\(Loyalty\\: ?(\\d+)\\))?,? ?([^\\( ]+)? ?\\(?(\\d+)?\\)?");
	
	/**
	 * The cached database helper object.
	 */
	private MagicDatabaseHelper databaseHelper;
	/**
	 * The dao object, that allows access to the carddate table.
	 */
	private Dao<MagicCardData, Integer> dao;
	
	/**
	 * Creates the service.
	 */
	public MagicWebscrapingService()
	{
		super("MagicWebscrapingService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		int result = MagicWebscrapingService.RESULT_FAILURE;
		String[] expansions = intent.getStringArrayExtra(MagicWebscrapingService.KEY_EXPANSIONS);

		try
		{
			databaseHelper = OpenHelperManager.getHelper(this, MagicDatabaseHelper.class);
			dao = databaseHelper.getMagicDao();
			
			sendStatusMessage(getResources().getString(R.string.webscraping_initialize));
			
			TableUtils.clearTable(databaseHelper.getConnectionSource(), MagicCardData.class);
			
			Document doc = null;
			Element nextCard = null;
			
			String text = null;
			Matcher matcher = null;
			
			for(String nextExpansion: expansions)
			{
				sendStatusMessage(nextExpansion);
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
					Element imageUrl = doc.select("body > table").get(2).select("tr:eq(0) td:eq(0) img").first();
	
					MagicCardData data = new MagicCardData();
					data.name = name.text();
					data.flavorText = flavor.text();
					data.artist = artist.text().replaceFirst("Illus. ", "");
					data.imageUrl = (imageUrl == null ? "" : imageUrl.attr("src"));
					data.types = "-";
					data.power = "-";
					data.toughness = "-";
					data.loyalty = "-";
					data.cost = "-";
					data.convertedCost = "-";
					data.number = "-";
					data.expansion = "-";
					data.rarity = MagicCardRarity.NONE;
					
					// Set rulestext with newlines
					StringBuilder rulesStr = new StringBuilder();
					for(Node node: rules.childNode(0).childNodes())
					{
						if(node instanceof TextNode)
						{
							if(!rulesStr.toString().equals(""))
							{
								rulesStr.append("\n\n");
							}
							rulesStr.append(node.toString());
						}
					}
					data.rulesText = rulesStr.toString();
					
					// Set card rules faq with newlines
					if(cardrules == null)
					{
						data.cardRules = "-";
					}
					else {
						StringBuilder cardRulesStr = new StringBuilder();
						for(Node child: cardrules.children())
						{
							for(Node node: child.childNodes())
							{
								if(node instanceof TextNode)
								{
									// text
									cardRulesStr.append(node.toString());
								}
								else {
									// date
									if(!cardRulesStr.toString().equals(""))
									{
										cardRulesStr.append("\n");
									}
									cardRulesStr.append(node.childNode(0).toString());
								}
							}
						}
						data.cardRules = cardRulesStr.toString();
					}
					
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
					if(nextCard != null)
					{
						if(cardCounter != 0 && cardCounter % CARDS_TO_DOWNLOAD_BEFORE_SLEEP == 0)
						{
							Thread.sleep(1800);
						}
						doc = Jsoup.connect(nextCard.absUrl("href")).get();
					}
					
					sendStatusMessage(nextExpansion + " (" + ++cardCounter + ")");
				}while(nextCard != null);
			}
			
			result = MagicWebscrapingService.RESULT_OK;
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
		finally {
			if(databaseHelper != null)
			{
				OpenHelperManager.releaseHelper();
				databaseHelper = null;
			}
		}
		
		Intent resultIntent = new Intent(MagicWebscrapingService.KEY_NOTIFICATION);
		resultIntent.putExtra(MagicWebscrapingService.KEY_RESULT, result);
	    sendBroadcast(resultIntent);
	}
	
	/**
	 * This method sends a status message to all registered receivers while downloading the data.
	 * @param message the message that shall be send
	 */
	private void sendStatusMessage(String message)
	{
		Intent intent = new Intent(MagicWebscrapingService.KEY_NOTIFICATION);
		intent.putExtra(MagicWebscrapingService.KEY_RESULT, MagicWebscrapingService.RESULT_UPDATE);
		intent.putExtra(MagicWebscrapingService.KEY_UPDATE_MESSAGE, message);
		sendBroadcast(intent);
	}
}
