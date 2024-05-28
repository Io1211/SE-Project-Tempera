package at.qe.skeleton.rest.frontend.payload.response;

import at.qe.skeleton.rest.frontend.dtos.AccumulatedTimeDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleGroupDto;
import at.qe.skeleton.rest.frontend.dtos.SimpleProjectDto;

import java.util.List;

/**
 * Response payload for accumulated time data.
 *
 * @param accumulatedTimes All time records needed to calculate the accumulated time
 * @param availableProjects The projects that a manager/group leader is assigned to
 * @param availableGroups The groups that a manager/group leader is assigned to
 */
public record AccumulatedTimeResponse(
        List<AccumulatedTimeDto> accumulatedTimes,
        List<SimpleProjectDto> availableProjects,
        List<SimpleGroupDto> availableGroups) {}
