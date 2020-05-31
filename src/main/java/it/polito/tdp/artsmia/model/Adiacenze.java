package it.polito.tdp.artsmia.model;

public class Adiacenze implements Comparable<Adiacenze> {
	Integer obj1;
	Integer obj2;
	Double peso;
	
	public Adiacenze(Integer obj1, Integer obj2, Double peso) {
		super();
		this.obj1 = obj1;
		this.obj2 = obj2;
		this.peso = peso;
	}

	public Integer getObj1() {
		return obj1;
	}

	public void setObj1(Integer obj1) {
		this.obj1 = obj1;
	}

	public Integer getObj2() {
		return obj2;
	}

	public void setObj2(Integer obj2) {
		this.obj2 = obj2;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacenze o) {
		return -this.peso.compareTo(o.getPeso());
	}
	
	

}
