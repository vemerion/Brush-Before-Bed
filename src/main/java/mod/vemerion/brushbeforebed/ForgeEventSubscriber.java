package mod.vemerion.brushbeforebed;

import mod.vemerion.brushbeforebed.brushcapability.BrushTeeth;
import mod.vemerion.brushbeforebed.brushcapability.BrushTeethProvider;
import mod.vemerion.brushbeforebed.brushcapability.IBrushTeeth;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = BrushBeforeBed.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {

	@ObjectHolder(BrushBeforeBed.MODID + ":tooth_brush")
	public static final Item TOOTH_BRUSH = null;

	// Checks that player has brushed teeth before going to bed
	@SubscribeEvent
	public static void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (!event.getEntity().world.isRemote) {
				boolean canSleep = !player.getCapability(BrushTeethProvider.BRUSHTEETH_CAP).filter((brushTeeth) -> {
					return !brushTeeth.isTeethBrushed();
				}).isPresent();
				if (!canSleep) {
					event.setResult(PlayerEntity.SleepResult.OTHER_PROBLEM);
					player.sendStatusMessage(new StringTextComponent("You forgot to brush your teeth!"), true);
				}
			}
		}
	}

	// Reset sleep boolean
	@SubscribeEvent
	public static void onWakeup(PlayerWakeUpEvent event) {
		IBrushTeeth brushTeeth = event.getPlayer().getCapability(BrushTeethProvider.BRUSHTEETH_CAP)
				.orElse(new BrushTeeth());
		brushTeeth.setTeethBrushed(false);
	}

	// Cancel drink sound when brushing teeth
	@SubscribeEvent()
	public static void onPlaySound(PlaySoundAtEntityEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (player.world.isRemote
					&& player.getActiveItemStack().isItemEqualIgnoreDurability(new ItemStack(TOOTH_BRUSH))
					&& (event.getSound().getName().getPath().equals("entity.generic.drink")
							|| event.getSound().getName().getPath().equals("item.armor.equip_generic"))) {
				event.setCanceled(true);
			}
		}
	}

	public static final ResourceLocation BRUSHTEETH_CAP = new ResourceLocation(BrushBeforeBed.MODID, "brushteeth");

	// Add brush teeth capability to player
	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof PlayerEntity))
			return;

		event.addCapability(BRUSHTEETH_CAP, new BrushTeethProvider());
	}
}
