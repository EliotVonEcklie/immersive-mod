package moe.eliotvonecklie.immersive.api;

import io.icker.factions.api.persistents.Faction;
import io.icker.factions.api.persistents.User;
import net.minecraft.nbt.NbtCompound;

/**
 * This class is used with {@link FactionOwnable} to get the faction of the block. Allows for easy access to the faction's name and
 * UUID, with a few helpful methods as well.
 *
 * @author eliotvonecklie
 */
public class FactionOwner {
	private String factionOwnerName = "factionOwnerName";
	private String factionOwnerUUID = "factionOwnerUUID";
	private String factionOwnerRank = "factionOwnerRank";

	public FactionOwner() {}

	public FactionOwner(User user) {
		Faction faction = user.getFaction();
		
		factionOwnerName = faction.getName();
		factionOwnerUUID = faction.getID().toString();
		factionOwnerRank = user.getRankName();
	}

	public FactionOwner(String factionName, String factionUUID, String factionRank) {
		this.factionOwnerName = factionName;
		this.factionOwnerUUID = factionUUID;
		this.factionOwnerRank = factionRank;
	}

	public static FactionOwner fromCompound(NbtCompound nbt) {
		FactionOwner owner = new FactionOwner();

		if (nbt != null)
			owner.load(nbt);

		return owner;
	}

	public void load(NbtCompound nbt) {
		if (nbt.contains("factionOwnerName"))
			factionOwnerName = nbt.getString("factionOwnerName");

		if (nbt.contains("factionOwnerUUID"))
			factionOwnerUUID = nbt.getString("factionOwnerUUID");
		
		if (nbt.contains("factionOwnerRank"))
			factionOwnerUUID = nbt.getString("factionOwnerRank");
	}

	public void save(NbtCompound nbt) {
		nbt.putString("factionOwnerName", factionOwnerName);
		nbt.putString("factionOwnerUUID", factionOwnerUUID);
		nbt.putString("factionOwnerRank", factionOwnerRank);
	}

	/**
	 * @return If this user is the owner of the given blocks.
	 */
	public boolean owns(FactionOwnable... ownables) {	
		for (FactionOwnable ownable : ownables) {
			if (ownable == null)
				continue;

			FactionOwner ownableOwner = ownable.getOwner();
			String uuidToCheck = ownableOwner.getUUID();
			String nameToCheck = ownableOwner.getName();
			String rankToCheck = ownableOwner.getRank();
			
			// Check the player's UUID first.
			if (uuidToCheck != null && !uuidToCheck.equals(factionOwnerUUID))
				return false;

			// If the BlockEntity doesn't have a UUID saved, use the player's name instead.
			else if (nameToCheck != null && uuidToCheck.equals("factionOwnerUUID") && !nameToCheck.equals("factionOwnerName") && !nameToCheck.equals(factionOwnerName))
				return false;
			
			// Lastly, check the rank
			if (rankToCheck != null && !rankToCheck.equals(factionOwnerRank))
				return false;
		}

		return true;
	}

	/**
	 * Set the UUID and name of a new owner using strings.
	 */
	public void set(String uuid, String name, String rank) {
		factionOwnerName = name;
		factionOwnerUUID = uuid;
		factionOwnerRank = rank;
	}

	/**
	 * Set the owner's new name.
	 *
	 * @param name The new owner's name
	 */
	public void setName(String name) {
		factionOwnerName = name;
	}

	/**
	 * Set the owner's new UUID.
	 *
	 * @param uuid The new owner's UUID
	 */
	public void setUUID(String uuid) {
		factionOwnerUUID = uuid;
	}
	
	/**
	 * Set the owner's new rank.
	 *
	 * @param uuid The new owner's rank
	 */
	public void setRank(String rank) {
		factionOwnerRank = rank;
	}

	/**
	 * @return The owner's name.
	 */
	public String getName() {
		return factionOwnerName;
	}

	/**
	 * @return The owner's UUID.
	 */
	public String getUUID() {
		return factionOwnerUUID;
	}

	/**
	 * @return The owner's rank.
	 */
	public String getRank() {
		return factionOwnerRank;
	}
	
	@Override
	public String toString() {
		return "Name: " + factionOwnerName + "  UUID: " + factionOwnerUUID + " Rank: " + factionOwnerRank;
	}
}
