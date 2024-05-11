package pt.iscte.poo.sokobanstarter;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

import pt.iscte.poo.gui.ImageMatrixGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine implements Observer {

	// Dimensoes da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private static GameEngine INSTANCE; // Referencia para o unico objeto GameEngine (singleton)
	private static ImageMatrixGUI gui; // Referencia para ImageMatrixGUI (janela de interface com o utilizador)
	private static Empilhadora bobcat; // Referencia para a empilhadora
	private static List<GameElement> gameElements;
	private static List<GameElement> obstacles;
	private static int level;
	private static String playerName;

	// Construtor
	private GameEngine() {
		gameElements = new ArrayList<>();
		obstacles = new ArrayList<>();
		level = 0;
	}

	// Implementacao do singleton para o GameEngine
	public static GameEngine getInstance() {
		if (INSTANCE == null)
			return INSTANCE = new GameEngine();
		return INSTANCE;
	}

	// GETTERS AND SETTERS
	protected static List<GameElement> getObstacles() {
		return obstacles;
	}

	protected static void setElements(List<GameElement> newelements) {
		gameElements = newelements;

	}

	protected static int getLevel() {
		return level;

	}

	protected static List<GameElement> getElementosCopy() {
		List<GameElement> copy = new ArrayList<>();
		for (GameElement i : gameElements) {
			copy.add(i);
		}
		return copy;
	}

	protected static List<GameElement> getGameElements() {
		return gameElements;
	}

	protected static ImageMatrixGUI getGui() {
		return gui;
	}

	protected static Point2D getNextPoint(int key) {
		Point2D nextpoint = null;
		if (Direction.isDirection(key)) {
			Direction direction = Direction.directionFor(key);
			Vector2D movement = direction.asVector();
			nextpoint = bobcat.getPosition().plus(movement);
		}
		return nextpoint;
	}

	protected static Teleporte getTeleport(Predicate<GameElement> filter) {
		for (GameElement element : gameElements) {
			if (filter.test(element) && element instanceof Teleporte) {
				return (Teleporte) element;
			}

		}
		return null;
	}

	// Inicio
	public void start() {

		// Setup inicial da janela que faz a interface com o utilizador

		gui = ImageMatrixGUI.getInstance();
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);
		gui.registerObserver(this);

		// Pedido do username
		Scanner nameScanner = new Scanner(System.in);
		System.out.print("User: ");
		playerName = nameScanner.next();
		nameScanner.close();

		// lançamento da GUI para o Jogador
		gui.go();

		// Criacao do cenario de jogo com todos os elementos
		createWarehouse("levels/level" + level + ".txt");

		// Escrever uma mensagem na StatusBar
		gui.setStatusMessage("Level: " + level + " " + playerName + " Moves: " + bobcat.getMoves() + " Energia: "
				+ bobcat.getEnergy());
		gui.update();

	}
	// GUI FUNCTIONS

	// Da update a GUI de jogo sempre que o jogador clica numa tecla
	@Override
	public void update(Observed source) {

		int key = gui.keyPressed();

		// Se o Jogador pressionar 'R' o nivel leva reset
		if (key == KeyEvent.VK_R) {
			levelChange(level);
			gui.update();
		} else {
			if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_UP || key == KeyEvent.VK_RIGHT
					|| key == KeyEvent.VK_DOWN) { // se
				Direction keyDirection = Direction.directionFor(key);
				Point2D newPosition = bobcat.getPosition().plus(keyDirection.asVector());
				if (bobcat.getEnergy() == 0) {
					levelChange(level);
				}
				Usable usable = (Usable) bobcat.verifyNextElement(getNextPoint(key), p -> p instanceof Usable);
				if (usable != null)
					usable.use(bobcat);

				Buraco hole = (Buraco) bobcat.verifyNextElement(getNextPoint(key), p -> p instanceof Buraco);
				if (hole != null) {
					hole.remove();
					levelChange(level);
				} else {
					ParedeRachada pr = (ParedeRachada) bobcat.verifyNextElement(getNextPoint(key),
							p -> p instanceof ParedeRachada);
					if (pr != null) {
						pr.Break(bobcat);
					}
					ObstaculoMo movable = (ObstaculoMo) bobcat.verifyNextElement(getNextPoint(key),
							p -> p instanceof ObstaculoMo);
					if (movable != null) {
						movable.move(keyDirection, newPosition);
						bobcat.setEnergy(bobcat.getEnergy() - 1);
					}

					GameElement obstacle = bobcat.verifyNextElement(getNextPoint(key), p -> p instanceof ObstaculoImo);
					if (obstacle == null)
						bobcat.move(keyDirection);

					if (bobcat.verifyWin(p -> p instanceof Alvo, p -> p instanceof Caixote)) {
						StatsExport.exportPlayers(new Player(playerName, level, bobcat.getMoves()));
						StatsExport.selectTop3("Nível " + level);
						StatsExport.showTop3(level);
						levelChange(++level);
					}
				}
			}
		}
		gui.setStatusMessage("Level: " + level + " " + playerName + " Moves: " + bobcat.getMoves() + " Energia: "
				+ bobcat.getEnergy());

		// redesenha a lista de ImageTiles na GUI,tendo em conta as novas posicoes dos
		// objetos
		gui.update();
	}

	// Criacao da planta do armazem 
	private static void createWarehouse(String fileName) {
		List<ImageTile> tileList = new ArrayList<>();
		for (int y = 0; y < GRID_HEIGHT; y++)
			for (int x = 0; x < GRID_HEIGHT; x++)
				tileList.add(new Chao(new Point2D(x, y)));
		int y = 0;
		try {
			Scanner filescanner = new Scanner(new File(fileName));
			while (filescanner.hasNextLine()) {
				String line = filescanner.nextLine();
				if (line.isEmpty()) {
					continue;
				}

				char[] elements = line.toCharArray();
				for (int x = 0; x < GRID_HEIGHT; x++) {

					if (elements[x] == ' ' || elements[x] == 'E') {
						tileList.add(new Chao(new Point2D(x, y)));
						gameElements.add(new Chao(new Point2D(x, y)));
					}
					if (elements[x] == '#') {
						GameElement c = new Parede(new Point2D(x, y));
						tileList.add(c);
						gameElements.add(c);
						obstacles.add(c);
					}
					if (elements[x] == '=') {
						tileList.add(new Vazio(new Point2D(x, y)));
						gameElements.add(new Vazio(new Point2D(x, y)));
					}
					if (elements[x] == 'E') {
						bobcat = new Empilhadora(new Point2D(x, y));
						tileList.add(bobcat);
						gameElements.add(bobcat);

					}
					if (elements[x] == 'C') {
						Caixote caixote = new Caixote(new Point2D(x, y));
						tileList.add((GameElement) caixote);
						gameElements.add((GameElement) caixote);
						obstacles.add((GameElement) caixote);
					}
					if (elements[x] == 'X') {
						tileList.add(new Alvo(new Point2D(x, y)));
						gameElements.add(new Alvo(new Point2D(x, y)));
					}
					if (elements[x] == 'B') {
						Bateria pilha = new Bateria(new Point2D(x, y));
						tileList.add(pilha);
						gameElements.add(pilha);
					}
					if (elements[x] == 'O') {
						GameElement buraco = new Buraco(new Point2D(x, y));
						tileList.add(buraco);
						gameElements.add(buraco);
						obstacles.add(buraco);
					}
					if (elements[x] == 'P') {
						Palete palete = new Palete(new Point2D(x, y));
						tileList.add((GameElement) palete);
						gameElements.add((GameElement) palete);
						obstacles.add((GameElement) palete);
					}
					if (elements[x] == 'M') {
						Martelo martelo = new Martelo(new Point2D(x, y));
						tileList.add(martelo);
						gameElements.add(martelo);
					}
					if (elements[x] == '%') {
						ParedeRachada pr = new ParedeRachada(new Point2D(x, y));
						tileList.add(pr);
						gameElements.add(pr);
						obstacles.add(pr);
					}
					if (elements[x] == 'T') {
						GameElement teleporte = new Teleporte(new Point2D(x, y));
						tileList.add(teleporte);
						gameElements.add(teleporte);
					}
				}
				y++;
			}
			filescanner.close();
			sendImagesToGUI(tileList);
		} catch (FileNotFoundException e) {
			System.err.println("Ficheiro " + fileName + " nao encontrado");
		}
	}

	// Envio das mensagens para a GUI
	private static void sendImagesToGUI(List<ImageTile> tileList) {
		gui.addImages(tileList);
	}

	// OTHER FUNCTIONS

	// Mudança ou Reset do Nível
	public static void levelChange(int level) {
		if (level < 7) {
			gameElements.clear();
			obstacles.clear();
			gui.clearImages();
			createWarehouse("levels/level" + level + ".txt");
		} else {
			System.out.println("Parabens concluiste todos os niveis do Sokoban!");
			System.exit(0);
		}

	}

	protected static void removeGameElement(GameElement gameElement) {
		Iterator<GameElement> iterator = gameElements.iterator();
		while (iterator.hasNext()) {
			GameElement element = iterator.next();
			if (element.equals(gameElement)) {
				iterator.remove();
				gui.removeImage(gameElement);
				if (gameElement instanceof ParedeRachada) {
					obstacles.remove(gameElement);
				}
			}
		}
	}
}
