package delivery;
import java.util.*;
public final class DemoData {
 public static Route route(String id,double d,int t,double base,double rate,double fees,double co2,int traffic,int weather,boolean available) { return new Route(id,new Location("Warehouse",44.85,65.5),new Location("Customer",44.9,65.6),d,t,base,rate,fees,co2,traffic,weather,available); }
 public static List<Route> routes() { return List.of(
 route("R1",12,20,3000,100,100,4,12,2,true),
 route("R2",16,18,4000,100,200,5,2,1,true),
 route("R3",10,42,1000,80,0,2,3,2,true),
 route("R4",14,38,1500,100,0,0.5,2,0,true),
 route("R5",8,35,1800,100,100,3,4,1,true),
 route("R6",20,25,2300,100,100,2.5,3,2,true),
 route("R7",6,10,500,50,0,0.2,0,0,false),
 route("R8",11,29,2200,100,0,1.5,2,1,true)); }
 public static DeliveryRequest standard() { return new DeliveryRequest(DeliveryPriority.STANDARD,10000,90,false,10,Set.of()); }
 public static List<RouteStrategy> strategies() { return List.of(new FastestRouteStrategy(),new CheapestRouteStrategy(),new EcoFriendlyRouteStrategy(),new BalancedRouteStrategy(.4,.3,.1,.2),new DistanceFirstStrategy()); }
}
