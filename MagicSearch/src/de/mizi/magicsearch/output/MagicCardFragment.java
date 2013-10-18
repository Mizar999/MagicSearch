package de.mizi.magicsearch.output;

import de.mizi.magicsearch.R;
import de.mizi.magicsearch.data.MagicCardData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MagicCardFragment extends Fragment
{
	public static final String KEY_DATA = "data";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle args = getArguments();
		MagicCardData data = (MagicCardData)args.getParcelable(KEY_DATA);
		
		View rootView = inflater.inflate(R.layout.swipe_fragment, container, false);
		((TextView) rootView.findViewById(R.id.debugout)).setText(data.id + " " + data.name);
		return rootView;
	}
}
