package de.mizi.magicsearch.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.mizi.magicsearch.R;

/**
 * The helper class for the ORMLite framework.
 */
public class MagicDatabaseHelper extends OrmLiteSqliteOpenHelper
{
	/**
	 * The name of the database that shall be created.
	 */
	private static final String DATABASE_NAME = "magicsearch.db";
	/**
	 * The versionnumber of the database. Increase this value to create an all new database.
	 */
	private static final int DATABASE_VERSION = 6;
	
	/**
	 * The cached dao object, that will be returned by thsi class.
	 */
	private Dao<MagicCardData, Integer> magicDao = null;
	
	/**
	 * The constructor, that is needed to create an object of this helperclass
	 * 
	 * @param context the needed context object
	 */
	public MagicDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
	{
		try {
			TableUtils.createTable(connectionSource, MagicCardData.class);
		} catch(SQLException e) {
			Log.e(MagicDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		if(newVersion > oldVersion)
		{
			try {
				TableUtils.dropTable(connectionSource, MagicCardData.class, true);
				onCreate(database, connectionSource);
			} catch(SQLException e) {
				Log.e(MagicDatabaseHelper.class.getName(), "Can't drop database", e);
				throw new RuntimeException(e);
			}
		}
		else {
			String message = "no upgrade needed (new version: " + newVersion + ", old version: " + oldVersion + ")";
			Log.i(MagicDatabaseHelper.class.getName(), message);
		}
	}
	
	@Override
	public void close() {
		super.close();
		magicDao = null;
	}
	
	/**
	 * This method returns a cached dao object for access to the magiccard table of the database.
	 * 
	 * @return a cached dao object for the magiccard table
	 */
	public Dao<MagicCardData, Integer> getMagicDao() throws SQLException
	{
		if(magicDao == null) {
			magicDao = getDao(MagicCardData.class);
		}
		return magicDao;
	}
}
