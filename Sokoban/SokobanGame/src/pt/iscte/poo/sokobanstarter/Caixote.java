package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Caixote extends GameElement implements ImageTile, ObstaculoMo {

	public Caixote(Point2D Point2D) {
		super(Point2D);

	}

	@Override
	public String getName() {
		return "Caixote";
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
				Point2D boxPosition = newPosition.plus(keyDirection.asVector());
				if (!hasObstacle(boxPosition, p -> p instanceof ObstaculoImo || p instanceof ObstaculoMo)) {
					Teleporte teleport = (Teleporte) this.verifyNextElement(boxPosition,
							p -> p instanceof Teleporte);
					if (teleport != null) {
						Teleporte teleporte2 = GameEngine
								.getTeleport(p -> p instanceof Teleporte && !p.equals(teleport));
						teleport.teleport(teleporte2.getPosition(), this);
					} else {
						setPosition(boxPosition);
					}
				} else {
					Buraco hole = (Buraco) this.verifyNextElement(boxPosition, p -> p instanceof Buraco);
					if (hole != null) {
						remove();
						if (!(this.getNumBoxes(p -> p instanceof ObstaculoMo) < Alvo
								.getListAlvos(p -> p instanceof Alvo).size()))
							GameEngine.levelChange(GameEngine.getLevel());
					}
				}
			}
		}
	}
}