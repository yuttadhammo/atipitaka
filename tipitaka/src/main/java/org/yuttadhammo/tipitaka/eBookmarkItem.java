package  org.yuttadhammo.tipitaka;

public class eBookmarkItem {
	private String title;
	private String url;
	
	public eBookmarkItem(String _title, String _url) {
		title = _title;
		url = _url;
	}
	
	public eBookmarkItem(String s) throws Exception{
		String [] tokens = s.split(":");		
		if(tokens.length == 2) {
			title = tokens[0].trim();
			url = tokens[1].trim();
		} 
		else {
			throw new Exception("Bookmark: Input format is invalid: " + tokens.length);
		}
		
	}
	
	@Override
	public String toString() {
		return String.format(" %s : %s ", title, url);
	}
	
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setTitle(String _title) {
		title = _title;
	}
	
	public void setUrl(String _url) {
		url = _url;
	}
}