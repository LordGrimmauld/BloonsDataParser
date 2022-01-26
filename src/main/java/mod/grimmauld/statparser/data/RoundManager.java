package mod.grimmauld.statparser.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

public class RoundManager {
	private static final Gson gson = new Gson();
	public final int startingCash;
	public final int totalCash;
	public final List<Round> rounds;

	public RoundManager() {
		JsonObject json = JsonParser.parseReader(new InputStreamReader(FileManager.getFileFromResourceAsStream("data/income-chimps.json"))).getAsJsonObject();
		startingCash = json.get("starting_cash").getAsInt();
		AtomicInteger accumulatedPrice = new AtomicInteger(650);
		rounds = StreamSupport.stream(json.get("rounds").getAsJsonArray().spliterator(), false)
			.map(jsonElement -> gson.fromJson(jsonElement, Round.class))
			.sorted(Comparator.comparing(Round::getRound))
			.peek(round -> round.setAccumulatedBefore(accumulatedPrice.getAndAdd(round.cashThisRound)))
			.toList();
		totalCash = accumulatedPrice.get();
	}
}
