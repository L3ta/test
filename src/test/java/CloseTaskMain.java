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
    public void setUp() {
        userManager = new UserManager();
    }

    @Step("Executing closeTaskTest")
    @Test
    public void closeTaskTest() {
        String ownerId = userManager.createUser(); // Create user and get userId

        if (ownerId != null) {
            ProjectManager projectManager = new ProjectManager();
            String projectId = projectManager.createProject(ownerId); // Create project and get projectId

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
                    apiAuth.deleteTask(taskId); // Delete task
                    apiAuth.deleteProject(projectId); // Delete project
                }

                // Close the browser
                loginManager.quit();
            }

            // Delete user via API
            userManager.deleteUser(); // Delete user
        } else {
            System.out.println("User creation failed. Aborting.");
        }
    }

    @AfterClass
    public void tearDown() {
        // Any cleanup code if needed
    }
}
