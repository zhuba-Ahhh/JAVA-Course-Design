package bighomework;


import java.util.ArrayList;
public class Map {
	private int[][] map;
	private int count;
	private int n;
	public Map(int count,int n){//һ����count�ֲ�ͬ��ͼ��,n��n��		
		map = MapFactory.getMap(n);//��ȡn��n�е�����,��ʼ��Ϊ-1
		this.count = count;
		this.n = n;
	}
	public int[][] getMap(){
		ArrayList<Integer> list = new ArrayList<Integer>();//�Ƚ�����ͼƬID��ӵ�list��
		for(int i=0;i<n*n/2;i++){			
			int index= (int) (Math.random()*count);//random()ֻ������0-1֮�����
			list.add(index);
			list.add(index);		
		}		
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				int	index = (int) (Math.random()*list.size());//��list�����ȡһ��ͼƬID����������ӵ������У��ٴ�list��ɾ������
				map[i][j] = list.get(index);
				list.remove(index);	
			}
		}
		return map;//����һ��ͼƬ������ɵĵ�ͼ����
	}
	//��ȡ�ٴδ��Һ�ĵ�ͼ��Ϣ
	public int[][] getResetMap(){	
		ArrayList<Integer> list = new ArrayList<Integer>();//list�����洢ԭ�ȵĵ�ͼ��Ϣ	
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if(map[i][j]!=-1)//���(x,y)����ͼƬID��Ϊ-1����ô����ͼƬid��ӵ�list�����ҳ�ʼ��map
					list.add(map[i][j]);		
				map[i][j]=-1;
			}
		}
		//��ԭ�ȵ�ͼ��ʣ���δ��ȥ��ͼƬ����
		while(!list.isEmpty()){
			int	index = (int) (Math.random()*list.size());//��list�����ȡһ��ͼƬID����������ӵ������У��ٴ�list��ɾ������
			boolean flag = false;
			while(!flag){
				int i = (int) (Math.random()*n);//��ȡ����ĵ�ͼ����
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
