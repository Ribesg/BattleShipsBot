package fr.ribesg.brainjar.battleshipsbot;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Timer {

	private static DecimalFormat format;

	public static DecimalFormat getFormatter() {
		if (format == null) {
			format = new DecimalFormat("#0.00");
			format.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
		}
		return format;
	}

	private long startTime, endTime;

	public Timer start() {
		startTime = System.nanoTime();
		return this;
	}

	public Timer stop() {
		endTime = System.nanoTime();
		return this;
	}

	public long nanoDiff() {
		if (startTime != -1 && endTime != -1) {
			return endTime - startTime;
		} else {
			return -1;
		}
	}

	public String diffString() {
		return parseDiff(nanoDiff());
	}

	public long hotNanoDiff() {
		return System.nanoTime() - startTime;
	}

	public String hotDiffString() {
		return parseDiff(hotNanoDiff());
	}

	public static String parseDiff(final long nano) {
		if (nano < 1_000L) {
			return nano + "ns";
		} else if (nano < 1_000_000L) {
			return getFormatter().format(nano / 1_000D) + "µs";
		} else if (nano < 1_000_000_000L) {
			return getFormatter().format(nano / 1_000_000D) + "ms";
		} else {
			return getFormatter().format(nano / 1_000_000_000D) + "s";
		}
	}
}