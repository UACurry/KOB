package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{
//    锁
    private final ReentrantLock lock = new ReentrantLock();

//    空的就阻塞，如果有东西 就发送信号量唤醒
    private final Condition condition = lock.newCondition();
//    一个执行代码的队列
    private final Queue<Bot> bots = new LinkedList<>();

//    往队列插入一个bot 在 botrunningservicimpl 用
    public void addBot(Integer userId, String botCode, String input) {
//        涉及到对 队列的 修改 所以 要加锁
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
//            加好 bot 的信息之后 就需要唤醒一下 消费队列 线程
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

//    可以改成 对于docker执行
    private void consume(Bot bot) {
//        每次执行一个代码 搞一个新的线程 方便控制 代码 执行时间
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }


// 线程执行的时候 开始执行
    @Override
    public void run() {
//        队列空 阻塞 如果有东西 就发送信号量唤醒
//        这个循环就是一个 消费队列
        while (true) {
            lock.lock();
            if (bots.isEmpty()) {
//                bots 队列是空的  阻塞住 把当前线程 阻塞住
                try {
//                    await 时候  自动释放锁
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
//                    报异常 自动解锁
                    lock.unlock();
                    break;
                }
            } else {
//                返回并删除队头
                Bot bot = bots.remove();
                lock.unlock();
//                消费一下这个任务
                // 比较耗时，可能会执行几秒钟 所以加到unlock后面 因为要编译执行
                consume(bot);
            }
        }
    }
}
