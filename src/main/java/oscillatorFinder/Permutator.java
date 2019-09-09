package oscillatorFinder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Permutator {
    int width;
    int height;
    int length;
    String path;

    public Permutator(int width, int height, String path) {
        this.width = width;
        this.height = height;
        this.path = path+createFileName();
        this.length = width*height;
    }

    public void permutate() {
        genAndSavePermut();
    }

    private void genAndSavePermut() {
        long count = (long) Math.pow(2, length);

        for(long i = 1; i <= count; i++) {
            String tmp = Integer.toString(Integer.parseInt(i+"", 10), 2);
            tmp = completePermutation(tmp);

            addPermutTo(tmp);
        }
    }

    private void addPermutTo(String permutation) {
        try(FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(permutation);
        } catch (Exception ignored) {}
    }

    private String completePermutation(String permutation) {
        if(permutation.length() == length)
            return permutation;
        else
            return "0".repeat(length - permutation.length()) + permutation;
    }

    private String createFileName() {
        return width + "x" + height + ".btt";
    }
}
