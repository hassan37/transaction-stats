package stats.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Transaction {

	@NotNull(message = "Amount must be not null")
	private BigDecimal amount;

	@Positive(message = "Timestamp must be positive")
	private long timestamp;

	public Transaction() {
	}
	public Transaction(@NotNull(message = "Amount must be not null") BigDecimal amount) {
		this.amount = amount;
	}


	private transient Instant inst;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@JsonIgnore
	public Instant getTimestampInstant() {
		if (null == inst)
			inst = Instant.ofEpochMilli(timestamp);

		return inst;
	}

	@Override
	public String toString() {
		return "Transaction [amount=" + amount + ", timestamp=" + timestamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		Transaction other = (Transaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

}
