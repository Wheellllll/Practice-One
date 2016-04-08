package wheellllll.license;
/**
 * This class provide two license functions, capacity and throughput.
 */
public class License {

    /**
     * The default time unit for limit value of throughput, it can be change by method <code>setDefaultTimeUnit</code>.
     */
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

    /**
     * Constructor for single functional type, if use capacity function only or not set the time
     * unit, the time unit will be set as the default value.
     * @param type Functional type of <code>License</code>
     * @param limit Limit value for the license
     * @param timeUnit Time unit value for throughput function, the default time unit
     *                 is <code>TimeUnit.SECONDS</code> without change
     */
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

    /**
     * Brief constructor for single function type
     * @param type Functional type of <code>License</code>
     * @param limit Limit value for the license
     */
    public License(LicenseType type, int limit) {
        this(type, limit, defaultTimeUnit);
    }


    /**
     * Constructor for full functions, if not set the time
     * unit, the time unit will be set as the default value.
     * @param type Functional type of <code>License</code>
     * @param capacityLimit Limit value for capacity
     * @param throughputLimit Limit value for throughput
     * @param timeUnit Time unit value for throughput function, the default time unit
     *                 is <code>TimeUnit.SECONDS</code> without change
     */
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

    /**
     * Brief constructor for full functions.
     * @param type Functional type of <code>License</code>
     * @param capacityLimit Limit value for capacity
     * @param throughputLimit Limit value for throughput
     */
    public License(LicenseType type, int capacityLimit, int throughputLimit) {
        this(type, capacityLimit, throughputLimit, defaultTimeUnit);
    }

    /**
     * Get the default time unit.
     */
    public static TimeUnit getDefaultTimeUnit() {
        return defaultTimeUnit;
    }

    /**
     * Set the default time unit.
     */
    public static void setDefaultTimeUnit(TimeUnit defaultTimeUnit) {
        License.defaultTimeUnit = defaultTimeUnit;
    }

    /**
     * Enable the capacity function.
     * @param capacityLimit Limit value for capacity
     * @param maintain This value reflect whether continue from the old place. If set as True, the initial
     *                 capacity value will be set to previous capacity value, otherwise it will be set to 0
     */
    public void enableCapacity(int capacityLimit, boolean maintain) {
        capacityOn = true;
        this.capacityLimit = capacityLimit;
        currentCapacity = maintain ? previousCapacity : 0;
    }

    /**
     * Enable the throughput function.
     * @param throughputLimit Limit value for throughput
     * @param maintain This value reflect whether continue from the old place. If set as True, the initial
     *                 throughput value will be set to previous throughput value, otherwise it will be set to 0
     * @param timeUnit Time unit for throughput function
     */
    public void enableThroughput(int throughputLimit, boolean maintain, TimeUnit timeUnit) {
        throughputOn = true;
        this.throughputLimit = throughputLimit;
        this.timeUnit = timeUnit;
        currentThroughput = maintain ? previousThroughput : 0;
    }

    /**
     * Brief form to enable the throughput function.
     */
    public void enableThroughput(int throughputLimit, boolean maintain) {
        enableThroughput(throughputLimit, maintain, defaultTimeUnit);
    }


    /**
     * Disable the capacity function.
     */
    public void disableCapacity() {
        capacityOn = false;
        previousCapacity = currentCapacity;
    }

    /**
     * Disable the throughput function.
     */
    public void disableThroughput() {
        throughputOn = false;
        previousThroughput = currentThroughput;
    }

    /**
     * This method is called where license usage is needed.
     * @return Availability represent whether the license is still available. <code>AVAILABLE</code> represent
     * it is available, <code>CAPACITYEXCEEDED</code> represent capacity reaches limit,
     * <code>THROUGHPUTEXCEEDED</code> represent throughput reaches limit, <code>BOTHEXCEEDED</code> represent
     * both reaches limit.
     * @see Availability
     */
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

    /**
     * Reset the current license value.
     * @param type Type of this license. If type is <code>LicenseType.BOTH</code>, both capacity value and
     *             throughput value will be set to 0, otherwise the chosen target will be set to 0.
     */
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

    /**
     * This method increase one to capacity value, and return differently for different condition.
     * @return  Return True if the capacity license is still available, otherwise False.
     */
    private boolean increaseCapacity() {
        currentCapacity++;
        return currentCapacity <= capacityLimit;
    }

    /**
     * This method increase one to throughput value, and return differently for different condition.
     * @return  Return True if the throughput license is still available, otherwise False.
     */
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

    /**
     * Enum for license functional type.
     */
    public enum LicenseType {CAPACITY, THROUGHPUT, BOTH}

    /**
     * Enum for availability.
     */
    public enum Availability {AVAILABLE, CAPACITYEXCEEDED, THROUGHPUTEXCEEDED, BOTHEXCEEDED}

    /**
     * Enum for time unit, every enum has a value. value of <code>MILLISECONDS</code> set to 1,
     * other unit value set to the ratio of itself and millisecond.
     */
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
