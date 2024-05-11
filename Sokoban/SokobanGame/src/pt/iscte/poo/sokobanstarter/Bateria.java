package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Bateria extends GameElement implements ImageTile, Usable {

	private static final int carga = 50;

	public Bateria(Point2D Point2D) {
		super(Point2D);
		
	}

	@Override
	public String getName() {
		return "Bateria";
	}



	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean isUsable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void use(Empilhadora bobcat) {
		bobcat.setEnergy(bobcat.getEnergy() + carga);
		this.remove();
	}
	





	

}
