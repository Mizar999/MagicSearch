package de.mizi.magicsearch.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class represents a card. It's also the only table in the database and each object is parcelable.
 */
@DatabaseTable(tableName = "magiccard")
public class MagicCardData implements Parcelable
{
	/**
	 * The name of the cardname column in the database
	 */
	public static final String CARDNAME_FIELD_NAME = "cardname";
	
	/**
	 * The unique id field of a magiccard.
	 */
	@DatabaseField(generatedId = true)
	public long id;
	
	/**
	 * The name of a amagiccard.
	 */
	@DatabaseField(canBeNull = false, columnName = CARDNAME_FIELD_NAME)
	public String name;
	/**
	 * The manacost of a magiccard.
	 */
	@DatabaseField
	public String cost;
	/**
	 * The converted manacost of a magiccard.
	 */
	@DatabaseField
	public String convertedCost;
	/**
	 * The typeline of a magiccard.
	 */
	@DatabaseField
	public String types;
	/**
	 * The rulestext of a magiccard.
	 */
	@DatabaseField
	public String rulesText;
	/**
	 * The flavortext of a magiccard.
	 */
	@DatabaseField
	public String flavorText;
	/**
	 * The artistname of a magiccard.
	 */
	@DatabaseField
	public String artist;
	/**
	 * The power value of a magiccard.
	 */
	@DatabaseField
	public String power;
	/**
	 * The toughness value of a magiccard.
	 */
	@DatabaseField
	public String toughness;
	/**
	 * The loyalty value of a magiccard.
	 */
	@DatabaseField
	public String loyalty;
	/**
	 * The expansion name of a magiccard.
	 */
	@DatabaseField(canBeNull = false)
	public String expansion;
	/**
	 * The rarity of a magiccard.
	 */
	@DatabaseField
	public MagicCardRarity rarity;
	/**
	 * The collectorsnumber of a magiccard.
	 */
	@DatabaseField
	public String number;
	/**
	 * The rules faq of a magiccard.
	 */
	@DatabaseField
	public String cardRules;
	/**
	 * The image url of a magiccard.
	 */
	@DatabaseField
	public String imageUrl;
	
	/**
	 * The dafault constructor for creating a magiccard.
	 */
	public MagicCardData()
	{
		// needed for ORMLite
	}
	
	/**
	 * Returns a string representation of all values of a magiccard for debugging reasons.
	 * 
	 * @return a string representation of a magiccard
	 */
	@Override
	public String toString()
	{
		return id + ";" + name + ";" + cost + ";" + convertedCost + ";"
				+ types + ";" + rulesText + ";" + flavorText + ";" + artist + ";"
				+ power + ";" + toughness + ";" + loyalty + ";" + expansion + ";"
				+ rarity + ";"+ number + ";" + cardRules + ";" + imageUrl;
	}
	
	/**
	 * This object describes how to create a magiccard object from parcelable data and vice versa
	 */
	public static final Parcelable.Creator<MagicCardData> CREATOR = new Parcelable.Creator<MagicCardData>()
	{
		@Override
		public MagicCardData createFromParcel(Parcel source) {
			return new MagicCardData(source);
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
		dest.writeString(convertedCost);
		dest.writeString(types);
		dest.writeString(rulesText);
		dest.writeString(flavorText);
		dest.writeString(artist);
		dest.writeString(power);
		dest.writeString(toughness);
		dest.writeString(loyalty);
		dest.writeString(expansion);
		dest.writeInt(rarity == null ? null : rarity.ordinal());
		dest.writeString(number);
		dest.writeString(cardRules);
		dest.writeString(imageUrl);
	}
	
	/**
	 * This constructor creates a magiccard object from parcelable data.
	 */
	public MagicCardData(Parcel source)
	{
		id = source.readLong();
		name = source.readString();
		cost= source.readString();
		convertedCost = source.readString();
		types = source.readString();
		rulesText = source.readString();
		flavorText = source.readString();
		artist = source.readString();
		power = source.readString();
		toughness = source.readString();
		loyalty = source.readString();
		expansion = source.readString();
		rarity = MagicCardRarity.values()[source.readInt()];
		number = source.readString();
		cardRules = source.readString();
		imageUrl = source.readString();
	}
}
