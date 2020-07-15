package mod.vemerion.brushbeforebed.brushcapability;

public class BrushTeeth implements IBrushTeeth {
	
	private boolean brushTeeth = false;

	@Override
	public void setTeethBrushed(boolean value) {
		brushTeeth = value;
	}

	@Override
	public boolean isTeethBrushed() {
		return brushTeeth;
	}

}
