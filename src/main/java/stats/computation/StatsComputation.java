package stats.computation;

import java.time.Instant;

import stats.cache.TransactionStatsCache;
import stats.model.TransStats;

public interface StatsComputation {

	void compute(TransactionStatsCache cache, Instant timestamp);

	TransStats getComputedStats();
}
