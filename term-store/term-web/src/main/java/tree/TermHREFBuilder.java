package tree;

import persistence.beans.Term;

public class TermHREFBuilder {
	public String href(String servletPath, Term term) {
		return servletPath + "?format=json&id=" + String.valueOf(term.getId());
	}
}
