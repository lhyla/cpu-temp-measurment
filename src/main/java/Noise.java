public class Noise {

  public static void main(String[] args) {
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
    new Thread(new RunMe()).start();
  }

  private static class RunMe implements Runnable {
    @Override
    public void run() {
      System.out.println("Run start!");
      double val = 1;
      long iterator = 1;

      while (true) {
        if (iterator % 1000000000 == 0) {
          System.out.println("Val:" + val + " after iteration:" + iterator);
        }
        iterator++;
        val += (iterator / 10000000);
      }
    }
  }
}
