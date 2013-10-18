package de.mizi.magicsearch.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "magiccard")
public class MagicCardData
{
	@DatabaseField(generatedId = true)
	public long id;
	
	@DatabaseField(canBeNull = false)
	public String name;
	@DatabaseField
	public String cost;
	@DatabaseField
	public int convertedCost;
	@DatabaseField
	public String types;
	@DatabaseField
	public String rulesText;
	@DatabaseField
	public String flavorText;
	@DatabaseField
	public String artist;
	@DatabaseField
	public String power;
	@DatabaseField
	public String toughness;
	@DatabaseField(canBeNull = false)
	public String expansion;
	@DatabaseField
	public MagicCardRarity rarity;
	@DatabaseField
	public String number;
	@DatabaseField
	public String cardRules;
	@DatabaseField
	public String filename;
	
	public MagicCardData()
	{
		// needed for ORMLite
	}
	
	@Override
	public String toString()
	{
		return id + ";" + name + ";" + cost + ";" + convertedCost + ";"
				+ types + ";" + rulesText + ";" + flavorText + ";" + artist + ";"
				+ power + ";" + toughness + ";" + expansion + ";" + rarity + ";"
				+ number + ";" + cardRules + ";" + filename;
	}
}
