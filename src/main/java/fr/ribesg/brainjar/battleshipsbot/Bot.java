package fr.ribesg.brainjar.battleshipsbot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.ribesg.brainjar.battleshipsbot.ship.Ship;

import java.util.Random;
import java.util.Set;

public class Bot {

	private static final Random RANDOM = new Random();

	/**
	 * Represents a Battleships game state
	 */
	private class State {

		public String      cmd;
		public Set<String> hit;
		public Set<String> missed;
		public Set<String> moves;
		public Set<String> destroyed;
	}

	public static void main(final String[] args) {
		final State state = parseState(args);

		if (state != null) {
			if (state.cmd.equals("init")) {
				System.out.println(getConfig());
			} else {
				final String move = getPossibleMove(state);
				System.out.println("{\"move\":\"" + move + "\"}");
			}
		} else {
			System.out.println("{\"move\":\"WTF?\"}");
		}
	}

	static State parseState(final String[] args) {
		if (args.length == 0) {
			return null;
		}
		return new Gson().fromJson(args[0], State.class);
	}

	static String getConfig() {
		final JsonObject result = new JsonObject();

		final Ship ship5;
		if (RANDOM.nextBoolean()) {
			ship5 = new Ship(5, getRandomMove(4, 8), true);
		} else {
			ship5 = new Ship(5, getRandomMove(8, 4), false);
		}
		result.add("5", ship5.toJsonObject());

		Ship ship4;
		do {
			if (RANDOM.nextBoolean()) {
				ship4 = new Ship(4, getRandomMove(5, 8), true);
			} else {
				ship4 = new Ship(4, getRandomMove(8, 5), false);
			}
		} while (ship4.collides(ship5));
		result.add("4", ship4.toJsonObject());

		Ship ship3;
		do {
			if (RANDOM.nextBoolean()) {
				ship3 = new Ship(3, getRandomMove(6, 8), true);
			} else {
				ship3 = new Ship(3, getRandomMove(8, 6), false);
			}
		} while (ship3.collides(ship5) || ship3.collides(ship4));
		result.add("3", ship3.toJsonObject());

		Ship ship2;
		do {
			if (RANDOM.nextBoolean()) {
				ship2 = new Ship(2, getRandomMove(7, 8), true);
			} else {
				ship2 = new Ship(2, getRandomMove(8, 7), false);
			}
		} while (ship2.collides(ship5) || ship2.collides(ship4) || ship2.collides(ship3));
		result.add("2", ship2.toJsonObject());

		return result.toString();
	}

	static String getPossibleMove(final State state) {
		String move;
		do {
			move = getRandomMove(8, 8);
		} while (!state.missed.contains(move) && !state.hit.contains(move));
		return move;
	}

	static String getRandomMove(final int xBound, final int yBound) {
		return String.format("%d%d", RANDOM.nextInt(xBound), RANDOM.nextInt(yBound));
	}
}
