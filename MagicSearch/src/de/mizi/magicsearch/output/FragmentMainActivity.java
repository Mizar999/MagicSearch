package de.mizi.magicsearch.output;

import java.util.List;

import de.mizi.magicsearch.R;
import de.mizi.magicsearch.data.MagicCardData;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class FragmentMainActivity extends FragmentActivity
{
	public static final String KEY_DATA = "carddata";
	public static final String KEY_START_ITEM = "startitem";
	
	private List<MagicCardData> data;
	private MagicCardPagerAdapter adapter;
	private ViewPager pager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
		
		Bundle bundle = getIntent().getExtras();
		
		data = bundle.getParcelableArrayList(FragmentMainActivity.KEY_DATA);
		adapter = new MagicCardPagerAdapter(getSupportFragmentManager(), data);
		pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(adapter);
		
		int startIndex = bundle.getInt(FragmentMainActivity.KEY_START_ITEM, -1);
		if(startIndex >= 0 && startIndex < data.size()) {
			pager.setCurrentItem(startIndex);
		}
	}
}
