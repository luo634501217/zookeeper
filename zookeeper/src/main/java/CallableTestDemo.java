import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 有返回值线程
 * @ClassName: CallableTestDemo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author luoyongchun
 * @date 2016年5月16日 上午11:57:48
 *
 */
public class CallableTestDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service =  Executors.newCachedThreadPool();
        CallableTest test = new CallableTest();
        Future<Integer> i = service.submit(test);
        System.out.println(i.get());
    }
}


class CallableTest implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Thread.sleep(1000);
        return 1;
    }

}