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

public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		scraping.execute("ths", "dgm");
	}
}
