package ru.senkols;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class App {
    public static void main(String[] args) {
        int limit = 8300;
        long start = System.nanoTime();
        SubTask subTask = new SubTask(0, limit);
        BigInteger fact = ForkJoinPool.commonPool().invoke(subTask);
        System.out.println("Time parallel recursion: " + (System.nanoTime() - start));
        System.out.println("Factorial if " + limit + " is: " + fact);

        start = System.nanoTime();
        fact = calcSeqFactorial(0, limit);
        System.out.println("Time simple recursion: " + (System.nanoTime() - start));
        System.out.println("Factorial if " + limit + " is: " + fact);

        start = System.nanoTime();
        fact = calcLoopFactorial(0, limit);
        System.out.println("Time loop: " + (System.nanoTime() - start));
        System.out.println("Factorial if " + limit + " is: " + fact);
    }

    private static BigInteger calcLoopFactorial(int i, int limit) {
        BigInteger fact = BigInteger.valueOf(limit);
        while (limit > 1){
            fact = fact.multiply(BigInteger.valueOf(limit-1));
            limit--;
        }
        return fact;
    }

    private static BigInteger calcSeqFactorial(int lo, int hi) {
        BigInteger temp = BigInteger.valueOf(hi);
        if (lo == 0 && hi == 1) {
            return BigInteger.ONE;
        } else if (lo < hi) {
            temp = temp.multiply(calcSeqFactorial(lo, hi - 1));
        }
        return temp;
    }

    private static class SubTask extends RecursiveTask<BigInteger> {
        private final int lo;
        private final int hi;
        private static final int THRESHOLD = 10;

        public SubTask(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
        }

        private static BigInteger calcSeqFactorial(int lo, int hi) {
            BigInteger temp = BigInteger.valueOf(hi);
            if (lo == 0 && hi == 1) {
                return BigInteger.ONE;
            } else if (lo < hi) {
                temp = temp.multiply(SubTask.calcSeqFactorial(lo, hi - 1));
            }
            return temp;
        }

        @Override
        protected BigInteger compute() {
            BigInteger fact = BigInteger.ONE;
            if ((hi - lo) <= THRESHOLD) {
                return SubTask.calcSeqFactorial(lo, hi);
            } else {
                SubTask left = new SubTask(lo, (lo + hi) / 2);
                SubTask right = new SubTask(((lo + hi) / 2)+1, hi);
                left.fork();
                BigInteger rFact = right.compute();
                BigInteger lFact = left.join();
                return rFact.multiply(lFact);
            }
        }
    }
}
