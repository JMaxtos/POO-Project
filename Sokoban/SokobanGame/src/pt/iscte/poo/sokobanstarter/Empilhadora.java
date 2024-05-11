package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Empilhadora extends GameElement implements ImageTile {

	private String imageName;
	private int energy;
	private int moves;
	private Martelo hammer;

	public Empilhadora(Point2D initialPosition) {
		super(initialPosition);

		imageName = "Empilhadora_U";
		energy = 100;
		moves = 0;
	}

	@Override
	public String getName() {
		return imageName;
	}

	@Override
	public int getLayer() {
		return 5;
	}

	public int getEnergy() {
		return energy;
	}

	public Martelo getHammer() {
		return hammer;

	}

	public void setHammer(Martelo m) {
		hammer = m;
	}

	public void move(Direction keyDirection) {
		if (this.energy > 0) {
			Point2D newPosition = super.getPosition().plus(keyDirection.asVector());
			switch (keyDirection) {
			case DOWN:
				imageName = "Empilhadora_D";
				break;
			case LEFT:
				imageName = "Empilhadora_L";
				break;
			case RIGHT:
				imageName = "Empilhadora_R";
				break;
			case UP:
				imageName = "Empilhadora_U";
				break;
			}

			// Move segundo a direcao gerada, mas so' se estiver dentro dos limites

			if (newPosition.getX() >= 0 && newPosition.getX() < 10 && newPosition.getY() >= 0
					&& newPosition.getY() < 10) {
				if (!hasObstacle(newPosition, p -> p instanceof ObstaculoMo)) {
					Teleporte teleport = (Teleporte) verifyNextElement(newPosition, p -> p instanceof Teleporte);
					if (teleport != null) {
						Teleporte teleporte2 = GameEngine
								.getTeleport(p -> p instanceof Teleporte && !p.equals(teleport));
						teleport.teleport(teleporte2.getPosition(), this);
					} else {
						setPosition(newPosition);
					}
					energy--;
					moves++;
				}

			}

		}
	}

	public void setEnergy(int newEnergy) {
		energy = newEnergy;
	}

	public int getMoves() {
		return moves;
	}

}