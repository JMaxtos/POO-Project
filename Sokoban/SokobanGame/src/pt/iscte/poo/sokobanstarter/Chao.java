package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Point2D;

public class Chao extends GameElement{

	private Point2D position;
	
	public Chao(Point2D Point2D){
		super(Point2D);
		position=super.getPosition();
	}
	
	@Override
	public String getName() {
		return "Chao";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public int getLayer() {
		return 0;
	}



	

	

	

}
