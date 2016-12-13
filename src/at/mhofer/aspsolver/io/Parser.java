package at.mhofer.aspsolver.io;

import java.io.File;
import java.io.IOException;

import at.mhofer.aspsolver.data.Program;

public interface Parser {

	/**
	 * 
	 * @param file
	 * @return a tuple containing the parsed rules and an initial assignment
	 * @throws IOException
	 */
	public Program parse(File file) throws IOException;

}
