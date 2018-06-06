package stats.computation;

import java.time.Instant;
import java.util.Collection;

import org.springframework.stereotype.Service;

import stats.cache.TransactionStatsCache;
import stats.model.TransStats;
import stats.settings.StatsProperties;

@Service
final class PreComputedStats implements StatsComputation {

	private TransStats computedStats = TransStats.EMPTY;

	public TransStats getComputedStats() {
		return computedStats;
	}
	private void setComputedStats(TransStats computedStats) {
		this.computedStats = computedStats;
	}

	@Override
	public void compute(TransactionStatsCache cache, Instant timestamp) {
		Collection<TransStats> stats = getStatsWithinGivenRange(cache, timestamp);

		TransStats updatedStats = new TransStats.Builder().from(stats);
		setComputedStats(updatedStats);
	}

	private Collection<TransStats> getStatsWithinGivenRange(TransactionStatsCache cache, Instant timestamp) {
		Long window = timestamp.minusSeconds(StatsProperties.VALID_TRANS_WINDOW_TIME_IN_SEC).getEpochSecond();

		return cache.getRecentStatsWithinTime(window);
	}

}
