package test;

class CarInitTest {
 /*
    private static List<Car> fullList;
    private static RandomInputStrategy strategy;
    @BeforeAll
    static void init() throws Exception {
        strategy = new RandomInputStrategy(100);
        fullList = new ArrayList<>(strategy.getCars());
    }
    @Test
    void testRandomInit() throws Exception {
        RandomInputStrategy smallStrategy = new RandomInputStrategy(8);
        List<Car> randomCars = smallStrategy.getCars();
        assertEquals(8, randomCars.size());

        assertTrue(fullList.containsAll(randomCars));

        Set<Car> set = new HashSet<>(randomCars);
        assertEquals(randomCars.size(), set.size(), "В выборке есть дубликаты");
    }
    @Test
    void testCarReader() throws Exception {
        List<Car> carsFromFile = CarReader.readCarsFromFile("randomCarsFile.txt",8);
        assertEquals(8, carsFromFile.size());
        assertTrue(fullList.containsAll(carsFromFile));

        Set<Car> set = new HashSet<>(carsFromFile);
        assertEquals(carsFromFile.size(), set.size(), "В выборке есть дубликаты");
    } */
}