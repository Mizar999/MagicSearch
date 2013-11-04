package de.mizi.magicsearch;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mizi.magicsearch.data.MagicDatabaseHelper;
import de.mizi.magicsearch.output.FragmentMainActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button search = (Button)findViewById(R.id.main_button_search);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
//				try
//				{
					EditText edit = (EditText)findViewById(R.id.main_edit);
					String nameToSearch = edit.getText().toString();
					
					Intent intent = new Intent(MainActivity.this, SearchResultListActivity.class);
					intent.putExtra(SearchResultListActivity.KEY_NAME_TO_SEARCH, nameToSearch);
					startActivity(intent);
					
//					List<MagicCardData> tmp;
//					if(cardname.equals("")) {
//						tmp = getHelper().getMagicDao().queryForAll();
//					} else {
//						QueryBuilder<MagicCardData, Integer> builder = getHelper().getMagicDao().queryBuilder();
//						builder.where().like(MagicCardData.CARDNAME_FIELD_NAME, "%" + cardname + "%");
//						tmp = getHelper().getMagicDao().query( builder.prepare() );
//					}
//					ArrayList<MagicCardData> data = new ArrayList<MagicCardData>(tmp);
//					
//					if(data.size() > 0)
//					{
//						Intent intent = new Intent(MainActivity.this, SearchResultListActivity.class);
//						intent.putParcelableArrayListExtra(SearchResultListActivity.KEY_NAME_TO_SEARCH, data);
//						startActivity(intent);
//					}
//					else {
//						Resources res = MainActivity.this.getResources();
//						Toast.makeText(MainActivity.this, res.getString(R.string.no_search_result), Toast.LENGTH_LONG).show();
//					}
//				}
//				catch (SQLException e) {
//					e.printStackTrace();
//				}
			}
		});
		
		Button showall = (Button)findViewById(R.id.main_button_showall);
		showall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, FragmentMainActivity.class);
				startActivity(intent);
			}
		});
		
		Button webscraping = (Button)findViewById(R.id.main_button_webscraping);
		webscraping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				try {
					MainActivity.this.startWebscraping();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void startWebscraping() throws SQLException
	{
		Resources res = getResources();
		String status = res.getString(R.string.webscraping_status);
		String message = res.getString(R.string.webscraping_message);
		
		ProgressDialog progress = ProgressDialog.show(this, status, message);
		MagicWebscrapingAsyncTask scraping = new MagicWebscrapingAsyncTask(getHelper(), progress);
		scraping.execute("rtr", "gtc", "dgm", "m14", "ths");
	}
}
