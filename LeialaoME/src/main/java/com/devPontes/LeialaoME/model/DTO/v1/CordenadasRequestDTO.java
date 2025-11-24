package com.devPontes.LeialaoME.model.DTO.v1;

import java.io.Serializable;
import java.util.Arrays;

public class CordenadasRequestDTO  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Double latlong[][];


	public CordenadasRequestDTO() {
		super();
	}

	public CordenadasRequestDTO(Double[][] latlong) {
		this.latlong = latlong;
	}
	
	

	public Double[][] getLatlong() {
		return latlong;
	}

	public void setLatlong(Double[][] latlong) {
		this.latlong = latlong;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(latlong);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CordenadasRequestDTO other = (CordenadasRequestDTO) obj;
		return Arrays.deepEquals(latlong, other.latlong);
	}

	@Override
	public String toString() {
		return "CordenadasRequestDTO [latlong=" + Arrays.toString(latlong) + "]";
	}
	
	
	
	
}