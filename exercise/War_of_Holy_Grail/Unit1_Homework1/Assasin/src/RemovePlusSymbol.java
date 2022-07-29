public class RemovePlusSymbol {
    private String str;

    public RemovePlusSymbol() {
    }

    public RemovePlusSymbol(String string) {
        this.str = string.replaceAll("\\+\\+", "\\+");
        this.str = str.replaceAll("--", "\\+");
        this.str = str.replaceAll("\\+-", "-");
        this.str = str.replaceAll("-\\+", "-");
        this.str = str.replaceAll("\\*\\+", "\\*");
        this.str = str.replaceAll("\\*-", "#");
    }

    public String getStr() {
        return str;
    }

}
