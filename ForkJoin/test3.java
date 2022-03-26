package 零碎的一些test;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
public class test3 extends RecursiveTask<Integer>{

    //设置区间长度
    final int total = 20;
    int first,last;

    public test3(int first, int last){
        this.first = first;
        this.last = last;
    }

    @Override
    protected Integer compute() {
        int result = 0;
        // TODO Auto-generated method stub
        if(last-first<=total){
            for(int i=first;i<=last;i++){
                result += i;
            }
        } else {
            //转化成流进行计算
            return ForkJoinTask.invokeAll(creaksubtest())
            .stream()
            .mapToInt(ForkJoinTask::join)
            .sum();
        }
        return result;
    }

    private List<test3> creaksubtest(){
        List<test3> dividedtasks = new ArrayList<>();
        if(last-first>=total){
            //普通的递归
            dividedtasks.add(new test3(first, first+(last-first)/2));
            dividedtasks.add(new test3(first+(last-first)/2+1,last));
        }
        return dividedtasks;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        test3 example = new test3(1, 100);
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        long l = System.currentTimeMillis();
        Future future = forkJoinPool.submit(example);
        System.out.println(future.get());
        //计算程序操作耗时
        System.out.println("耗时:"+(System.currentTimeMillis()-l)+"ms");
    }
}

