package de.mizi.magicsearch.output;

import de.mizi.magicsearch.R;
import de.mizi.magicsearch.data.MagicCardData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This class represents a visible fragment that holds all informations of one single magiccard.
 */
public class MagicCardFragment extends Fragment
{
	/**
	 * The key for the bundle to lookup the object that represents the cardinfos of a single card
	 */
	public static final String KEY_DATA = "carddata";
	/**
	 * The key for the bundle to lookup the pagenumber of the current fragment.
	 */
	public static final String KEY_PAGENUMBER = "pagenumber";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		MagicCardData data = (MagicCardData)args.getParcelable(MagicCardFragment.KEY_DATA);
		String pagenumber = args.getString(MagicCardFragment.KEY_PAGENUMBER);
		
		View rootView = inflater.inflate(R.layout.swipe_fragment, container, false);
		((TextView) rootView.findViewById(R.id.cardIdOut)).setText("" + data.id);
		((TextView) rootView.findViewById(R.id.nameOut)).setText(data.name);
		((TextView) rootView.findViewById(R.id.costOut)).setText(data.cost);
		((TextView) rootView.findViewById(R.id.convertedCostOut)).setText("" + data.convertedCost);
		((TextView) rootView.findViewById(R.id.typesOut)).setText(data.types);
		((TextView) rootView.findViewById(R.id.rulesOut)).setText(data.rulesText);
		((TextView) rootView.findViewById(R.id.flavorOut)).setText(data.flavorText);
		((TextView) rootView.findViewById(R.id.artistOut)).setText(data.artist);
		((TextView) rootView.findViewById(R.id.powerOut)).setText(data.power);
		((TextView) rootView.findViewById(R.id.toughnessOut)).setText(data.toughness);
		((TextView) rootView.findViewById(R.id.loyaltyOut)).setText(data.loyalty);
		((TextView) rootView.findViewById(R.id.expansionOut)).setText(data.expansion);
		((TextView) rootView.findViewById(R.id.rarityOut)).setText(data.rarity.toString());
		((TextView) rootView.findViewById(R.id.numberOut)).setText(data.number);
		((TextView) rootView.findViewById(R.id.cardRulesOut)).setText(data.cardRules);
		
		((TextView) rootView.findViewById(R.id.pagenumber)).setText(pagenumber);
		
		// Prepare all elements that are needed for the image download of a magiccard.
		ImageView image = (ImageView) rootView.findViewById(R.id.cardImage);
		ProgressBar progress = (ProgressBar) rootView.findViewById(R.id.cardImageProgress);
		TextView text = (TextView) rootView.findViewById(R.id.cardImageMessage);
		// Start the image download of a magiccard in an asynchronus task.
		MagicCardImageAsyncTask task = new MagicCardImageAsyncTask(image, progress, text);
		task.execute(data.imageUrl);
		
		return rootView;
	}
}
