package com.example.demo.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;

import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class AccountController {

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping
	public ResponseEntity<List<Account>> getUsers() {
		final List<Account> accounts = accountRepository.findAll();

		if (org.springframework.util.CollectionUtils.isEmpty(accounts)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(accounts, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Account> addAccount(@RequestBody final Account account) {

		final Account exisingAccount = accountRepository.findByEmail(account.getEmail());

		if (Objects.isNull(exisingAccount)) {
			final Account createdAccount = accountRepository.save(account);
			return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
		}

		return new ResponseEntity<>(HttpStatus.CONFLICT);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") Long accountId) {
		Optional<Account> accountOptional = accountRepository.findById(accountId);
		if (accountOptional.isPresent()) {
			final Account account = accountOptional.get();
			return new ResponseEntity<>(account, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@PostMapping("/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable("id") final Long accountId,
			@RequestBody final Account account) {

		final Optional<Account> exisingAccountOptional = accountRepository.findById(accountId);

		if (exisingAccountOptional.isPresent()) {
			final Account updatedAccount = accountRepository.save(account);
			return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

}
