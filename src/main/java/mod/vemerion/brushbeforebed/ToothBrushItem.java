package mod.vemerion.brushbeforebed;

import java.util.Random;

import mod.vemerion.brushbeforebed.brushcapability.BrushTeeth;
import mod.vemerion.brushbeforebed.brushcapability.BrushTeethProvider;
import mod.vemerion.brushbeforebed.brushcapability.IBrushTeeth;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

public class ToothBrushItem extends Item {
	
	@ObjectHolder(BrushBeforeBed.MODID + ":brush_particle")
	public static final Item BRUSH_PARTICLE = null;
	
	private Random rand = new Random();
	private int i = 0;

	public ToothBrushItem() {
		super(new Item.Properties().maxStackSize(1).maxDamage(16).group(ItemGroup.TOOLS));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if (!worldIn.isRemote) {
			stack.damageItem(1, entityLiving, (entity) -> {
				entity.sendBreakAnimation(entity.getActiveHand());
			});
			IBrushTeeth brushTeeth = entityLiving.getCapability(BrushTeethProvider.BRUSHTEETH_CAP).orElse(new BrushTeeth());
			brushTeeth.setTeethBrushed(true);
		}
		return stack;
	   }

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return ActionResult.resultConsume(itemstack);
	}
	
	@Override
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (worldIn.isRemote) {
			addBrushParticles(3, livingEntityIn);
			i++;
			if (i % 4 == 0) {
				i = 0;
				livingEntityIn.playSound(livingEntityIn.getEatSound(stack), 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}
	
	private void addBrushParticles(int count, LivingEntity livingEntityIn) {
	      for(int i = 0; i < count; ++i) {
	         Vec3d vec3d = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
	         vec3d = vec3d.rotatePitch(-livingEntityIn.rotationPitch * ((float)Math.PI / 180F));
	         vec3d = vec3d.rotateYaw(-livingEntityIn.rotationYaw * ((float)Math.PI / 180F));
	         double d0 = (double)(-rand.nextFloat()) * 0.6D - 0.3D;
	         Vec3d vec3d1 = new Vec3d(((double)rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
	         vec3d1 = vec3d1.rotatePitch(-livingEntityIn.rotationPitch * ((float)Math.PI / 180F));
	         vec3d1 = vec3d1.rotateYaw(-livingEntityIn.rotationYaw * ((float)Math.PI / 180F));
	         vec3d1 = vec3d1.add(livingEntityIn.getPosX(), livingEntityIn.getPosYEye(), livingEntityIn.getPosZ());
	         if (livingEntityIn.world instanceof ServerWorld) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
	            ((ServerWorld)livingEntityIn.world).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(BRUSH_PARTICLE)), vec3d1.x, vec3d1.y, vec3d1.z, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
	         else
	        	 livingEntityIn.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(BRUSH_PARTICLE)), vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
	      }

	   }

}
