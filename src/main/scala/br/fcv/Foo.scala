package br.fcv;

class Foo {

	def sum(x: Int, y: Int) = x + y

	def diff(x: Int, y: Int) = {
		val bar = new Bar
		bar.diff(x, y)
	}

}
