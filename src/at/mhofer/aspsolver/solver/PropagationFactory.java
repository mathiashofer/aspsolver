package at.mhofer.aspsolver.solver;

import java.util.List;

import at.mhofer.aspsolver.data.Nogood;

public interface PropagationFactory {
	
	public Propagation create(List<Nogood> instance);

}
