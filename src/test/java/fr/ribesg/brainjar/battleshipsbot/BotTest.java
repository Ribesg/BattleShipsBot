package fr.ribesg.brainjar.battleshipsbot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BotTest {

	private String[] input;

	@Before
	public void setup() {
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

		this.input = input.toString().split("\\s");
	}

	@Test
	public void testMain() {
		Bot.main(new String[] {"init"});
		Bot.main(input);
	}
}
