package Business;

import static java.lang.Boolean.TRUE;

public class QuickSort {
    /**
     * Function that sorts values in order descendent
     * @param values values to be sorted
     * @param names names to be sorted in the same position as the values ()
     * @param i initial position to sort
     * @param j last position to sort
     */
    public static void quickSort(int[] values, String[] names, int i, int j) {

        if (i >= j) {return;}
        if (i < j) {
            int p = particio(values, names, i, j);
            quickSort(values, names, i, p);
            quickSort(values, names, p+1, j);
        }
    }

    /**
     * Function that orders elements in array and returns a pivot
     * @param values values provided
     * @param names names according to each value
     * @param i actual initial position
     * @param j actual last position
     * @return pivot position in values array
     */
    private static int particio(int[] values, String[] names, int i, int j) {
        int l = i;
        int r = j;

        int medium = (i + j) / 2;
        int pivot = values[medium];

        while (TRUE) {
            while (values[l] > pivot) {
                l++;
            }

            while (values[r] < pivot) {
                r--;
            }

            if (l >= r) {
                break;
            }

            swap(values, names, l, r);
            r--;
            l++;

        }
        return r;
    }

    /**
     * function that swaps two elements in both values and names array
     * @param values array of values to be modified
     * @param names array of names to be modified
     * @param i left position to be swapped
     * @param j right position to be swapped
     */
    private static void swap(int[] values, String[] names, int i, int j) {
        int auxInt = values[i];
        String auxName = names[i];

        values[i] = values[j];
        values[j] = auxInt;

        names[i] = names[j];
        names[j] = auxName;

    }
}
