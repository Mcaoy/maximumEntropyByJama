package code;

import java.util.ArrayList;

import Jama.Matrix;

public class Init {
	public ArrayList<Matrix> W;
	public ArrayList<Matrix> gradW;
	
	public Init(TotalTable table){
		W = new ArrayList<Matrix>();
		gradW = new ArrayList<Matrix>();
		for(int i=0;i<table.features_table.size();i++){
			Matrix m_w = new Matrix(1,2,1);
			Matrix m_gradw = new Matrix(1,2);
			W.add(m_w);
			gradW.add(m_gradw);
		}
	}
	
	public void gradWclear(){
		int size = gradW.size();
		gradW.clear();
		for(int i=0;i<size;i++){
			Matrix m = new Matrix(1,2);
			gradW.add(m);
		}
	}
}
