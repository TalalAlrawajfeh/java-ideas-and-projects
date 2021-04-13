package utils;

public class CopySetting {
	private Class<?> from;
	private Class<?> to;

	public CopySetting(Class<?> from, Class<?> to) {
		this.from = from;
		this.to = to;
	}

	public Class<?> getFrom() {
		return from;
	}

	public void setFrom(Class<?> from) {
		this.from = from;
	}

	public Class<?> getTo() {
		return to;
	}

	public void setTo(Class<?> to) {
		this.to = to;
	}
}
