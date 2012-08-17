package org.rm.scripts;

import java.util.List;
import java.util.Map;

public interface Analysis {
	public List<Map<String, String>> AnalysisContext(StringBuffer buffer,String regex) ;
	public List<Map<String, String>> AnalysisArp(StringBuffer buffer,String regex);
	public boolean AnalysisPing(StringBuffer buffer) ;
	
}
