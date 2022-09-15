package bighomework;


import java.util.ArrayList;
public class Map {
	private int[][] map;
	private int count;
	private int n;
	public Map(int count,int n){//一共有count种不同的图案,n行n列		
		map = MapFactory.getMap(n);//获取n行n列的数组,初始化为-1
		this.count = count;
		this.n = n;
	}
	public int[][] getMap(){
		ArrayList<Integer> list = new ArrayList<Integer>();//先将等量图片ID添加到list中
		for(int i=0;i<n*n/2;i++){			
			int index= (int) (Math.random()*count);//random()只能生成0-1之间的数
			list.add(index);
			list.add(index);		
		}		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				int	index = (int) (Math.random()*list.size());//从list中随机取一个图片ID，并将其添加到数组中，再从list中删除掉它
				map[i][j] = list.get(index);
				list.remove(index);	
			}
		}
		return map;//返回一个图片随机生成的地图数组
	}
	//获取再次打乱后的地图信息
	public int[][] getResetMap(){	
		ArrayList<Integer> list = new ArrayList<Integer>();//list用来存储原先的地图信息	
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]!=-1)//如果(x,y)处的图片ID不为-1，那么将该图片id添加到list，并且初始化map
					list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		//将原先地图上剩余的未消去的图片打乱
		while(!list.isEmpty()){
			int	index = (int) (Math.random()*list.size());//从list中随机取一个图片ID，并将其添加到数组中，再从list中删除掉它
			boolean flag = false;
			while(!flag){
				int i = (int) (Math.random()*n);//获取随机的地图行列
				int j = (int) (Math.random()*n);
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
