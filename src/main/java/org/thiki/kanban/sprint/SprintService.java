package org.thiki.kanban.sprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.thiki.kanban.foundation.common.date.DateService;
import org.thiki.kanban.foundation.exception.BusinessException;
import org.thiki.kanban.foundation.exception.ResourceNotFoundException;
import org.thiki.kanban.procedure.Procedure;
import org.thiki.kanban.procedure.ProceduresService;

import javax.annotation.Resource;

/**
 * Created by xubt on 04/02/2017.
 */
@Service
public class SprintService {
    public static Logger logger = LoggerFactory.getLogger(SprintService.class);

    @Resource
    private SprintPersistence sprintPersistence;

    @Resource
    private ProceduresService proceduresService;

    @CacheEvict(value = "sprint", key = "contains('#boardId')", allEntries = true)
    public Sprint createSprint(Sprint sprint, String boardId, String userName) {
        logger.info("Creating sprint.sprint:{},boardId:{},userName", sprint, boardId, userName);
        if (sprint.isStartTimeAfterEndTime()) {
            throw new BusinessException(SprintCodes.START_TIME_IS_AFTER_END_TIME);
        }
        boolean isExistUnArchivedSprint = sprintPersistence.isExistUnArchivedSprint(boardId);
        if (isExistUnArchivedSprint) {
            throw new BusinessException(SprintCodes.UNARCHIVE_SPRINT_EXIST);
        }
        sprintPersistence.create(sprint, boardId, userName);
        Sprint createdSprint = sprintPersistence.findById(sprint.getId());
        logger.info("Created sprint:{}", createdSprint);
        return createdSprint;
    }

    @CacheEvict(value = "sprint", key = "contains('#boardId')", allEntries = true)
    public Sprint updateSprint(String sprintId, Sprint sprint, String boardId, String userName) {
        logger.info("Updating sprint.sprintId:{},sprint:{},boardId:{},userName", sprintId, sprint, boardId, userName);
        if (sprint.isStartTimeAfterEndTime()) {
            logger.info("Sprint startTime is after endTime.startTime:{},endTime:{}", sprint.getStartTime(), sprint.getEndTime());
            throw new BusinessException(SprintCodes.START_TIME_IS_AFTER_END_TIME);
        }
        Sprint originSprint = sprintPersistence.findById(sprintId);
        if (originSprint == null) {
            logger.info("No specified sprint was found.sprintId:{}", sprintId);
            throw new BusinessException(SprintCodes.SPRINT_IS_NOT_EXIST);
        }
        if (sprint.isCompleted()) {
            if (originSprint.isCompleted()) {
                logger.info("Sprint was already archived.sprintId:{}", sprintId);
                throw new BusinessException(SprintCodes.SPRINT_ALREADY_ARCHIVED);
            }
            sprint.setCompetedTime(DateService.instance().getNow_EN());
            Procedure archivedProcedure = proceduresService.archive(originSprint, boardId, userName);
            sprint.setArchiveId(archivedProcedure.getId());
        }
        sprintPersistence.update(sprintId, sprint, boardId);
        Sprint savedSprint = sprintPersistence.findById(sprintId);
        logger.info("Saved sprint:{}", savedSprint);
        return savedSprint;
    }

    @Cacheable(value = "sprint", key = "'activeSprint'+#boardId")
    public Sprint loadActiveSprint(String boardId, String userName) {
        logger.info("Loading active sprint.boardId:{},userName", boardId, userName);
        Sprint activeSprint = sprintPersistence.loadActiveSprint(boardId);
        if (activeSprint == null) {
            logger.info("No active sprint was found.boardId:{}", boardId);
            throw new ResourceNotFoundException(SprintCodes.ACTIVE_SPRINT_IS_NOT_FOUND);
        }
        logger.info("Active sprint:{}", activeSprint);
        return activeSprint;
    }
}