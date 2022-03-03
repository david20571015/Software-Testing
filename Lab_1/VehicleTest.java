import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VehicleTest {
  private Vehicle vehicle;
  private int numVehicle = 0;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    vehicle = new Vehicle();
    numVehicle++;
  }

  @org.junit.jupiter.api.AfterEach
  void tearDown() {
    vehicle.finalize();
    numVehicle--;
  }

  @org.junit.jupiter.api.Test
  void testFinalize() {
    // https://www.baeldung.com/java-testing-system-out-println
    final PrintStream standardOut = System.out;
    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));

    new Vehicle().finalize();

    assertEquals("finalize has been called", outputStreamCaptor.toString().trim());
    System.setOut(standardOut);
  }

  @org.junit.jupiter.api.Test
  void testSpeed() {
    assertEquals(0, vehicle.getSpeed());

    vehicle.setSpeed(1);
    assertEquals(1, vehicle.getSpeed());
    vehicle.setSpeed(5);
    assertEquals(5, vehicle.getSpeed());
    vehicle.setSpeed(10);
    assertEquals(10, vehicle.getSpeed());
    vehicle.setSpeed(20);
    assertEquals(20, vehicle.getSpeed());
  }

  @org.junit.jupiter.api.Test
  void testDir() {
    assertEquals("north", vehicle.getDir());

    vehicle.setDir("south");
    assertEquals("south", vehicle.getDir());
    vehicle.setDir("east");
    assertEquals("east", vehicle.getDir());
    vehicle.setDir("west");
    assertEquals("west", vehicle.getDir());
  }

  @org.junit.jupiter.api.Test
  void totalVehicle() {
    assertEquals(numVehicle, Vehicle.totalVehicle());
  }
}
