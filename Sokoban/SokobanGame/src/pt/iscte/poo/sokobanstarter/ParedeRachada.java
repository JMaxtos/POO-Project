package pt.iscte.poo.sokobanstarter;


import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class ParedeRachada extends GameElement implements ImageTile, ObstaculoImo {



	public ParedeRachada(Point2D Point2D) {
		super(Point2D);
		
	}
	@Override
	public String getName() {
		return "ParedeRachada";
	}



	@Override
	public int getLayer() {
		return 1;
	}
	
	
	public void Break(Empilhadora Bobcat) {
		if(Bobcat.getHammer()!= null) {
			remove();
		}
	}



	@Override
	public boolean isObstaculoImo() {
		return true;
	}

}
