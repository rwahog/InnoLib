package com.kitaphana.Servlet.Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

  private ServletContext context;

  @Override
  public void init(FilterConfig filterConfig) {
    this.context = filterConfig.getServletContext();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    HttpSession session = request.getSession(false);
    String URI = request.getRequestURI();
    if ((session == null && (URI.endsWith("login") || URI.endsWith("registration")))
            || URI.contains("resources") || URI.contains("logger")
            || session != null && session.getAttribute("user") == null
            && (URI.endsWith("login") || URI.endsWith("registration"))) {
      filterChain.doFilter(request, response);
    } else if (session != null && session.getAttribute("user") != null) {
      filterChain.doFilter(request, response);
    } else if (URI.endsWith(".css") || URI.endsWith(".js")) {
      filterChain.doFilter(request, response);
    } else {
      response.sendRedirect("/login");
    }
  }

  @Override
  public void destroy() {

  }
}
