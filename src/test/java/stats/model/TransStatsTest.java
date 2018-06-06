package stats.model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import stats.settings.StatsProperties;

public class TransStatsTest {

	private TransStats emptyStats;
	private List<Transaction> fourTransactions;
	private BigDecimal sumOf4Trans;
	private BigDecimal maxOf4Trans;
	private BigDecimal minOf4Trans;
	private BigDecimal avgOf4Trans;
	private long count;

	@Before
	public void init() {
		emptyStats = TransStats.EMPTY;
		fourTransactions = Arrays.asList(
			new Transaction(BigDecimal.valueOf(2.5)),
			new Transaction(BigDecimal.valueOf(12.5)),
			new Transaction(BigDecimal.valueOf(-4.5)),
			new Transaction(BigDecimal.valueOf(-14.5))
		);
		sumOf4Trans = fourTransactions
			.stream()
			.map(t -> t.getAmount())
	        .reduce(BigDecimal.ZERO, (x, y) -> x.add(y, StatsProperties.DECIMAL_MATCH_CONTEXT));
		maxOf4Trans = fourTransactions
				.stream()
				.map(t -> t.getAmount())
		        .reduce(BigDecimal.ZERO, (x, y) -> x.max(y));
		minOf4Trans = fourTransactions
				.stream()
				.map(t -> t.getAmount())
		        .reduce(BigDecimal.ZERO, (x, y) -> x.min(y));

		BigDecimal total = BigDecimal.valueOf(fourTransactions.size());
		avgOf4Trans = sumOf4Trans.divide(total, StatsProperties.DECIMAL_PRECISION, StatsProperties.DECIMAL_ROUND_MODE);

		count = fourTransactions.size();
	}

	@Test
	public void emptyStatsObjectCorrectlyBuilt() {
		TransStats es = new TransStats.Builder()
			.count(0)
			.max(BigDecimal.ZERO)
			.min(BigDecimal.ZERO)
			.sum(BigDecimal.ZERO)
			.build();

		assertEquals(Boolean.TRUE, emptyStats.equals(es));
	}

	@Test
	public void should_maxMinSumAvgBeSameAndCountBeOne_when_onlyOneTransaction() {
		BigDecimal amount = BigDecimal.valueOf(12.5);
		Transaction singleTrans = new Transaction();
		singleTrans.setAmount(amount);
		TransStats es = new TransStats.Builder().from(singleTrans);

		assertEquals(amount, es.getMax());
		assertEquals(amount, es.getMin());
		assertEquals(amount, es.getSum());

		BigDecimal avg = amount.divide(BigDecimal.ONE, StatsProperties.DECIMAL_PRECISION, StatsProperties.DECIMAL_ROUND_MODE);
		assertEquals(avg, es.getAvg());

		long expectedCount = 1l;
		assertEquals(expectedCount, es.getCount());
	}

	@Test
	public void testStatsCorrectionFor4ransactions() {
		List<TransStats> stats = fourTransactions
			.stream()
			.map(t -> new TransStats.Builder().from(t))
			.collect(Collectors.toList());

		final TransStats s = new TransStats.Builder().from(stats);
		assertEquals(sumOf4Trans, s.getSum());
		assertEquals(maxOf4Trans, s.getMax());
		assertEquals(minOf4Trans, s.getMin());
		assertEquals(avgOf4Trans, s.getAvg());
		assertEquals(count, s.getCount());
	}

}
