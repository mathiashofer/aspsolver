package at.mhofer.aspsolver.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SimpleWatchlistGenerator implements WatchlistGenerator {

	@Override
	public Tuple<Map<Nogood, Tuple<Literal, Literal>>, Map<Literal, List<Nogood>>> generateWatchlists(
			List<Nogood> instance) {
		Map<Nogood, Tuple<Literal, Literal>> nogoodWatchlist = new HashMap<Nogood, Tuple<Literal, Literal>>();
		Map<Literal, List<Nogood>> literalWatchlist = new HashMap<Literal, List<Nogood>>();
		for (Nogood nogood : instance) {
			List<Literal> literals = nogood.getLiterals();
			Literal l1 = literals.get(0);
			Literal l2 = literals.size() > 1 ? literals.get(1) : l1;
			nogoodWatchlist.put(nogood, new Tuple<Literal, Literal>(l1, l2));

			List<Nogood> n1 = literalWatchlist.getOrDefault(l1, new LinkedList<Nogood>());
			n1.add(nogood);
			literalWatchlist.put(l1, n1);

			List<Nogood> n2 = literalWatchlist.getOrDefault(l2, new LinkedList<Nogood>());
			n2.add(nogood);
			literalWatchlist.put(l2, n2);
		}
		return new Tuple<Map<Nogood, Tuple<Literal, Literal>>, Map<Literal, List<Nogood>>>(nogoodWatchlist,
				literalWatchlist);
	}

}
