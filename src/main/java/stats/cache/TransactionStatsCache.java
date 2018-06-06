package stats.cache;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import stats.model.TransStats;

@Component
public class TransactionStatsCache {

	private final ConcurrentNavigableMap<Long, TransStats> cache;

	private TransactionStatsCache() {
		cache = new ConcurrentSkipListMap<>();
	}

	public TransStats getTransStats(Instant instant) {
		if (cache.containsKey(instant.getEpochSecond()))
			return cache.get(instant.getEpochSecond());

		return TransStats.EMPTY;
	}

	public void add(Instant key, TransStats value) {
		cache.put(key.getEpochSecond(), value);
	}

	public void removeOutdatedStatsLessThan(Long outDatedTimestamp) {
		cache.headMap(outDatedTimestamp, Boolean.TRUE).clear();
	}

	public Collection<TransStats> getRecentStatsWithinTime(Long timestamp) {
		if (hasZerosStats())
			return Collections.emptyList();

		ConcurrentNavigableMap<Long, TransStats> recentStats = cache.tailMap(timestamp, Boolean.FALSE);
		if (CollectionUtils.isEmpty(recentStats))
			return Collections.emptyList();

		return recentStats.values();
	}

	public boolean hasZerosStats() {
		return cache.size() == 0;
	}

}
