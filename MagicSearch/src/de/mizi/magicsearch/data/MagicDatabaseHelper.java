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

public class MagicDatabaseHelper extends OrmLiteSqliteOpenHelper
{
	private static final String DATABASE_NAME = "magicsearch.db";
	private static final int DATABASE_VERSION = 6;
	
	private Dao<MagicCardData, Integer> magicDao = null;
	
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
	
	public Dao<MagicCardData, Integer> getMagicDao() throws SQLException
	{
		if(magicDao == null) {
			magicDao = getDao(MagicCardData.class);
		}
		return magicDao;
	}
}
