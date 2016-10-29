package code;

import java.util.ArrayList;

import Jama.Matrix;

public class Converter {
	public Matrix y;
	public Matrix enPy;
	public Matrix gradw_item;

	public ArrayList<Matrix> W2;
	public double EPS = 1e-6;
	public double ALPA = 0.01;
	public double REG = 1e-8;

	public Converter(TotalTable table) {
		W2 = new ArrayList<Matrix>();
		for(int i=0;i<table.features_table.size();i++){
			Matrix m_w2 = new Matrix(1,2);
			W2.add(m_w2);
		}
		gradw_item = new Matrix(1,2);
		enPy = new Matrix(1,2);
	}

	// 求y
	public void getY(ArrayList<Integer> tfeature, ArrayList<Matrix> W) {
		y = new Matrix(1,2);
		for (int i = 0; i < tfeature.size(); i++) {
			y.plusEquals(W.get(tfeature.get(i)));
		}
	}

	// 概率化
	public void enProbability(Matrix y) {
		double max = y.get(0,0);
		if (y.get(0,0) < y.get(0,1)) {
			max = y.get(0,1);
		}
		double a = Math.pow(Math.E, y.get(0,0) - max);
		double b = Math.pow(Math.E, y.get(0,1) - max);
		double sum = a + b;
		enPy.set(0,0,a / sum);
		enPy.set(0,1,b / sum);
	}

	// 梯度
	public void getGradw(Matrix enPy, ArrayList<Integer> tlabel) {
		for(int i=0;i<enPy.getColumnDimension();i++){
			gradw_item.set(0, i, enPy.get(0, i)-tlabel.get(i));
		}
	}

	// 更新W
	public void updataW(ArrayList<Integer> tfeature, ArrayList<Matrix> gradW,ArrayList<Matrix> W) {
		for (int i = 0; i < tfeature.size(); i++) {
			int feature_id = tfeature.get(i);
/*			System.out.println("up"+gradW.get(feature_id).get(0, 0)+","+gradW.get(feature_id).get(0, 1)+","+feature_id);*/
			W2.get(feature_id).plusEquals(gradW.get(feature_id).arrayTimes(gradW.get(feature_id)));
			Matrix sqrt_W2 = sqrtMatrix(plusNumber(W2.get(feature_id),EPS));
			Matrix m = (((W.get(feature_id).arrayTimes(sqrt_W2)).minus(gradW.get(feature_id).times(ALPA))).arrayRightDivide(plusNumber(sqrt_W2,ALPA*REG))).copy();
			for(int j=0;j<W.get(feature_id).getColumnDimension();j++){
				W.get(feature_id).set(0, j, m.get(0, j));
			}
		}
	}
	public Matrix plusNumber(Matrix m,double number){
		int col = m.getColumnDimension();
		int row = m.getRowDimension();
		Matrix result = new Matrix(row,col);
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				result.set(i, j, m.get(i, j)+number);
			}
		}
		return result;
	}
	public Matrix sqrtMatrix(Matrix m){
		int col = m.getColumnDimension();
		int row = m.getRowDimension();
		Matrix result = new Matrix(row,col);
		for(int i=0;i<row;i++){
			for(int j=0;j<col;j++){
				result.set(i, j, Math.sqrt(m.get(i, j)));
			}
		}
		return result;
	}
}
