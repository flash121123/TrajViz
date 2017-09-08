package gmu.edu.core.frame;

import gmu.edu.core.Collections.WordList;
import gmu.edu.core.gi.GrammarRules;

public interface GInterface {
	
	public GrammarRules run(WordList w) throws Exception;
	abstract void updateOccurrences(GrammarRules rules, WordList w);
	abstract void updateInterval(GrammarRules rules, WordList w);
	
}
