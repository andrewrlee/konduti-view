package uk.co.optimisticpanda.reports;

import java.util.List;

import uk.co.optimisticpanda.storybuilder.Story;
import uk.co.optimisticpanda.storybuilder.section.Catalogue;
import uk.co.optimisticpanda.storybuilder.section.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StoryReport {
	private transient Gson gson;
	private List<String> indexes;
	private Story story;

	public StoryReport(Story currentStory) {
		this.story = currentStory;
		Catalogue<Story> catalogue = Catalogue.create(story);
		this.indexes = getDirectories(catalogue);
		this.gson = new GsonBuilder().create();
	}

	private List<String> getDirectories(Catalogue<Story> catalogue) {
		List<String> directories = Lists.newArrayList();
		for (Node node : catalogue.getIndex().getLeaves()) {
			directories.add(node.getDotSeperatedPath());
		}
		return directories;
	}

	public Iterable<String> getIndexes(){
		return indexes;
	}
	
	public String getName() {
		return story.getName();
	}

	public String toJson() {
		return gson.toJson(this);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(StoryReport.class) //
				.add("story", story) //
				.add("indexes", indexes) //
				.toString();
	}
}