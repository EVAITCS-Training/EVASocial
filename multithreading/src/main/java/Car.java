public class Car extends Thread {
    private String carName;
    private int speed;
    private int odometer;
    private boolean isOn;

    public Car(String name, int speed) {
        this.carName = name;
        this.speed = speed;
        this.odometer = 0;
        this.isOn = false;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    @Override
    public void run() {
        while (isOn) {
            odometer += speed;
            System.out.println(carName + " has traveled " + odometer + " miles");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
