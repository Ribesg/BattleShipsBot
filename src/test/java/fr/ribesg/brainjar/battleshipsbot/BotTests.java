package fr.ribesg.brainjar.battleshipsbot;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fr.ribesg.brainjar.battleshipsbot.ship.Ship;
import org.junit.Assert;
import org.junit.Test;

public class BotTests {

	@Test
	public void shipCollisionTest() {
		final Ship ship1 = new Ship(5, 1, 1, true);
		final Ship ship2 = new Ship(3, 3, 0, false);
		final Ship ship3 = new Ship(4, 3, 2, false);

		Assert.assertTrue(ship1.collides(ship2));
		Assert.assertFalse(ship1.collides(ship3));
	}

	@Test
	public void testGetConfig() {
		final Timer timer = new Timer();
		timer.start();
		Bot.getConfig();
		timer.stop();
		Assert.assertTrue(timer.nanoDiff() < 2_000_000_000);
	}

	@Test
	public void testBot() {
		final JsonObject input = new JsonObject();

		final JsonArray hitArray = new JsonArray();
		hitArray.add(new JsonPrimitive("12"));
		hitArray.add(new JsonPrimitive("13"));
		hitArray.add(new JsonPrimitive("14"));

		final JsonArray missedArray = new JsonArray();
		hitArray.add(new JsonPrimitive("10"));
		hitArray.add(new JsonPrimitive("32"));
		hitArray.add(new JsonPrimitive("74"));

		input.add("hit", hitArray);
		input.add("missed", missedArray);

		final JsonElement move = new JsonPrimitive("move");
		input.add("cmd", move);

		final String[] gameInput = input.toString().split("\\s");

		final String init[] = {"{\"cmd\":\"init\"}"};
		Bot.main(init);
		Bot.main(gameInput);
	}
}
