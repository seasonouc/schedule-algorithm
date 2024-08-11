package com;

import java.util.PriorityQueue;

public class TestTaskService {

    public static class TestTask implements Comparable<TestTask> {

        public TestTask(int rank) {
            this.rank = rank;

        }

        public int rank;

        @Override
        public int compareTo(TestTask o) {
            return this.rank - o.rank;
        }
    }

    public static void main(String args[]) {

        PriorityQueue<TestTask> taskQueue = new PriorityQueue<>();

        taskQueue.offer(new TestTask(1));

        taskQueue.offer(new TestTask(3));

        taskQueue.offer(new TestTask(2));

        while (taskQueue.size() > 0) {
            System.out.println(taskQueue.poll().rank);
        }

    }

}
