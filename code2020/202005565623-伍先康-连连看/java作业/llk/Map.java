package llk;

import java.util.ArrayList;

public class Map {
	
	public int[][] map;
	public int n;

	public Map(int n){		
		map = new int[n][n];
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				map[i][j] = -1;
			}
		}	
		this.n = n;
	}

	public int[][] getMap(){
		//list用来存储原先的地图信息
		ArrayList<Integer> list = new ArrayList<Integer>();
		//先放入图片序号，再进行随机选取
		for(int i=0;i<n;i++){			
			for(int j=0;j<n;j++){		
				list.add(j);
			}			
		}		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				int	index = (int)(Math.random()*list.size());
				map[i][j] = list.get(index);
				list.remove(index);	
			}
		}
		return map;
	}

	//刷新地图
	public int[][] getnewMap(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]!=-1)
					list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		while(!list.isEmpty()){
			int	index = (int) (Math.random()*list.size());
			boolean flag = false;
			//利用flag不断寻找，直到所有的图片都被放入
			while(!flag){
				int i = (int)(Math.random()*n);
				int j = (int)(Math.random()*n);
				if(map[i][j]==-1){
					map[i][j] = list.get(index);
					list.remove(index);
					flag = true;
				}	
			}
		}
		return map;
	}
}
