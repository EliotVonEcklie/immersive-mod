package moe.eliotvonecklie.immersive.blockentities;

import moe.eliotvonecklie.immersive.api.Ownable;
import moe.eliotvonecklie.immersive.api.Owner;
import moe.eliotvonecklie.immersive.blocks.SecurityFactionRankReader;
import moe.eliotvonecklie.immersive.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class SecurityIdentityReaderBlockEntity extends BlockEntity implements Ownable {
	private Owner owner;

	public SecurityIdentityReaderBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.SECURITY_FACTION_RANK_READER_BLOCKENTITY, pos, state);
	}

	public void configure(PlayerEntity player) {
		this.owner = new Owner(player);
	}
	
	@Override
    public void writeNbt(NbtCompound nbt) {
    	if (getCachedState().get(SecurityFactionRankReader.CONFIGURED)) {
    		owner.save(nbt);
    	}
 
        super.writeNbt(nbt);
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
     
    	if (getCachedState().get(SecurityFactionRankReader.CONFIGURED)) {
    		owner.load(nbt);
    	}
    }

	@Override
	public Owner getOwner() {
		return owner;
	}
}
