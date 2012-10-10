package uk.co.optimisticpanda.base;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import uk.co.optimisticpanda.storybuilder.StoryTest;
import uk.co.optimisticpanda.storybuilder.section.CatalogueTest;
import uk.co.optimisticpanda.storybuilder.section.NodeTest;
import uk.co.optimisticpanda.storybuilder.section.SectionTest;
import uk.co.optimisticpanda.storybuilder.section.SectionsTest;

@RunWith(Suite.class)
@SuiteClasses({ //
CatalogueTest.class,//
		NodeTest.class, //
		SectionsTest.class, //
		SectionTest.class,//
		StoryTest.class, //
		NaturalLanguagePhraseConverterTest.class//
})
public class AllTests {

}
