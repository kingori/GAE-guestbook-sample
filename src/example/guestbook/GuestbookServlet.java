package example.guestbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GuestbookServlet extends HttpServlet {

	GuestbookService service;

	@Override
	public void init() throws ServletException {
		super.init();
		service = new GuestbookServiceImpl();
	}

	private static final Logger LOG = Logger
			.getLogger(GuestbookServiceImpl.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String reqPath = getRequestPath(req);
		String servletPath = req.getServletPath();
		try {
			if (reqPath.equals("/list")) {
				processList(req, resp);
			} else if (reqPath.equals("/delete")) {
				processDelete(req, resp);
				resp.sendRedirect(servletPath + "/list");
			} else {
				resp.sendRedirect(servletPath + "/list");
			}
		} catch (Throwable e) {
			LOG.log(Level.WARNING, "exception in doGet:", e);
			req.setAttribute("exception", e);
			resp.sendRedirect("/error.jsp");
		}
	}

	private String getRequestPath(HttpServletRequest req) {
		String uri = req.getRequestURI();

		LOG.log(Level.INFO, "uri:" + uri);
		LOG.log(Level.INFO, "servlet path:" + req.getServletPath());

		String reqPath = uri.substring(req.getServletPath().length());
		LOG.log(Level.INFO, "req path:" + reqPath);
		return reqPath;
	}

	private void processList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			List<GuestbookEntry> entries = service.listEntry();
			LOG.log(Level.INFO, "entries:" + entries);
			req.setAttribute("list", entries);
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/list.jsp");
			dispatcher.forward(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/error.jsp");
			dispatcher.forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String reqPath = getRequestPath(req);
		String servletPath = req.getServletPath();

		try {
			if (reqPath.equals("/save")) {
				processSave(req, resp);
				resp.sendRedirect(servletPath + "/list");
			} else if (reqPath.equals("/send_mail")) {
				processSendMail(req, resp);
				resp.sendRedirect(servletPath + "/list");
			} else {
				resp.sendRedirect(servletPath + "/list");
			}
		} catch (Throwable e) {
			LOG.log(Level.WARNING, "exception in doPost:", e);
			req.setAttribute("exception", e);
			resp.sendRedirect("/error.jsp");
		}
	}

	private void processDelete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String id = req.getParameter("id");
		service.deleteEntry(Long.parseLong(id));
	}

	private void processSendMail(HttpServletRequest req,
			HttpServletResponse resp) throws UnsupportedEncodingException,
			MessagingException {
		String emailAddress = req.getParameter("email");
		String content = req.getParameter("content");

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("kingori@gmail.com",
				"Example.com Admin"));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
				emailAddress, "Mr. User"));
		msg.setSubject("방문해 주셔서 고맙습니다!", "utf-8");
		msg.setText(content);
		Transport.send(msg);

	}

	private void processSave(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String name = req.getParameter("name");
		String comment = req.getParameter("comment");
		String email = req.getParameter("email");

		GuestbookEntry entry = new GuestbookEntry();
		entry.setName(name);
		entry.setComment(comment);
		entry.setEmail(email);

		service.addEntry(entry);
	}

}
