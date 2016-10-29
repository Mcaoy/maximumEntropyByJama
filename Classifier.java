package code;

import java.util.ArrayList;
import java.util.Collections;

import Jama.Matrix;

public class Classifier {
	public static void main(String[] args) {
		String trainpath = "D:/Data/car.train.utf8";
		String testpath = "D:/Data/car.test.utf8";
		ArrayList<Item> train = new ArrayList<Item>();
		ArrayList<Item> test = new ArrayList<Item>();
		Readfile rf = new Readfile();
		rf.readFile(trainpath, train);
		rf.readFile(testpath, test);
		TotalTable totalTable = new TotalTable(train);
		ArrayList<Template> train_temp = new ArrayList<Template>();
		ArrayList<Template> test_temp = new ArrayList<Template>();
		Transform T = new Transform();
		T.translated(totalTable, train, train_temp);
		T.translated(totalTable, test, test_temp);
		
		Init variable = new Init(totalTable);
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		for(int i = 0;i <train_temp.size();i++){     
			index.add(i);    
        } 
		
		for (int times = 0; times < 300; times++) {
			
			Collections.shuffle(index); 
			
			for (int i = 0; i <train_temp.size(); i++) {					//µÚ5¾ä
				Converter c = new Converter(totalTable);
				c.getY(train_temp.get(index.get(i)).tfeature, variable.W);
				c.enProbability(c.y);
				c.getGradw(c.enPy, train_temp.get(index.get(i)).tlabel);
				for (int j = 0; j < train_temp.get(index.get(i)).tfeature.size(); j++) {
					int feature_id = train_temp.get(index.get(i)).tfeature.get(j);
					variable.gradW.get(feature_id).plusEquals(c.gradw_item);
				}
				c.updataW(train_temp.get(index.get(i)).tfeature, variable.gradW, variable.W);				//´í
				variable.gradWclear();
			}
			int yes = 0;
			for (int i = 0; i < test_temp.size(); i++) {
				Matrix y = new Matrix(1,2);
				for (int j = 0; j < test_temp.get(i).tfeature.size(); j++) {
					y.plusEquals(variable.W.get(test_temp.get(i).tfeature.get(j)));
				}
				if (y.get(0,0) > y.get(0,1)) {
					y.set(0,0,1);
					y.set(0,1,0);
				} else {
					y.set(0,0,0);
					y.set(0,1,1);
				}
				if (test_temp.get(i).tlabel.get(0) == y.get(0,0) && test_temp.get(i).tlabel.get(1) == y.get(0,1)) {
					yes++;
				}
			}
			System.out.println(yes / (test_temp.size() + 0.0) * 100 + "%");
		}
	}
}
