package com.huan.test;

import com.huan.Person;
import com.huan.Sort;
import com.huan.sort.*;
import com.huan.tools.Asserts;
import com.huan.tools.Integers;

import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

//        int[] nums = new int[]{5,6,10,50,42,40};
//        sleepSort(nums);
        Integer[] array = Integers.random(1000000, 1, 1000000);
        testSorts(array,
                new HeapSort(),
                new HeapSort2(),
//                new BubbleSort1(),
//                new BubbleSort2(),
//                new BubbleSort3(),
//                new SelectionSort(),
//                new InsertionSort1(),
                new CountingSort1(),
                new RadixSort(),
//                new InsertionSort3(),
                new ShellSort(),
                new QuickSort(),
//                new InsertionSort2(),
                new MergeSort()
        );

//        Person[] persons = new Person[]{
//                new Person("jack",15,180),
//                new Person("rose",19,160),
//                new Person("jim",13,180),
//                new Person("amy",25,180),
//                new Person("miachle",17,180),
//                new Person("candy",10,80)
//        };
//        testSortPerson(persons,new SelectionSort(Comparator.comparingInt(o -> ((Person) o).getAge())));
    }

    @SafeVarargs
    private static void testSorts(Integer[] array, Sort<Integer>... sorts){
        for (Sort<Integer> sort : sorts) {
            Integer[] copy = Integers.copy(array);
            sort.sort(copy);
            Asserts.test(Integers.isAscOrder(copy));
        }
        Arrays.sort(sorts);
        for (Sort<Integer> sort : sorts) {
            System.out.println(sort);
        }
    }

    private static void testSort(Integer[] array,Sort<Integer> sort){
        sort.sort(array);
        Asserts.test(Integers.isAscOrder(array));
    }

    public static void testSortPerson(Person[] array, Sort<Person> sort){
        sort.sort(array);
        for (Person person : array){
            System.out.println(person);
        }
        System.out.println(sort);
    }


    public static void sleepSort(int[] array){
        for(int i = 0;i < array.length;++i){
            final int num = array[i];
            new Thread(() -> {
                try {
                    Thread.sleep(num);
                    System.out.print(num + ",");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}

