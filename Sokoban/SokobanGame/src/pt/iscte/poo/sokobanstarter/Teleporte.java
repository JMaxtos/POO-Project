package pt.iscte.poo.sokobanstarter;



import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Teleporte extends GameElement implements ImageTile {

	public Teleporte(Point2D Point2D) {
		super(Point2D);
		
	}

	@Override
	public String getName() {
		return "Teleporte";
	}
	@Override
	public int getLayer() {
		return 1;
	}

	
	public void teleport(Point2D point, GameElement element) {
		if (!hasObstacle(point)) {
			element.setPosition(point);
		} else {
			element.setPosition(this.getPosition());
		}
	}

}
