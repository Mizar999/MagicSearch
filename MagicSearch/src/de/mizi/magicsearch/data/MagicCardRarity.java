package de.mizi.magicsearch.data;

/**
 * This enum represents the rarity of a magiccard.
 */
public enum MagicCardRarity
{
	COMMON("Common"),
	UNCOMMON("Uncommon"),
	RARE("Rare"),
	MYTHIC_RARE("Mythic Rare"),
	TIMESHIFTED("Timeshifted"),
	SPECIAL("Special"),
	NONE("none");
	
	/**
	 * The string representation of the rarity value.
	 */
	private final String name;
	
	/**
	 * Creates a rarity value.
	 */
	private MagicCardRarity(String name)
	{
		this.name = name;
	}
	
	/**
	 * Returns a printable string represntation of the rarity value
	 * 
	 * @return a printable string representation
	 */
	public String toString()
	{
		return name;
	}
	
	/**
	 * Transforms a string value into a matching rarity value.
	 * 
	 * @param rarity the string value that shall be transformed into a rarity value
	 * @return the matching rarity value or the rarity value 'NONE', if no rarity value matches the string value
	 */
	public static MagicCardRarity stringToRarity(String rarity)
	{
		for(MagicCardRarity element: values())
		{
			if(element.toString().equals(rarity))
			{
				return element;
			}
		}
		
		return NONE;
	}
}
