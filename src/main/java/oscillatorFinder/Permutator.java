package oscillatorFinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Permutator {
    public void permutator(int width, int height, String path) {
        String output = path+createFileName(width, height);
        genAndSavePermut(width, height, output);
    }

    private void genAndSavePermut(int width, int height, String output) {
        int length = width*height;
        long count = (long) Math.pow(2, length);

        for(long i = 1; i <= count; i++) {
            String tmp = Integer.toString(Integer.parseInt(i+"", 10), 2);
            tmp = completePermutation(tmp, length);

            addPermutTo(tmp, output);
        }
    }

    private void addPermutTo(String permutation, String output) {
        try(FileWriter fw = new FileWriter(output, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(permutation);
        } catch (Exception ignored) {}
    }

    private String completePermutation(String permutation, int length) {
        if(permutation.length() == length)
            return permutation;
        else
            return "0".repeat(length - permutation.length()) + permutation;
    }

    private String createFileName(int width, int height) {
        return width + "x" + height;
    }
}
