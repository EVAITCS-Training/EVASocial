import java.io.*;

public class Main {

    static void main(String[] args) {
        //FileWriter and BufferedWriter
        // Old Original way of doing thing
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter("example.csv");
//            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            fileWriter.close();
//        }

        try(
                FileWriter fileWriter = new FileWriter("src/main/resources/example.csv");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
                ) {
            bufferedWriter.write("Jab,CharacterName,WhenToSay");
            bufferedWriter.newLine();
            bufferedWriter.write("\"This Trap is so basic a slime can see it\",Kaelen,\"trap placement\",");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //FileReader and BufferedReader

        try (
                FileReader fileReader = new FileReader("src/main/resources/example.csv");
                BufferedReader bufferedReader = new BufferedReader(fileReader)
                ) {
            for (String line : bufferedReader.lines().toList()) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
