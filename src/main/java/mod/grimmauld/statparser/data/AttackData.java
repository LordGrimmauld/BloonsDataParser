package mod.grimmauld.statparser.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.stream.StreamSupport;

public class AttackData {
	private final boolean canHitCamo;
	private final int immunities;

	public AttackData(JsonObject json) {
		// Camo
		boolean camo = true;
		if (json.has("filters")) {
			JsonElement filters = json.get("filters");
			if (filters instanceof JsonArray filterArray) {
				camo = StreamSupport.stream(filterArray.spliterator(), false)
					.filter(JsonElement::isJsonObject)
					.map(JsonElement::getAsJsonObject)
					.filter(jo -> jo.has("name") && jo.get("name").getAsString().equals("FilterInvisibleModel_"))
					.noneMatch(jo -> jo.has("isActive") && jo.get("isActive").getAsBoolean());
			} else {
				System.out.println("filter isn't array");
			}
		}else {
			System.out.println("no filters");
		}
		canHitCamo = camo;

		JsonArray behaviours = json.getAsJsonArray("behaviors");
		immunities = StreamSupport.stream(behaviours.spliterator(), false)
			.filter(JsonElement::isJsonObject)
			.map(JsonElement::getAsJsonObject)
			.filter(jo -> jo.has("immuneBloonProperties"))
			.map(jo -> jo.get("immuneBloonProperties"))
			.filter(JsonElement::isJsonPrimitive)
			.map(JsonElement::getAsJsonPrimitive)
			.filter(JsonPrimitive::isNumber)
			.map(JsonPrimitive::getAsInt)
			.findFirst()
			.orElse(0);
	}

	public boolean canAttack(int bloonType) {
		return (immunities & bloonType) == 0;
	}

	public boolean canHitCamo() {
		return canHitCamo;
	}
}
