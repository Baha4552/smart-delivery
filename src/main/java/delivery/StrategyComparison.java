package delivery;
import java.util.*;
public final class StrategyComparison {
 public record Outcome(String strategy, RouteDecision decision, String failure) {}
 public List<Outcome> compare(List<RouteStrategy> strategies,List<Route> routes,DeliveryRequest q) {
  List<Outcome> outcomes=new ArrayList<>();
  for(RouteStrategy s:strategies) { try { outcomes.add(new Outcome(s.getStrategyName(),s.selectRoute(routes,q),null)); } catch(NoSuitableRouteException ex) { outcomes.add(new Outcome(s.getStrategyName(),null,ex.getMessage())); } }
  return List.copyOf(outcomes);
 }
}
