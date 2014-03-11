package fr.ribesg.brainjar.battleshipsbot;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Bot {

	private static final String SHIP_2      = "2";
	private static final String SHIP_3      = "3";
	private static final String SHIP_4      = "4";
	private static final String SHIP_5      = "5";
	private static final String POINT       = "point";
	private static final String ORIENTATION = "orientation";
	private static final String VERTICAL    = "vertical";
	private static final String HORIZONTAL  = "horizontal";
	
	private static final Random RANDOM = new Random();

	private class State {
		public String cmd;
		public List<String>hit;
		public List<String>missed;
		public List<String>moves;
		public List<String>destroyed;
	}

	public static void main(final String[] args) {
		
		if (args.length > 0){
			Gson gson = new Gson();
			State state = gson.fromJson(args[0], State.class);
	
			if (state.cmd.equals("init")) {
				System.out.println(getConfig());
			} else {
				
				boolean ok;
				String move = "00";
				do {
				 	move = getRandomMove();
				 	ok = true;
				 	for (final String e : state.hit) {
				 		if (move.equals(e)) {
				 			ok = false;
				 			break;
				 		}
				 	}
				 	if (ok) {
				 		for (final String e : state.missed) {
				 			if (move.equals(e)) {
				 				ok = false;
				 				break;
				 			}
				 		}
				 	}
				} while (!ok);
	
				System.out.println("{\"move\":\"" + move + "\"}");
			}
		}else{
			System.out.println("{\"move\":\"WTF?\"}");
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
}
