public class RemoveBlank {
    private String str;

    public RemoveBlank() {
    }

    public RemoveBlank(String string) {
        this.str = "";
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ' && string.charAt(i) != '\t') {
                this.str += string.charAt(i);
            }
        }
    }

    public String getStr() {
        return str;
    }
}
