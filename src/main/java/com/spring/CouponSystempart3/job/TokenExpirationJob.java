package com.spring.CouponSystempart3.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.CouponSystempart3.TokensManager;
import com.spring.CouponSystempart3.beans.TokenValue;

@Component
public class TokenExpirationJob implements Runnable {
	private boolean quit;
	private static final int JOB_INTERVAL_IN_MILISECONDS = 1000 * 60;
	private static final int TOKEN_THERSHOLD_IN_MILISECONDS = 30 * 60 * 1000;

	@Autowired
	private TokensManager tokensManager;

	public TokenExpirationJob() {
		quit = false;
	}

	@Override
	public void run() {
		while (!quit) {
			try {
				deleteExpiredTokens();
				Thread.sleep(JOB_INTERVAL_IN_MILISECONDS);
			} catch (Exception e) {
				System.out.println("Failed to run token expiration job");
				e.printStackTrace();
			}
		}

	}

	public void stop() {
		quit = true;
	}
	
	private void deleteExpiredTokens() {
		List<TokenValue> tokenValues = tokensManager.getAllTokens();

		List<TokenValue> tokensToRemove = findExpiredTokens(tokenValues, new Date());

		for (TokenValue token : tokensToRemove) {
			tokensManager.deleteToken(token.getToken(), token.getClientType());
		}

	}

	private List<TokenValue> findExpiredTokens(List<TokenValue> tokenValues, Date today) {
		List<TokenValue> results = new ArrayList<TokenValue>();

		if (tokenValues != null) {
			for (TokenValue token : tokenValues) {
				long diffInMillies = today.getTime() - token.getTimestamp().getTime();

				if (diffInMillies >= TOKEN_THERSHOLD_IN_MILISECONDS) {
					results.add(token);
				}
			}
		}

		return results;
	}

}
