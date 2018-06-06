package stats.model;

public class TransactionProcessingResponse {

	private boolean transAdded;

	public boolean isTransAdded() {
		return transAdded;
	}

	public void setTransAdded(boolean transAdded) {
		this.transAdded = transAdded;
	}

	public static TransactionProcessingResponse newInvalidTransResp(Boolean transAdded) {
		TransactionProcessingResponse resp = new TransactionProcessingResponse();
		resp.setTransAdded(transAdded);

		return resp;
	}

}
