package ru.curs.celesta.score;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ioann on 09.08.2017.
 */
public class ParameterizedView extends View {

  private final Map<String, Parameter> parameters = new LinkedHashMap<>();
  private final List<String> parameterRefsWithOrder = new ArrayList<>();

  public ParameterizedView(GrainPart grainPart, String name) throws ParseException {
    super(grainPart, name);
  }

  @Override
  String viewType() {
    return "function";
  }


  @Override
  void finalizeWhereConditionParsing() throws ParseException {
    List<TableRef> t = new ArrayList<>(getTables().values());
    if (whereCondition != null) {
      whereCondition.resolveFieldRefs(t);
      ParameterResolverResult paramResolveResult = whereCondition.resolveParameterRefs(parameters);

      if (!paramResolveResult.getUnusedParameters().isEmpty()) {
        String unusedParametersStr = String.join(", ", paramResolveResult.getUnusedParameters());
        throw new ParseException(String.format("%s '%s' contains not used parameters %s.",
            viewType(), getName(), unusedParametersStr));
      }
      whereCondition.validateTypes();

      parameterRefsWithOrder.addAll(paramResolveResult.getParametersWithUsageOrder());
    }
  }


  public void addParameter(Parameter parameter) throws ParseException {
    if (parameter == null)
      throw new IllegalArgumentException();

    if (parameter.getName() == null || parameter.getName().isEmpty()) {
      throw new ParseException(String.format("%s '%s' contains a parameter with undefined name.",
          viewType(), getName()));
    }

    if (parameters.containsKey(parameter.getName())) {
      throw new ParseException(
          String.format("%s '%s' already contains parameter with name '%s'. Use unique names for %s parameters.",
              viewType(), getName(), parameter.getName(), viewType())
      );
    }

    parameters.put(parameter.getName(), parameter);
  }

  public Map<String, Parameter> getParameters() {
    return new LinkedHashMap<>(parameters);
  }

  public List<String> getParameterRefsWithOrder() {
    return parameterRefsWithOrder;
  }

  @Override
  void save(PrintWriter bw) throws IOException {
    SQLGenerator gen = new CelestaSQLGen();
    Grain.writeCelestaDoc(this, bw);
    createViewScript(bw, gen);
    bw.println(";");
    bw.println();
  }

  /**
   * Генератор CelestaSQL.
   */
  class CelestaSQLGen extends AbstractView.CelestaSQLGen {
    @Override
    protected String preamble(AbstractView view) {
      return String.format("create %s %s (%s) as",
          viewType(),
          viewName(view),
          parameters.values().stream()
              .map(p -> p.getName() + " " + p.getType().toString())
              .collect(Collectors.joining(", ")));
    }
  }
}
