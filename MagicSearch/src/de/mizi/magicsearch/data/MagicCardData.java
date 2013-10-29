package de.mizi.magicsearch.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "magiccard")
public class MagicCardData implements Parcelable
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
	
	// Code for Parcelable interface
	public static final Parcelable.Creator<MagicCardData> CREATOR = new Parcelable.Creator<MagicCardData>()
	{
		@Override
		public MagicCardData createFromParcel(Parcel source) {
			return null;
		}
		
		@Override
		public MagicCardData[] newArray(int size) {
			return new MagicCardData[size];
		}
	};
	
	@Override
	public int describeContents()
	{
		// ignored
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(cost);
		dest.writeInt(convertedCost);
		dest.writeString(types);
		dest.writeString(rulesText);
		dest.writeString(flavorText);
		dest.writeString(artist);
		dest.writeString(power);
		dest.writeString(toughness);
		dest.writeString(expansion);
		dest.writeInt(rarity == null ? null : rarity.ordinal());
		dest.writeString(number);
		dest.writeString(cardRules);
		dest.writeString(filename);
	}
	
	// Constructor from parcel reads back fields in the order they were written
	public MagicCardData(Parcel source)
	{
		id = source.readLong();
		name = source.readString();
		cost= source.readString();
		convertedCost = source.readInt();
		types = source.readString();
		rulesText = source.readString();
		flavorText = source.readString();
		artist = source.readString();
		power = source.readString();
		toughness = source.readString();
		expansion = source.readString();
		rarity = MagicCardRarity.values()[source.readInt()];
		number = source.readString();
		cardRules = source.readString();
		filename = source.readString();
	}
}
