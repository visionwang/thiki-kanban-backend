package org.thiki.kanban.teamMembers;

import org.springframework.stereotype.Service;
import org.thiki.kanban.team.Team;
import org.thiki.kanban.team.TeamsService;

import javax.annotation.Resource;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by 濤 on 7/26/16.
 */
@Service
public class TeamMembersService {
    @Resource
    private TeamMembersPersistence teamMembersPersistence;
    @Resource
    private TeamsService teamsService;


    public List<TeamMember> loadAll() {
        return teamMembersPersistence.loadAll();
    }

    public TeamMember joinTeam(String teamId, final TeamMember teamMember, String userName) {
        Team targetTeam = teamsService.findById(teamId);
        if (targetTeam == null) {
            throw new InvalidParameterException(MessageFormat.format("Team {0} is not found.", teamId));
        }
        teamMember.setReporter(userName);
        teamMember.setTeamId(teamId);
        teamMembersPersistence.joinTeam(teamMember);
        return teamMembersPersistence.findById(teamMember.getId());
    }

    public TeamMember findById(String id) {
        return teamMembersPersistence.findById(id);
    }
}
