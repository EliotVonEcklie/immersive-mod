package moe.eliotvonecklie.immersive.blockentities;

import io.icker.factions.api.persistents.User;
import moe.eliotvonecklie.immersive.api.FactionOwnable;
import moe.eliotvonecklie.immersive.api.FactionOwner;
import moe.eliotvonecklie.immersive.blocks.SecurityFactionRankReader;
import moe.eliotvonecklie.immersive.registry.BlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public class SecurityFactionRankReaderBlockEntity extends BlockEntity implements FactionOwnable {
	private FactionOwner owner;
	
	public SecurityFactionRankReaderBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.SECURITY_FACTION_RANK_READER_BLOCKENTITY, pos, state);
	}

	public void configure(User user) {
		this.owner = new FactionOwner(user);
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
	public FactionOwner getOwner() {
		return owner;
	}

}
