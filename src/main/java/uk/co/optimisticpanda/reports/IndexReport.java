package uk.co.optimisticpanda.reports;

import static com.google.common.collect.Iterables.transform;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import uk.co.optimisticpanda.storybuilder.Story;
import uk.co.optimisticpanda.storybuilder.section.Catalogue;
import uk.co.optimisticpanda.storybuilder.section.Node;
import uk.co.optimisticpanda.storybuilder.section.Section;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IndexReport {

	private final Node sections;
	private final Map<String, Collection<String>> sectionsToStories;
	private final Map<String, String> dotSeperatedToPath;
	private final Map<String, String> pathTodotSeperated;
	private transient Gson gson;

	public IndexReport(Catalogue<Story> catalogue) {
		this.sections = catalogue.getIndex();
		this.dotSeperatedToPath = Maps.newHashMap();
		this.pathTodotSeperated = Maps.newHashMap();
		this.sectionsToStories = Maps.newHashMap();
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		populateIndex(catalogue.getContents());
	}

	private void populateIndex(Map<Section, Collection<Story>> map) {
		for (Entry<Section, Collection<Story>> entry : map.entrySet()) {
			Iterable<String> names = transform(entry.getValue(), new Function<Story, String>() {
				@Override
				public String apply(Story input) {
					return input.getName();
				}
			});
			sectionsToStories.put(entry.getKey().getDotSeperated(), Lists.newArrayList(names));
			dotSeperatedToPath.put(entry.getKey().getDotSeperated(), entry.getKey().getFullPathName());
			pathTodotSeperated.put(entry.getKey().getFullPathName(), entry.getKey().getDotSeperated());
		}
	}

	public String toJson() {
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(StoryReport.class) //
				.add("sections", sections) //
				.add("sectionsToStories", sectionsToStories) //
				.toString();
	}
}