package stats.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import stats.model.Transaction;
import stats.model.TransactionProcessingResponse;
import stats.transactions.processing.TransactionProcessing;

@RestController
public class TransactionController {

	private final TransactionProcessing transProc;

	@Autowired
	public TransactionController(TransactionProcessing transProc) {
		this.transProc = transProc;
	}


	@PostMapping(path = "/transactions", consumes = "application/json")
	public HttpStatus postTransaction(@Valid @RequestBody final Transaction trans) {
		TransactionProcessingResponse resp = transProc.process(trans);

		if (resp.isTransAdded())
			return HttpStatus.CREATED;

		return HttpStatus.NO_CONTENT;
	}
}
