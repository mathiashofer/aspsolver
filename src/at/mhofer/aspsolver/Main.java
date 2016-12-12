package at.mhofer.aspsolver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Rule;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.io.LParseParser;
import at.mhofer.aspsolver.io.Parser;

public class Main {

	public static void main(String[] args) throws IOException {
		File input = new File(args[0]);
		Parser parser = new LParseParser();
		Tuple<List<Rule>, Assignment> tuple = parser.parse(input);
		List<Rule> rules = tuple.getValue1();
		Assignment assignment = tuple.getValue2();
	}
	
}
