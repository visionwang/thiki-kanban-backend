package org.thiki.kanban.sprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xubt on 04/02/2017.
 */
@Service
public class SprintService {
    public static Logger logger = LoggerFactory.getLogger(SprintService.class);

    @Resource
    private SprintPersistence sprintPersistence;

    @CacheEvict(value = "sprint", key = "contains('#boardId')", allEntries = true)
    public Sprint createSprint(Sprint sprint, String boardId, String userName) {
        logger.info("Creating sprint.sprint:{},boardId:{},userName", sprint, boardId, userName);
        sprintPersistence.create(sprint, userName, boardId);
        Sprint createdSprint = sprintPersistence.findById(sprint.getId());
        logger.info("Created sprint:{}", createdSprint);
        return createdSprint;
    }
}
