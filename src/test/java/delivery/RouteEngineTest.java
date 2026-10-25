package delivery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
class RouteEngineTest {
 final List<Route> routes=DemoData.routes(); final DeliveryRequest q=DemoData.standard();
 DeliveryRequest request(double budget,int deadline,double emissions,Set<String> banned) { return new DeliveryRequest(DeliveryPriority.STANDARD,budget,deadline,false,emissions,banned); }
 String id(RouteStrategy s) { return s.selectRoute(routes,q).route().id(); }
 @Test void fastestIncludesPenalties() { assertEquals("R2",id(new FastestRouteStrategy())); assertEquals(21,new FastestRouteStrategy().selectRoute(routes,q).finalMinutes()); }
 @Test void cheapestUsesTotalCost() { var d=new CheapestRouteStrategy().selectRoute(routes,q); assertEquals("R3",d.route().id()); assertEquals(1800,d.totalCost()); }
 @Test void urgentSurcharge() { var u=new DeliveryRequest(DeliveryPriority.URGENT,10000,90,false,10,Set.of()); assertEquals(2300,RouteMetrics.cost(routes.get(2),u)); }
 @Test void ecoChoosesLowImpact() { assertEquals("R4",id(new EcoFriendlyRouteStrategy())); }
 @Test void balancedTimeOnly() { assertEquals("R2",id(new BalancedRouteStrategy(1,0,0,0))); }
 @Test void balancedCostOnly() { assertEquals("R3",id(new BalancedRouteStrategy(0,1,0,0))); }
 @Test void defaultBalancedScore() { var d=new BalancedRouteStrategy(.4,.3,.1,.2).selectRoute(routes,q); assertEquals("R8",d.route().id()); assertEquals(.4*(11.0/26)+.3*(1500.0/4000)+.1*(3.0/12)+.2*(1.0/4.5),d.score(),1e-9); }
 @Test void switchingSameContext() { var p=new RoutePlanner(new FastestRouteStrategy()); assertEquals("R2",p.plan(routes,q).route().id()); p.setStrategy(new CheapestRouteStrategy()); assertEquals("R3",p.plan(routes,q).route().id()); p.setStrategy(new EcoFriendlyRouteStrategy()); assertEquals("R4",p.plan(routes,q).route().id()); p.setStrategy(new DistanceFirstStrategy()); assertEquals("R5",p.plan(routes,q).route().id()); }
 @Test void selectorMappings() { var a=new FastestRouteStrategy(); var b=new CheapestRouteStrategy(); var c=new EcoFriendlyRouteStrategy(); var d=new BalancedRouteStrategy(.4,.3,.1,.2); var m=Map.<DeliveryPriority,RouteStrategy>of(DeliveryPriority.URGENT,a,DeliveryPriority.ECONOMY,b,DeliveryPriority.ECO,c,DeliveryPriority.STANDARD,d); var selector=new StrategySelector(m); for(var p:DeliveryPriority.values()) assertSame(m.get(p),selector.select(new DeliveryRequest(p,10000,90,false,10,Set.of()))); }
 @Test void ecoPreferenceForStandard() { var eco=new EcoFriendlyRouteStrategy(); var selector=new StrategySelector(Map.of(DeliveryPriority.ECO,eco)); assertSame(eco,selector.select(new DeliveryRequest(DeliveryPriority.STANDARD,100,10,true,10,Set.of()))); }
 @Test void urgentOverridesEcoPreference() { var fast=new FastestRouteStrategy(); assertSame(fast,new StrategySelector(Map.of(DeliveryPriority.URGENT,fast)).select(new DeliveryRequest(DeliveryPriority.URGENT,100,10,true,10,Set.of()))); }
 @Test void budgetFiltersBeforeScore() { assertEquals("R4",new FastestRouteStrategy().selectRoute(routes,request(3000,90,10,Set.of())).route().id()); }
 @Test void deadlineFiltersCheapRoutes() { assertEquals("R6",new CheapestRouteStrategy().selectRoute(routes,request(10000,30,10,Set.of())).route().id()); }
 @Test void unavailableCannotWin() { for(var s:DemoData.strategies()) assertTrue(s.selectRoute(routes,q).rejectedRoutes().get("R7").contains("Unavailable")); }
 @Test void emissionsAreHardLimit() { assertEquals("R8",new FastestRouteStrategy().selectRoute(routes,request(10000,90,1.5,Set.of())).route().id()); }
 @Test void prohibitedCannotWin() { assertEquals("R6",new FastestRouteStrategy().selectRoute(routes,request(10000,90,10,Set.of("R2"))).route().id()); }
 @Test void noSuitableRoute() { assertThrows(NoSuitableRouteException.class,()->new FastestRouteStrategy().selectRoute(routes,request(0,0,0,Set.of()))); }
 @Test void emptyList() { assertThrows(NoSuitableRouteException.class,()->new CheapestRouteStrategy().selectRoute(List.of(),q)); }
 @Test void invalidWeights() { for(double[] w:List.of(new double[]{1,1,1,1},new double[]{-1,1,1,0},new double[]{Double.NaN,0,0,1})) assertThrows(IllegalArgumentException.class,()->new BalancedRouteStrategy(w[0],w[1],w[2],w[3])); }
 @Test void fifthStrategy() { assertEquals("R5",id(new DistanceFirstStrategy())); }
 @Test void comparisonUsesSameRequest() { var result=new StrategyComparison().compare(List.of(new FastestRouteStrategy(),new CheapestRouteStrategy()),routes,q); assertEquals(List.of("R2","R3"),result.stream().map(o->o.decision().route().id()).toList()); }
 @Test void boundaryExactLimitsAccepted() { var r=routes.get(1); assertEquals("R2",new FastestRouteStrategy().selectRoute(List.of(r),request(5800,21,5,Set.of())).route().id()); }
 @Test void equalMetricsNormalizeToZero() { assertEquals(0,new BalancedRouteStrategy(.4,.3,.1,.2).selectRoute(List.of(routes.get(0)),q).score()); }
 @Test void tieBreakById() { var a=DemoData.route("A",1,1,1,1,0,1,0,0,true); var b=DemoData.route("B",1,1,1,1,0,1,0,0,true); assertEquals("A",new FastestRouteStrategy().selectRoute(List.of(b,a),q).route().id()); }
 @Test void negativeDistanceRejected() { assertThrows(IllegalArgumentException.class,()->DemoData.route("bad",-1,1,1,1,0,1,0,0,true)); }
 @Test void missingRequestRejected() { assertThrows(NullPointerException.class,()->new FastestRouteStrategy().selectRoute(routes,null)); }
 @Test void duplicateIdsRejected() { assertThrows(IllegalArgumentException.class,()->new FastestRouteStrategy().selectRoute(List.of(routes.get(0),routes.get(0)),q)); }
 @Test void comparisonReportsAllFailures() { var results=new StrategyComparison().compare(DemoData.strategies(),List.of(),q); assertEquals(5,results.size()); assertTrue(results.stream().allMatch(o->o.decision()==null && o.failure().contains("No suitable route"))); }
}
