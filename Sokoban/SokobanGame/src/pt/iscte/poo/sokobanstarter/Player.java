package pt.iscte.poo.sokobanstarter;




	public class Player implements Comparable<Player> {

		private String name;
		private int level;
		private int moves;
		
		public Player(String name, int level, int moves) {

			this.name = name;
			this.level = level;
			this.moves = moves;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public int getMoves() {
			return moves;
		}

		public void setMoves(int moves) {
			this.moves = moves;
		}

		

		@Override
		public int compareTo(Player o) {
			return this.getMoves() - o.getMoves();

}

		public String toStringPlayer() {

			return this.level + "," + this.name + "," + this.moves;
		}


}
