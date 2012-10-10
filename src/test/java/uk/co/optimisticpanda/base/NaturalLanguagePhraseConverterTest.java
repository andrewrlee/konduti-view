package uk.co.optimisticpanda.base;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.base.NaturalLanguagePhraseConverter.NaturalLanguagePhrase;

public class NaturalLanguagePhraseConverterTest {

	private NaturalLanguagePhraseConverter converter;

	@Before
	public void setup() {
		converter = NaturalLanguagePhraseConverter.createWithDefaultWords();
	}

	@Test
	public void accepts() {
		assertThat(converter.accept(NaturalLanguagePhrase.class), is(true));
		assertThat(converter.accept(String.class), is(false));
	}

	@Test
	public void convert() {
		assertThat(convert("is").asBoolean(), is(true));
		assertThat(convert("is ").asBoolean(), is(true));
		assertThat(convert(" is").asBoolean(), is(true));
		assertThat(convert("is not").asBoolean(), is(false));
		assertThat(convert("IS not").asBoolean(), is(false));
		assertThat(convert("ISn't").asBoolean(), is(false));
		assertThat(convert("  ISn't").asBoolean(), is(false));
	}

	@Test
	public void notFound() {
		try {
			convert("bobbins");
			fail("unknown phrase");
		} catch (IllegalArgumentException e) {
			assertThat(
					e.getMessage(),
					is("The phrase [bobbins] was not found in either the known true or false phrases.\nTrue Phrases: [do, am, is, will, can, should]\nFalse Phrases: [don't, do not, am not, isn't, is not, won't, will not, can't, can not, shouldn't, should not]"));
		}
	}

	private NaturalLanguagePhrase convert(String text) {
		return (NaturalLanguagePhrase) converter.convertValue(text,
				NaturalLanguagePhrase.class);
	}
}


