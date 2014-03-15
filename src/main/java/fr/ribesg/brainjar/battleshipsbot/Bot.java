package fr.ribesg.brainjar.battleshipsbot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.ribesg.brainjar.battleshipsbot.ship.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Bot {

	public static final Random RANDOM = new Random();

	/**
	 * Represents a Battleships game state
	 */
	private class State {

		public String       cmd;
		public Set<String>  hit;
		public Set<String>  missed;
		public List<String> moves;
		public List<String> destroyed;
	}

	public static void main(final String[] args) {
		final State state = parseState(args);

		if (state != null) {
			if (state.cmd.equals("init")) {
				System.out.println(getConfig());
			} else {
				final String move = getNextMove(state);
				System.out.println("{\"move\":\"" + move + "\"}");
			}
		} else {
			System.out.println("{\"move\":\"WTF?\"}");
		}
	}

	public static State parseState(final String[] args) {
		if (args.length == 0) {
			return null;
		}
		return new Gson().fromJson(args[0], State.class);
	}

	public static String getConfig() {

		Ship ship2, ship3, ship4, ship5;
		boolean isNotOptimal;
		int i = 15;
		do {
			if (RANDOM.nextBoolean()) {
				ship5 = new Ship(5, getRandomMove(4, 8), true);
			} else {
				ship5 = new Ship(5, getRandomMove(8, 4), false);
			}

			do {
				if (RANDOM.nextBoolean()) {
					ship4 = new Ship(4, getRandomMove(5, 8), true);
				} else {
					ship4 = new Ship(4, getRandomMove(8, 5), false);
				}
			} while (ship4.collides(ship5));

			do {
				if (RANDOM.nextBoolean()) {
					ship3 = new Ship(3, getRandomMove(6, 8), true);
				} else {
					ship3 = new Ship(3, getRandomMove(8, 6), false);
				}
			} while (ship3.collides(ship5) || ship3.collides(ship4));

			do {
				if (RANDOM.nextBoolean()) {
					ship2 = new Ship(2, getRandomMove(7, 8), true);
				} else {
					ship2 = new Ship(2, getRandomMove(8, 7), false);
				}
			} while (ship2.collides(ship5) || ship2.collides(ship4) || ship2.collides(ship3));

			isNotOptimal = ship2.touches(ship3) || ship2.touches(ship4) || ship2.touches(ship5) ||
			               ship3.touches(ship4) || ship3.touches(ship5) || ship4.touches(ship5);
		} while (isNotOptimal && i-- > 0);

		final JsonObject result = new JsonObject();
		result.add("2", ship2.toJsonObject());
		result.add("3", ship3.toJsonObject());
		result.add("4", ship4.toJsonObject());
		result.add("5", ship5.toJsonObject());
		return result.toString();
	}

	public static String getNextMove(final State state) {
		for (int i = state.moves.size() - 1; i >= 0; i--) {
			final String move = state.moves.get(i);
			if (move.charAt(0) == '0' && move.charAt(3) == '3') {
				final int x = Integer.parseInt(move.substring(1, 2));
				final int y = Integer.parseInt(move.substring(2, 3));
				final List<String> moves = new ArrayList<>(4);
				if (x > 0) { moves.add((x - 1) + "" + y); }
				if (x < 7) { moves.add((x + 1) + "" + y); }
				if (y > 0) { moves.add(x + "" + (y - 1)); }
				if (y < 7) { moves.add(x + "" + (y + 1)); }
				for (final String nextMove : moves) {
					if (isMovePossible(state, nextMove)) {
						return nextMove;
					}
				}
			}
		}
		final String move = getUsualMove(state);
		return move == null ? getAnyInterestingPossibleMove(state) : move;
	}

	public final static String[] usualMoves = new String[] {
			// Diagonal moves
			"00",
			"11",
			"22",
			"33",
			"44",
			"55",
			"66",
			"77",
			"70",
			"61",
			"52",
			"43",
			"34",
			"25",
			"16",
			"07",

			// Others
			"31",
			"41",
			"13",
			"14",
			"36",
			"46",
			"63",
			"64"
	};

	public static String getUsualMove(final State state) {
		// Try to take a random one
		for (int i = 0; i < 15; i++) {
			final String move = usualMoves[RANDOM.nextInt(usualMoves.length)];
			if (isMovePossible(state, move)) {
				return move;
			}
		}

		// If it fails to much, try to take the first one available
		for (final String diagonalMove : usualMoves) {
			if (isMovePossible(state, diagonalMove)) {
				return diagonalMove;
			}
		}

		// All of them have been played
		return null;
	}

	public static String getAnyInterestingPossibleMove(final State state) {
		int triesCount = 0;
		String move = getRandomPossibleMove(state);

		String bestMove = move;
		int bestMovePossibleNeighbours = -1;

		while (triesCount++ < 50 && bestMovePossibleNeighbours < 4) {
			final int x = Integer.parseInt(move.substring(0, 1));
			final int y = Integer.parseInt(move.substring(1, 2));
			int i = 0;
			final List<String> neighborMoves = new ArrayList<>(4);
			if (x > 0) { neighborMoves.add((x - 1) + "" + y); } else { i++; }
			if (x < 7) { neighborMoves.add((x + 1) + "" + y); } else { i++; }
			if (y > 0) { neighborMoves.add(x + "" + (y - 1)); } else { i++; }
			if (y < 7) { neighborMoves.add(x + "" + (y + 1)); } else { i++; }
			for (final String neighborMove : neighborMoves) {
				if (isMovePossible(state, neighborMove)) {
					i++;
				}
			}
			if (i > bestMovePossibleNeighbours) {
				bestMove = move;
				bestMovePossibleNeighbours = i;
			}
			move = getRandomPossibleMove(state);
		}
		return bestMove;
	}

	public static String getRandomPossibleMove(final State state) {
		String move;
		do {
			move = getRandomMove(8, 8);
		} while (!isMovePossible(state, move));
		return move;
	}

	public static boolean isMovePossible(final State state, final String move) {
		return !state.missed.contains(move) && !state.hit.contains(move);
	}

	public static String getRandomMove(final int xBound, final int yBound) {
		return String.format("%d%d", RANDOM.nextInt(xBound), RANDOM.nextInt(yBound));
	}
}
