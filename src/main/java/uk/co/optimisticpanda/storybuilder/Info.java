package uk.co.optimisticpanda.storybuilder;

import com.google.common.base.Objects;

public class Info {

	public enum Type {
		MESSAGE, IMAGE, ERROR
	}

	private Type type;
	private String payload;

	public Info(Type type, String payload) {
		this.type = type;
		this.payload = payload;
	}

	public String toString() {
		return Objects.toStringHelper(Info.class).add("type", type).add("payload", payload).toString();
	};
}
