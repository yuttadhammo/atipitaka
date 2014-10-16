package org.yuttadhammo.tipitaka;

public class QuizButton {
	private boolean right;
	private String[] text;
	private int position;

	public QuizButton(boolean right, String[] text, int position) {
		this.setRight(right);
		this.setText(text);
		this.position = position;
	}

	public String[] getText() {
		return text;
	}

	public void setText(String[] text) {
		this.text = text;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
}
