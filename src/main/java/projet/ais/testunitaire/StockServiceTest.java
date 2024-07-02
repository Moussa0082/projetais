// package projet.ais.testunitaire;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;

// import projet.ais.repository.StockRepository;
// import projet.ais.services.StockService;

// @SpringBootTest
// public class StockServiceTest {

//     @Autowired
//     private StockService stockService;

//     @MockBean
//     private StockRepository stockRepository;

//     @Test
//     public void testGetAllStocksPageableByPays() {
//         // Configurer les données de test
//         String pays = "France";
//         Pageable pageable = PageRequest.of(0, 10);
//         List<Stock> stocks = Arrays.asList(new Stock(), new Stock());
//         Page<Stock> stockPage = new PageImpl<>(stocks, pageable, stocks.size());

//         // Simuler le comportement du repository
//         Mockito.when(stockRepository.findAllByStatutSotckTrueAndPaysAndActeurStatutActeurTrue(
//                 Mockito.eq(pays.toLowerCase()), Mockito.any(Pageable.class)))
//                 .thenReturn(stockPage);

//         // Appeler la méthode à tester
//         Page<Stock> result = stockService.getAllStocksPageableByPays(pays, pageable);

//         // Vérifier les résultats
//         assertNotNull(result);
//         assertEquals(2, result.getContent().size());
//     }
// }

