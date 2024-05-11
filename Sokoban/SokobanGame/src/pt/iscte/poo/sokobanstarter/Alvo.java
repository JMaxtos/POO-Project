package pt.iscte.poo.sokobanstarter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public class Alvo extends GameElement implements ImageTile{


	public Alvo(Point2D Point2D) {
		super(Point2D);
		
	}
	@Override
	public String getName() {
		return "Alvo";
	}
	
	@Override
	public int getLayer() {
		return 1;
	}

	public static List<GameElement> getListAlvos(Predicate<GameElement> alvos) {
		List<GameElement> alvo = new ArrayList<>();
		List<GameElement> newElements=GameEngine.getElementosCopy();
		for (GameElement g :  newElements) {

			if (alvos.test(g)) {
				alvo.add(g);

			}

		}
		return alvo;
	}
	
	


}
