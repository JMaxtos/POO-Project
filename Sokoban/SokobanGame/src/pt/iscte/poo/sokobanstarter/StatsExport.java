package pt.iscte.poo.sokobanstarter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StatsExport<T> implements statsToLine<T> {

	public void export(List<T> list, statsToLine<T> ol, int level) {
		String fileName = "Nível " + level;
		List<String> oldLevels = new ArrayList<>();
		File f = new File(fileName);

		if (f.exists()) {
			try {
				Scanner filescanner = new Scanner(new File(fileName));
				while (filescanner.hasNextLine()) {
					String line = filescanner.nextLine();
					oldLevels.add(line);
				}
				filescanner.close();
				PrintWriter fileWriter = new PrintWriter(fileName);
				oldLevels.forEach(t -> fileWriter.println(t));
				list.forEach(t -> fileWriter.println(ol.toLine(t)));
				fileWriter.close();

			} catch (FileNotFoundException e) {
				System.err.println("Erro ao exportar ");
			}

		} else {
			try {
				PrintWriter fileWriter = new PrintWriter(fileName);
				list.forEach(t -> fileWriter.println(ol.toLine(t)));
				fileWriter.close();
			} catch (FileNotFoundException e) {
				System.err.println(" Erro ao escrever");
			}
		}
	}

	@Override
	public String toLine(Object t) {
		return null;
	}

	public static void exportPlayers(Player player) {
		StatsExport<Player> oe = new StatsExport<>();
		List<Player> players = getStatsList(player);
		oe.export(players, p -> p.getLevel() + "," + p.getName() + "," + p.getMoves(), player.getLevel());
	}

	public static List<Player> getStatsList(Player p) {
		List<Player> stats = new ArrayList<>();
		stats.add(p);

		return stats;
	}

	public static void selectTop3(String string) {

		List<Player> players = new ArrayList<>();
		try {
			Scanner filescanner = new Scanner(new File(string));
			while (filescanner.hasNextLine()) {
				String[] line = filescanner.nextLine().split(",");
				players.add(new Player(line[1], Integer.parseInt(line[0]), Integer.parseInt(line[2])));
			}
			Collections.sort(players);
			filescanner.close();
			PrintWriter fileWriter = new PrintWriter(string);
			players.forEach(t -> fileWriter.println(t.toStringPlayer()));

			fileWriter.close();
			writeTop3(string);
		} catch (FileNotFoundException e) {
			System.err.println("Erro ao selecionar Top3");
		}

	}

	private static void writeTop3(String string) {
		List<String> top = new ArrayList<>();
		try {
			Scanner filescanner = new Scanner(new File(string));
			int i = 0;
			while (filescanner.hasNextLine()) {
				if (i == 3) {
					break;
				}
				String line = filescanner.nextLine();
				top.add(line);
				i++;
			}
			filescanner.close();
			PrintWriter fileWriter = new PrintWriter(string);
			top.forEach(t -> fileWriter.println(t));
			fileWriter.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro ao escrever Top3");
		}
	}

	public static void showTop3(int level) {

		try {
			Scanner scanner = new Scanner(new File("Nível " + level));
			System.out.println("Nivel " + level + " -> Top 3");
			int i = 0;
			while (scanner.hasNextLine()) {

				if (i == 3) {
					break;
				}
				String line = scanner.nextLine();
				System.out.println((i + 1) + ": " + line);
				i++;
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			System.out.println("Erro ao mostrar Top 3");
		}
	}

}