package at.mhofer.aspsolver.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import at.mhofer.aspsolver.data.Rule;

public interface Parser {

	public List<Rule> parse(File file) throws IOException;
	
}
