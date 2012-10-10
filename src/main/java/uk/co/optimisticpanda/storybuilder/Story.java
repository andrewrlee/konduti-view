package uk.co.optimisticpanda.storybuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jbehave.core.model.Meta;

import uk.co.optimisticpanda.storybuilder.section.Catalogue.Catalogued;

import com.google.common.base.CaseFormat;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Story implements Catalogued {
	private static final Pattern lineEnding = Pattern.compile("\r\n|\n|\r");
	private static final Pattern urlPattern = Pattern.compile("(.*?)\\[(.*?)\\]");
	private final String name;
	private List<Scenario> scenarios = Lists.newArrayList();
	private String duration;
	private Map<String, String> narrative;
	private String description;
	private Meta meta;
	private String[] sections;
	//Name/Description to Link
	private Map<String,String> links;
	private Statistics statistics;

	public Story(String name, String description, Meta meta) {
		this.name = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name).replaceAll("_", " ").replace(".story", "");
		this.description = description;
		this.meta = meta;
		this.sections = gatherSections(meta);
		this.links = gatherLinks(meta);
	}

	public void addScenario(Scenario scenario) {
		this.scenarios.add(scenario);
	}

	public void endStory(long duration) {
		this.duration = String.valueOf(duration / 1000)+ "." + (duration % 1000) +"s";
		getStats();
	}

	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

	public String getName() {
		return name;
	}

	public void setNarrative(Map<String, String> narrative) {
		this.narrative = narrative;
	}

	public Statistics getStats() {
		Statistics stats = new Statistics();
		for (Scenario scenario : scenarios) {
			stats.addStats(scenario.calculateStats());
		}
		this.statistics= stats;
		return statistics;
	}

	public String[] getSections() {
		return sections;
	}
	public Map<String,String> getLinks() {
		return links;
	}

	private String[] gatherSections(Meta meta) {
		return gatherMetaListValues(meta, "section", "sections");
	}
	
	Map<String, String> gatherLinks(Meta meta) {
		String[] linkMetaValues = gatherMetaListValues(meta, "link", "links");
		Map<String, String> result = Maps.newLinkedHashMap();
		for (String linkMetaValue : linkMetaValues) {
			Matcher matcher = urlPattern.matcher(linkMetaValue);
			if(matcher.find()){
				result.put(matcher.group(1).trim(), matcher.group(2).trim());
			}else{
				result.put(linkMetaValue, null);
			}
		}	
		return result;
	}

	String[] gatherMetaListValues(Meta meta, String... equivalentNames){
		List<String> results = Lists.newArrayList();
		for (String name : meta.getPropertyNames()) {
			for (String equivalent : equivalentNames) {
				if (name.equalsIgnoreCase(equivalent)) {
					Iterables.addAll(results, readLines(meta.getProperty(name)));
				}
			}
		}
		return Iterables.toArray(results, String.class);
	}
	
	private Iterable<String> readLines(String value){
		return Splitter.on(lineEnding).omitEmptyStrings().trimResults().split(value);
	}   

	@Override
	public String toString() {
		return Objects.toStringHelper(Story.class) //
				.add("name", name) //
				.add("scenarios", scenarios) //
				.add("duration", duration) //
				.add("narrative", narrative) //
				.add("description", description) //
				.add("meta", meta) //
				.add("sections", Arrays.toString(sections)) //
				.add("storyIds", links) //
				.add("statistics", statistics).toString(); //
	}

}
