package de.mizi.magicsearch.output;

import java.util.List;

import de.mizi.magicsearch.data.MagicCardData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * This class creates each fragment with the cardinfo that is shown in the detail view of a magiccard.
 */
public class MagicCardPagerAdapter extends FragmentStatePagerAdapter
{
	/**
	 * All cards that shall be shown.
	 */
	private List<MagicCardData> data;
	
	/**
	 * Creates the adapter with the given carddata that shall be shown. There will be a fragment created for each single magiccard.
	 * 
	 * @param manager needed for backward support
	 * @param data all cards that shall be shown
	 */
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
		args.putString(MagicCardFragment.KEY_PAGENUMBER, String.format("%s / %s", listIndex + 1, data.size()));
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public int getCount()
	{
		return data.size();
	}
}
