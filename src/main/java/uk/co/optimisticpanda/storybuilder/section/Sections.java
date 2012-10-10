package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.toArray;
import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
public class Sections {

	private final List<Section> sections;

	public Sections(Section... sections) {
		this.sections = Collections.unmodifiableList(asList(sections));
	}
	
	public Sections(Catalogued... catalogues) {
		List<Section> sections = Lists.newArrayList();
		for (Catalogued catalogue : catalogues) {
			for (String section: catalogue.getSections()) {
				sections.add(Section.newSection(section));
			}
		}
		this.sections = Collections.unmodifiableList(sections);
	}
	
	//Merge all distinct sections into a tree
	public Node asTree() {
		Node root = new Node("root");
		Iterable<Section> distinct = getDistinct();
		for (Section section : distinct) {
			root.addDescendent(toArray(section.asPathOfNamesToRoot(),String.class));
		}
		return root;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(sections);
	}

	Iterable<Section> getDistinct() {
		return ImmutableSortedSet.copyOf(sections);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sections other = (Sections) obj;
		return Iterables.elementsEqual(this.sections, other.sections);
	}

	@Override
	public String toString() {
		return Section.shallowToString(sections);
	}

}
