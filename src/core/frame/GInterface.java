package core.frame;

import core.Collections.WordList;
import core.gi.GrammarRules;

public interface GInterface {
	
	public GrammarRules run(WordList w) throws Exception;
	abstract void updateOccurrences(GrammarRules rules, WordList w);
	abstract void updateInterval(GrammarRules rules, WordList w);
	
}
