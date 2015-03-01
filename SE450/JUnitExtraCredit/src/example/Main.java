
package example;

import example.common.CannotFitException;
import example.common.InvalidDataException;
import example.common.Point3D;
import example.domain.ContainerTruck;
import example.domain.StandardCargoTruck;
import example.domain.TankerTruck;
import example.truck.Truck;
import java.util.ArrayList;

/*
    Expected output from this "main" is found in comments at the end of the file.
*/
public class Main {

    private static final ArrayList<Truck> allTrucks = new ArrayList<>();

    public static void main(String[] args) {

        int outputCounter = 1;
        try {
            makeSampleTrucks();
        } catch (InvalidDataException ex) {
            System.out.format("Fatal error occurred when creating sample trucks: %s%n", ex.getMessage());
            ex.printStackTrace();
        }

        System.out.format("%d) Truck List:%n", outputCounter++);
        displayTrucks();

        System.out.format("%n%d) Loading cargo with weight 2250.0 into each truck:%n", outputCounter++);
        loadCargo();

        System.out.format("%n%d) Calling 'update' on all trucks & printing final status:%n", outputCounter++);
        updateTrucks();

        System.out.format("%n%d) Calling 'update' a second time on all trucks & printing final status:%n", outputCounter++);
        updateTrucks();

        System.out.format("%n%d) Unloading 1000.0 of cargo from each truck:%n", outputCounter++);
        unloadCargo();

    }

    private static void makeSampleTrucks() throws InvalidDataException {
        allTrucks.add(
                new StandardCargoTruck(new Point3D(1.1, 2.2, 3.3), new Point3D(333.3, 222.2, 111.1), 55.0, 95.0, 10000.0));
        allTrucks.add(
                new StandardCargoTruck(new Point3D(4.4, 5.5, 6.6), new Point3D(444.4, 555.5, 666.6), 38.0, 69.0, 8500.0));
        allTrucks.add(
                new ContainerTruck(new Point3D(1.2, 3.4, 5.6), new Point3D(12.3, 45.6, 78.9), 43.0, 82.0, 8000.0));
        allTrucks.add(
                new ContainerTruck(new Point3D(9.8, 7.6, 5.4), new Point3D(22.3, 33.4, 44.5), 57.0, 89.0, 6500.0));
        allTrucks.add(
                new TankerTruck(new Point3D(100.0, 200.0, 300.0), new Point3D(900.0, 800.0, 700.0), 47.0, 75.0, 8000.0));
        allTrucks.add(
                new TankerTruck(new Point3D(9.0, 7.0, 5.0), new Point3D(19.8, 17.6, 15.4), 22.0, 93.0, 7200.0));
    }

    private static void displayTrucks() {
        for (Truck t : allTrucks) {
            System.out.format("    %s%n%n", t);
        }
    }

    private static void loadCargo() {

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(0).getIdentifier(), 2250.0);
            allTrucks.get(0).load(2250.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(0).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(0));

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(1).getIdentifier(), 4500.0);
            allTrucks.get(1).load(4500.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(1).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(1));

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(2).getIdentifier(), 1550.0);
            allTrucks.get(2).load(1550.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(2).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(2));

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(3).getIdentifier(), 6500.0);
            allTrucks.get(3).load(6500.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(3).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(3));

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(4).getIdentifier(), 1500.0);
            allTrucks.get(4).load(1500.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(4).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(4));

        try {
            System.out.format("    Loading Truck %s with %.1f%n", allTrucks.get(5).getIdentifier(), 2250.0);
            allTrucks.get(5).load(2250.0);
        } catch (CannotFitException | InvalidDataException ex) {
            System.out.format("    *** Error loading %s: %s%n", allTrucks.get(5).getIdentifier(), ex.getMessage());
        }
        System.out.format("    After Loading:%n      %s%n%n", allTrucks.get(5));

    }

    private static void updateTrucks() {
        for (Truck t : allTrucks) {
            try {
                System.out.format("    Before Update:%n     %s%n", t);
                t.update(1500.0);
                System.out.format("    After Update:%n     %s%n", t);
            } catch (InvalidDataException ex) {
                System.out.format("    ***Error occurred in update: %s%n", ex.getMessage());
            }
            System.out.format("%n");
        }
    }

    private static void unloadCargo() {
        for (Truck t : allTrucks) {
            try {
                System.out.format("    Unloading Truck %s%n", t);
                t.unLoad(1000.0);
            } catch (CannotFitException | InvalidDataException ex) {
                System.out.format("\t***Error occurred in unLoad: %s%n", ex.getMessage());
            }
            System.out.format("    After Loading:%n      %s%n%n", t);
        }
    }
}


/*
1) Truck List:
    I am StandardCargoTruck ID1.
	I am at [1.10, 2.20, 3.30] and am heading to [333.30, 222.20, 111.10].
	My load is 0.0 and my max load is 10000.0.
	Distance to my destination is 412.77. I'm not there yet

    I am StandardCargoTruck ID2.
	I am at [4.40, 5.50, 6.60] and am heading to [444.40, 555.50, 666.60].
	My load is 0.0 and my max load is 8500.0.
	Distance to my destination is 965.25. I'm not there yet

    I am ContainerTruck ID3.
	I am at [1.20, 3.40, 5.60] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 85.30. I'm not there yet

    I am ContainerTruck ID4.
	I am at [9.80, 7.60, 5.40] and am heading to [22.30, 33.40, 44.50].
	My load is 0.0 and my max load is 6500.0.
	Distance to my destination is 48.48. I'm not there yet

    I am TankerTruck ID5.
	I am at [100.00, 200.00, 300.00] and am heading to [900.00, 800.00, 700.00].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 1077.03. I'm not there yet

    I am TankerTruck ID6.
	I am at [9.00, 7.00, 5.00] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 18.36. I'm not there yet


2) Loading cargo with weight 2250.0 into each truck:
    Loading Truck ID1 with 2250.0
    After Loading:
      I am StandardCargoTruck ID1.
	I am at [1.10, 2.20, 3.30] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 412.77. I'm not there yet

    Loading Truck ID2 with 4500.0
    After Loading:
      I am StandardCargoTruck ID2.
	I am at [4.40, 5.50, 6.60] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 965.25. I'm not there yet

    Loading Truck ID3 with 1550.0
    *** Error loading ID3: A Container Truck can only be *fully* loaded (8000.0)- not partially loaded (1550.0)
    After Loading:
      I am ContainerTruck ID3.
	I am at [1.20, 3.40, 5.60] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 85.30. I'm not there yet

    Loading Truck ID4 with 6500.0
    After Loading:
      I am ContainerTruck ID4.
	I am at [9.80, 7.60, 5.40] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 48.48. I'm not there yet

    Loading Truck ID5 with 1500.0
    After Loading:
      I am TankerTruck ID5.
	I am at [100.00, 200.00, 300.00] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 1077.03. I'm not there yet

    Loading Truck ID6 with 2250.0
    *** Error loading ID6: Loading 2250.0 at one time exceeds the TankerTruck load rate limit of 2000.0 at a time.
    After Loading:
      I am TankerTruck ID6.
	I am at [9.00, 7.00, 5.00] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 18.36. I'm not there yet


3) Calling 'update' on all trucks & printing final status:
    Before Update:
     I am StandardCargoTruck ID1.
	I am at [1.10, 2.20, 3.30] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 412.77. I'm not there yet
    After Update:
     I am StandardCargoTruck ID1.
	I am at [67.50, 46.17, 24.85] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 330.27. I'm not there yet

    Before Update:
     I am StandardCargoTruck ID2.
	I am at [4.40, 5.50, 6.60] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 965.25. I'm not there yet
    After Update:
     I am StandardCargoTruck ID2.
	I am at [30.38, 37.98, 45.57] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 908.25. I'm not there yet

    Before Update:
     I am ContainerTruck ID3.
	I am at [1.20, 3.40, 5.60] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 85.30. I'm not there yet
    After Update:
     I am ContainerTruck ID3.
	I am at [9.59, 35.31, 61.02] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 20.80. I'm not there yet

    Before Update:
     I am ContainerTruck ID4.
	I am at [9.80, 7.60, 5.40] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 48.48. I'm not there yet
    After Update:
     I am ContainerTruck ID4.
	I am at [22.30, 33.40, 44.50] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 0.00. I am there!

    Before Update:
     I am TankerTruck ID5.
	I am at [100.00, 200.00, 300.00] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 1077.03. I'm not there yet
    After Update:
     I am TankerTruck ID5.
	I am at [152.37, 239.27, 326.18] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 1006.53. I'm not there yet

    Before Update:
     I am TankerTruck ID6.
	I am at [9.00, 7.00, 5.00] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 18.36. I'm not there yet
    After Update:
     I am TankerTruck ID6.
	I am at [19.80, 17.60, 15.40] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 0.00. I am there!


4) Calling 'update' a second time on all trucks & printing final status:
    Before Update:
     I am StandardCargoTruck ID1.
	I am at [67.50, 46.17, 24.85] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 330.27. I'm not there yet
    After Update:
     I am StandardCargoTruck ID1.
	I am at [133.89, 90.14, 46.39] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 247.77. I'm not there yet

    Before Update:
     I am StandardCargoTruck ID2.
	I am at [30.38, 37.98, 45.57] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 908.25. I'm not there yet
    After Update:
     I am StandardCargoTruck ID2.
	I am at [56.37, 70.46, 84.55] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 851.25. I'm not there yet

    Before Update:
     I am ContainerTruck ID3.
	I am at [9.59, 35.31, 61.02] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 20.80. I'm not there yet
    After Update:
     I am ContainerTruck ID3.
	I am at [12.30, 45.60, 78.90] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 0.00. I am there!

    Before Update:
     I am ContainerTruck ID4.
	I am at [22.30, 33.40, 44.50] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 0.00. I am there!
    After Update:
     I am ContainerTruck ID4.
	I am at [22.30, 33.40, 44.50] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 0.00. I am there!

    Before Update:
     I am TankerTruck ID5.
	I am at [152.37, 239.27, 326.18] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 1006.53. I'm not there yet
    After Update:
     I am TankerTruck ID5.
	I am at [204.73, 278.55, 352.37] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 936.03. I'm not there yet

    Before Update:
     I am TankerTruck ID6.
	I am at [19.80, 17.60, 15.40] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 0.00. I am there!
    After Update:
     I am TankerTruck ID6.
	I am at [19.80, 17.60, 15.40] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 0.00. I am there!


5) Unloading 1000.0 of cargo from each truck:
    Unloading Truck I am StandardCargoTruck ID1.
	I am at [133.89, 90.14, 46.39] and am heading to [333.30, 222.20, 111.10].
	My load is 2250.0 and my max load is 10000.0.
	Distance to my destination is 247.77. I'm not there yet
    After Loading:
      I am StandardCargoTruck ID1.
	I am at [133.89, 90.14, 46.39] and am heading to [333.30, 222.20, 111.10].
	My load is 1250.0 and my max load is 10000.0.
	Distance to my destination is 247.77. I'm not there yet

    Unloading Truck I am StandardCargoTruck ID2.
	I am at [56.37, 70.46, 84.55] and am heading to [444.40, 555.50, 666.60].
	My load is 4500.0 and my max load is 8500.0.
	Distance to my destination is 851.25. I'm not there yet
    After Loading:
      I am StandardCargoTruck ID2.
	I am at [56.37, 70.46, 84.55] and am heading to [444.40, 555.50, 666.60].
	My load is 3500.0 and my max load is 8500.0.
	Distance to my destination is 851.25. I'm not there yet

    Unloading Truck I am ContainerTruck ID3.
	I am at [12.30, 45.60, 78.90] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 0.00. I am there!
	***Error occurred in unLoad: A Container Truck can only be *fully* unloaded (8000.0)- not partially unloaded (1000.0)
    After Loading:
      I am ContainerTruck ID3.
	I am at [12.30, 45.60, 78.90] and am heading to [12.30, 45.60, 78.90].
	My load is 0.0 and my max load is 8000.0.
	Distance to my destination is 0.00. I am there!

    Unloading Truck I am ContainerTruck ID4.
	I am at [22.30, 33.40, 44.50] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 0.00. I am there!
	***Error occurred in unLoad: A Container Truck can only be *fully* unloaded (6500.0)- not partially unloaded (1000.0)
    After Loading:
      I am ContainerTruck ID4.
	I am at [22.30, 33.40, 44.50] and am heading to [22.30, 33.40, 44.50].
	My load is 6500.0 and my max load is 6500.0.
	Distance to my destination is 0.00. I am there!

    Unloading Truck I am TankerTruck ID5.
	I am at [204.73, 278.55, 352.37] and am heading to [900.00, 800.00, 700.00].
	My load is 1500.0 and my max load is 8000.0.
	Distance to my destination is 936.03. I'm not there yet
    After Loading:
      I am TankerTruck ID5.
	I am at [204.73, 278.55, 352.37] and am heading to [900.00, 800.00, 700.00].
	My load is 500.0 and my max load is 8000.0.
	Distance to my destination is 936.03. I'm not there yet

    Unloading Truck I am TankerTruck ID6.
	I am at [19.80, 17.60, 15.40] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 0.00. I am there!
	***Error occurred in unLoad: UnLoading 1000.0 will make the load weight negative: 1000.0
    After Loading:
      I am TankerTruck ID6.
	I am at [19.80, 17.60, 15.40] and am heading to [19.80, 17.60, 15.40].
	My load is 0.0 and my max load is 7200.0.
	Distance to my destination is 0.00. I am there!
*/