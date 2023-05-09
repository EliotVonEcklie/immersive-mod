package moe.eliotvonecklie.immersive.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * This class is used with {@link Ownable} to get the player of the block. Allows for easy access to the player's IGN and
 * UUID, with a few helpful methods as well.
 *
 * @author eliotvonecklie
 * @author Geforce
 */
public class Owner {
	private String ownerName = "ownerName";
	private String ownerUUID = "ownerUUID";

	public Owner() {}

	public Owner(Entity entity) {
		if (entity instanceof PlayerEntity player) {
			ownerName = player.getName().getString();
			ownerUUID = player.getGameProfile().getId().toString();
		}
	}

	public Owner(PlayerEntity player) {
		this.ownerName = player.getName().getString();
		this.ownerUUID = player.getGameProfile().getId().toString();
	}

	public Owner(String playerName, String playerUUID) {
		this.ownerName = playerName;
		this.ownerUUID = playerUUID;
	}

	public static Owner fromCompound(NbtCompound nbt) {
		Owner owner = new Owner();

		if (nbt != null)
			owner.load(nbt);

		return owner;
	}

	public void load(NbtCompound nbt) {
		if (nbt.contains("ownerName"))
			ownerName = nbt.getString("ownerName");

		if (nbt.contains("ownerUUID"))
			ownerUUID = nbt.getString("ownerUUID");
	}

	public void save(NbtCompound nbt) {
		nbt.putString("ownerName", ownerName);
		nbt.putString("ownerUUID", ownerUUID);
	}

	/**
	 * @return If this user is the owner of the given blocks.
	 */
	public boolean owns(Ownable... ownables) {
		for (Ownable ownable : ownables) {
			if (ownable == null)
				continue;

			Owner ownableOwner = ownable.getOwner();
			String uuidToCheck = ownableOwner.getUUID();
			String nameToCheck = ownableOwner.getName();
			
			// Check the player's UUID first.
			if (uuidToCheck != null && !uuidToCheck.equals(ownerUUID))
				return false;

			// If the BlockEntity doesn't have a UUID saved, use the player's name instead.
			if (nameToCheck != null && uuidToCheck.equals("ownerUUID") && !nameToCheck.equals("ownerName") && !nameToCheck.equals(ownerName))
				return false;
		}

		return true;
	}

	/**
	 * Set the UUID and name of a new owner using strings.
	 */
	public void set(String uuid, String name) {
		ownerName = name;
		ownerUUID = uuid;
	}

	/**
	 * Set the owner's new name.
	 *
	 * @param name The new owner's name
	 */
	public void setName(String name) {
		ownerName = name;
	}

	/**
	 * Set the owner's new UUID.
	 *
	 * @param uuid The new owner's UUID
	 */
	public void setUUID(String uuid) {
		ownerUUID = uuid;
	}

	/**
	 * @return The owner's name.
	 */
	public String getName() {
		return ownerName;
	}

	/**
	 * @return The owner's UUID.
	 */
	public String getUUID() {
		return ownerUUID;
	}

	@Override
	public String toString() {
		return "Name: " + ownerName + "  UUID: " + ownerUUID;
	}
}
