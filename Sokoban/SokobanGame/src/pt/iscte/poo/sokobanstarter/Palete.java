package pt.iscte.poo.sokobanstarter;


import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Palete extends GameElement implements ImageTile, ObstaculoMo {

	public Palete(Point2D Point2D) {
		super(Point2D);

	}

	@Override
	public String getName() {
		return "Palete";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	@Override
	public boolean isObstaculoMo() {
		return true;
	}

	public void move(Direction keyDirection, Point2D newPosition) {
		if (newPosition.getX() >= 1 && newPosition.getX() <= 9 && newPosition.getY() >= 1 && newPosition.getY() <= 9) {
			if (hasObstacle(newPosition, p -> p.equals(this))) {
				Point2D palletPosition = newPosition.plus(keyDirection.asVector());
				if (!hasObstacle(palletPosition, p -> p instanceof ObstaculoImo || p instanceof ObstaculoMo)) {
					Teleporte teleport = (Teleporte) this.verifyNextElement(palletPosition, p -> p instanceof Teleporte);
					if (teleport != null) {
						Teleporte teleport2 = GameEngine
								.getTeleport(p -> p instanceof Teleporte && !p.equals(teleport));
						teleport.teleport(teleport2.getPosition(), this);
					} else {
						setPosition(palletPosition);
					}
				} else if (objectInHole(palletPosition, p -> p instanceof Buraco)) {
					setPosition(palletPosition);
					transformFloor(getPosition(), l -> l instanceof Buraco, g -> g instanceof Palete);
				}

			}
		}

	}
}