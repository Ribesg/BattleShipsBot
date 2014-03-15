package fr.ribesg.brainjar.battleshipsbot.ship;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Set;

public class Ship {

	private static final String POINT       = "point";
	private static final String ORIENTATION = "orientation";
	private static final String VERTICAL    = "vertical";
	private static final String HORIZONTAL  = "horizontal";

	/* package */ final int     size;
	/* package */ final int     x;
	/* package */ final int     y;
	/* package */ final boolean horizontal;

	private final Set<String> points;

	public Ship(final int size, final String origin, final boolean horizontal) {
		this(size, Integer.parseInt(origin.substring(0, 1)), Integer.parseInt(origin.substring(1)), horizontal);
	}

	public Ship(final int size, final int x, final int y, final boolean horizontal) {
		this.size = size;
		this.x = x;
		this.y = y;
		this.horizontal = horizontal;

		this.points = new HashSet<>();
		if (horizontal) {
			final String yString = Integer.toString(this.y);
			for (int pointX = x; pointX < x + size; pointX++) {
				this.points.add(pointX + yString);
			}
		} else {
			final String xString = Integer.toString(this.x);
			for (int pointY = y; pointY < y + size; pointY++) {
				this.points.add(xString + pointY);
			}
		}
	}

	public boolean collides(final Ship o) {
		for (final String point : this.points) {
			if (o.points.contains(point)) {
				return true;
			}
		}
		return false;
	}

	public JsonObject toJsonObject() {
		final JsonObject res = new JsonObject();
		res.addProperty(POINT, this.x + "" + this.y);
		res.addProperty(ORIENTATION, this.horizontal ? HORIZONTAL : VERTICAL);
		return res;
	}
}
