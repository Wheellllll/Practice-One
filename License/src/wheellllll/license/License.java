package wheellllll.license;

public class License {

    private static TimeUnit defaultTimeUnit = TimeUnit.SECONDS;
    private boolean capacityOn;
    private int capacityLimit;
    private int currentCapacity;
    private int previousCapacity;
    private boolean throughputOn;
    private int throughputLimit;
    private TimeUnit timeUnit;
    private int currentThroughput;
    private int previousThroughput;
    private long lastTime;

    public License() {
        capacityOn = false;
        capacityLimit = 0;
        currentCapacity = 0;
        previousCapacity = 0;

        throughputOn = false;
        throughputLimit = 0;
        currentThroughput = 0;
        previousThroughput = 0;
        lastTime = 0;
    }

    public License(LicenseType type, int limit, TimeUnit timeUnit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit value cannot be negative.");
        }
        if (type == LicenseType.THROUGHPUT) {
            throughputOn = true;
            this.throughputLimit = limit;
            this.timeUnit = timeUnit;
            currentThroughput = 0;
            previousThroughput = 0;
            lastTime = 0;
        } else if (type == LicenseType.CAPACITY) {
            capacityOn = true;
            this.capacityLimit = limit;
            currentCapacity = 0;
            previousCapacity = 0;
        } else {
            throw new IllegalArgumentException("Missing parameter, both throughput limit and capacity limit are required.");
        }
    }

    public License(LicenseType type, int limit) {
        this(type, limit, defaultTimeUnit);
    }


    public License(LicenseType type, int capacityLimit, int throughputLimit, TimeUnit timeUnit) {
        if (throughputLimit < 0 || capacityLimit < 0) {
            throw new IllegalArgumentException("Limit value cannot be negative.");
        }
        if (type == LicenseType.BOTH) {
            capacityOn = true;
            this.capacityLimit = capacityLimit;
            currentCapacity = 0;
            previousCapacity = 0;

            throughputOn = true;
            this.throughputLimit = throughputLimit;
            this.timeUnit = timeUnit;
            currentThroughput = 0;
            previousThroughput = 0;
            lastTime = 0;
        } else {
            throw new IllegalArgumentException("Too many parameters, consider change the LicenseType or remove a parameter.");
        }
    }

    public License(LicenseType type, int capacityLimit, int throughputLimit) {
        this(type, capacityLimit, throughputLimit, defaultTimeUnit);
    }

    public static TimeUnit getDefaultTimeUnit() {
        return defaultTimeUnit;
    }

    public static void setDefaultTimeUnit(TimeUnit defaultTimeUnit) {
        License.defaultTimeUnit = defaultTimeUnit;
    }

    public void enableCapacity(int capacityLimit, boolean maintain) {
        capacityOn = true;
        this.capacityLimit = capacityLimit;
        currentCapacity = maintain ? previousCapacity : 0;
    }

    public void enableThroughput(int throughputLimit, boolean maintain, TimeUnit timeUnit) {
        throughputOn = true;
        this.throughputLimit = throughputLimit;
        this.timeUnit = timeUnit;
        currentThroughput = maintain ? previousThroughput : 0;
    }

    public void enableThroughput(int throughputLimit, boolean maintain) {
        enableThroughput(throughputLimit, maintain, defaultTimeUnit);
    }


    public void disableCapacity() {
        capacityOn = false;
        previousCapacity = currentCapacity;
    }

    public void disableThroughput() {
        throughputOn = false;
        previousThroughput = currentThroughput;
    }

    public Availability use() {
        boolean capacityAvailability = false, throughputAvailability = false;
        if (capacityOn && throughputOn) {
            capacityAvailability = increaseCapacity();
            throughputAvailability = increaseThroughput();
            if (capacityAvailability && throughputAvailability)
                return Availability.AVAILABLE;
            else if (capacityAvailability)
                return Availability.THROUGHPUTEXCEEDED;
            else if (throughputAvailability)
                return Availability.CAPACITYEXCEEDED;
            else
                return Availability.BOTHEXCEEDED;
        } else if (capacityOn) {
            capacityAvailability = increaseCapacity();
            if (capacityAvailability)
                return Availability.AVAILABLE;
            else
                return Availability.CAPACITYEXCEEDED;
        } else if (throughputOn) {
            throughputAvailability = increaseThroughput();
            if (throughputAvailability)
                return Availability.AVAILABLE;
            else
                return Availability.THROUGHPUTEXCEEDED;
        } else {
            return Availability.AVAILABLE;
        }
    }

    public void reset(LicenseType type) {
        switch (type) {
            case CAPACITY:
                currentCapacity = 0;
                break;
            case THROUGHPUT:
                currentThroughput = 0;
                break;
            default:
                currentCapacity = 0;
                currentThroughput = 0;
        }
    }

    private boolean increaseCapacity() {
        currentCapacity++;
        return currentCapacity <= capacityLimit;
    }

    private boolean increaseThroughput() {
        long nowTime = 0;
        nowTime = System.currentTimeMillis();
        if (lastTime / timeUnit.getValue() != nowTime / timeUnit.getValue()) {
            lastTime = nowTime;
            currentThroughput = 1;
            return true;
        }
        lastTime = nowTime;
        currentThroughput ++;
        return currentThroughput <= throughputLimit;
    }

    public enum LicenseType {CAPACITY, THROUGHPUT, BOTH}

    public enum Availability {AVAILABLE, CAPACITYEXCEEDED, THROUGHPUTEXCEEDED, BOTHEXCEEDED}

    public enum TimeUnit {
        MILLISECONDS(1),
        SECONDS(1000),
        MINUTES(1000 * 60),
        HOURS(1000 * 60 * 60),
        DAYS(1000 * 60 * 60 * 24);
        private final long value;

        TimeUnit(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

}
