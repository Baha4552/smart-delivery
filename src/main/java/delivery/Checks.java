package delivery;
final class Checks {
 private Checks() {}
 static double nonnegative(double v, String field) { if (!Double.isFinite(v) || v < 0) throw new IllegalArgumentException(field); return v; }
 static String text(String v) { if (v == null || v.isBlank()) throw new IllegalArgumentException("Missing text"); return v; }
}
