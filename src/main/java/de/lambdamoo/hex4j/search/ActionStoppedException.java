package de.lambdamoo.hex4j.search;

public class ActionStoppedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ActionStoppedException(String s) {
		super(s);
	}

	public ActionStoppedException() {
	}

}
