package mod.grimmauld.statparser.data;


import mod.grimmauld.statparser.Main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileManager {

	// get a file from the resources folder
	// works everywhere, IDEA, unit test and JAR file.
	public static InputStream getFileFromResourceAsStream(String fileName) {
		InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}

	}

	public static Stream<String> getResourceListing(String path) {
		URL dirURL = Main.class.getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			String[] array;
			try {
				array = new File(dirURL.toURI()).list();
			} catch (URISyntaxException e) {
				throw new AssertionError("Main.class.getClassLoader().getResource(path) returned mal-formed URL");
			}
			return array != null ? Stream.of(array).map(filename -> new File(path, filename)).map(File::getPath) : Stream.empty();
		}

		if (dirURL == null) {
			String me = Main.class.getName().replace(".", "/") + ".class";
			dirURL = Main.class.getClassLoader().getResource(me);
		}

		if (dirURL != null && dirURL.getProtocol().equals("jar")) {
			String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
			try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
				return StreamSupport.stream(Spliterators.spliteratorUnknownSize(jar.entries().asIterator(), Spliterator.ORDERED), false)
					.filter(((Predicate<JarEntry>) JarEntry::isDirectory).negate())
					.map(JarEntry::getName)
					.filter(name -> name.startsWith(path))
					.collect(Collectors.toSet())
					.stream();
			} catch (IOException e) {
				Main.LOGGER.error("Can not open file stream", e);
				return Stream.empty();
			}
		}

		return Stream.empty();
	}

}
