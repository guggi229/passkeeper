package ch.bfh.guggisberg.stefan.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ch.bfh.guggisberg.stefan.beans.LoginBean;

public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 HttpServletRequest req = (HttpServletRequest) request;
		 LoginBean loginBean = (LoginBean) req.getSession().getAttribute("loginBean");
		
		if(loginBean==null){
			System.out.println("Logging bean null");
		}
		if(loginBean==null || !loginBean.isLogged()) {
			System.out.println("LoginFilter: Login ok!");
			String contextPath = ((HttpServletRequest)request).getContextPath();
			((HttpServletResponse)response).sendRedirect(contextPath+"/login.xhtml");
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// da gibts nichts zu tun! :-)

	}
	@Override
	public void destroy() {
		// da gibts nichts zu tun! :-)

	}


}
