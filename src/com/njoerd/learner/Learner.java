package com.njoerd.learner;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Learner {

	public static void main(final String[] args) {
		new Learner();
		Learner.parse();
	}

	public static List<Question> parse() {

		final String path = "/home/daha/Dropbox/temp/paper2";
		final Parser parser = new Parser();

		final File[] files = new File(path).listFiles(new FileFilter() {

			@Override
			public boolean accept(final File f) {
				return !f.isDirectory();
			}
		});

		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(final File o1, final File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		final List<Question> questions = new ArrayList<>();

		for (final File file : files) {
			try {
				questions.addAll(parser.parseFile(file));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}

		return questions;

	}

}
