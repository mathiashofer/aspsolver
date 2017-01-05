package at.mhofer.aspsolver.data.trans;

import java.util.List;

import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.Program;

public interface Translation {

	public List<Nogood> translate(Program program);
	
}
