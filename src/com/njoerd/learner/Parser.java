package com.njoerd.learner;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Parser {
	enum State {QUESTION_TEXT, QUESTIONS, NONE};
	static int counter =0;

	public List<Question> parseFile(final File file) throws IOException {
		final List<Question> questions = new ArrayList<>();
		final BufferedReader reader = new BufferedReader(new FileReader(file));

		State state = State.NONE;

		StringBuffer qText = new StringBuffer();
		List<String> qQuestions = new ArrayList<>();
		int correct =-1;
		String tOBuffer="";
		String line;
		while ((line = reader.readLine())!=null)
		{

			line = line.replaceAll("&nbsp;", " ");
			int index = 0;

			/*			if (counter==207)
				System.out.println(line);*/


			if (state == State.NONE && line.contains("Question ID"))
			{
				state = State.QUESTION_TEXT;
				index = line.indexOf("</p>")+4;
				qText = new StringBuffer();
			}
			if (state == State.QUESTION_TEXT)
			{
				String temp1 = line.substring(index);

				final int end = temp1.indexOf("Select one");

				if (end!=-1)
				{
					state = State.QUESTIONS;
					index+=end;
					temp1 = temp1.substring(0, end);
					qQuestions = new ArrayList<>();
				}

				final String temp2 = temp1.replaceAll("<p>", "\n");

				final String[] x = temp2.split("</*[a-zA-Z\"=:\\- #0-9;\\.\\%\\,/]*>");

				for (final String ttt : x) {
					if (ttt.length()>0)
					{
						qText.append(ttt);
						//						qText.append("\n");
					}
				}

			}
			if (state == State.QUESTIONS)
			{
				final String temp1 = line.substring(index);
				final int end = temp1.indexOf("Feedback");

				if (end!=-1)
				{
					state = State.NONE;
					index+=end;


					/*					if (counter==207)
						System.out.println();*/
					questions.add(storeQuestion(qText, qQuestions, correct));
				}
				else
				{

					final int index2 = temp1.indexOf("answernumber");
					if (index2!=-1 || tOBuffer.length()>0)
					{
						final int index3 =
								tOBuffer.length()>0 ?
										0 :
											temp1.indexOf("/span>", index2)+5;
								final int index4 = temp1.indexOf("</label>");
								if (temp1.contains("title=\"Correct\""))
								{
									correct = qQuestions.size();
								}

								final String temp2;

								if (index4==-1)
								{
									temp2= temp1.substring(index3+1);
									tOBuffer = tOBuffer + " "+  temp2;
								}
								else
								{
									temp2= tOBuffer + temp1.substring(index3+1, index4);
									tOBuffer = "";
									qQuestions.add(temp2);
								}


					}
				}
			}
		}
		reader.close();
		return questions;
	}

	private Question storeQuestion(final StringBuffer qText, final List<String> questions, final int correct) {
		return new Question(qText, questions, correct, counter++);
	}

}
