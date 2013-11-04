package de.mizi.magicsearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.stmt.QueryBuilder;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicDatabaseHelper;
import de.mizi.magicsearch.output.FragmentMainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SearchResultListActivity extends OrmLiteBaseListActivity<MagicDatabaseHelper>
{
	public static final String KEY_NAME_TO_SEARCH = "nameToSearch";
	
	private String nameToSearch;
	private List<MagicCardData> data;
	private ArrayList<String> cardValues;
	private ArrayAdapter<String> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		
		Bundle bundle = getIntent().getExtras();
		try {
			if(bundle == null)
			{
					data = getHelper().getMagicDao().queryForAll();
			}
			else {
				nameToSearch = bundle.getString(SearchResultListActivity.KEY_NAME_TO_SEARCH);
				if(nameToSearch == null)
				{
					data = getHelper().getMagicDao().queryForAll();
				}
				else {
					QueryBuilder<MagicCardData, Integer> builder = getHelper().getMagicDao().queryBuilder();
					builder.where().like(MagicCardData.CARDNAME_FIELD_NAME, "%" + nameToSearch + "%");
					data = getHelper().getMagicDao().query( builder.prepare() );
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(data.size() <= 0)
		{
			Toast.makeText(this, getResources().getString(R.string.no_search_result), Toast.LENGTH_LONG).show();
			finish();
		}

		cardValues = new ArrayList<String>();
		for(MagicCardData cardData: data)
		{
			cardValues.add(cardData.name + " (" + cardData.expansion + " #" + cardData.number + ")");
		}
		
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cardValues);
		setListAdapter(listAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent intent = new Intent(SearchResultListActivity.this, FragmentMainActivity.class);
		intent.putExtra(FragmentMainActivity.KEY_NAME_TO_SEARCH, nameToSearch);
		intent.putExtra(FragmentMainActivity.KEY_START_ITEM, position);
		startActivity(intent);
	}
}
