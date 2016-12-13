package at.mhofer.aspsolver.data;

import java.util.ArrayList;

public class Falsum extends Nogood{

	public Falsum() {
		super(new ArrayList<Literal>(0));
	}

	@Override
	public boolean isFalsifiedBy(Assignment assignment) {
		return true;
	}
	
	@Override
	public boolean isSatisfiedBy(Assignment assignment) {
		return false;
	}
}
