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
    private TimeUnit oldTimeUnit;
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
        System.out.println(this.oldTimeUnit);
    }

    public License(FunctionType type, int limit, TimeUnit timeUnit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Limit value cannot be negative.");
        }
        if (type == FunctionType.THROUGHPUT) {
            throughputOn = true;
            this.throughputLimit = limit;
            this.timeUnit = timeUnit;
            oldTimeUnit = timeUnit;
            currentThroughput = 0;
            previousThroughput = 0;
            lastTime = 0;
        } else if (type == FunctionType.CAPACITY) {
            capacityOn = true;
            this.capacityLimit = limit;
            currentCapacity = 0;
            previousCapacity = 0;
        } else {
            throw new IllegalArgumentException("Missing parameter, both throughput limit and capacity limit are required.");
        }
    }

    public License(FunctionType type, int limit) {
        this(type, limit, defaultTimeUnit);
    }


    public License(FunctionType type, int capacityLimit, int throughputLimit, TimeUnit timeUnit) {
        if (throughputLimit < 0 || capacityLimit < 0) {
            throw new IllegalArgumentException("Limit value cannot be negative.");
        }
        if (type == FunctionType.BOTH) {
            capacityOn = true;
            this.capacityLimit = capacityLimit;
            currentCapacity = 0;
            previousCapacity = 0;

            throughputOn = true;
            this.throughputLimit = throughputLimit;
            this.timeUnit = timeUnit;
            oldTimeUnit = timeUnit;
            currentThroughput = 0;
            previousThroughput = 0;
            lastTime = 0;
        } else {
            throw new IllegalArgumentException("Too many parameters, consider change the FunctionType or remove a parameter.");
        }
    }

    public License(FunctionType type, int capacityLimit, int throughputLimit) {
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
        if (oldTimeUnit == null) {
            oldTimeUnit = timeUnit;
        } else {
            oldTimeUnit = this.timeUnit;
        }
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

    public void clear(FunctionType type) {
        switch (type) {
            case CAPACITY:
                currentCapacity = 0;
                break;
            case THROUGHPUT:
                currentCapacity = 0;
                break;
            default:
                currentCapacity = 0;
                currentCapacity = 0;
        }
    }

    private boolean increaseCapacity() {
        currentCapacity++;
        return currentCapacity <= capacityLimit;
    }

    private boolean increaseThroughput() {
        long nowTime = 0;
        if (timeUnit == oldTimeUnit) {
            nowTime = System.currentTimeMillis() / timeUnit.getValue();
        } else {
            lastTime = lastTime * oldTimeUnit.getValue() / timeUnit.getValue();
            nowTime = System.currentTimeMillis() / timeUnit.getValue();
            oldTimeUnit = timeUnit;
        }
        if (nowTime != lastTime) {
            lastTime = nowTime;
            currentThroughput = 1;
            return true;
        }
        lastTime = nowTime;
        currentThroughput++;
        return currentThroughput <= throughputLimit;
    }

    public enum FunctionType {CAPACITY, THROUGHPUT, BOTH}

    public enum Availability {AVAILABLE, CAPACITYEXCEEDED, THROUGHPUTEXCEEDED, BOTHEXCEEDED}

    public enum TimeUnit {
        MILLISECONDS(1),
        SECONDS(1000),
        MINUTES(1000 * 60),
        HOURS(1000 * 60 * 60),
        DAYS(1000 * 60 * 60 * 24),
        WEEKS(1000 * 60 * 60 * 24 * 7);
        private final long value;

        TimeUnit(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }

}
