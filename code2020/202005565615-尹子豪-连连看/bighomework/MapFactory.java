package bighomework;


public class MapFactory {
	static int[][] map;
	public MapFactory(){	
	}
	public static int[][] getMap(int n){
		map = new int[n][n];//����n*n��ͼ
		//��ʼ����ͼ��ϢΪ��
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				map[i][j] = -1;
			}
		}
		return map;
	}
}
