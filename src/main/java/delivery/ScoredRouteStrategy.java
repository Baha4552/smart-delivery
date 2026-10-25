package delivery;
import java.util.*;
import java.util.function.ToDoubleFunction;
public abstract class ScoredRouteStrategy implements RouteStrategy {
 protected abstract ToDoubleFunction<Route> scorer(List<Route> valid, DeliveryRequest request);
 protected abstract String formula(Route r, DeliveryRequest q, double score);
 public final RouteDecision selectRoute(List<Route> routes, DeliveryRequest q) {
  Objects.requireNonNull(routes,"routes"); Objects.requireNonNull(q,"request");
  List<Route> valid=new ArrayList<>(); Map<String,String> rejected=new LinkedHashMap<>(); Set<String> ids=new HashSet<>(); RouteConstraints constraints=new RouteConstraints();
  for(Route r:List.copyOf(routes)) { if(!ids.add(r.id())) throw new IllegalArgumentException("Duplicate route ID: "+r.id()); String why=constraints.rejection(r,q); if(why.isEmpty()) valid.add(r); else rejected.put(r.id(),why); }
  if(valid.isEmpty()) throw new NoSuitableRouteException("No suitable route. Rejections: "+rejected);
  ToDoubleFunction<Route> scoring=scorer(List.copyOf(valid),q);
  Route winner=valid.stream().min(Comparator.comparingDouble(scoring).thenComparing(Route::id)).orElseThrow();
  double score=scoring.applyAsDouble(winner);
  return new RouteDecision(getStrategyName(),winner,score,RouteMetrics.time(winner),RouteMetrics.cost(winner,q),formula(winner,q,score)+"; minimum score among "+valid.size()+" feasible routes; ties resolved by route ID",rejected);
 }
}
