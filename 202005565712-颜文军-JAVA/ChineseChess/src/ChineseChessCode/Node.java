package ChineseChessCode;

public class Node {
    int index;
    int x,y;
    int oldX,oldY;
    int eatChessIndex;

    public Node(int index,int x,int y,int oldX,int oldY,int eatChessIndex){
        this.index = index;
        this.x = x;
        this.y = y;
        this.oldX = oldX;
        this.oldY = oldY;
        this.eatChessIndex = eatChessIndex;
    }
}
