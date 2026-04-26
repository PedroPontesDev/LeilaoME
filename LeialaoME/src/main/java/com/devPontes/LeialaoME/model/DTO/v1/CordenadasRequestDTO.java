package com.devPontes.LeialaoME.model.DTO.v1;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CordenadasRequestDTO  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<List<Double>> cordenadas;

	public CordenadasRequestDTO() {
	}
	
	public List<List<Double>> getCordenadas() {
		return cordenadas;
	}

	public void setCordenadas(List<List<Double>> cordenadas) {
		this.cordenadas = cordenadas;
	}
	
}