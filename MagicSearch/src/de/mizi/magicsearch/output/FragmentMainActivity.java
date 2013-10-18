package de.mizi.magicsearch.output;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.mizi.magicsearch.R;
import de.mizi.magicsearch.data.MagicCardData;
import de.mizi.magicsearch.data.MagicDatabaseHelper;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

public class FragmentMainActivity extends FragmentActivity
{
	private MagicDatabaseHelper databaseHelper = null;
	// When requested, this adapter returns a DemoObjectFragment,
	// representing an object in the collection.
//	DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
//	ViewPager mViewPager;
	List<MagicCardData> data;
	MagicCardPagerAdapter adapter;
	ViewPager pager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		try {
			data = getHelper().getMagicDao().queryForAll();
			Toast.makeText(this, "found " + data.size() + " entries", Toast.LENGTH_LONG).show();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		adapter = new MagicCardPagerAdapter(getSupportFragmentManager(), data);
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
//		mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mDemoCollectionPagerAdapter);
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
