package stats.transactions.processing;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import stats.cache.TransactionStatsCache;
import stats.model.TransStats;
import stats.model.Transaction;
import stats.model.TransactionProcessingResponse;
import stats.settings.StatsProperties;

@Service
final class RecentTransactionProcessing implements TransactionProcessing {

	private final TransactionStatsCache statsCache;

	@Autowired
	public RecentTransactionProcessing(TransactionStatsCache statsCache) {
		this.statsCache = statsCache;
	}

	@Override
	public TransactionProcessingResponse process(Transaction trans) {
		if (isNotValidTransaction(trans.getTimestampInstant()))
			return TransactionProcessingResponse.newInvalidTransResp(Boolean.FALSE);

		updateTransaction(trans);

		return TransactionProcessingResponse.newInvalidTransResp(Boolean.TRUE);
	}

		private boolean isNotValidTransaction(final Instant instant) {
			return !isValidTransaction(instant);
		}

			private boolean isValidTransaction(final Instant transTime) {
				final Instant nowTime = Instant.now();
				final int interval = (int) Math.abs(nowTime.getEpochSecond() - transTime.getEpochSecond());
		
				return interval <= StatsProperties.VALID_TRANS_WINDOW_TIME_IN_SEC; 
			}

		private void updateTransaction(Transaction trans) {
			TransStats existingStats = statsCache.getTransStats(trans.getTimestampInstant());
			TransStats.Builder b = new TransStats.Builder();
			TransStats stats;
			if(TransStats.EMPTY == existingStats)
				stats = b.from(trans);
			else
				stats = b.from(existingStats, trans);

			statsCache.add(trans.getTimestampInstant(), stats);
		}
}
