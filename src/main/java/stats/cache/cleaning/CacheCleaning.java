package stats.cache.cleaning;

import java.time.Instant;

import stats.cache.TransactionStatsCache;

public interface CacheCleaning {

	void clean(TransactionStatsCache cache, Instant timestamp);
}
