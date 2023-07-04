package slash.financing.config;

// @Component
// @RequiredArgsConstructor
// @DependsOn("defaultCategoriesInitializer")
// public class DefaultUsersInitializer implements CommandLineRunner {
// private final UserRepository userRepository;
// private final BudgetCategoryRepository budgetCategoryRepository;
// private final PasswordEncoder passwordEncoder;

// @Override
// public void run(String... args) {
// // Check if default categories already exist
// if (userRepository.count() == 0) {
// // Create and save the default categories
// createAndSaveDefaultCategories();
// }
// }

// private void createAndSaveDefaultCategories() {
// Set<BudgetCategory> budgetCategories = new HashSet<>();

// System.out.println(budgetCategories);

// User user1 = new User(UUID.randomUUID(), UserRole.USER, "John",
// "john@example.com",
// passwordEncoder.encode("password1"), true, budgetCategories);
// User user2 = new User(UUID.randomUUID(), UserRole.USER, "Jane",
// "jane@example.com",
// passwordEncoder.encode("password2"), true, budgetCategories);
// User user3 = new User(UUID.randomUUID(), UserRole.ADMIN, "Ryan",
// "gosling@example.com",
// passwordEncoder.encode("password3"), true, budgetCategories);
// User user4 = new User(UUID.randomUUID(), UserRole.USER, "Papzan",
// "papzan@example.com",
// passwordEncoder.encode("password4"), true, budgetCategories);
// User user5 = new User(UUID.randomUUID(), UserRole.ADMIN, "Danik",
// "danik@example.com",
// passwordEncoder.encode("danik12345"), true, budgetCategories);

// userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
// }
// }
