package de.mizi.magicsearch;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mizi.magicsearch.data.MagicDatabaseHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		TextView view = new TextView(this);
		view.setText("Test");
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
