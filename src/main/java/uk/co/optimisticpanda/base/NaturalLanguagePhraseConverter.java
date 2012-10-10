package uk.co.optimisticpanda.base;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import org.jbehave.core.steps.ParameterConverters.ParameterConverter;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class NaturalLanguagePhraseConverter implements ParameterConverter{

	private List<String> truthPhrases = Lists.newArrayList();
	private List<String> falsePhrases = Lists.newArrayList();

	public static NaturalLanguagePhraseConverter createWithDefaultWords(){
		return new NaturalLanguagePhraseConverter() //
				.addTruePhrases("do", "am", "is", "will", "can","should") //
				.addFalsePhrases("don't", "do not" ,"am not","isn't", "is not", "won't", "will not", "can't", "can not", "shouldn't",	"should not");
	}
	
	@Override
	public boolean accept(Type type) {
        if (type instanceof Class<?>) {
            return NaturalLanguagePhrase.class.isAssignableFrom((Class<?>) type);
        }
        return false;
	}

	@Override
	public Object convertValue(String phrase, Type type) {
		String key = phrase.toLowerCase(Locale.ENGLISH).trim();
		if(truthPhrases.contains(key)){
			return new NaturalLanguagePhrase(phrase, true);
		}
		if(falsePhrases.contains(key)){
			return new NaturalLanguagePhrase(phrase, false);
		}
		String message = String.format( //
				"The phrase [%s] was not found in either the known true or false phrases.\nTrue Phrases: %s\nFalse Phrases: %s", 
				phrase, truthPhrases, falsePhrases);
		throw new IllegalArgumentException(message);
	}

	
	public NaturalLanguagePhraseConverter addTruePhrases(String... truePhrases){
		this.truthPhrases.addAll(Lists.newArrayList(truePhrases));
		return this;
	}
	
	public NaturalLanguagePhraseConverter addFalsePhrases(String... falsePhrases){
		this.falsePhrases.addAll(Lists.newArrayList(falsePhrases));
		return this;
	}
	
	public static class NaturalLanguagePhrase {

		private String word;
		private boolean resolvedTo;

		public NaturalLanguagePhrase(String word, boolean resolvedTo){
			this.word = word;
			this.resolvedTo = resolvedTo;
		}
		
		public Boolean asBoolean() {
			return Boolean.valueOf(resolvedTo);
		}
		public boolean resolvedTo() {
			return resolvedTo;
		}

		@Override
		public String toString() {
			return Objects.toStringHelper(NaturalLanguagePhrase.class).add("word", word).add("resolvedTo", resolvedTo).toString();
		}
		
	}

}
