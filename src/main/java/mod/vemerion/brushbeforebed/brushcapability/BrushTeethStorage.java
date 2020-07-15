package mod.vemerion.brushbeforebed.brushcapability;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BrushTeethStorage implements IStorage<IBrushTeeth> {

	@Override
	public INBT writeNBT(Capability<IBrushTeeth> capability, IBrushTeeth instance, Direction side) {
		return ByteNBT.valueOf(instance.isTeethBrushed());
		
	}

	@Override
	public void readNBT(Capability<IBrushTeeth> capability, IBrushTeeth instance, Direction side, INBT nbt) {
		instance.setTeethBrushed(((ByteNBT)nbt).getByte() == 1);
	}
}
