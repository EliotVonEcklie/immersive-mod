package moe.eliotvonecklie.immersive.blocks;

import moe.eliotvonecklie.immersive.api.Owner;
import moe.eliotvonecklie.immersive.blockentities.SecurityIdentityReaderBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SecurityIdentityReader extends Block implements BlockEntityProvider {
	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
	public static final BooleanProperty POWERED = Properties.POWERED;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final BooleanProperty CONFIGURED = BooleanProperty.of("configured");

	public SecurityIdentityReader(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState()
			.with(FACING, Direction.NORTH)
			.with(POWERED, false)
			.with(WATERLOGGED, false)
			.with(CONFIGURED, false)
		);
	}
	
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, WATERLOGGED, CONFIGURED);
    }

    /**
     * FIXME: I'm not entirely happy with how this function works, come back and refactor it.
     * FIXME: Text literals, replace them with localized texts.
     */
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.isClient()) {
			SecurityIdentityReaderBlockEntity be = (SecurityIdentityReaderBlockEntity) world.getBlockEntity(pos);
			
			if (!state.get(CONFIGURED)) {
				if (player.isSneaking()) {
					be.configure(player);
					
					world.setBlockState(pos, state.with(CONFIGURED, true));
					
					player.sendMessage(Text.literal("Configured!"), false);
					
					return ActionResult.SUCCESS;
				}
				
				player.sendMessage(Text.literal("Not configured, use while sneaking to configure it with your identity."), false);
				
				return ActionResult.FAIL;
			}
			
			if (new Owner(player).owns(be)) {
				player.sendMessage(Text.literal("Authorized!"), false);
			} else {
				player.sendMessage(Text.literal("Not authorized!"), false);
			}
		}

		return ActionResult.SUCCESS;
	}
	
	public void activate(World world, BlockPos pos, int signalLength) {
		world.setBlockState(pos, world.getBlockState(pos).with(POWERED, true));
		//BlockUtils.updateIndirectNeighbors(level, pos, SCContent.KEYCARD_READER.get());
		//level.scheduleTick(pos, SCContent.KEYCARD_READER.get(), signalLength);
		
	}

	/*
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(POWERED, false));
		//BlockUtils.updateIndirectNeighbors(world, pos, SCContent.KEYCARD_READER.get());
	}
	*/
	/*
	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (state.getValue(POWERED)) {
				world.updateNeighbors(pos, this);
				BlockUtils.updateIndirectNeighbors(level, pos, this);
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	/*
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if ((state.getValue(POWERED))) {
			double x = pos.getX() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
			double y = pos.getY() + 0.7F + (rand.nextFloat() - 0.5F) * 0.2D;
			double z = pos.getZ() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
			double magicNumber1 = 0.2199999988079071D;
			double magicNumber2 = 0.27000001072883606D;
			float r = 0.6F + 0.4F;
			float g = Math.max(0.0F, 0.7F - 0.5F);
			float b = Math.max(0.0F, 0.6F - 0.7F);
			Vector3f vec = new Vector3f(r, g, b);

			level.addParticle(new DustParticleOptions(vec, 1), false, x - magicNumber2, y + magicNumber1, z, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(vec, 1), false, x + magicNumber2, y + magicNumber1, z, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(vec, 1), false, x, y + magicNumber1, z - magicNumber2, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(vec, 1), false, x, y + magicNumber1, z + magicNumber2, 0.0D, 0.0D, 0.0D);
			level.addParticle(new DustParticleOptions(vec, 1), false, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}
	*/

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SecurityIdentityReaderBlockEntity(pos, state);
    }
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}
}
