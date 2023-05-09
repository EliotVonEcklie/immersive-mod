package moe.eliotvonecklie.immersive.api;

import io.icker.factions.api.persistents.User;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface marks {@link BlockEntity}s and {@link net.minecraft.block.Block}s as "faction-ownable". Any block entity that implements this interface 
 * can only be broken or modified by anyone from the faction and the rank that placed it down.
 *
 * @author eliotvonecklie
 */
public interface FactionOwnable {
	/**
	 * @return A FactionOwner object containing the faction's name and UUID.
	 */
	public FactionOwner getOwner();

	/**
	 * Executes after the owner has been changed.
	 *
	 * @param level The current level.
	 * @param state The Ownable's state.
	 * @param pos The Ownable's position.
	 * @param player The player that changed the owner of the FactionOwnable.
	 */
	default void onOwnerChanged(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		BlockEntity be = (BlockEntity) this;
		be.markDirty();
	}

	/**
	 * Checks whether the given player owns this FactionOwnable.
	 *
	 * @param player The player to check ownership of.
	 * @return true if the given player owns this FactionOwnable, false otherwise.
	 */
	public default boolean isOwnedBy(User user) {
		if (user == null)
			return false;

		if (user.getFaction() == null)
			return false;
		
		return isOwnedBy(new FactionOwner(user));
	}

	/**
	 * Checks whether the given owner owns this FactionOwnable.
	 *
	 * @param otherOwner The owner to check ownership of.
	 * @return true if the given owner owns this FactionOwnable, false otherwise.
	 */
	public default boolean isOwnedBy(FactionOwner otherOwner) {
		FactionOwner self = getOwner();

		String selfUUID = self.getUUID();
		String otherUUID = otherOwner.getUUID();
		String otherName = otherOwner.getName();

		if (otherUUID != null && otherUUID.equals(selfUUID))
			return true;

		return otherName != null && selfUUID.equals("factionOwnerUUID") && otherName.equals(self.getName());
	}
}
