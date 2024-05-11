package pt.iscte.poo.sokobanstarter;

import java.util.ArrayList;

import java.util.List;
import java.util.function.Predicate;

import pt.iscte.poo.gui.ImageTile;

import pt.iscte.poo.utils.Point2D;

public abstract class GameElement implements ImageTile {
	private Point2D position;

	// Construtor
	public GameElement(Point2D pos) {
		position = pos;
	}

	// GETTERS AND SETTERS
	public Point2D getPosition() {
		return position;
	}

	public abstract String getName();

	public abstract int getLayer();

	public void setPosition(Point2D p) {
		this.position = p;
	}

	protected void remove() {
		GameEngine.removeGameElement(this);
	}

	// UTILITY FUNCTIONS

	public boolean hasObstacle(Point2D position, Predicate<GameElement> filter) {
		for (GameElement element : GameEngine.getElementosCopy()) {
			if (element.getPosition().equals(position)) {
				if (filter.test(element)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasObstacle(Point2D point) {
		for (GameElement element : GameEngine.getObstacles()) {
			if (element.getPosition().equals(point)) {
				return true;
			}
		}
		return false;
	}

	public static boolean objectInHole(Point2D point, Predicate<GameElement> filter) {
		for (GameElement element : GameEngine.getElementosCopy()) {
			if (element.getPosition().equals(point)) {
				if (filter.test(element)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getNumBoxes(Predicate<GameElement> boxes) {
		int count = 0;
		List<GameElement> newElements = GameEngine.getElementosCopy();
		for (GameElement element : newElements) {
			if (boxes.test(element)) {
				count++;
			}
		}
		return count;
	}

	public GameElement verifyNextElement(Point2D point, Predicate<GameElement> filter) {
		for (GameElement element : GameEngine.getElementosCopy()) {
			if (element.getPosition().equals(point)) {
				if (filter.test(element)) {
					return element;
				}
			}
		}
		return null;
	}

	public boolean verifyWin(Predicate<GameElement> targets, Predicate<GameElement> boxes) {
		List<GameElement> list = Alvo.getListAlvos(targets);
		for (GameElement element : GameEngine.getElementosCopy()) {
			if (targets.test(element)) {
				for (GameElement gameElement : GameEngine.getElementosCopy()) {

					if (element.getPosition().equals(gameElement.getPosition()) && boxes.test(gameElement)) {
						list.remove(element);
					}
				}
			}
		}
		if (list.size() == 0) {
			return true;
		}

		return false;
	}

	public static void transformFloor(Point2D point, Predicate<GameElement> filtro1, Predicate<GameElement> filtro2) {
		List<GameElement> newelements = new ArrayList<>();
		List<GameElement> removables = new ArrayList<>();
		for (GameElement element : GameEngine.getGameElements()) {
			if (element.getPosition().equals(point)) {
				if (!filtro1.test(element) || !filtro2.test(element)) {
					removables.add(element);
					newelements.add(new Chao(point));
					GameEngine.getGui().addImage(new Chao(point));
				}
			} else {
				newelements.add(element);
			}
		}
		for (GameElement element : removables) {
			element.remove();
			GameEngine.getObstacles().remove(element);
		}
		GameEngine.setElements(newelements);
	}
}
