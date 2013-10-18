package de.mizi.magicsearch.data;

public enum MagicCardRarity
{
	COMMON("Common"),
	UNCOMMON("Uncommon"),
	RARE("Rare"),
	MYTHIC_RARE("Mythic Rare"),
	TIMESHIFTED("Timeshifted"),
	SPECIAL("Special"),
	NONE("none");
	
	private final String name;
	
	private MagicCardRarity(String name)
	{
		this.name = name;
	}
	
	public String toString()
	{
		return name;
	}
}
