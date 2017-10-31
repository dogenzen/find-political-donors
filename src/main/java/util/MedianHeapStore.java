package util;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * A max-min-heap data-structure to store a list of numbers for efficient median computation
 *
 * Time Complexity
 * ---------------
 * insert - O(lg n)
 * compute median - O(1)
 *
 * @author Pradeep Das
 * @version 27th Oct 2017
 */
public class MedianHeapStore
{
    private PriorityQueue<Double> firstHalf = new PriorityQueue<>(10, Collections.<Double>reverseOrder());
    private PriorityQueue<Double> secondHalf = new PriorityQueue<>();

    /**
     *
     * @return returns the median value of the list stored in the heaps
     */
    public Double getMedian() {
        if (firstHalf.size() == secondHalf.size()) {
            return firstHalf.peek() + (secondHalf.peek() - firstHalf.peek()) / 2;
        } else {
            return firstHalf.size() > secondHalf.size() ? firstHalf.peek() : secondHalf.peek();
        }
    }

    /**
     *
     * @param number - add number to the list
     */
    public void add(Double number) {
        if (firstHalf.size() == 0 || number < firstHalf.peek()) {
            firstHalf.add(number);
        } else {
            secondHalf.add(number);
        }
        reBalance(firstHalf, secondHalf);
    }

    /**
     *
     * @param firstHalf - first half of the list stored in a max heap
     * @param secondHalf - second half of the list stored in a mean heap
     */
    private void reBalance(PriorityQueue<Double> firstHalf, PriorityQueue<Double> secondHalf) {
        int firstMinusSecond = firstHalf.size() - secondHalf.size();
        if (Math.abs(firstMinusSecond) > 1) {
            PriorityQueue<Double> bigger = firstMinusSecond > 0 ? firstHalf : secondHalf;
            PriorityQueue<Double> smaller = firstMinusSecond > 0 ? secondHalf : firstHalf;
            smaller.add(bigger.poll());
        }
    }
}