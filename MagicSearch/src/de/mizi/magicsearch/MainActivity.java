package de.mizi.magicsearch;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mizi.magicsearch.data.MagicDatabaseHelper;
import de.mizi.magicsearch.output.FragmentMainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This is the entry point for this app.
 */
public class MainActivity extends OrmLiteBaseActivity<MagicDatabaseHelper>
{
	/**
	 * The key for the bundle to lookup if the webscraping button shall be enabled or not.
	 */
	public static final String KEY_ENABLED = "enabled";
	/**
	 * The key for the bundle to lookup the text of the webscraping button.
	 */
	public static final String KEY_TEXT = "text";
	
	/**
	 * This object receives all infos about the download service intent, that downloads the carddata.
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			if(bundle != null)
			{
				Button webscraping = (Button)MainActivity.this.findViewById(R.id.main_button_webscraping);
				
				int result = bundle.getInt(MagicWebscrapingService.KEY_RESULT);
				if(result == MagicWebscrapingService.RESULT_UPDATE)
				{
					webscraping.setText( bundle.getString(MagicWebscrapingService.KEY_UPDATE_MESSAGE) );
				}
				else {
					webscraping.setEnabled(true);
					webscraping.setText(context.getResources().getString(R.string.main_button_webscraping));
					
					if(result == MagicWebscrapingService.RESULT_OK)
					{
						Toast.makeText(context, context.getResources().getString(R.string.webscraping_success), Toast.LENGTH_LONG).show();
					}
					else {
						Toast.makeText(context, context.getResources().getString(R.string.webscraping_failure), Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button search = (Button)findViewById(R.id.main_button_search);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				EditText edit = (EditText)findViewById(R.id.main_edit);
				String nameToSearch = edit.getText().toString();
				
				Intent intent = new Intent(MainActivity.this, SearchResultListActivity.class);
				intent.putExtra(SearchResultListActivity.KEY_NAME_TO_SEARCH, nameToSearch);
				startActivity(intent);
			}
		});
		
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		Button webscraping = (Button)MainActivity.this.findViewById(R.id.main_button_webscraping);
		outState.putBoolean(KEY_ENABLED, webscraping.isEnabled());
		outState.putString(KEY_TEXT, webscraping.getText().toString());
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		
		Button webscraping = (Button)MainActivity.this.findViewById(R.id.main_button_webscraping);
		webscraping.setEnabled( savedInstanceState.getBoolean(KEY_ENABLED) );
		webscraping.setText( savedInstanceState.getString(KEY_TEXT) );
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		registerReceiver(receiver, new IntentFilter(MagicWebscrapingService.KEY_NOTIFICATION));
	}
	
	/**
	 * This method defines the magic expansions that shall be downloaded and initiates the download service.
	 * 
	 * @throws SQLException if the database is not reachable
	 */
	private void startWebscraping() throws SQLException
	{
		String[] expansions = new String[]{"rtr", "gtc", "dgm", "m14", "ths"};
		Intent intent = new Intent(MainActivity.this, MagicWebscrapingService.class);
		intent.putExtra(MagicWebscrapingService.KEY_EXPANSIONS, expansions);
		startService(intent);
		
		Button webscraping = (Button)MainActivity.this.findViewById(R.id.main_button_webscraping);
		webscraping.setEnabled(false);
		
		registerReceiver(receiver, new IntentFilter(MagicWebscrapingService.KEY_NOTIFICATION));
	}
}
