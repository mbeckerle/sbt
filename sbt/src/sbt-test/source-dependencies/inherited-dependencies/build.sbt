lazy val verifyDeps = taskKey[Unit]("verify inherited dependencies are properly extracted")

verifyDeps := {
	val a = compile.in(Compile).value
	same(a.relations.publicInherited.internal.forwardMap, expectedDeps.forwardMap)
}

lazy val expected = Seq(
	"A" -> Seq("C", "D", "E", "G", "J"),
	"B" -> Seq(),
	"C" -> Seq("D", "G", "J"),
	"D" -> Seq("G", "J"),
	"E" -> Seq(),
	"F" -> Seq("C", "D", "G", "J"),
	"G" -> Seq("J"),
	"J" -> Seq()
)
lazy val pairs =
	expected.map { case (from,tos) =>
		(toFile(from), tos.map(toFile))
	}
lazy val expectedDeps = (Relation.empty[File,File] /: pairs) { case (r, (x,ys)) => r + (x,ys) }
def toFile(s: String) = file(s + ".scala").getAbsoluteFile

def same[T](x: T, y: T) {
	assert(x == y, s"\nActual: $x, \nExpected: $y")
}
