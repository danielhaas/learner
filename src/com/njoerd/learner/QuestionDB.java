package com.njoerd.learner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDB {

	final List<Question> questions;
	final List<Integer> values;


	final File qdb = new File("qdb.data");
	boolean doWrite = false;


	public QuestionDB(final List<Question> questions_) {
		questions = questions_;
		values = new ArrayList<>();


		if (qdb.exists())
		{
			try {
				final BufferedReader reader = new BufferedReader(new FileReader(qdb));

				String line;
				while ((line = reader.readLine())!=null)
				{
					values.add(Integer.parseInt(line));
				}
				reader.close();
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}
		if (values.size()==0)
		{
			for (int i = 0; i < questions.size(); i++) {
				for (int j = 0; j < 5; j++) {
					values.add(i);
				}
			}
		}
		startWriter();
	}


	private void startWriter() {
		new Thread(new Runnable() {

			private final boolean running = true;

			@Override
			public void run() {

				while (running)
				{
					try {
						Thread.sleep(30 * 1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					if (doWrite)
					{
						writeDB();
						doWrite = false;
					}
				}
			}
		}, "Writer thread").start();
	}


	void writeDB()
	{
		try {
			final BufferedWriter writer = new BufferedWriter(new FileWriter(qdb));

			for (final Integer x : values) {
				writer.write(""+x + "\n");
			}
			writer.close();
			System.out.println("wrote");

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	int size()
	{
		return values.size();
	}

	int getQNumber() {
		return (int)(Math.random()*size());
	}

	void storeQuestion(final int nr, final boolean success) {
		doWrite = true;
		if (success)
		{
			values.remove(nr);
		}
		else
		{
			final int x = values.get(nr);
			for (int j = 0; j < 5; j++) {
				values.add(x);
			}
		}
	}

	public Question get(final int qnr) {
		//354
		//		if (true) return questions.get(207);
		final int x = values.get(qnr);
		return questions.get(x);
	}

}
