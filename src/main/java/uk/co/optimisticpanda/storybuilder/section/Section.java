package uk.co.optimisticpanda.storybuilder.section;

import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Iterables.limit;
import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;

import java.util.List;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;
import uk.co.optimisticpanda.utils.Functions.LowerAndReplaceWhitespaceWithUnderscores;
import uk.co.optimisticpanda.utils.Functions.Named;
import uk.co.optimisticpanda.utils.Functions.ToName;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.primitives.Ints;

public class Section implements Comparable<Section>, Named{

	private final String name;
	private Section parent;

	private Section(String sectionName) {
		this.name = sectionName;
	}

	/**
	 * Create a section chain from a string. Subsections are seperated by >.
	 * @throws IllegalStateException e, if sectionTypes does not contain at least one section.
	 */
	public static Section newSection(String sectionPath) {
		Optional<Section> section = SectionBuilder.newSection(sectionPath, '>');
		if(!section.isPresent()){
			throw new IllegalStateException("["+ sectionPath + "] does not have a section in it, > seperated");
		}
		return section.get();
	}
	
	public Section getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int compareTo(Section o) {
		List<Section> fromRootToThisLeaf = reverse(this.getPathToRoot());
		List<Section> fromRootToOtherLeaf = reverse(o.getPathToRoot());

		int indexToCompare = Math.min(fromRootToThisLeaf.size(), fromRootToOtherLeaf.size());
		
		//Compare up to the shared minimum depth [a>b, a>b>c -> a>b]
		for (int i = 0; i < indexToCompare; i++) {
			Section otherCurrent = fromRootToOtherLeaf.get(i);
			Section thisCurrent = fromRootToThisLeaf.get(i);
			if(thisCurrent.getName().compareTo(otherCurrent.getName()) != 0){
				return thisCurrent.getName().compareTo(otherCurrent.getName());
			}
		}
		//Must match to the shared minimum depth, make longer paths last
		return Ints.compare(fromRootToThisLeaf.size() , fromRootToOtherLeaf.size());
	}

	static String shallowToString(Iterable<Section> sections){
		return "[" + Joiner.on(", ").join(asNames(sections))+"]";
	}

	public String getFullPathName() {
		return Joiner.on(" > ").join(asPathOfNamesToRoot());
	}
	
	public String getDotSeperated(){
		return Joiner.on(".").join(transform(asPathOfNamesToRoot(), new LowerAndReplaceWhitespaceWithUnderscores()));
	}

	public Iterable<String> asPathOfNamesToRoot() {
		return asNames(reverse(getPathToRoot()));
	}

	public List<Section> getPathToRoot() {
		List<Section> list = newArrayList(this);
		Section current = this.parent;
		while(current != null){
			list.add(current);
			current = current.parent;
		}
		return list;
	}
	
	private static Iterable<String> asNames(Iterable<Section> sections){
		return transform(sections, new ToName()); 
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(parent, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Section other = (Section) obj;
		return Objects.equal(name, other.name) && Objects.equal(parent, other.parent);
	}

	public boolean contains(Catalogued cataloguedItem) {
		for (String sectionAsString : cataloguedItem.getSections()) {
			if (this.equals(newSection(sectionAsString))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(Section.class) //
				.add("type", String.valueOf(name)) //
				.add("parent", parent != null ? parent.name : "null")//
				.add("pathToRoot", shallowToString(getPathToRoot()))//
				.toString();
	}
	
	private static class SectionBuilder{
		/**
		 * Create a new section chain from sectionTypes. Subsections are seperated by the passed in seperator.
		 * This will return an absent optional if sectionTypes does not contain any sections. 
		 */
		private static Optional<Section> newSection(String sectionTypes, char seperator) {
			Iterable<String> sections = Splitter.on(seperator).omitEmptyStrings().trimResults().split(sectionTypes);
			switch (size(sections)) {
			case 0:
				return Optional.absent();
			case 1:
				return Optional.of(new Section(getFirst(sections, null)));
			default:
				return Optional.of(createNodeWithParents(sections));
			}
		}
		
		//recursively create node and add parent
		private static Section createNodeWithParents(Iterable<String> ancestors) {
			return createNode(new Section(getLast(ancestors)), limit(ancestors, size(ancestors) - 1));
		}

		private static Section createNode(Section node, Iterable<String> ancestors) {
			node.parent = size(ancestors) == 1 ? new Section(getFirst(ancestors, null)) : createNodeWithParents(ancestors);
			return node;
		}
	}

}
