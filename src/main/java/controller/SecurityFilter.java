package controller;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;

public class SecurityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		try {
			Set<String> allowedParams = getAllowedParameters(req, res);
			Set<String> requestParams = request.getParameterMap().keySet();
			System.out.println("allowed: " + allowedParams);
			System.out.println("requested= " + requestParams);
			if (requestParams.size() > allowedParams.size()) {
				throw new CustomException("Invalid Request Parameters!");
			}
			for (String param : requestParams) {
				if (!allowedParams.contains(param)) {
					throw new CustomException("Invalid Request Parameters!");
				}
			}
		} catch (InvalidValueException | CustomException e) {
			e.printStackTrace();
			res.getWriter().write(
					"<script>const referrer=document.referrer;window.location.href=referrer+(referrer.includes('?')?'&':'?')+'error="
							+ e.getMessage() + "'</script>");
			return;
		}
		chain.doFilter(request, response);
	}

	public Set<String> getAllowedParameters(HttpServletRequest req, HttpServletResponse res)
			throws InvalidValueException, CustomException {
		Set<String> allowedParams = new HashSet<String>();
		allowedParams.add("error");
		allowedParams.add("message");
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			String filePath = req.getServletContext().getRealPath("security.xml");
			Document document = builder.parse(new File(filePath));
			document.getDocumentElement().normalize();
			String urlPath = req.getPathInfo().replace("/", "-").substring(1);
			NodeList nodeList = document.getElementsByTagName(urlPath);
			int size = nodeList.getLength();
			for (int i = 0; i < size; i++) {
				Element element = (Element) nodeList.item(i);
				String method = element.getAttribute("method");
				String requestMethod = req.getMethod();
				if (method.equals(requestMethod)) {
					NodeList paramsList = element.getElementsByTagName("property");
					int paramsSize = paramsList.getLength();
					for (int j = 0; j < paramsSize; j++) {
						Element param = (Element) paramsList.item(j);
						String patternName = param.getAttribute("pattern");
						if (!patternName.isEmpty()) {
							Element patternsList = (Element) document.getElementsByTagName("patterns").item(0);
							Element patternNode = (Element) patternsList.getElementsByTagName(patternName).item(0);
							if (!req.getParameter(param.getTextContent()).matches(patternNode.getTextContent())) {
								throw new InvalidValueException("Invalid Value for " + param.getTextContent());
							}
						}
						allowedParams.add(param.getTextContent());
					}
					break;
				}
			}
			return allowedParams;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new CustomException("Security Exception", e);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
