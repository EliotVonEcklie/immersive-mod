package moe.eliotvonecklie.immersive.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface marks {@link BlockEntity}s and {@link net.minecraft.block.Block}s as "ownable". Any block entity that implements this interface 
 * can only be broken or modified by the person who placed it down.
 *
 * @author eliotvonecklie
 * @author GeForce
 */
public interface Ownable {
	/**
	 * @return An Owner object containing the player's name and UUID
	 */
	public Owner getOwner();

	/**
	 * Executes after the owner has been changed.
	 *
	 * @param level The current level
	 * @param state The Ownable's state
	 * @param pos The Ownable's position
	 * @param player The player that changed the owner of the Ownable
	 */
	default void onOwnerChanged(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		BlockEntity be = (BlockEntity) this;
		be.markDirty();
	}

	/**
	 * Checks whether the given player owns this Ownable.
	 *
	 * @param player The player to check ownership of
	 * @return true if the given player owns this Ownable, false otherwise
	 */
	public default boolean isOwnedBy(PlayerEntity player) {
		if (player == null)
			return false;

		return isOwnedBy(new Owner(player));
	}

	/**
	 * Checks whether the given owner owns this Ownable.
	 *
	 * @param otherOwner The owner to check ownership of
	 * @return true if the given owner owns this Ownable, false otherwise
	 */
	public default boolean isOwnedBy(Owner otherOwner) {
		Owner self = getOwner();

		String selfUUID = self.getUUID();
		String otherUUID = otherOwner.getUUID();
		String otherName = otherOwner.getName();

		if (otherUUID != null && otherUUID.equals(selfUUID))
			return true;

		return otherName != null && selfUUID.equals("ownerUUID") && otherName.equals(self.getName());
	}
}
