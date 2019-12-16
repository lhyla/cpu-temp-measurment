import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {
    new TempMeasureService().measureTemp();
  }

  public static class TempMeasureService {

    public void measureTemp() {
      System.out.println("Pomiary:");
      Collection<Measure> measuredTemp = getMeasuredTemp();

      System.out.println("Wartości średnie");
      printAvgTemp(measuredTemp);

      Statistics statistics = new Statistics(Utils.toPrimitiveArray(measuredTemp.stream().map(Measure::getTempValue).collect(Collectors.toList())));
      System.out.println("Wszystkie;" + "Odchylenie standardowe;" + Utils.roundUpTo2DecimalPlaces(statistics.getStdDev()));

      System.out.println("Wszystkie;" + "Wariancja;" + Utils.roundUpTo2DecimalPlaces(statistics.getVariance()));
    }

    private Collection<Measure> getMeasuredTemp() {
      List<Measure> tempMeasures = new ArrayList<>();

      System.out.println("Numer pomiaru;Nazwa rdzenia;Temperatura");
      for (int i = 1; i <= 15; i++) {
        int finalI = i;
        JSensors.get.components().cpus.forEach(cpu -> {
                  cpu.sensors.temperatures.forEach(temp -> {
                    if (!"Temp Package id 0".equals(temp.name)) {
                      Measure measure = new Measure(getCoreName(temp), temp.value);
                      tempMeasures.add(measure);
                      System.out.println(finalI + ";" + measure);
                    }
                  });
                }
        );
        if (i != 15) {
          Utils.sleep15Seconds();
        }
      }
      return tempMeasures;
    }

    private void printAvgTemp(Collection<Measure> measures) {
      Map<Boolean, List<Measure>> allCoresMeasures = measures.stream().collect(Collectors.partitioningBy(measure -> measure.getCoreId() > 2));
      Map<Boolean, List<Measure>> firstTwo = allCoresMeasures.get(false).stream().collect(Collectors.partitioningBy(measure -> measure.getCoreId() > 1));
      Map<Boolean, List<Measure>> lastTwo = allCoresMeasures.get(true).stream().collect(Collectors.partitioningBy(measure -> measure.getCoreId() > 3));

      List<Measure> firstCore = firstTwo.get(false);
      List<Measure> secCore = firstTwo.get(true);
      List<Measure> thirdCore;
      thirdCore = lastTwo.get(false);
      List<Measure> forthCore = lastTwo.get(true);

      System.out.println("Nazwa rdzenia;Nazwa wartości;Wartość");
      printAvgForCore(firstCore, "1");
      printAvgForCore(secCore, "2");
      printAvgForCore(thirdCore, "3");
      printAvgForCore(forthCore, "4");
      printAvgForCore(measures, "Wszystkie");
    }

    private void printAvgForCore(Collection<Measure> coreMeasures, String coreId) {
      double avg = coreMeasures.stream().mapToDouble(Measure::getTempValue).average().getAsDouble();
      System.out.println(coreId + ";" + "Średnia temperatura;" + Utils.roundUpTo2DecimalPlaces(avg));
    }


    private Integer getCoreName(Temperature temp) {
      return Integer.parseInt(String.valueOf(temp.name.charAt(10))) + 1;
    }
  }

  @Data
  @AllArgsConstructor
  public static class Measure {
    private Integer coreId;
    private Double tempValue;

    @Override
    public String toString() {
      return coreId + ";" + tempValue;
    }
  }
}
