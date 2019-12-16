import java.util.List;

public class Utils {

  public static void sleep15Seconds() {
    try {
      Thread.sleep(15 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static double[] toPrimitiveArray(List<Double> doubles) {
    double[] primitiveArray = new double[doubles.size()];

    for (int i = 0; i < doubles.size(); i++) {
      primitiveArray[i] = doubles.get(i);
    }

    return primitiveArray;
  }

  public static double roundUpTo2DecimalPlaces(double value){
    return  (double) Math.round(value * 100) / 100;
  }
}
