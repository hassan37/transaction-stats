package stats.cache.cleaning;

import java.time.Instant;

import org.springframework.stereotype.Service;

import stats.cache.TransactionStatsCache;
import stats.settings.StatsProperties;

@Service
final class SecondsBasedCacheCleaning implements CacheCleaning {

	@Override
	public void clean(TransactionStatsCache cache, Instant timestamp) {
		if (cache.hasZerosStats())
			return;

		Long outDatedTimestamp = calculateOutDatedTimestampWindow(timestamp);

		cache.removeOutdatedStatsLessThan(outDatedTimestamp);
	}

	private Long calculateOutDatedTimestampWindow(Instant timestamp) {
		final int freshWindow = StatsProperties.VALID_TRANS_WINDOW_TIME_IN_SEC;
		Long outDatedTimestamp = timestamp.minusSeconds(freshWindow).getEpochSecond();

		return outDatedTimestamp;
	}

}
