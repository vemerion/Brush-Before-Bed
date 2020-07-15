package mod.vemerion.brushbeforebed;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.vemerion.brushbeforebed.brushcapability.BrushTeeth;
import mod.vemerion.brushbeforebed.brushcapability.BrushTeethStorage;
import mod.vemerion.brushbeforebed.brushcapability.IBrushTeeth;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BrushBeforeBed.MODID)
public class BrushBeforeBed {
	
	public static final String MODID = "brush-before-bed";
	private static final Logger LOGGER = LogManager.getLogger();
	
	public BrushBeforeBed() {
		LOGGER.debug("Hello from " + MODID);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}
	
	private void setup(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(IBrushTeeth.class, new BrushTeethStorage(), BrushTeeth::new);
    }
}
