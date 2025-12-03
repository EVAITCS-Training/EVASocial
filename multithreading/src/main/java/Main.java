import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Car> cars = new ArrayList<>();
        while (true) {
            System.out.println("""
                    +------------------------+
                    |     Cars Travelling    |
                    +------------------------+
                    | 1. Create Car          |
                    | 2. Start a Car         |
                    | 3. Stop a Car          |
                    | 4. Exit                |
                    +------------------------+
                    """);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.println("Please enter a car name");
                    String carName = scanner.nextLine();
                    System.out.println("Please enter a starting speed");
                    int speed = scanner.nextInt();
                    Car car = new Car(carName, speed);
                    cars.add(car);
                    System.out.println("Car created and stored");
                    break;
                case 2:
                    scanner.nextLine();
                    AtomicInteger count = new AtomicInteger(0);
                    cars.forEach(c -> {
                        System.out.println(count.get() + " " + c);
                    });
                    count.getAndIncrement();
                    System.out.println("please select the car to start");
                    int carSelection = scanner.nextInt();
                    if(carSelection < cars.size()){
                        car = cars.get(carSelection);
                        car.setOn(true);
                        car.start();
                        System.out.println("Car " + car.getCarName()+ " has started");
                    } else {
                        System.out.println("Car was never created");
                    }
                    break;
                case 3:
                    scanner.nextLine();
                    count = new AtomicInteger(0);
                    cars.forEach(c -> {
                        System.out.println(count.get() + " " + c);
                    });
                    count.getAndIncrement();
                    System.out.println("please select the car to stop");
                    carSelection = scanner.nextInt();
                    if(carSelection < cars.size()){
                        car = cars.get(carSelection);
                        car.setOn(false);
                        System.out.println("Car has stopped at " + car.getOdometer());
                    } else {
                        System.out.println("Car was never created");
                    }
                    break;
                case 4:
                    cars.forEach(Thread::interrupt);
                    scanner.close();
                    System.exit(0);
            }
        }
    }
}
