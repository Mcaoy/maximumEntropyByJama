package code;

import java.util.ArrayList;

public class ExtractF {
	public ExtractF(ArrayList<String> words,ArrayList<String> features){
		for(String s:words){
			features.add("uni"+s);
		}
		for(int i = 0; i < words.size()-1;i++){
			features.add("bi"+words.get(i)+"#"+words.get(i+1));
		}
	}
}
