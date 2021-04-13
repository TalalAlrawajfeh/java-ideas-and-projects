package actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.TermPersistenceService;
import persistence.beans.Term;
import tree.JSONConverter;
import tree.TreeConverter;
import tree.XMLConverter;

public class TermTreeRestfulServcieActionFactory implements ServletActionFactory {
	private TermPersistenceService termPersistenceService = new TermPersistenceService();

	private Map<String, Function<List<Term>, ServletAction>> formatsConverterMap = new HashMap<>();

	private Predicate<String> parameterNameValid = p -> Stream.of(new String[] { "id", "depth", "format" })
			.filter(vp -> vp.equalsIgnoreCase(p)).findAny().isPresent();
	private Predicate<String> isIdValid = this::isStringParsableToLong;
	private Predicate<String> isDepthValid = this::isStringParsableToLong;
	private Predicate<String> isFormatValid = p -> Stream.of(new String[] { "json", "xml" }).filter(vp -> vp.equalsIgnoreCase(p))
			.findAny().isPresent();
	private List<Predicate<String>> paramterValidations = new ArrayList<>();

	Function<List<Term>, ServletAction> sendJSON = ts -> {
		return (req, resp) -> {
			resp.setContentType("application/json");
			sendConvertedTree(ts, resp, new JSONConverter(req.getRequestURL().toString()));
		};
	};

	Function<List<Term>, ServletAction> sendXML = ts -> {
		return (req, resp) -> {
			resp.setContentType("application/xml");
			sendConvertedTree(ts, resp, new XMLConverter());
		};
	};

	private Predicate<HttpServletRequest> areParametersValid = req -> {
		Enumeration<String> parameterNames = req.getParameterNames();
		boolean formatParameterFound = false;

		while (parameterNames.hasMoreElements()) {
			String parameter = (String) parameterNames.nextElement();

			if ("format".equals(parameter))
				formatParameterFound = true;

			if ((!parameterNameValid.test(parameter)) && paramterValidations.stream()
					.map(v -> v.test(req.getParameter(parameter))).reduce(true, Boolean::logicalAnd))
				return false;
		}

		if (!formatParameterFound)
			return false;
		return true;
	};

	private ServletAction rootRequest = (req, resp) -> {
		if (areParametersValid.test(req)) {
			String depth = req.getParameter("depth");

			Term term;
			if (Objects.nonNull(depth)) {
				term = termPersistenceService.traverseRootDownToDepth(Integer.parseInt(depth));
			} else {
				term = termPersistenceService.getRoot();
			}

			formatsConverterMap.get(req.getParameter("format")).apply(Arrays.asList(term)).doAction(req, resp);
		} else {
			sendInvalidParametersErrorNumber(resp);
		}
	};

	private ServletAction termIdRequest = (req, resp) -> {
		if (areParametersValid.test(req)) {
			Long id = Long.valueOf(req.getParameter("id"));

			Term term;
			String depth = req.getParameter("depth");
			if (Objects.nonNull(depth)) {
				term = termPersistenceService.traverseTermByIdDownToDepth(id, Integer.parseInt(depth));
			} else {
				term = termPersistenceService.traverseTermByIdUpToRoot(id);
			}

			if (Objects.isNull(term)) {
				try {
					resp.sendError(404);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

			formatsConverterMap.get(req.getParameter("format")).apply(Arrays.asList(term)).doAction(req, resp);
		} else {
			sendInvalidParametersErrorNumber(resp);
		}
	};

	public TermTreeRestfulServcieActionFactory() {
		paramterValidations.add(isIdValid);
		paramterValidations.add(isDepthValid);
		paramterValidations.add(isFormatValid);

		formatsConverterMap.put("json", sendJSON);
		formatsConverterMap.put("xml", sendXML);
	}

	@Override
	public ServletAction getAction(HttpServletRequest req) {
		if (Objects.isNull(req.getParameter("id"))) {
			return rootRequest;
		}
		return termIdRequest;
	}

	private boolean isStringParsableToLong(String p) {
		try {
			Long.decode(p);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void sendInvalidParametersErrorNumber(HttpServletResponse resp) {
		try {
			resp.sendError(400);
		} catch (IOException e) {
			// TODO: Error page not yet implemented
			e.printStackTrace();
		}
	}

	private void sendConvertedTree(List<Term> termTree, HttpServletResponse resp,
			TreeConverter<String, Term> converter) {
		PrintWriter writer;
		try {
			writer = resp.getWriter();
			writer.print(converter.convert(termTree));
			writer.flush();
			resp.setStatus(200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
