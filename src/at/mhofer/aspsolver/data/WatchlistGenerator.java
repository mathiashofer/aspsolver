package at.mhofer.aspsolver.data;

import java.util.List;
import java.util.Map;

public interface WatchlistGenerator {

	public Tuple<Map<Nogood, Tuple<Literal, Literal>>, Map<Literal, List<Nogood>>> generateWatchlists(List<Nogood> nogoods);
	
}
