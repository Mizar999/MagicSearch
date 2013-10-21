package de.mizi.magicsearch;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mizi.magicsearch.data.MagicDatabaseHelper;
import de.mizi.magicsearch.output.FragmentMainActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
		TextView view = new TextView(this);
		view.setText("use the menu button ...");
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.menuWebscrapping:
            try {
				startWebscraping();
			} catch (SQLException e) {
				e.printStackTrace();
			}
            return true;
        case R.id.menuShowdata:
            Intent intent = new Intent(this, FragmentMainActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	private void startWebscraping() throws SQLException
	{
		ProgressDialog progress = ProgressDialog.show(this, "Status", "collecting data ...");
		MagicWebscrapingAsyncTask scraping = new MagicWebscrapingAsyncTask(getHelper(), progress);
		scraping.execute("dgm");
	}
}
