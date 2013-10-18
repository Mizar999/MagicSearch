package de.mizi.magicsearch.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "magiccard")
public class MagicCardData
{
	@DatabaseField(generatedId = true)
	private long id;
	
	@DatabaseField(canBeNull = false)
	private String name;
	@DatabaseField
	private String cost;
	@DatabaseField
	private int convertedCost;
	@DatabaseField
	private String types;
	@DatabaseField
	private String text;
	@DatabaseField
	private String power;
	@DatabaseField
	private String toughness;
	@DatabaseField(canBeNull = false)
	private String expansion;
	@DatabaseField
	private MagicCardRarity rarity;
	@DatabaseField
	private String number;
	
	public MagicCardData()
	{
		// needed for ORMLite
	}
}
