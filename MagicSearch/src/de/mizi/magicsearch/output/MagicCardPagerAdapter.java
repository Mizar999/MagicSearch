package de.mizi.magicsearch.output;

import java.util.List;

import de.mizi.magicsearch.data.MagicCardData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MagicCardPagerAdapter extends FragmentStatePagerAdapter
{
	private List<MagicCardData> data;
	
	public MagicCardPagerAdapter(FragmentManager manager, List<MagicCardData> data)
	{
		super(manager);
		this.data = data;
	}

	@Override
	public Fragment getItem(int listIndex)
	{
		Fragment fragment = new MagicCardFragment();
		Bundle args = new Bundle();
		args.putParcelable(MagicCardFragment.KEY_DATA, data.get(listIndex));
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public int getCount()
	{
		return data.size();
	}
}
