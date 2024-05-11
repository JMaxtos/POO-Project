package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Buraco extends GameElement implements ImageTile, ObstaculoImo{



	public Buraco(Point2D Point2D) {
		super(Point2D);
		
	}

	@Override
	public String getName() {
		return "Buraco";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isObstaculoImo() {
		return true;
	}


}
