import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.example.CloseTask;
import org.example.KanboardApiAuth;
import org.example.ProjectManager;
import org.example.UserManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CloseTaskMain {

    private UserManager userManager;

    @BeforeClass
    @Step("Ініціалізація користувача")
    public void setUp() {
        userManager = new UserManager();
    }

    @Test
    @Description("Тест на закриття задачі")
    @Step("Запуск тесту на закриття задачі")
    public void closeTaskTest() {
        String ownerId = createUser(); // Create user and get userId

        if (ownerId != null) {
            String projectId = createProject(ownerId); // Create project and get projectId

            if (projectId != null) {
                CloseTask loginManager = new CloseTask();

                // Log in to Kanboard
                loginManager.loginToKanboard(userManager.getUsername(), userManager.getPassword(), "chrome"); // or "firefox"

                // Go to project and create task
                loginManager.goToProject();
                loginManager.createTask();

                // Get taskId and delete task via API
                String taskId = loginManager.getCreatedTaskId();
                if (taskId != null) {
                    KanboardApiAuth apiAuth = new KanboardApiAuth(userManager.getUsername(), userManager.getPassword());
                    deleteTask(apiAuth, taskId); // Delete task
                    deleteProject(apiAuth, projectId); // Delete project
                }

                // Close the browser
                loginManager.quit();
            }

            // Delete user via API
            deleteUser(); // Delete user
        } else {
            System.out.println("User creation failed. Aborting.");
        }
    }

    @Step("Створення користувача")
    private String createUser() {
        return userManager.createUser(); // Create user and get userId
    }

    @Step("Створення проекту з ідентифікатором власника: {ownerId}")
    private String createProject(String ownerId) {
        ProjectManager projectManager = new ProjectManager();
        return projectManager.createProject(ownerId); // Create project and get projectId
    }

    @Step("Видалення задачі з ідентифікатором: {taskId}")
    private void deleteTask(KanboardApiAuth apiAuth, String taskId) {
        apiAuth.deleteTask(taskId); // Delete task
    }

    @Step("Видалення проекту з ідентифікатором: {projectId}")
    private void deleteProject(KanboardApiAuth apiAuth, String projectId) {
        apiAuth.deleteProject(projectId); // Delete project
    }

    @Step("Видалення користувача")
    private void deleteUser() {
        userManager.deleteUser(); // Delete user
    }

    @AfterClass
    @Step("Очистка після тесту")
    public void tearDown() {
        // Any cleanup code if needed
    }
}
