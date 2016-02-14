package vg.civcraft.mc.civmenu.guides;

public class EventResponse {

	private final String text;
	private final String url;
	private final String book;
	
	public EventResponse(String text, String url, String book) {
		this.text = text;
		this.url = url;
		this.book = book;
	}
	
	public EventResponse(String text) {
		this(text, null, null);
	}

	public String getText() {
		return text;
	}

	public String getUrl() {
		return url;
	}

	public String getBook() {
		return book;
	}
}
