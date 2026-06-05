package com.example;

import com.example.model.*;
import com.example.repository.ReservationRepository;
import com.example.repository.ReservationRepositoryImpl;
import com.example.repository.SalleRepository;
import com.example.repository.SalleRepositoryImpl;
import com.example.service.ReservationService;
import com.example.service.ReservationServiceImpl;
import com.example.service.SalleService;
import com.example.service.SalleServiceImpl;
import com.example.test.TestScenarios;
import com.example.util.DataInitializer;
import com.example.util.DatabaseMigrationTool;
import com.example.util.PerformanceReport;

import com.example.util.CustomPersistenceUnitInfo;
import org.hibernate.jpa.HibernatePersistenceProvider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.*;

public class App {
    public static void main(String[] args) {
        System.out.println("=== APPLICATION DE RÉSERVATION DE SALLES ===");

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.driver", "org.h2.Driver");
        properties.put("javax.persistence.jdbc.url", "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        properties.put("javax.persistence.jdbc.user", "sa");
        properties.put("javax.persistence.jdbc.password", "");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.cache.use_second_level_cache", "true");
        properties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.put("hibernate.cache.use_query_cache", "true");
        properties.put("hibernate.generate_statistics", "true");

        List<String> classes = Arrays.asList(
                "com.example.model.Salle",
                "com.example.model.Equipement",
                "com.example.model.Reservation",
                "com.example.model.Utilisateur"
        );

        CustomPersistenceUnitInfo persistenceUnitInfo = new CustomPersistenceUnitInfo("gestion-reservations", classes, properties);
        EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, null);
        EntityManager em = emf.createEntityManager();

        try {
            // Initialisation des repositories et services
            SalleRepository salleRepository = new SalleRepositoryImpl(em);
            SalleService salleService = new SalleServiceImpl(em, salleRepository);

            ReservationRepository reservationRepository = new ReservationRepositoryImpl(em);
            ReservationService reservationService = new ReservationServiceImpl(em, reservationRepository);

            // Menu principal
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n=== MENU PRINCIPAL ===");
                System.out.println("1. Initialiser les données de test");
                System.out.println("2. Exécuter les scénarios de test");
                System.out.println("3. Exécuter le script de migration");
                System.out.println("4. Générer un rapport de performance");
                System.out.println("5. Quitter");
                System.out.print("Votre choix: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choice) {
                    case 1:
                        // Initialiser les données de test
                        DataInitializer dataInitializer = new DataInitializer(emf);
                        dataInitializer.initializeData();
                        break;

                    case 2:
                        // Exécuter les scénarios de test
                        TestScenarios testScenarios = new TestScenarios(emf, salleService, reservationService);
                        testScenarios.runAllTests();
                        break;

                    case 3:
                        // Exécuter le script de migration
                        System.out.println("Cette fonctionnalité nécessite une base de données externe.");
                        System.out.print("Voulez-vous continuer avec une simulation? (o/n): ");
                        String confirm = scanner.nextLine();

                        if (confirm.equalsIgnoreCase("o")) {
                            System.out.println("Simulation de la migration...");
                            System.out.println("Dans un environnement réel, utilisez la classe DatabaseMigrationTool.");
                            System.out.println("Exemple: DatabaseMigrationTool.main(args);");
                        }
                        break;

                    case 4:
                        // Générer un rapport de performance
                        PerformanceReport performanceReport = new PerformanceReport(emf);
                        performanceReport.runPerformanceTests();
                        break;

                    case 5:
                        // Quitter
                        exit = true;
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }

        } finally {
            em.close();
            emf.close();
        }
    }
}
