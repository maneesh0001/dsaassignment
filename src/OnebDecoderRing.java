//1b)Decoder Ring

public class OnebDecoderRing {

    // Function to decipher the message
    public static String decipherMessage(String s, int[][] shifts) {
        char[] message = s.toCharArray();

        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int direction = shift[2];

            // Apply the rotation for each character in the specified range
            for (int i = start; i <= end; i++) {
                message[i] = rotateCharacter(message[i], direction);
            }
        }

        return new String(message);
    }

    // Helper function to rotate a character
    private static char rotateCharacter(char ch, int direction) {
        if (direction == 1) {
            // Clockwise rotation: increment the character
            return ch == 'z' ? 'a' : (char) (ch + 1);
        } else {
            // Counter-clockwise rotation: decrement the character
            return ch == 'a' ? 'z' : (char) (ch - 1);
        }
    }

    // Main function to test the solution
    public static void main(String[] args) {
        String s = "hello";
        int[][] shifts = {
            {0, 1, 1}, // Rotate discs 0 and 1 clockwise
            {2, 3, 0}, // Rotate discs 2 and 3 counter-clockwise
            {0, 2, 1}  // Rotate discs 0, 1, and 2 clockwise
        };

        String result = decipherMessage(s, shifts);
        System.out.println("Deciphered message: " + result); // Output: jglko
    }
}
