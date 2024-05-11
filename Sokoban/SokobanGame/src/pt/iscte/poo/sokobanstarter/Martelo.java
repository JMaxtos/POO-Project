package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Martelo extends GameElement implements ImageTile,Usable {

	private Point2D position;

	public Martelo(Point2D Point2D) {
		super(Point2D);
		position=super.getPosition();
	}

	@Override
	public String getName() {
		return "Martelo";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 2;
	}


	@Override
	public boolean isUsable() {
		
		return true;
	}

	@Override
	public void use(Empilhadora bobcat) {
		bobcat.setHammer(this);
		this.remove();
	}

	

}
