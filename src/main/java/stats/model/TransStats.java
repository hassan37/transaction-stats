package stats.model;

import static stats.settings.StatsProperties.DECIMAL_MATCH_CONTEXT;
import static stats.settings.StatsProperties.DECIMAL_PRECISION;
import static stats.settings.StatsProperties.DECIMAL_ROUND_MODE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;

import org.springframework.util.CollectionUtils;


public class TransStats {

	public static final TransStats EMPTY;

	static {
		Builder b = new Builder();
		EMPTY = b.buildEmpty();
	}

	private final BigDecimal sum;
	private final BigDecimal max;
	private final BigDecimal min;
	private final BigDecimal avg;
	private final long count;

	private TransStats(Builder b) {
		this.sum = b.sum;
		this.max = b.max;
		this.min = b.min;
		this.count = b.count;
		this.avg = calculateAvg();
	}

	public BigDecimal getSum() {
		return sum;
	}

	public BigDecimal getAvg() {
		return avg;
	}
	private BigDecimal calculateAvg() {
		if (getCount() < 1)
			return BigDecimal.ZERO;
		
		BigDecimal total = BigDecimal.valueOf(getCount());

		BigDecimal cAvg = getSum().divide(total, DECIMAL_PRECISION, DECIMAL_ROUND_MODE);

		return cAvg;
	}

	public BigDecimal getMax() {
		return max;
	}

	public BigDecimal getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "TransStats [sum=" + sum + ", max=" + max + ", min=" + min + ", count=" + count + ", Avg()=" + getAvg() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((avg == null) ? 0 : avg.hashCode());
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		result = prime * result + ((sum == null) ? 0 : sum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransStats other = (TransStats) obj;
		if (avg == null) {
			if (other.avg != null)
				return false;
		} else if (!avg.equals(other.avg))
			return false;
		if (count != other.count)
			return false;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		if (sum == null) {
			if (other.sum != null)
				return false;
		} else if (!sum.equals(other.sum))
			return false;
		return true;
	}



	public static class Builder {
		private BigDecimal sum;
		private BigDecimal max;
		private BigDecimal min;
		private long count = 0;

		public Builder sum(BigDecimal sum) {
			this.sum = sum;

			return this;
		}
		public Builder max(BigDecimal max) {
			this.max = max;

			return this;
		}
		public Builder min(BigDecimal min) {
			this.min = min;

			return this;
		}
		public Builder count(long count) {
			this.count = count;

			return this;
		}

		public Builder incrementSum(BigDecimal sum) {
			this.sum = null == this.sum ? sum : this.sum.add(sum, MathContext.DECIMAL128);

			return this;
		}
		public Builder incrementCount(long count) {
			this.count += count;

			return this;
		}
		public Builder updateMax(BigDecimal max) {
			this.max = null == this.max ? max : this.max.max(max);

			return this;
		}
		public Builder updateMin(BigDecimal min) {
			this.min = null == this.min ? min : this.min.min(min);

			return this;
		}

		public TransStats build() {
			return new TransStats(this);
		}
		public TransStats buildEmpty() {
			count(0)
			.max(BigDecimal.ZERO)
			.min(BigDecimal.ZERO)
			.sum(BigDecimal.ZERO);
			
			return new TransStats(this);
		}
		public TransStats from(final Transaction tr) {
			BigDecimal amount = tr.getAmount();
			this.sum(amount)
				.count(1)
				.max(amount)
				.min(amount);

			return build();
		}

		public TransStats from(final TransStats existingTransStats, final Transaction tr) {
			BigDecimal amount = tr.getAmount();
			this.sum(existingTransStats.getSum().add(amount, DECIMAL_MATCH_CONTEXT))
			.count(existingTransStats.getCount() + 1)
			.max(existingTransStats.getMax().max(amount))
			.min(existingTransStats.getMin().min(amount));

			return build();
		}

		public TransStats from(Collection<TransStats> stats) {
			if (CollectionUtils.isEmpty(stats))
				return TransStats.EMPTY;

			stats.forEach(s -> { this
				.incrementCount(s.getCount())
				.updateMax(s.getMax())
				.updateMin(s.getMin())
				.incrementSum(s.getSum());
			});

			return build();
			
		}
	}

}
