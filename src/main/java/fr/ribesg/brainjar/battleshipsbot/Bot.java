package fr.ribesg.brainjar.battleshipsbot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Random;

public class Bot {

	private static final String SHIP_2      = "2";
	private static final String SHIP_3      = "3";
	private static final String SHIP_4      = "4";
	private static final String SHIP_5      = "5";
	private static final String POINT       = "point";
	private static final String ORIENTATION = "orientation";
	private static final String VERTICAL    = "vertical";
	private static final String HORIZONTAL  = "horizontal";

	private static final String HIT    = "hit";
	private static final String MISSED = "missed";

	private static final Random RANDOM = new Random();

	public static void main(final String[] args) {
		if (args[0].equals("init")) {
			System.out.println(getConfig());
		} else {
			final JsonObject input = (JsonObject) new JsonParser().parse(merge(args));
			final JsonArray hitArray = input.getAsJsonArray(HIT);
			final JsonArray missedArray = input.getAsJsonArray(MISSED);

			boolean ok;
			String move;
			do {
				move = getRandomMove();
				ok = true;
				for (final JsonElement e : hitArray) {
					if (move.equals(e.getAsString())) {
						ok = false;
						break;
					}
				}
				if (ok) {
					for (final JsonElement e : missedArray) {
						if (move.equals(e.getAsString())) {
							ok = false;
							break;
						}
					}
				}
			} while (!ok);

			System.out.println("{move:'" + move + "'}");
		}
	}

	private static String getConfig() {
		final JsonObject result = new JsonObject();

		final JsonObject ship2 = new JsonObject();
		ship2.addProperty(POINT, "11");
		ship2.addProperty(ORIENTATION, VERTICAL);
		result.add(SHIP_2, ship2);

		final JsonObject ship3 = new JsonObject();
		ship3.addProperty(POINT, "31");
		ship3.addProperty(ORIENTATION, HORIZONTAL);
		result.add(SHIP_3, ship3);

		final JsonObject ship4 = new JsonObject();
		ship4.addProperty(POINT, "74");
		ship4.addProperty(ORIENTATION, VERTICAL);
		result.add(SHIP_4, ship4);

		final JsonObject ship5 = new JsonObject();
		ship5.addProperty(POINT, "07");
		ship5.addProperty(ORIENTATION, HORIZONTAL);
		result.add(SHIP_5, ship5);

		return result.toString();
	}

	private static String getRandomMove() {
		return String.format("%d%d", RANDOM.nextInt(8), RANDOM.nextInt(8));
	}

	private static String merge(final String[] strings) {
		if (strings == null || strings.length < 1) {
			throw new IllegalArgumentException("No.");
		}
		final StringBuilder builder = new StringBuilder(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			builder.append(' ').append(strings[i]);
		}
		return builder.toString();
	}
}
