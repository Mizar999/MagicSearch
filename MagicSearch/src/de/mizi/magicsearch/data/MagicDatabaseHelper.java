package de.mizi.magicsearch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import de.mizi.magicsearch.R;

public class MagicDatabaseHelper extends OrmLiteSqliteOpenHelper
{
	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "magicsearch.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	
	public MagicDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		
	}
}
