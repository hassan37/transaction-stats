package stats.transactions.processing;

import stats.model.Transaction;
import stats.model.TransactionProcessingResponse;

public interface TransactionProcessing {

	TransactionProcessingResponse process(Transaction trans);
}
