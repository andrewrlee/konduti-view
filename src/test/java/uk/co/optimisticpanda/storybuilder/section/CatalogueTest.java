package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.contains;
import static com.google.common.collect.Iterables.elementsEqual;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;

import com.google.common.collect.Lists;
import static uk.co.optimisticpanda.storybuilder.section.Section.*;
import static com.google.common.collect.Lists.*;
public class CatalogueTest {

	@Test
	public void createCatalogue() {
		TestContent test1 = new TestContent("test1", "a > b");
		TestContent test2 = new TestContent("test2", "a > b > c");

		Map<Section, Collection<TestContent>> result = Catalogue.create(test1, test2).getContents();

		Iterable<Section> expectedKeys = newArrayList(newSection("a > b"), newSection("a > b > c"));
		assertTrue("Not ordered as expected:" + expectedKeys + ", result" + result.keySet(),//
				elementsEqual(result.keySet(), expectedKeys));

		assertTrue(contains(result.get(newSection("a > b")), test1));
		assertTrue(contains(result.get(newSection("a > b > c")), test2));
	}

	@Test
	public void createCatalogue_Complex() {
		TestContent test1 = new TestContent("test1", "a > b");
		TestContent test2 = new TestContent("test2", "a > b > c");
		TestContent test3 = new TestContent("test3", "a > b > c", "a > b");
		TestContent test4 = new TestContent("test4", "a > b > c", "a");
		TestContent test5 = new TestContent("test5", "a > b > c", "a > b", "a");
		TestContent test6 = new TestContent("test6", "c");

		Catalogue<TestContent> view = Catalogue.create(test1, test2, test3, test4, test5, test6);
		Map<Section, Collection<TestContent>> result = view.getContents();

		Iterable<Section> expectedKeys = Lists.newArrayList( //
				newSection("a"), //
				newSection("a > b"), //
				newSection("a > b > c"), //
				newSection("c") //
				);

		// Should order by alphabetic order of all elements and then by size of
		// path
		assertTrue("Not ordered as expected:" + expectedKeys + ", result" + result.keySet(),//
				elementsEqual(result.keySet(), expectedKeys));

		assertThat(result.get(newSection("a")).size(), is(2));
		assertThat(result.get(newSection("a")), hasItems(test4, test5));

		assertThat(result.get(newSection("a > b")).size(), is(3));
		assertThat(result.get(newSection("a > b")), hasItems(test1, test3, test5));

		assertThat(result.get(newSection("a > b > c")).size(), is(4));
		assertThat(result.get(newSection("a > b > c")), hasItems(test2, test3, test4, test5));

		assertThat(result.get(newSection("c")).size(), is(1));
		assertThat(result.get(newSection("c")), hasItems(test6));
	}

	private static class TestContent implements Catalogued {

		private String[] sections;
		private String name;

		private TestContent(String name, String... sections) {
			this.name = name;
			this.sections = sections;
		}

		@Override
		public String[] getSections() {
			return sections;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestContent other = (TestContent) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
}
