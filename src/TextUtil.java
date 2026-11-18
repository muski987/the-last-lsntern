/**
 * Small helper: breaks long text into lines so it fits the console window.
 */
public class TextUtil {

    public static String wrap(String text, int width) {
        String[] words = text.split(" ");
        String result = "";
        int lineLength = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (lineLength > 0 && lineLength + 1 + word.length() > width) {
                result = result + "\n";
                lineLength = 0;
            } else if (lineLength > 0) {
                result = result + " ";
                lineLength++;
            }
            result = result + word;
            lineLength = lineLength + word.length();
        }
        return result;
    }

    /** Joins a list of ids as "A -> B -> C". */
    public static String pathToString(java.util.ArrayList<String> path) {
        String result = "";
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) {
                result = result + " -> ";
            }
            result = result + path.get(i);
        }
        return result;
    }
}
