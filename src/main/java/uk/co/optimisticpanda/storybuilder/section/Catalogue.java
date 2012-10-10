package uk.co.optimisticpanda.storybuilder.section;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;

import com.google.common.collect.LinkedHashMultimap;

public class Catalogue<D extends Catalogued> {

	private final Sections sections;
	private final Collection<D> content;

	public static <D extends Catalogued> Catalogue<D> create(D... content) {
		return new Catalogue<D>(content);
	}

	private Catalogue(D... hasSections) {
		this.content = Arrays.asList(hasSections);
		this.sections = new Sections(hasSections);
	}

	/**
	 * @returns a map that has ordered the catalogue's sections as it's keys and
	 *          the {@link Catalogued} items in that section as it's keys values
	 */
	public Map<Section, Collection<D>> getContents() {
		LinkedHashMultimap<Section, D> multiMap = LinkedHashMultimap.create();
		for (Section section : sections.getDistinct()) {
			for (D cataloguedItem : content) {
				if (section.contains(cataloguedItem)) {
					multiMap.put(section, cataloguedItem);
				}
			}
		}
		return multiMap.asMap();
	}

	/**
	 * @returns a tree that represents this catalogues index. The root of the
	 *          tree is just a default node (to prevent the tree from being
	 *          multi-rooted, which in retrospect isn't really an issue)
	 */
	public Node getIndex() {
		return sections.asTree();
	}

	public static interface Catalogued {

		String[] getSections();

	}
	
}
