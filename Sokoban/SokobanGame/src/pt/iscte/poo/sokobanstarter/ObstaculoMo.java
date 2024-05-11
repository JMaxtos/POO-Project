package pt.iscte.poo.sokobanstarter;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public interface ObstaculoMo {
	
	boolean isObstaculoMo();
	public void move(Direction keyDirection, Point2D point);
}
