package mod.vemerion.brushbeforebed.brushcapability;


import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BrushTeethProvider implements ICapabilitySerializable<INBT>{
	@CapabilityInject(IBrushTeeth.class)
	public static final Capability<IBrushTeeth> BRUSHTEETH_CAP = null;

	private LazyOptional<IBrushTeeth> instance = LazyOptional.of(BRUSHTEETH_CAP::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return BRUSHTEETH_CAP.orEmpty(cap, instance);
	}

	@Override
	public INBT serializeNBT() {
		return BRUSHTEETH_CAP.getStorage().writeNBT(BRUSHTEETH_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		BRUSHTEETH_CAP.getStorage().readNBT(BRUSHTEETH_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
