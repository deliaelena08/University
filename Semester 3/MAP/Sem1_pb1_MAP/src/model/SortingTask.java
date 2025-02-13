package model;

public class SortingTask extends Task{
    int[] array;
    private AbstractSorter sorter;

    SortingTask(String taskId, String description,int[] array, AbstractSorter sorter) {
        super(taskId, description);
        this.array = array;
        this.sorter = sorter;

    }

    @Override
    public void execute() {
        sorter.sort(array);
        System.out.println("Vectorul este:");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }
}
