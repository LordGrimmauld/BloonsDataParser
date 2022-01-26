package mod.grimmauld.statparser.data;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Tower {
	private static final Gson gson = new Gson();
	public final Set<TowerInstance> instances;
	private final String path;

	public Tower(String path) {
		this.path = Path.of(path).getFileName().toString();
		instances = FileManager.getResourceListing(path)
			.map(FileManager::getFileFromResourceAsStream)
			.map(InputStreamReader::new)
			.map(JsonParser::parseReader)
			.map(je -> gson.fromJson(je, TowerInstance.class))
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
	}

	public Set<TowerInstance> getInstances() {
		return instances;
	}
}
