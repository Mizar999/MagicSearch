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

/**
 * Represents the 'main container' for the detail view of a magiccard.
 */
public class FragmentMainActivity extends FragmentActivity
{
	/**
	 * The key for the bundle to lookup the cardname to search for.
	 */
	public static final String KEY_NAME_TO_SEARCH = "nameToSearch";
	/**
	 * The key for the bundle to lookup the first card that shall be shown in the detail view.
	 */
	public static final String KEY_START_ITEM = "startItem";
	
	/**
	 * A cached object to get access to the database helper class.
	 */
	private MagicDatabaseHelper databaseHelper;
	/**
	 * This list holds the result of the database queries.
	 */
	private List<MagicCardData> data;
	/**
	 * This adapter holds the fragments that shall be shown.
	 */
	private MagicCardPagerAdapter adapter;
	/**
	 * This pager holds the adapter with the cardinfos.
	 */
	private ViewPager pager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		data = new LinkedList<MagicCardData>();
		
		Bundle bundle = getIntent().getExtras();
		try
		{
			// Check if there is a cardname to search for. If not then show all cards.
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
		
		// Check if there is start index set. If not then leave the start index as it is.
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
		// release the cached object
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
	/**
	 * This method grants access to the cached database helper object
	 * 
	 * @return the cached database helper object.
	 */
	private MagicDatabaseHelper getHelper()
	{
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, MagicDatabaseHelper.class);
		}
		return databaseHelper;
	}
}
