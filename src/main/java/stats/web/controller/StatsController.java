package stats.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import stats.computation.StatsComputation;
import stats.model.TransStats;

@RestController
public class StatsController {

	private final StatsComputation statsComputation;

	@Autowired
	public StatsController(StatsComputation statsComputation) {
		this.statsComputation = statsComputation;
	}

	@GetMapping(path = "/statistics", produces = "application/json")
	public TransStats getStats() {
		return statsComputation.getComputedStats();
	}

}
