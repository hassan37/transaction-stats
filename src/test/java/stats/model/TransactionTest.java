package stats.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Test;

public class TransactionTest {

	@Test
	public void transactionDataCorrectlyPopulated() {
		BigDecimal amount = BigDecimal.valueOf(12.5);
		Instant inst = Instant.now();
		Transaction trans = new Transaction();
		trans.setAmount(amount);
		trans.setTimestamp(inst.toEpochMilli());

		assertEquals(amount, trans.getAmount());
		assertEquals(inst.toEpochMilli(), trans.getTimestamp());
		assertEquals(inst, trans.getTimestampInstant());
	}

}
