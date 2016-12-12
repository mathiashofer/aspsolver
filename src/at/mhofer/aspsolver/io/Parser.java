package at.mhofer.aspsolver.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.mhofer.aspsolver.data.Assignment;
import at.mhofer.aspsolver.data.Rule;
import at.mhofer.aspsolver.data.Tuple;

public interface Parser {

	/**
	 * 
	 * @param file
	 * @return a tuple containing the parsed rules and an initial assignment
	 * @throws IOException
	 */
	public Tuple<List<Rule>, Assignment> parse(File file) throws IOException;

}
