package br.fcv;

public class Bar {

	public int sum(int x, int y) {
		Foo foo = new Foo();
		return foo.sum(x, y);
	}

	public int diff(int x, int y) {
		return x - y;
	}
}
