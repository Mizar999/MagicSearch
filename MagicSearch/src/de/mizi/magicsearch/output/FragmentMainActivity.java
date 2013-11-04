package de.mizi.magicsearch.output;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;

import de.mizi.magicsearch.R;
import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicDatabaseHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class FragmentMainActivity extends FragmentActivity
{
	public static final String KEY_NAME_TO_SEARCH = "nameToSearch";
	public static final String KEY_START_ITEM = "startItem";
	
	private MagicDatabaseHelper databaseHelper;
	private List<MagicCardData> data;
	private MagicCardPagerAdapter adapter;
	private ViewPager pager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		data = new LinkedList<MagicCardData>();
		
		Bundle bundle = getIntent().getExtras();
		try
		{
			if(bundle == null)
			{
				data = getHelper().getMagicDao().queryForAll();
			}
			else {
				String nameToSearch = bundle.getString(FragmentMainActivity.KEY_NAME_TO_SEARCH);
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
		
		adapter = new MagicCardPagerAdapter(getSupportFragmentManager(), data);
		pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(adapter);
		
		if(bundle != null)
		{
			int startIndex = bundle.getInt(FragmentMainActivity.KEY_START_ITEM, -1);
			if(startIndex >= 0 && startIndex < data.size()) {
				pager.setCurrentItem(startIndex);
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
	private MagicDatabaseHelper getHelper()
	{
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, MagicDatabaseHelper.class);
		}
		return databaseHelper;
	}
}
