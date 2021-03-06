package org.thiki.kanban.worktile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thiki.kanban.TestBase;
import org.thiki.kanban.foundation.annotations.Domain;
import org.thiki.kanban.foundation.annotations.Scenario;
import org.thiki.kanban.foundation.application.DomainOrder;
import org.thiki.kanban.foundation.common.date.DateService;
import org.thiki.kanban.foundation.common.date.DateStyle;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;

/**
 * Created by xubt on 11/1/16.
 */
@Domain(order = DomainOrder.WORKTILE, name = "Worktile")
@RunWith(SpringJUnit4ClassRunner.class)
public class WorktileControllerTest extends TestBase {
    @Resource
    private DateService dateService;

    @Scenario("数据导入>数据导入时,新建worktile board")
    @Test
    public void createNewBoard() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks.json");
        String expectedWorktileName = "worktile" + dateService.DateToString(new Date(), DateStyle.YYYY_MM_DD);
        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(201)
                .body("id", equalTo("fooId"))
                .body("name", equalTo(expectedWorktileName))
                .body("author", equalTo("someone"))
                .body("creationTime", notNullValue())
                .body("_links.all.href", endsWith("/someone/projects/project-fooId/boards"))
                .body("_links.self.href", endsWith("/someone/projects/project-fooId/boards/fooId"));

        assertEquals(expectedWorktileName, jdbcTemplate.queryForObject("select name from kb_board where author='someone'", String.class));
    }

    @Scenario("数据导入>新建board后,导入entry")
    @Test
    public void importEntry() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks.json");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(201)
                .body("id", equalTo("fooId"))
                .body("author", equalTo("someone"))
                .body("creationTime", notNullValue())
                .body("_links.all.href", endsWith("/someone/projects/project-fooId/boards"))
                .body("_links.self.href", endsWith("/someone/projects/project-fooId/boards/fooId"));

        assertEquals("Product Backlog", jdbcTemplate.queryForObject("select title from kb_stage where board_id='fooId'", String.class));
    }

    @Scenario("数据导入>新建board后,导入tasks")
    @Test
    public void importCards() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks.json");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(201)
                .body("id", equalTo("fooId"))
                .body("author", equalTo("someone"))
                .body("creationTime", notNullValue())
                .body("_links.all.href", endsWith("/someone/projects/project-fooId/boards"))
                .body("_links.self.href", endsWith("/someone/projects/project-fooId/boards/fooId"));

        assertEquals("任务名称", jdbcTemplate.queryForObject("select summary from kb_card where author='someone'", String.class));
    }

    @Scenario("数据导入>新建tasks后,导入todos")
    @Test
    public void importTodos() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks.json");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(201)
                .body("id", equalTo("fooId"))
                .body("author", equalTo("someone"))
                .body("creationTime", notNullValue())
                .body("_links.all.href", endsWith("/someone/projects/project-fooId/boards"))
                .body("_links.self.href", endsWith("/someone/projects/project-fooId/boards/fooId"));

        assertEquals("todo", jdbcTemplate.queryForObject("select summary from kb_acceptance_criterias where card_id='fooId' AND author='someone'", String.class));
        assertEquals("TRUE", jdbcTemplate.queryForObject("select finished from kb_acceptance_criterias where card_id='fooId' AND author='someone'", String.class));
    }

    @Scenario("数据导入>如果文件长度为空,则抛出异常")
    @Test
    public void throwExceptionIfFileContentIsEmpty() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks_empty.json");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(400)
                .body("code", equalTo(WorktileCodes.FILE_IS_EMPTY.code()))
                .body("message", equalTo(WorktileCodes.FILE_IS_EMPTY.message()));
    }

    @Scenario("数据导入>如果文件内容格式错误,则抛出异常")
    @Test
    public void throwExceptionIfFileContentFormatIsInvalid() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks_format_error.json");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(400)
                .body("code", equalTo(WorktileCodes.FILE_CONTENT_FORMAT_INVALID.code()))
                .body("message", equalTo(WorktileCodes.FILE_CONTENT_FORMAT_INVALID.message()));
    }

    @Scenario("数据导入>如果文件类型错误,则抛出异常")
    @Test
    public void throwExceptionIfFileFormatIsInvalid() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks_invalid_file_format.jpg");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(400)
                .body("code", equalTo(WorktileCodes.FILE_TYPE_INVALID.code()))
                .body("message", equalTo(WorktileCodes.FILE_TYPE_INVALID.message()));
    }

    @Scenario("数据导入>如果文件没有拓展名,则抛出异常")
    @Test
    public void throwExceptionIfNoExtensionNameWasFound() {
        File worktileTasks = new File("src/test/resources/import/worktile_tasks");

        given().header("userName", "someone")
                .multiPart("worktileTasks", worktileTasks)
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(400)
                .body("code", equalTo(WorktileCodes.FILE_NAME_INVALID.code()))
                .body("message", equalTo(WorktileCodes.FILE_NAME_INVALID.message()));
    }

    @Scenario("数据导入>如果未上传文件,则抛出异常")
    @Test
    public void throwExceptionIfNoFileWasUpload() {
        given().header("userName", "someone")
                .when()
                .post("/someone/projects/project-fooId/worktileTasks")
                .then()
                .statusCode(400)
                .body("code", equalTo(WorktileCodes.FILE_IS_UN_UPLOAD.code()))
                .body("message", equalTo(WorktileCodes.FILE_IS_UN_UPLOAD.message()));
    }
}
