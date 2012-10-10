package uk.co.optimisticpanda.storybuilder.section;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static uk.co.optimisticpanda.storybuilder.section.Section.newSection;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
public class SectionTest {

	private Section parentChildSection;
	private Section parentChildSubChildSection;
	private Section parentSection;

	@Before
	public void before(){
		parentSection = newSection("parent");
		parentChildSection = newSection("parent > child");
		parentChildSubChildSection = newSection("parent > child > subchild");
	}

	@Test
	public void getName(){
		assertThat(newSection("a > b").getFullPathName(), is("a > b"));
	}
	
	@Test 
	public void checkStructure(){
		assertEquals("Section{type=parent, parent=null, pathToRoot=[parent]}", parentSection.toString());
		assertEquals("Section{type=child, parent=parent, pathToRoot=[child, parent]}", parentChildSection.toString());
		assertEquals("Section{type=subchild, parent=child, pathToRoot=[subchild, child, parent]}", parentChildSubChildSection.toString());
	}
	
	@Test
	public void getPathFromThisToRoot(){
		
		assertThat(parentSection.getPathToRoot(), // 
				hasItems(parentSection));
		
		assertThat(parentChildSection.getPathToRoot(), // 
				hasItems(parentChildSection,parentSection));
		
		assertThat(parentChildSubChildSection.getPathToRoot(), //
				hasItems(parentChildSubChildSection,
						parentChildSection,
						parentSection));
	}

	@Test
	public void equalsHashcodeBehaviour(){
		Iterable<Section> expected = ImmutableSet.of(
				newSection("a"),
				newSection("a > b"),
				newSection("a > b > c"),
				newSection("b"),
				newSection("b > a"),
				newSection("b > b"),
				newSection("b > b > c"),
				newSection("c")
				);
		
		assertThat(Iterables.size(expected), is(8)); 
	}
	
	@Test
	public void asPathFromRootToLeaf(){
		Section section = newSection("a > b > c");
		String[] typePath = Iterables.toArray(section.asPathOfNamesToRoot(), String.class);
		assertThat(typePath.length , is(3));
		assertThat(typePath[0] , is("a"));
		assertThat(typePath[1] , is("b"));
		assertThat(typePath[2] , is("c"));
	}

	@Test
	public void contains(){
		Catalogued cataloguedItem = new Catalogued() {
			@Override
			public String[] getSections() {
				return new String[]{"a > b", "b > c"};
			}
		};
		assertTrue(newSection("a >b").contains(cataloguedItem));
		assertTrue(newSection("b > c").contains(cataloguedItem));
		assertFalse(newSection("b > d").contains(cataloguedItem));
	}
}
