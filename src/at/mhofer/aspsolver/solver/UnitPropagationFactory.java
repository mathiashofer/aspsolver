package at.mhofer.aspsolver.solver;

import java.util.List;
import java.util.Map;

import at.mhofer.aspsolver.data.Literal;
import at.mhofer.aspsolver.data.Nogood;
import at.mhofer.aspsolver.data.SimpleWatchlistGenerator;
import at.mhofer.aspsolver.data.Tuple;
import at.mhofer.aspsolver.data.WatchlistGenerator;

public class UnitPropagationFactory implements PropagationFactory{
	
	@Override
	public Propagation create(List<Nogood> instance) {
		WatchlistGenerator wlGenerator = new SimpleWatchlistGenerator();
		Tuple<Map<Nogood, Tuple<Literal, Literal>>, Map<Literal, List<Nogood>>> watchlists = wlGenerator.generateWatchlists(instance);
		Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist = watchlists.getValue1();
		Map<Literal, List<Nogood>> literalWatchlist = watchlists.getValue2();
		
		return new UnitPropagation(nogoodWatchlist, literalWatchlist);
	}

}
