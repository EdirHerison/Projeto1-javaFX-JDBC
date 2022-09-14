package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidateExceptions extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> erros = new HashMap<>();
	
	public ValidateExceptions (String msg) {
		super(msg);
	}
	
	public Map<String, String> getErros(){
		return erros;
	}
	
	public void addErro(String fieldNome, String erroMensagem) {
		erros.put(fieldNome, erroMensagem);
	}

}
