package delivery;
import java.util.*;
public final class Main {
 private static void print(RouteDecision d) { System.out.printf(Locale.ROOT,"%s -> %s | time=%d min | cost=%.2f | CO2=%.2f | score=%.4f%n  %s%n  Rejected: %s%n",d.strategyName(),d.route().id(),d.finalMinutes(),d.totalCost(),d.route().carbonEmission(),d.score(),d.explanation(),d.rejectedRoutes()); }
 public static void main(String[] args) {
  var routes=DemoData.routes(); var q=DemoData.standard(); var strategies=DemoData.strategies();
  System.out.println("COMPARISON same request"); new StrategyComparison().compare(strategies,routes,q).forEach(o->{ if(o.decision()!=null) print(o.decision()); else System.out.println(o.failure()); });
  System.out.println("RUNTIME SWITCHING same planner, three switches"); var planner=new RoutePlanner(strategies.get(0)); print(planner.plan(routes,q)); for(int i=1;i<4;i++) { planner.setStrategy(strategies.get(i)); print(planner.plan(routes,q)); }
  var selector=new StrategySelector(Map.of(DeliveryPriority.URGENT,strategies.get(0),DeliveryPriority.ECONOMY,strategies.get(1),DeliveryPriority.ECO,strategies.get(2),DeliveryPriority.STANDARD,strategies.get(3)));
  System.out.println("AUTOMATIC SELECTION"); for(var priority:DeliveryPriority.values()) { var request=new DeliveryRequest(priority,10000,90,false,10,Set.of()); planner.setStrategy(selector.select(request)); print(planner.plan(routes,request)); }
  System.out.println("DYNAMIC CONSTRAINTS fastest with budget 3000"); print(strategies.get(0).selectRoute(routes,new DeliveryRequest(DeliveryPriority.STANDARD,3000,90,false,10,Set.of())));
  System.out.println("FAILURE CASES"); try { planner.plan(List.of(),q); } catch(NoSuitableRouteException e) { System.out.println(e.getMessage()); }
  try { planner.plan(routes,new DeliveryRequest(DeliveryPriority.STANDARD,0,0,false,0,Set.of())); } catch(NoSuitableRouteException e) { System.out.println(e.getMessage()); }
  try { new BalancedRouteStrategy(1,1,1,1); } catch(IllegalArgumentException e) { System.out.println(e.getMessage()); }
 }
}
