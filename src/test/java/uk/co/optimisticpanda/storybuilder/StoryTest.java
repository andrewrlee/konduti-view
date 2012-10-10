package uk.co.optimisticpanda.storybuilder;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jbehave.core.model.Meta;
import org.junit.Test;

public class StoryTest {

	@Test
	public void getSections() {
		List<String> result = parseLines("a > b > c\na > b");
		assertThat(result.size(), is(2));
		assertThat(result, hasItems("a > b > c", "a > b"));
		
		result = parseLines("a > b > c\ra > b");
		assertThat(result.size(), is(2));
		assertThat(result, hasItems("a > b > c", "a > b"));
		
		result = parseLines("a > b > c\r\na > b");
		assertThat(result.size(), is(2));
		assertThat(result, hasItems("a > b > c", "a > b"));
		
		result = parseLines("a > b > c    ");
		assertThat(result.size(), is(1));
		assertThat(result, hasItems("a > b > c"));
		
		result = parseLines("a > b > c\n\n");
		assertThat(result.size(), is(1));
		assertThat(result, hasItems("a > b > c"));
		
		result = parseLines("\na > b > c\n\n");
		assertThat(result.size(), is(1));
		assertThat(result, hasItems("a > b > c"));
		
		result = parseLines("\n\na > b > c\n\n");
		assertThat(result.size(), is(1));
		assertThat(result, hasItems("a > b > c"));
	}

	@Test
	public void getLinks() {
		Map<String, String> result = parseLinks("a [c]");
		assertThat(result.size(), is(1));
		assertThat(result, hasEntry("a", "c"));
		
		result = parseLinks("a[c]\nb  [b   ]");
		assertThat(result.size(), is(2));
		assertThat(result, hasEntry(is("a"), is("c")));
		assertThat(result, hasEntry(is("b"), is("b")));
		
		result = parseLinks("");
		assertTrue(result.isEmpty());

		result = parseLinks("\n");
		assertTrue(result.isEmpty());
		
		result = parseLinks("\n\n");
		assertTrue(result.isEmpty());

		result = parseLinks("a");
		assertThat(result.size(), is(1));
		assertTrue(result.containsKey("a"));
		assertNull(result.get("a"));
		
		result = parseLinks("a[a][b]");
		assertThat(result.size(), is(1));
		assertThat(result, hasEntry(is("a"), is("a")));
		
		result = parseLinks("LP - Determine Visitor Type [SOIIC-15]");
		assertThat(result.size(), is(1));
		assertThat(result, hasEntry(is("LP - Determine Visitor Type"), is("SOIIC-15")));
	}
	
	private Map<String, String> parseLinks(String string) {
		Properties properties = new Properties();
		properties.put("Link", string);
		Meta meta = new Meta(properties);
		return new Story("", "", meta).getLinks();
	}

	private List<String> parseLines(String list){
		Properties properties = new Properties();
		properties.put("a", list);
		Meta meta = new Meta(properties);
		return Arrays.asList(new Story("", "", meta).gatherMetaListValues(meta, "a"));
	}
	
}
