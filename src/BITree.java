public class BITree {
    int[] tree;
    public BITree(int size){
        tree = new int[size + 1];
    }
    public long getSum(int index){
        long sum = 0;
        while (index > 0){
            sum += tree[index];
            index -= index & (-index);
        }
        return sum;
    }
    void update(int index, int val){
        while(index <= tree.length){
            tree[index] += val;
            index += index & (-index);
        }
    }

    public static void main(String[] args) {
        BITree biTree = new BITree(10);
        biTree.update(10, 5);
        biTree.update(1, 1);
        System.out.print(biTree.getSum(0));
    }
}
