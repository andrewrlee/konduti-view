package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Iterables.size;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.optimisticpanda.storybuilder.section.Section.newSection;

import org.junit.Test;

import com.google.common.collect.Lists;

public class SectionsTest {

	@Test
	public void getDistinctAreUniqueAndOrdered(){
		Sections many = new Sections(
				newSection("a > b > c"),
				newSection("a > b"),
				newSection("a"),
				newSection("b"),
				newSection("c"),
				newSection("b > a"),
				newSection("b > b"),
				newSection("b > b > c"),
				newSection("a > b"),
				newSection("a > b > c"),
				newSection("a > b"));
		
		Iterable<Section> expected = Lists.newArrayList(
				newSection("a"),
				newSection("a > b"),
				newSection("a > b > c"),
				newSection("b"),
				newSection("b > a"),
				newSection("b > b"),
				newSection("b > b > c"),
				newSection("c")
				);
		
		Iterable<Section> result = many.getDistinct();
		assertThat(size(result), is(8));
		assertTrue("Not ordered as expected:" + expected + ", result" + result ,elementsEqual(expected, result));
	}
	
	@Test
	public void asTree(){
		Sections sections = new Sections(
				newSection("a > b > c"),
				newSection("a > b"),
				newSection("a"),
				newSection("b"),
				newSection("c"),
				newSection("b > a"),
				newSection("b > b"),
				newSection("b > b > c"),
				newSection("a > b"),
				newSection("a > b > c"),
				newSection("a > b"));
		
		Node root = sections.asTree();
		assertThat(root.getChildren().size(), is(3));
		assertThat(root.getChildren().get(0).getName(), is("a"));
		assertThat(root.getChildren().get(1).getName(), is("b"));
		assertThat(root.getChildren().get(2).getName(), is("c"));
		
		Node b = root.getDescendent("b");
		assertThat(b.getChildren().size(), is(2));
		assertThat(b.getChildren().get(0).getName(), is("a"));
		assertThat(b.getChildren().get(1).getName(), is("b"));
	}
}
