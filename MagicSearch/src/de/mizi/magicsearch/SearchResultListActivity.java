package de.mizi.magicsearch;

import java.util.ArrayList;

import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.output.FragmentMainActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchResultListActivity extends ListActivity
{
	public static final String KEY_DATA = "carddata";
	
	private ArrayList<MagicCardData> data;
	private ArrayList<String> cardValues;
	private ArrayAdapter<String> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchresult);
		
		Bundle bundle = getIntent().getExtras();
		data = bundle.getParcelableArrayList(FragmentMainActivity.KEY_DATA);
		
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
		intent.putParcelableArrayListExtra(FragmentMainActivity.KEY_DATA, data);
		intent.putExtra(FragmentMainActivity.KEY_START_ITEM, position);
		startActivity(intent);
	}
}
