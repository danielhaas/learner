package com.njoerd.learner;


import java.util.List;

public class Question {
	public final int id;
	final StringBuffer qText;
	final List<String> questions;
	final int correct;

	public Question(final StringBuffer qText_, final List<String> questions_, final int correct_, final int id_) {
		qText = qText_;
		questions = questions_;
		correct = correct_;
		id = id_;
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();

		buffer.append(qText);
		buffer.append("\n");

		for (int i = 0; i < questions.size(); i++) {
			if (i==correct)
				buffer.append("*");
			buffer.append("\t");
			buffer.append(questions.get(i));
			buffer.append("\n");
		}

		// TODO Auto-generated method stub
		return buffer.toString();
	}
}
