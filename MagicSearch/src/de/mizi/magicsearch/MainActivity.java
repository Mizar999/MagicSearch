package de.mizi.magicsearch;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.Dao;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicCardRarity;
import de.mizi.magicsearch.data.MagicDatabaseHelper;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		TextView view = new TextView(this);
		
		try {
			doSomething(view);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void doSomething(TextView view) throws SQLException
	{
		Dao<MagicCardData, Integer> dao = getHelper().getMagicDao();
		StringBuilder sb = new StringBuilder();
		
		// query all data objects
		List<MagicCardData> list = dao.queryForAll();
		sb.append("query for all: ").append(list.size()).append(" entries\n");
		for(MagicCardData data: list) {
			sb.append("*****-----*****-----*****\n");
			sb.append(data).append("\n");
		}
		
		// delete all data objects
		for(MagicCardData data: list) {
			dao.delete(data);
			sb.append("deleted id ").append(data.id).append("\n");
		}
		
		// create new data objects
		int rnd = new Random().nextInt(3) + 2;
		for(int i = 0; i < rnd; ++i)
		{
			MagicCardData data = new MagicCardData();
			data.name = "Nevermore";
			data.cost = "1WW";
			data.convertedCost = 3;
			data.types = "Enchantment";
			data.rulesText = "As Nevermore enters the battlefield, name a nonland card.\n\nThe named card can't be cast.";
			data.flavorText = "\"By the law of Avacyn, the following thoughts, words, and deeds are henceforth disallowed.\"";
			data.artist = "Jason A. Engle";
			data.power = null;
			data.toughness = null;
			data.expansion = "Innistrad";
			data.rarity = MagicCardRarity.RARE;
			data.number = "25";
			data.cardRules = null;
			data.filename = "testfile.png";
			dao.create(data);
		}
		sb.append("created ").append(rnd).append(" new objects\n");
		
		view.setText(sb);
	}
}
