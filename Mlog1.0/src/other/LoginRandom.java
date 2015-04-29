package other;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class LoginRandom extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		SecurityUtils.getSubject().logout();
		//req.getSession().invalidate();
		String loginRandom = RandomProviderManager.getRandromProvider().genRandom();
		SessionControllers
				.set(req, SessionControllers.LoginRandom, loginRandom);
		resp.getWriter().write(loginRandom);
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

}
