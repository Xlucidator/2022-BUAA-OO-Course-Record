public class Shutter {
    private static final Shutter SHUTTER = new Shutter();
    private final Shutter self = this;
    private final Thread thread = new Thread() {
        @Override
        public void run() {
            while (true) {
                synchronized (self) {
                    if (count == 0) {
                        boolean out = false;
                        synchronized (MultiBuilding.getUnhandled()) {
                            if (!MultiBuilding.getUnhandled().isEmpty()) {
                                out = true;
                            }
                        }
                        if (!out) {
                            MultiBuilding multiBuilding = MultiBuilding.getMultiBuilding();
                            for (Building building : multiBuilding.getBuildings()) {
                                building.turnDown();
                            }
                            for (Building building : multiBuilding.getFloors()) {
                                building.turnDown();
                            }
                            multiBuilding.turnDownHandler();
                            break;
                        }
                    }
                    try {
                        self.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private int count = 0;

    public Shutter() {
        thread.start();
    }

    public static Shutter getShutter() {
        return SHUTTER;
    }

    public int getCount() {
        return count;
    }

    public void decrease() {
        --count;
    }

    public void increase() {
        ++count;
    }
}
