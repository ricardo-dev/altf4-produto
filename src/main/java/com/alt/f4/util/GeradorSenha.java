package com.alt.f4.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {
	
	// alterar senha no post de usuario
		public static String gerarBCrypt(String senha) {
			if(senha == null)
				return senha;
			
			// log.info("Gerando Hash com BCrypt");
			BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
			String s = bCryptEncoder.encode(senha);
			// log.info(s);
			return bCryptEncoder.encode(senha);
		}

}
