package stats.scheduling;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import stats.cache.TransactionStatsCache;
import stats.cache.cleaning.CacheCleaning;
import stats.computation.StatsComputation;

@Component
public class StatsCacheComputationSchedule {

	private final TransactionStatsCache cache;
	private final StatsComputation computation;

	final CacheCleaning cleaning;

	@Autowired
	public StatsCacheComputationSchedule(TransactionStatsCache cache, CacheCleaning cleaning, StatsComputation computation) {
		this.cache = cache;
		this.cleaning = cleaning;
		this.computation = computation;
	}

	@Scheduled(initialDelay = 1000, fixedDelay = 1000)
	public void cleanCacheStats() {
		Instant timestamp = Instant.now();
		refreshCache(timestamp);
		computeStatistics(timestamp);
	}

	private void refreshCache(Instant timestamp) {
		cleaning.clean(cache, timestamp);
	}

	private void computeStatistics(Instant timestamp) {
		computation.compute(cache, timestamp);
	}
	
}
