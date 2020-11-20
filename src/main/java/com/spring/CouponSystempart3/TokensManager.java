package com.spring.CouponSystempart3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.spring.CouponSystempart3.beans.ClientType;
import com.spring.CouponSystempart3.beans.TokenValue;
import com.spring.CouponSystempart3.facade.ClientFacade;

@Component
public class TokensManager {
	private final Map<String, TokenValue> clientTokens;

	public TokensManager() {
		clientTokens = new HashMap<String, TokenValue>();
	}

	public List<TokenValue> getAllTokens() {
		return new ArrayList<>(clientTokens.values());
	}

	public String generateToken(ClientFacade facade, ClientType clientType) {
		String token = UUID.randomUUID().toString();

		clientTokens.put(token, new TokenValue(new Date(), token, clientType, facade));

		return token;
	}

	public ClientFacade getFacadeByToken(String token) {
		if (Strings.isEmpty(token)) {
			return null;
		}

		TokenValue tokenValue = clientTokens.get(token);

		if (tokenValue != null) {
			tokenValue.setTimestamp(new Date());
			return tokenValue.getClientFacade();
		}

		return null;
	}

	public boolean deleteToken(String token, ClientType clientType) {
		TokenValue tokenValue = clientTokens.get(token);

		if (tokenValue != null && tokenValue.getClientType() == clientType) {
			clientTokens.remove(token);
			return true;
		}

		return false;
	}

}
