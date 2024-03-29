package example.guestbook;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String reqPath = getRequestPath(req);
		String servletPath = req.getServletPath();
		try {
			if (reqPath.equals("/list")) {
				processList(req, resp);
			} else if (reqPath.equals("/delete")) {
				processDelete(req, resp);
				redirectToList(servletPath, resp);
			} else if (reqPath.equals("/save")) {
				processSave(req, resp);
				redirectToList(servletPath, resp);
			} else if (reqPath.equals("/send_mail")) {
				processSendMail(req, resp);
				redirectToList(servletPath, resp);
			} else if (reqPath.equals("/search")) {
				processSearch(req, resp);

			} else if (reqPath.equals("/") || reqPath.equals("")) {
				redirectToList(servletPath, resp);
			}
		} catch (Throwable e) {
			LOG.log(Level.WARNING, "exception in service:", e);
			req.setAttribute("exception", e);
			resp.sendRedirect("/error.jsp");
		}
	}

	private void processSearch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String keyword = URLDecoder.decode(req.getParameter("keyword"),"UTF-8");
		List<GuestbookEntry> entries;
		if( keyword == null ||  keyword.length() < 1) {
			entries = service.listEntry();
		} else {
			entries = service.searchEntry( keyword);	
		}
		
		LOG.log(Level.INFO,"keyword:"+keyword);
		LOG.log(Level.INFO, "entries:" + entries);
		req.setAttribute("list", entries);
		req.setAttribute("keyword",keyword );
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/list.jsp");
		dispatcher.forward(req, resp);
	}

	private void redirectToList(String servletPath, HttpServletResponse resp)
			throws IOException {
		resp.sendRedirect(servletPath + "/list");
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
			throws Exception {
		List<GuestbookEntry> entries = service.listEntry();
		LOG.log(Level.INFO, "entries:" + entries);
		req.setAttribute("list", entries);
		req.setAttribute("keyword","");
		RequestDispatcher dispatcher = getServletContext()
				.getRequestDispatcher("/list.jsp");
		dispatcher.forward(req, resp);
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
