package com.njoerd.learner;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LearnerServlet
 */
@WebServlet("/LearnerServlet")
public class LearnerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	final QuestionDB db;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LearnerServlet() {
		super();
		db = new QuestionDB(Learner.parse());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {


		final String ret = request.getParameter("ret");
		final String nr = request.getParameter("nr");

		if (ret!=null)
		{
			db.storeQuestion(Integer.parseInt(nr), ret.equals("true"));
		}


		final int qnr = db.getQNumber();
		printQuestion(response.getWriter(), qnr);

	}

	class Answer
	{
		final int index;
		final String text;
		final boolean correct;
		public Answer(final int index_, final String text_, final boolean correct_) {
			index = index_;
			text = text_;
			correct = correct_;
		}
	}

	List<Answer> getAnswers(final Question q)
	{
		final List<Answer> myReturn = new ArrayList<>();

		for (int i = 0; i < q.questions.size(); i++) {
			final String t = q.questions.get(i);
			myReturn.add(new Answer(i, t, q.correct==i));
		}

		return myReturn;
	}

	private void printQuestion(final PrintWriter out, final int qnr) {
		final Question q = db.get(qnr);

		final List<Answer> answers = getAnswers(q);

		Collections.shuffle(answers);

		out.println("<html><head><title>Question: "+q.id + "("+qnr+")"+"</title>");

		out.println("<script>");
		out.println("\tfunction resolve(correct) {");


		for (final Answer answer : answers) {
			out.print("document.getElementById(\"lq_"+answer.index+"\").style.backgroundColor = \"");
			if (answer.correct)
				out.print("green");
			else
				out.print("red");

			out.println("\";");
		}

		out.println("document.getElementById(\"sub\").disabled = false;");
		out.print("document.getElementById(\"ret\").value = correct");


		out.println(";");
		out.println("");
		out.println("");
		out.println("");
		out.println("");
		out.println("");
		out.println("}");
		out.println("</script>");


		out.println("</head><body>");

		out.println("<form><fieldset>");
		out.println("<table><tr><td>");

		final String text = q.qText.toString();

		/*		//	text = text.replace((char) 160, ' ');
		if (text.contains("\nIV"))
		{

			final int xx1 = text.indexOf("\nI.");
			final String xxx = text.substring(xx1, xx1+20);
			System.out.println(xxx);

			text = text.replace("\nI[.]", "\n<br>I ");
			text = text.replace("\nII[\\s\\.]", "\n<br>II ");
			text = text.replace("\nIII[\\s\\.]", "\n<br>III ");
			text = text.replace("\nIV[\\s\\.]", "\n<br>IV ");
		}*/
		out.println(clean(text));

		out.println("</td></tr>");


		for (final Answer answer : answers) {
			out.println("<tr><td>");

			out.println("<input type=\"radio\" id=\"q_"+answer.index+"\" name=\"question\" value=\"q_"+answer.index+"\" ");

			out.println(" onClick=\"resolve(");
			out.println(answer.correct);
			out.println(" );\"");


			out.println("\">");
			out.println("<label id=\"lq_"+answer.index+"\" for=\"q_"+answer.index+"\">");
			out.println(clean(answer.text));
			out.println("</label></td></tr>");
		}
		out.println("</table>");
		out.println("</fieldset></form><form><input type=\"submit\" id=\"sub\" disabled>");
		out.println("<input type=\"hidden\" id=\"nr\" name=\"nr\" value=\""+qnr+"\">");
		out.println("<input type=\"hidden\" id=\"ret\" name=\"ret\"></form>");

		out.println("<br><br><small>"+ db.size() + " questions to go. id="+q.id+" </small>");
		out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


	String clean(final String t_)
	{
		String text = t_.replaceAll("\n\n", "\n");
		text = text.replace("\n", "\n<br>");
		text = text.replaceAll("\n<br>\n<br>", "\n<br>");
		text = text.replaceAll("’", "&rsquo;");
		text = text.replaceAll("‘", "&lsquo;");

		return text;
	}
}
